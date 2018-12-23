package cz.wake.lobby;

import cz.wake.lobby.armorstands.podlobby.CrystalBox;
import cz.wake.lobby.seasons.christmas.Kalendar;
import cz.wake.lobby.seasons.christmas.Kalendar_command;
import cz.wake.lobby.seasons.christmas.SilvesterTask;
import cz.wake.lobby.seasons.halloween.ScarePlayerTask;
import cz.wake.lobby.gadgets.morphs.*;
import cz.wake.lobby.gui.GadgetsMenu;
import cz.wake.lobby.gui.Profil;
import cz.wake.lobby.gui.Servers;
import cz.wake.lobby.armorstands.ArmorStandManager;
import cz.wake.lobby.armorstands.ArmorStandUpdateTask;
import cz.wake.lobby.commands.*;
import cz.wake.lobby.commands.servers.*;
import cz.wake.lobby.gadgets.banners.BannerAPI;
import cz.wake.lobby.gadgets.cloaks.CloaksAPI;
import cz.wake.lobby.gadgets.gadget.*;
import cz.wake.lobby.gadgets.heads.HeadsAPI;
import cz.wake.lobby.gadgets.pets.PetManager;
import cz.wake.lobby.gadgets.pets.PetsAPI;
import cz.wake.lobby.listeners.*;
import cz.wake.lobby.manager.*;
import cz.wake.lobby.settings.SettingsMenu;
import cz.wake.lobby.sql.SQLManager;
import cz.wake.lobby.utils.Log;
import cz.wake.lobby.utils.mobs.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends JavaPlugin implements PluginMessageListener {

    private static Main instance;
    private CloaksAPI cloaks = new CloaksAPI();
    private GadgetsAPI gadgets = new GadgetsAPI();
    private PetsAPI pets = new PetsAPI();
    private GadgetsMenu gMenu = new GadgetsMenu();
    private ArmorStandManager asm = new ArmorStandManager();
    private Profil m = new Profil();
    private Servers servers = new Servers();
    private TimeTask tt = new TimeTask();
    public boolean debug;
    public HashMap<Block, String> _BlocksToRestore = new HashMap();
    public static ArrayList<Entity> noFallDamageEntities = new ArrayList();
    public static ArrayList<ExplosiveSheep> explosiveSheep = new ArrayList();
    public static ArrayList<Player> preQuest = new ArrayList();
    public static ArrayList<Player> inQuest = new ArrayList();
    private static ArrayList<Player> inPortal = new ArrayList();
    private RewardsManager rm = new RewardsManager();
    private static ByteArrayOutputStream b = new ByteArrayOutputStream();
    public ArrayList<Player> at_list = new ArrayList<>();
    private String idServer;
    private SQLManager sql;
    private boolean isSilvester;
    private boolean isChristmas;
    private boolean isHalloween;

    public void onEnable() {

        // Instance
        instance = this;

        // Config
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        // Listeners
        Log.info("Nacitani listeneru...");
        loadListeners();
        loadCommands();

        // Debug rezim
        debug = false;

        // HikariCP
        Log.info("Nacitani databaze...");
        initDatabase();

        //Detekce TPS
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new LagManager(), 100L, 1L);

        // Plugin Mesages
        Log.info("Nacitani plugin messages.");
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "PlayerBalancer");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "PlayerBalancer", this);

        // Deaktivace fire + bezpecnostni odebrani vsech entit
        Log.info("Preventivni nastavovani svetu pro lobby.");
        for (World w : Bukkit.getWorlds()) {
            w.setGameRuleValue("doFireTick", "false");
            w.setGameRuleValue("doDaylightCycle", "false");
            for (Entity e : w.getEntities()) {
                if(!(e instanceof ItemFrame)){
                    e.remove();
                }
            }
        }

        // Automaticka zmena casu v lobby podle Real casu
        if (getConfig().getBoolean("timer", true)) {
            Log.info("Aktivace zmeny casy podle realneho casu.");
            tt.initTimeSetter();
        }

        // Id serveru
        idServer = getConfig().getString("server");
        Log.info("Server zaevidovany jako " + idServer);

        // Nastaveni specialnich eventu
        isChristmas = getConfig().getBoolean("seasons.christmas", false);
        isSilvester = getConfig().getBoolean("seasons.silvester", false);
        isHalloween = getConfig().getBoolean("seasons.halloween", false);

        //Register custom entit pro Pets (1.11.2)
        Log.info("Registrace custom entit pro Pets");
        CustomEntityRegistry.registerCustomEntity(92, "Cow", RideableCow.class);
        CustomEntityRegistry.registerCustomEntity(91, "Sheep", RideableSheep.class);
        CustomEntityRegistry.registerCustomEntity(93, "Chicken", RideableChicken.class);
        CustomEntityRegistry.registerCustomEntity(90, "Pig", RideablePig.class);
        CustomEntityRegistry.registerCustomEntity(60, "Silverfish", RideableSilverfish.class);
        CustomEntityRegistry.registerCustomEntity(95, "Wolf", RideableWolf.class);
        CustomEntityRegistry.registerCustomEntity(54, "Zombie", RideableZombie.class);
        CustomEntityRegistry.registerCustomEntity(101, "Rabbit", RideableRabbit.class);
        CustomEntityRegistry.registerCustomEntity(98, "Ozelot", RideableCat.class);
        CustomEntityRegistry.registerCustomEntity(100, "EntityHorse", RideableHorse.class);
        CustomEntityRegistry.registerCustomEntity(96, "MushroomCow", RideableMushroomCow.class);
        CustomEntityRegistry.registerCustomEntity(55, "Slime", RideableSlime.class);
        CustomEntityRegistry.registerCustomEntity(52, "Spider", RideableSpider.class);
        CustomEntityRegistry.registerCustomEntity(59, "CaveSpider", RideableCaveSpider.class);
        CustomEntityRegistry.registerCustomEntity(99, "VillagerGolem", RideableGolem.class);
        CustomEntityRegistry.registerCustomEntity(66, "Witch", RideableWitch.class);
        CustomEntityRegistry.registerCustomEntity(58, "Enderman", RideableEnderman.class);
        CustomEntityRegistry.registerCustomEntity(61, "Blaze", RideableBlaze.class);
        CustomEntityRegistry.registerCustomEntity(120, "Villager", RideableVillager.class);
        CustomEntityRegistry.registerCustomEntity(50, "Creeper", RideableCreeper.class);
        CustomEntityRegistry.registerCustomEntity(51, "Skeleton", RideableSkeleton.class);
        CustomEntityRegistry.registerCustomEntity(62, "LavaSlime", RideableMagmaCube.class);
        CustomEntityRegistry.registerCustomEntity(102, "PolarBear", RideableBear.class);
        CustomEntityRegistry.registerCustomEntity(68, "Guardian", RideableGuardian.class);
        CustomEntityRegistry.registerCustomEntity(36, "Vindicator", RideableVindicator.class);
        CustomEntityRegistry.registerCustomEntity(34, "Evoker", RideableEvoker.class);
        CustomEntityRegistry.registerCustomEntity(103, "Llama", RideableLlama.class);
        CustomEntityRegistry.registerCustomEntity(4, "ElderGuardian", RideableGuardianElder.class);
        CustomEntityRegistry.registerCustomEntity(27, "ZombieVillager", RideableZombieVillager.class);
        CustomEntityRegistry.registerCustomEntity(5, "WitherSkeleton", RideableWitherSkeleton.class);
        CustomEntityRegistry.registerCustomEntity(91, "Sheep", CustomSheep.class);

        // Spawn armorstandu
        Log.info("Inicializace vsech armorstandu.");
        ArmorStandManager.init();
        ArmorStandManager.spawn();
        Log.success("Vsechny armorstandy byly spawnuty.");

        getServer().getScheduler().runTaskTimer(getInstance(), new ArmorStandUpdateTask(), 200L, 1200L);

        // Update AT time
        getServer().getScheduler().runTaskTimerAsynchronously(this, new ATChecker(), 200, 1200);

        if (isHalloween) {
            Log.info("Aktivace Halloween eventu pro lobby.");
            getServer().getScheduler().runTaskTimerAsynchronously(this, new ScarePlayerTask(), 200L, 200L);
        }

        // Silvester ohnostroje
        if(isSilvester){
            Log.info("Aktivace Silvester eventu pro lobby.");
            SilvesterTask.runLauncher();
        }
    }

    public void onDisable() {
        sql.onDisable();
        instance = null;
    }

    private void loadListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(this), this);
        pm.registerEvents(new InvClick(), this);
        pm.registerEvents(new GadgetsMenu(), this);
        pm.registerEvents(new TeleportStick(this), this);
        pm.registerEvents(new FunCannon(this), this);
        pm.registerEvents(new TNTBomb(this), this);
        pm.registerEvents(new Pee(this), this);
        pm.registerEvents(new Fireworks(this), this);
        pm.registerEvents(new BlizzardBlaster(this), this);
        pm.registerEvents(new AntiGravity(this), this);
        pm.registerEvents(new Servers(), this);
        pm.registerEvents(new Trampoline(this), this);
        pm.registerEvents(new PetManager(), this);
        pm.registerEvents(new Chickenator(this), this);
        pm.registerEvents(new Tsunami(this), this);
        pm.registerEvents(new ExplosiveSheep(this), this);
        pm.registerEvents(new SlimeHat(this), this);
        pm.registerEvents(new FlowerPopper(this), this);
        pm.registerEvents(new BlackHole(this), this);
        pm.registerEvents(new BatBlaster(this), this);
        pm.registerEvents(new ColorBomb(this), this);
        pm.registerEvents(new WitherCatapult(this), this);
        pm.registerEvents(new Ghosts(this), this);
        pm.registerEvents(new PoopBomb(this), this);
        pm.registerEvents(new CookieFountain(this), this);
        pm.registerEvents(new PigFly(this), this);
        pm.registerEvents(new DiscoBall(this), this);
        pm.registerEvents(new PartyCoins(this), this);
        pm.registerEvents(new WakeArmy(this), this);
        pm.registerEvents(new CloaksAPI(), this);
        pm.registerEvents(new GadgetsAPI(), this);
        pm.registerEvents(new PetsAPI(), this);
        pm.registerEvents(new FunCannonIce(this), this);
        pm.registerEvents(new BannerAPI(), this);
        pm.registerEvents(new DiamondsFountain(this), this);
        pm.registerEvents(new Rocket(this), this);
        pm.registerEvents(new Parachute(this), this);
        pm.registerEvents(new HeadsAPI(), this);
        pm.registerEvents(new SnowBall(this), this);
        pm.registerEvents(new SettingsMenu(), this);
        pm.registerEvents(new Shop(), this);
        pm.registerEvents(new ArmorStandInteract(), this);
        pm.registerEvents(new ChatListener(), this);
        pm.registerEvents(new MorphAPI(), this);
        pm.registerEvents(new VillagerMorph(), this);
        pm.registerEvents(new PigMorph(), this);
        pm.registerEvents(new Profil(), this);
        pm.registerEvents(new SnowmanMorph(), this);
        pm.registerEvents(new ChickenMorph(), this);
        pm.registerEvents(new CrystalBox(), this);
        pm.registerEvents(new TimedResetListener(), this);
        pm.registerEvents(new RewardsManager(), this);

        if (getConfig().getString("server").equalsIgnoreCase("main")
                && pm.isPluginEnabled("RogueParkour")) {
            Log.info("Detekce a aktivace Parkour pluginu.");
            pm.registerEvents(new ParkourListener(), this);
        }

        if(getConfig().getBoolean("seasons.christmas")){
            pm.registerEvents(new Kalendar(), this);
            Log.info("Aktivace Vanocnich eventu pro lobby.");
        }
    }

    private void loadCommands() {
        getCommand("clobby").setExecutor(new Craftlobby_Command());
        getCommand("cl").setExecutor(new Craftlobby_Command()); //TODO: Proc je tu alias?
        getCommand("vip").setExecutor(new VIP_Command());
        getCommand("survival").setExecutor(new Survival_command());
        getCommand("skyblock").setExecutor(new Skyblock_command());
        getCommand("creative").setExecutor(new Creative_command());
        getCommand("prison").setExecutor(new Prison_command());
        getCommand("vanilla").setExecutor(new Vanilla_command());
        getCommand("bedwars").setExecutor(new BedWars_command());
        //getCommand("skywars").setExecutor(new SkyWars_command());
        //getCommand("murder").setExecutor(new Murder_command());
        //getCommand("vanillasb").setExecutor(new VanillaSb_command());
        getCommand("oldlobby").setExecutor(new OldLobbyCommand());
        getCommand("oldcrafttokens").setExecutor(new CraftTokens_command());

        if(getConfig().getBoolean("seasons.christmas")){
            getCommand("kalendar").setExecutor(new Kalendar_command());
        }
    }

    public static Main getInstance() {
        return instance;
    }

    public SQLManager getSQL() {
        return this.sql;
    }

    public boolean isDebug() {
        return debug;
    }

    public static Plugin getPlugin() {
        return Bukkit.getPluginManager().getPlugin("CraftLobby");
    }

    public double getTPS() {
        return LagManager.getTPS();
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equalsIgnoreCase("BungeeCord")) return;
    }

    public CloaksAPI getCloaksAPI() {
        return cloaks;
    }

    public GadgetsMenu getMainGadgetsMenu() {
        return gMenu;
    }

    public GadgetsAPI getGadgetsAPI() {
        return gadgets;
    }

    public PetsAPI getPetsAPI() {
        return pets;
    }

    public Profil getMenu() {
        return m;
    }

    public void addPortal(Player p) {
        inPortal.add(p);
    }

    public boolean inPortal(Player p) {
        return inPortal.contains(p);
    }

    public void removePortal(Player p) {
        inPortal.remove(p);
    }

    private void initDatabase() {
        sql = new SQLManager(this);
    }

    public String getIdServer() {
        return idServer;
    }

    public ArrayList<Player> getPreQuestPlayers() {
        return preQuest;
    }

    public ArrayList<Player> getInQuestPlayers() {
        return inQuest;
    }

    public ArmorStandManager getASM() {
        return asm;
    }

    public void sendToServer(Player player, String target) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
        player.sendPluginMessage(Main.getInstance(), "BungeeCord", b.toByteArray());
    }

    public boolean isSilvester() {
        return isSilvester;
    }

    public boolean isChristmas() {
        return isChristmas;
    }
}
