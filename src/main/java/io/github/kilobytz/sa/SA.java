package io.github.kilobytz.sa;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import io.github.kilobytz.sa.command.DelWarp;
import io.github.kilobytz.sa.command.Heal;
import io.github.kilobytz.sa.command.RankCom;
import io.github.kilobytz.sa.command.SetWarp;
import io.github.kilobytz.sa.command.Spawn;
import io.github.kilobytz.sa.command.SpawnShulkerBoss;
import io.github.kilobytz.sa.command.Suicide;
import io.github.kilobytz.sa.command.Tip;
import io.github.kilobytz.sa.command.Warp;
import io.github.kilobytz.sa.commandfunctions.CollisionManager;
import io.github.kilobytz.sa.commandfunctions.WarpHandling;
import io.github.kilobytz.sa.entities.EntityManager;
import io.github.kilobytz.sa.entities.ShulkerBoss;
import io.github.kilobytz.sa.misc.CompassWarp;
import io.github.kilobytz.sa.misc.Grapple;
import io.github.kilobytz.sa.misc.KittyDrugs;
import io.github.kilobytz.sa.misc.NoInteracting;
import io.github.kilobytz.sa.misc.Pistons;
import io.github.kilobytz.sa.players.PlayerManager;
import io.github.kilobytz.sa.players.PlayerListener;
import io.github.kilobytz.sa.tips.TipManager;
import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.EntityShulker;
import net.minecraft.server.v1_12_R1.EntityTypes;
import net.minecraft.server.v1_12_R1.MinecraftKey;


public class SA extends JavaPlugin {

    GlobalValues values = new GlobalValues();
    WarpHandling wH = new WarpHandling(this);
    EntityManager eM = new EntityManager();
    NoInteracting pNH = new NoInteracting();
    Warp warp = new Warp();
    SetWarp setWarp = new SetWarp();
    DelWarp delWarp = new DelWarp();
    Suicide suicide = new Suicide();
    Spawn spawn = new Spawn();
    Heal heal = new Heal();
    SpawnShulkerBoss sBoss = new SpawnShulkerBoss();
    TipManager tM = new TipManager(this);
    Tip tip = new Tip();
    RankCom rank = new RankCom();
    Pistons pst = new Pistons();
    PlayerListener pL = new PlayerListener();
    PlayerManager pM = new PlayerManager(this);
    Grapple grapple = new Grapple(this);
    CollisionManager cM = new CollisionManager(pM);
    CompassWarp cWarp = new CompassWarp(this,wH);
    KittyDrugs crack = new KittyDrugs();
    boolean delayLogin = true;

    String host, port, database, username, password;

    public static Connection connection;


    @Override
    public void onEnable() {
        setSQL();
        createConfig();
        registerListeners();
        registerCommands();
        classSetups();
        CustomEntities.registerEntities();
        startTips();
        setPermMessages();
        loginDelay();
    }

    @Override
    public void onDisable() {
        
        
    }

    public void setSQL() {
        try{
            host = (this.getConfig().get("database." + "host")).toString();
            port = (this.getConfig().get("database." + "port")).toString();
            database = (this.getConfig().get("database." + "database")).toString();
            username = (this.getConfig().get("database." + "username")).toString();
            password = (this.getConfig().get("database." + "password")).toString();
        }catch(NullPointerException e) {}
    }

    public void registerCommands() {
        this.getCommand("warp").setExecutor(this.warp);
        this.getCommand("setwarp").setExecutor(this.setWarp);
        this.getCommand("delwarp").setExecutor(this.delWarp);
        this.getCommand("ded").setExecutor(this.suicide);
        this.getCommand("spawn").setExecutor(this.spawn);
        this.getCommand("heal").setExecutor(this.heal);
        this.getCommand("spawnboss").setExecutor(this.sBoss);
        this.getCommand("tip").setExecutor(this.tip);
        this.getCommand("rank").setExecutor(this.rank);
    }

    public void setPermMessages() {
        String ob = ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrator if you believe that this is in error.";
        this.getCommand("warp").setPermissionMessage(ob);
        this.getCommand("setwarp").setPermissionMessage(ob);
        this.getCommand("delwarp").setPermissionMessage(ob);
        this.getCommand("ded").setPermissionMessage(ob);
        this.getCommand("spawn").setPermissionMessage(ob);
        this.getCommand("heal").setPermissionMessage(ob);
        this.getCommand("spawnboss").setPermissionMessage(ob);
        this.getCommand("tip").setPermissionMessage(ob);
    }

    public void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(this.cM, this);
        pluginManager.registerEvents(this.eM, this);
        pluginManager.registerEvents(this.pNH, this);
        pluginManager.registerEvents(this.pL, this);
        pluginManager.registerEvents(this.pst, this);
        pluginManager.registerEvents(this.grapple, this);
        pluginManager.registerEvents(this.cWarp, this);
        pluginManager.registerEvents(this.crack, this);   
    }

    public void classSetups() {
        warp.setup(wH);
        setWarp.setup(wH);
        delWarp.setup(wH);
        tip.setTipData(tM);
        pL.setRanks(pM,this);
        rank.setRankData(pM);
        pNH.setRanks(pM, this);

    }

    public void openConnection() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return;
        }
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://"
                + this.host + ":" + this.port + "/" + this.database,
                this.username, this.password);
    }



    public void startTips() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

            @Override
            public void run() {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    if(tM.numOfTips() != 0) {
                        p.sendMessage(ChatColor.DARK_PURPLE + "TIP: " + tM.getTip());
                    }
                }
            }
        },50L, 3000);
    }

    static void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);
  
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
  
        field.set(null, newValue);
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

    public enum CustomEntities {

        SHULKERBOSS("Corrupted Ice", 69, EntityType.SHULKER, EntityShulker.class, ShulkerBoss.class);

        private String name;
        private int id;
        private EntityType entityType;
        private Class<? extends Entity> nmsClass;
        private Class<? extends Entity> customClass;
        private MinecraftKey key;
        private MinecraftKey oldKey;

        private CustomEntities(String name, int id, EntityType entityType, Class<? extends Entity> nmsClass, Class<? extends Entity> customClass) {
            this.name = name;
            this.id = id;
            this.entityType = entityType;
            this.nmsClass = nmsClass;
            this.customClass = customClass;
            this.key = new MinecraftKey(name);
            this.oldKey = EntityTypes.b.b(nmsClass);
        }

        public static void registerEntities() { for (CustomEntities ce : CustomEntities.values()) ce.register(); }
        public static void unregisterEntities() { for (CustomEntities ce : CustomEntities.values()) ce.unregister(); }

        private void register() {
            EntityTypes.d.add(key);
            EntityTypes.b.a(id, key, customClass);
        }

        private void unregister() {
            EntityTypes.d.remove(key);
            EntityTypes.b.a(id, oldKey, nmsClass);
        }

        public String getName() {
            return name;
        }

        public int getID() {
            return id;
        }

        public EntityType getEntityType() {
            return entityType;
        }

        public Class<?> getCustomClass() {
            return customClass;
        }
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