package io.github.kilobytz.sa.entities;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ShulkerBoss extends EntityShulker {


    public ShulkerBoss(World world) {
        super(world);
    }

    public void spawn(double x, double y, double z, World world) {
        ShulkerBoss sBoss = new ShulkerBoss(world);
        sBoss.setLocation(x,y,z,0,0);
        sBoss.setSilent(true);
        sBoss.setNoAI(true);
        sBoss.dropDeathLoot(false,0);
        sBoss.getAttributeInstance(GenericAttributes.maxHealth).setValue(20);
        sBoss.getAttributeInstance(GenericAttributes.h).setValue(0);
        DyeColor color = DyeColor.WHITE;
        sBoss.getDataWatcher().set(EntityShulker.COLOR, color.getWoolData());
        world.addEntity(sBoss);

    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.dn() == 0) {
            Entity entity = damagesource.i();
            if (entity instanceof EntityArrow) {
                this.setHealth(this.getHealth()-f);
                PacketPlayOutAnimation dmg = new PacketPlayOutAnimation(this,1);
                for(Player play : Bukkit.getOnlinePlayers()) {
                    ((CraftPlayer) play).getHandle().playerConnection.sendPacket(dmg);
                }
                return true;
            }
        }

        if (super.damageEntity(damagesource, f)) {
            if ((double) this.getHealth() < (double) this.getMaxHealth() * 0.5D && this.random.nextInt(4) == 0) {
                this.p();
            }

            return true;
        } else {
            return false;
        }
    }
    @Override
    public void die(DamageSource damagesource) {
        if (!this.aU) {
            Entity entity = damagesource.getEntity();
            EntityLiving entityliving = this.ci();

            if (this.bb >= 0 && entityliving != null) {
                entityliving.a(this, this.bb, damagesource);
            }

            if (entity != null) {
                entity.b(this);
            }

            this.aU = true;
            this.getCombatTracker().g();
            if (!this.world.isClientSide) {
                int i = 0;

                if (entity instanceof EntityHuman) {
                    i = EnchantmentManager.g((EntityLiving) entity);
                }

                if (this.isDropExperience() && this.world.getGameRules().getBoolean("doMobLoot")) {
                    boolean flag = this.lastDamageByPlayerTime > 0;

                    this.a(flag, i, damagesource);
                }
            }

            this.world.broadcastEntityEffect(this, (byte) 3);
        }
    }

}
