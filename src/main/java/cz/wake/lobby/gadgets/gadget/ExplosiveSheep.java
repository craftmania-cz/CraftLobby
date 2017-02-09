package cz.wake.lobby.gadgets.gadget;

import com.google.common.collect.Sets;
import cz.wake.lobby.Main;
import cz.wake.lobby.listeners.MessagesListener;
import cz.wake.lobby.utils.UtilMath;
import net.minecraft.server.v1_10_R1.EntityCreature;
import net.minecraft.server.v1_10_R1.EntityInsentient;
import net.minecraft.server.v1_10_R1.EntitySheep;
import net.minecraft.server.v1_10_R1.PathfinderGoalSelector;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftSheep;
import org.bukkit.craftbukkit.v1_10_R1.util.UnsafeList;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ExplosiveSheep implements Listener {

    private HashMap<Player, Double> _time = new HashMap();
    HashMap<Player, BukkitRunnable> _cdRunnable = new HashMap();
    ArrayList<Sheep> sheepArrayList = new ArrayList();

    private Main plugin;

    public ExplosiveSheep(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSheep(PlayerInteractEvent e) {
        final Player player = e.getPlayer();
        ItemStack item = e.getItem();
        Action action = e.getAction();

        if (item == null) {
            return;
        }
        if (!item.hasItemMeta()) {
            return;
        }
        if (!item.getType().equals(Material.SHEARS)) {
            return;
        }
        if (!item.getItemMeta().getDisplayName().contains("ExplosiveSheep")) {
            return;
        }
        if (!player.hasPermission("craftlobby.gadgets.explosivesheep")) {
            return;
        }
        e.setCancelled(true);
        player.updateInventory();
        if ((action.equals(Action.RIGHT_CLICK_AIR)) || (action.equals(Action.RIGHT_CLICK_BLOCK))) {
            if (Main.getInstance().getTPS() < 17) {
                player.sendMessage("§cServer je pretizeny, nelze pouzivat gadget!");
                return;
            }
            if (this._time.containsKey(player)) {
                MessagesListener.messageCooldown(player, String.valueOf(arrondi(((Double) this._time.get(player)).doubleValue(), 1)));
                return;
            }
            this._time.put(player, Double.valueOf(30D + 0.1D));
            Location loc = player.getLocation().add(player.getEyeLocation().getDirection().multiply(0.5D));
            loc.setY(player.getLocation().getBlockY() + 1);
            Sheep s = (Sheep) player.getWorld().spawn(loc, Sheep.class);

            s.setNoDamageTicks(100000);
            this.sheepArrayList.add(s);

            EntitySheep entitySheep = ((CraftSheep) s).getHandle();
            try {
                Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
                bField.setAccessible(true);
                Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
                cField.setAccessible(true);
                bField.set(entitySheep.goalSelector, new UnsafeList());
                bField.set(entitySheep.targetSelector, new UnsafeList());
                cField.set(entitySheep.goalSelector, new UnsafeList());
                cField.set(entitySheep.targetSelector, new UnsafeList());
            } catch (Exception exc) {
                exc.printStackTrace();
            }
            Main.explosiveSheep.add(this);

            new SheepColorRunnable(7.0D, true, s, player, this);
            this._cdRunnable.put(player, new BukkitRunnable() {
                @Override
                public void run() {
                    ExplosiveSheep.this._time.put(player, Double.valueOf(((Double) ExplosiveSheep.this._time.get(player)).doubleValue() - 0.1D));
                    if (((Double) ExplosiveSheep.this._time.get(player)).doubleValue() < 0.01D) {
                        ExplosiveSheep.this._time.remove(player);
                        ExplosiveSheep.this._cdRunnable.remove(player);
                        cancel();
                    }

                }
            });
            ((BukkitRunnable) this._cdRunnable.get(player)).runTaskTimer(plugin, 2L, 2L);
        }
    }

    private static double arrondi(double A, int B) {

        return (int) (A * Math.pow(10.0D, B) + 0.5D) / Math.pow(10.0D, B);
    }


    class SheepColorRunnable extends BukkitRunnable {
        private boolean red;
        private double time;
        private Sheep s;
        private ExplosiveSheep gadgetExplosiveSheep;
        private Player player;

        public SheepColorRunnable(double time, boolean red, Sheep s, Player player, ExplosiveSheep gadgetExplosiveSheep) {
            this.red = red;
            this.time = time;
            this.s = s;
            runTaskLater(Main.getPlugin(), (int) time);
            this.gadgetExplosiveSheep = gadgetExplosiveSheep;
            this.player = player;
        }

        public void run() {
            if (this.red) {
                this.s.setColor(DyeColor.RED);
            } else {
                this.s.setColor(DyeColor.WHITE);
            }
            this.s.getWorld().playSound(this.s.getLocation(), Sound.UI_BUTTON_CLICK, 5.0F, 1.0F);
            this.red = (!this.red);
            this.time -= 0.2D;
            if (this.time < 0.5D) {
                this.s.getWorld().playSound(this.s.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2.0F, 1.0F);
                this.s.getWorld().spigot().playEffect(this.s.getLocation(), Effect.EXPLOSION_HUGE);
                for (int i = 0; i < 50; i++) {
                    final Sheep sheep = ((Sheep) player.getWorld().spawn(this.s.getLocation(), Sheep.class));
                    try {
                        sheep.setColor(DyeColor.values()[UtilMath.randomRangeInt(0, 15)]);
                    } catch (Exception localException1) {
                    }
                    Random r = new Random();
                    UtilMath.applyVelocity(sheep, new Vector(r.nextDouble() - 0.5D, r.nextDouble() / 2.0D, r.nextDouble() - 0.5D).multiply(2).add(new Vector(0.0D, 0.8D, 0.0D)));
                    sheep.setBaby();
                    sheep.setAgeLock(true);
                    sheep.setNoDamageTicks(120);
                    clearPathfinders(sheep);
                    makePanic(sheep);
                    Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {
                        public void run() {
                            sheep.getWorld().spigot().playEffect(sheep.getLocation(), Effect.LAVA_POP, 0, 0, 0.0F, 0.0F, 0.0F, 0.0F, 5, 32);
                            sheep.remove();
                            Main.explosiveSheep.remove(ExplosiveSheep.this);
                        }
                    }, 110L);
                }
                ExplosiveSheep.this.sheepArrayList.remove(this.s);
                this.s.remove();
                cancel();
            } else {
                Bukkit.getScheduler().cancelTask(getTaskId());
                new SheepColorRunnable(this.time, this.red, this.s, this.player, this.gadgetExplosiveSheep);
            }
        }
    }

    public void clearPathfinders(org.bukkit.entity.Entity entity) {
        net.minecraft.server.v1_10_R1.Entity nmsEntity = ((CraftEntity) entity).getHandle();
        try {
            Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
            bField.setAccessible(true);
            Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
            cField.setAccessible(true);
            bField.set(((EntityInsentient) nmsEntity).goalSelector, Sets.newLinkedHashSet());
            bField.set(((EntityInsentient) nmsEntity).targetSelector, Sets.newLinkedHashSet());
            cField.set(((EntityInsentient) nmsEntity).goalSelector, Sets.newLinkedHashSet());
            cField.set(((EntityInsentient) nmsEntity).targetSelector, Sets.newLinkedHashSet());
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public void makePanic(org.bukkit.entity.Entity entity) {
        EntityInsentient insentient = (EntityInsentient) ((CraftEntity) entity).getHandle();
        insentient.goalSelector.a(3, new CustomPathFinderGoalPanic((EntityCreature) insentient, 0.4D));
    }


}