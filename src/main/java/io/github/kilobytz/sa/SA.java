package io.github.kilobytz.sa;

import io.github.kilobytz.sa.command.*;
import io.github.kilobytz.sa.entities.EntityManager;
import io.github.kilobytz.sa.entities.ShulkerBoss;
import io.github.kilobytz.sa.commandfunctions.CollisionManager;
import io.github.kilobytz.sa.commandfunctions.WarpHandling;
import io.github.kilobytz.sa.misc.NoInteracting;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;


public class SA extends JavaPlugin {

    WarpHandling wH = new WarpHandling(this);
    CollisionManager cM = new CollisionManager();
    EntityManager eM = new EntityManager();
    NoInteracting pNH = new NoInteracting();
    Warp warp = new Warp();
    SetWarp setWarp = new SetWarp();
    DelWarp delWarp = new DelWarp();
    Suicide suicide = new Suicide();
    Spawn spawn = new Spawn();
    Heal heal = new Heal();
    SpawnShulkerBoss sBoss = new SpawnShulkerBoss();
    Boom boom = new Boom();

    @Override
    public void onEnable() {
        createConfig();
        registerListeners();
        registerCommands();
        classSetups();
        CustomEntities.registerEntities();
    }

    @Override
    public void onDisable() {

    }

    public void registerCommands() {
        this.getCommand("warp").setExecutor(this.warp);
        this.getCommand("setwarp").setExecutor(this.setWarp);
        this.getCommand("delwarp").setExecutor(this.delWarp);
        this.getCommand("ded").setExecutor(this.suicide);
        this.getCommand("spawn").setExecutor(this.spawn);
        this.getCommand("heal").setExecutor(this.heal);
        this.getCommand("spawnboss").setExecutor(this.sBoss);
        this.getCommand("boom").setExecutor(this.boom);


    }

    public void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(this.cM, this);
        pluginManager.registerEvents(this.eM, this);
        pluginManager.registerEvents(this.pNH, this);
    }

    public void classSetups() {
        cM.setTeamConfig();
        warp.setup(wH);
        setWarp.setup(wH);
        delWarp.setup(wH);

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