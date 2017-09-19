package cz.wake.lobby.armorstands.servers;

import cz.wake.lobby.Main;
import cz.wake.lobby.armorstands.ASInterface;
import cz.wake.lobby.utils.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

public class GetDown implements ASInterface {

    private Location loc1 = new Location(Bukkit.getWorld("omain"), 1524.5, 18, -1216.5, -90, 0);
    ArmorStand as, as2, as3;

    @Override
    public void spawn() {

        as = (ArmorStand) Bukkit.getWorld("omain").spawnEntity(loc1, EntityType.ARMOR_STAND);

        as.setGravity(false);
        as.setVisible(true);
        as.setCanPickupItems(false);
        as.setBasePlate(false);
        as.setArms(true);

        // x - nahoru/dolu, y - rotace, z - otáčení v ose

        as.setRightArmPose(new EulerAngle(5.8, 0, 0.3));

        ItemStack head = ItemFactory.createHead("test", "daaef8ad-4532-4cc8-bb84-7f8947add9a5", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjg2Yzg0Y2E1OTljYjQ0MGY4ZDkzMTM4ZWQwZmYxZjJiMTM2ZGZhZWRiMWRkNDZlZjFhODVjYzM5ZGNjYzMifX19");
        as.setHelmet(head);

        ItemStack chestplate = ItemFactory.createColouredLeather(Material.LEATHER_CHESTPLATE, 232, 239, 54);
        as.setChestplate(chestplate);

        ItemStack leggins = ItemFactory.createColouredLeather(Material.LEATHER_LEGGINGS, 232, 239, 54);
        as.setLeggings(leggins);

        ItemStack boots = ItemFactory.createColouredLeather(Material.LEATHER_BOOTS, 232, 239, 54);
        as.setBoots(boots);

        ItemStack item = new ItemStack(Material.IRON_BOOTS);
        as.setItemInHand(item);

        Main.getInstance().getASM().setMetadata((ArmorStand) as, "getdown", "getdown", Main.getInstance());

        subtextSpawn();
        hologramSpawn();

    }

    @Override
    public void hologramSpawn() {

        loc1.add(0, 0.3, 0);

        as2 = (ArmorStand) Bukkit.getWorld("omain").spawnEntity(loc1, EntityType.ARMOR_STAND);

        as2.setGravity(false);
        as2.setCanPickupItems(false);
        as2.setBasePlate(false);
        as2.setVisible(false);
        as2.setCustomNameVisible(true);

        as2.setCustomName("§e§lGetDown");
    }

    @Override
    public void subtextSpawn() {

        loc1.add(0, 0.3, 0);

        as3 = (ArmorStand) Bukkit.getWorld("omain").spawnEntity(loc1, EntityType.ARMOR_STAND);

        as3.setGravity(false);
        as3.setCanPickupItems(false);
        as3.setBasePlate(false);
        as3.setVisible(false);
        as3.setCustomNameVisible(true);

        as3.setCustomName("§7Nacitani...");

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void remove() {
        as.remove();
        as2.remove();
        as3.remove();
    }

    @Override
    public void updateArmorStand(String s, int i) {
        as3.setCustomName(Main.getInstance().getASM().formatPlayers(i));
    }
}