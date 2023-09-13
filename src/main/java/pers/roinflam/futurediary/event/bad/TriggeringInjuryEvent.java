package pers.roinflam.futurediary.event.bad;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.event.FutureEvent;
import pers.roinflam.futurediary.utils.java.random.RandomUtil;
import pers.roinflam.futurediary.utils.util.EntityUtil;

import javax.annotation.Nonnull;
import java.util.List;

//¥•∑¢–‘À…À
public class TriggeringInjuryEvent extends FutureEvent {

    @SubscribeEvent
    public void onLivingDamage(@Nonnull LivingDamageEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            DamageSource damageSource = evt.getSource();
            if (evt.getEntityLiving() instanceof EntityMob && damageSource.getImmediateSource() instanceof EntityLivingBase && RandomUtil.percentageChance(50)) {
                EntityLivingBase hurter = evt.getEntityLiving();
                EntityLivingBase attacker = (EntityLivingBase) damageSource.getImmediateSource();

                @Nonnull List<EntityMob> entities = EntityUtil.getNearbyEntities(
                        EntityMob.class,
                        hurter,
                        6,
                        entityMob -> !entityMob.equals(hurter) && !entityMob.equals(attacker)
                );
                for (@Nonnull EntityMob entityMob : entities) {
                    double x = attacker.posX - entityMob.posX;
                    double z = attacker.posZ - entityMob.posZ;
                    entityMob.attackedAtYaw = (float) (MathHelper.atan2(z, x) * (180D / Math.PI) - (double) entityMob.rotationYaw);
                    entityMob.knockBack(attacker, 0.5f, -x, -z);
                }

                this.sendReminder(attacker);
            }
        }
    }

    @Override
    public boolean isBadEvent() {
        return true;
    }

    @Override
    public boolean isGoodEvent() {
        return false;
    }

    @Override
    public boolean isCatastrophicEvent() {
        return false;
    }

    @Nonnull
    @Override
    public String getEventName() {
        return "triggering_injury";
    }
}
