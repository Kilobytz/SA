package io.github.kilobytz.sa;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import io.github.kilobytz.sa.command.DelWarp;
import io.github.kilobytz.sa.command.Heal;
import io.github.kilobytz.sa.command.Muteall;
import io.github.kilobytz.sa.command.OpenEnderChest;
import io.github.kilobytz.sa.command.PersonalSpawnpoint;
import io.github.kilobytz.sa.command.PvPToggle;
import io.github.kilobytz.sa.command.RankCom;
import io.github.kilobytz.sa.command.SetWarp;
import io.github.kilobytz.sa.command.Spawn;
import io.github.kilobytz.sa.command.Suicide;
import io.github.kilobytz.sa.command.Tip;
import io.github.kilobytz.sa.command.Warp;
import io.github.kilobytz.sa.command.WorldTP;
import io.github.kilobytz.sa.entities.EntityManager;
import io.github.kilobytz.sa.gui.GUIListener;
import io.github.kilobytz.sa.gui.ItemNMSRegistry;
import io.github.kilobytz.sa.gui.WarpEditManager;
import io.github.kilobytz.sa.misc.CollisionManager;
import io.github.kilobytz.sa.misc.Grapple;
import io.github.kilobytz.sa.misc.NoInteracting;
import io.github.kilobytz.sa.misc.Pistons;
import io.github.kilobytz.sa.misc.WorldListener;
import io.github.kilobytz.sa.misc.WorldLoader;
import io.github.kilobytz.sa.players.PlayerListener;
import io.github.kilobytz.sa.players.PlayerManager;
import io.github.kilobytz.sa.tips.TipManager;
import io.github.kilobytz.sa.warping.WarpHandling;


public class SA extends JavaPlugin {

    GlobalValues values = new GlobalValues();
    WarpHandling wH = new WarpHandling(this);
    EntityManager eM = new EntityManager();
    NoInteracting pNH = new NoInteracting();
    Warp warp = new Warp();
    SetWarp setWarp = new SetWarp();
    DelWarp delWarp = new DelWarp();
    Suicide suicide = new Suicide();
    PersonalSpawnpoint sps = new PersonalSpawnpoint();
    OpenEnderChest eC = new OpenEnderChest();
    Spawn spawn = new Spawn();
    Heal heal = new Heal();
    TipManager tM = new TipManager(this);
    Tip tip = new Tip();
    RankCom rank = new RankCom();
    Pistons pst = new Pistons();
    PlayerListener pL = new PlayerListener();
    PlayerManager pM = new PlayerManager(this);
    Grapple grapple = new Grapple(this);
    CollisionManager cM = new CollisionManager(pM);
    boolean delayLogin = true;
    boolean firstLogin = false;
    Muteall muteall = new Muteall();
    PvPToggle pvpTog = new PvPToggle();
    WorldLoader wLo = new WorldLoader(this);
    WorldTP wTP = new WorldTP();
    WorldListener wLi = new WorldListener();
    ItemNMSRegistry itemReg = new ItemNMSRegistry();
    WarpEditManager WeM = new WarpEditManager(this,wH);
    GUIListener guiL = new GUIListener(itemReg,WeM);


    boolean dbOn = false;

    String host, port, database, username, password;

    public static Connection connection;


    @Override
    public void onEnable() {
        createConfig();
        setSQL();
        registerListeners();
        registerCommands();
        classSetups();
        tM.startTips();
        setPermMessages();
        loginDelay();
        tM.loadTips();
        wH.loadWarps();
        setupItemActions();
        WeM.GUIWarpSetup();
    }

    @Override
    public void onDisable() {
        wH.saveWarps();
        tM.saveTips();
        WeM.saveGuiWarps();
    }

    public void setSQL() {
        try{
            host = (this.getConfig().get("database." + "host")).toString();
            port = (this.getConfig().get("database." + "port")).toString();
            database = (this.getConfig().get("database." + "database")).toString();
            username = (this.getConfig().get("database." + "username")).toString();
            password = (this.getConfig().get("database." + "password")).toString();
            dbOn = true;
        }catch(NullPointerException e) {
            getLogger().info("No Database found, some things may not be saved.");
        }
    }

    public void registerCommands() {
        this.getCommand("warp").setExecutor(this.warp);
        this.getCommand("setwarp").setExecutor(this.setWarp);
        this.getCommand("delwarp").setExecutor(this.delWarp);
        this.getCommand("suicide").setExecutor(this.suicide);
        this.getCommand("setpersonalspawnpoint").setExecutor(this.sps);
        this.getCommand("enderchest").setExecutor(this.eC);
        this.getCommand("spawn").setExecutor(this.spawn);
        this.getCommand("heal").setExecutor(this.heal);
        this.getCommand("tip").setExecutor(this.tip);
        this.getCommand("rank").setExecutor(this.rank);
        this.getCommand("muteall").setExecutor(this.muteall);
        this.getCommand("pvptoggle").setExecutor(this.pvpTog);
        this.getCommand("worldtp").setExecutor(this.wTP);
        
        Material mat = Material.CONCRETE;
    }



    public void setPermMessages() {
        String ob = ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrator if you believe that this is in error.";
        this.getCommand("warp").setPermissionMessage(ob);
        this.getCommand("setwarp").setPermissionMessage(ob);
        this.getCommand("delwarp").setPermissionMessage(ob);
        this.getCommand("suicide").setPermissionMessage(ob);
        this.getCommand("setpersonalspawnpoint").setPermissionMessage(ob);
        this.getCommand("enderchest").setPermissionMessage(ob);
        this.getCommand("spawn").setPermissionMessage(ob);
        this.getCommand("heal").setPermissionMessage(ob);
        this.getCommand("tip").setPermissionMessage(ob);
        this.getCommand("muteall").setPermissionMessage(ob);
        this.getCommand("pvptoggle").setPermissionMessage(ob);
        this.getCommand("worldtp").setPermissionMessage(ob);
    }

    public void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(this.cM, this);
        pluginManager.registerEvents(this.eM, this);
        pluginManager.registerEvents(this.pNH, this);
        pluginManager.registerEvents(this.pL, this);
        pluginManager.registerEvents(this.pst, this);
        pluginManager.registerEvents(this.grapple, this);
        pluginManager.registerEvents(this.guiL, this);
    }

    public void classSetups() {
        warp.setup(wH);
        setWarp.setup(wH);
        delWarp.setup(wH);
        tip.setTipData(tM);
        pL.setRanks(pM,this);
        rank.setRankData(pM);
        pNH.setRanks(pM, this);
        muteall.setup(pL);
        pvpTog.setup(pNH);
        wTP.setup(wLo);
        wLi.setInfo(wH, this);
        delTeams();
    }

    public void setupItemActions(){
        itemReg.registerItem("lonesword", (player,object) -> {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 0));
        });
        itemReg.registerItem("warpeditor", (player,object) -> {
            ((WarpEditManager)object).openFirstEditorPage(player);
        });
        itemReg.setActionObject("warpeditor",WeM);
        itemReg.registerItem("warper", (player,object) -> {
            ((WarpEditManager)object).openFirstWarperPage(player);
        });
        itemReg.setActionObject("warper",WeM);
    }

    public void openConnection() throws SQLException, ClassNotFoundException {
        if(!dbOn){
            throw new NumberFormatException();
        }
        if (connection != null && !connection.isClosed()) {
            return;
        }
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://"
                + this.host + ":" + this.port + "/" + this.database,
                this.username, this.password);
    }
    public boolean isDbOn(){
        return dbOn;
    }

    public void warpDelete(String warpName){
        WeM.warpDelete(warpName);
    }


    static void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);
  
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
  
        field.set(null, newValue);
     }

     public void delTeams(){
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();
        for(Team team : board.getTeams()){
            if(!values.getAllRanks().contains(team.getName())){
                team.unregister();
            }
        }
     }

    public void loginDelay() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

            @Override
            public void run() {
                delayLogin = false;
            }
        },20L);
    }

    public boolean getDelayLogin() {
        return delayLogin;
    }

    private void createConfig() {
            try {
                if (!getDataFolder().exists()) {
                    getDataFolder().mkdirs();
                }
                File file = new File(getDataFolder(), "config.yml");
                if (!file.exists()) {
                    getLogger().info("config.yml not found, creating!");
                    saveDefaultConfig();
                } else {
                    getLogger().info("config.yml found, loading!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }


}