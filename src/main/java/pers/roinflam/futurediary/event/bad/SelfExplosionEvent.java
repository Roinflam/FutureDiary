package pers.roinflam.futurediary.event.bad;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.event.FutureEvent;
import pers.roinflam.futurediary.utils.java.random.RandomUtil;

import javax.annotation.Nonnull;

//后备隐藏能源
public class SelfExplosionEvent extends FutureEvent {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLivingDeath(@Nonnull LivingDeathEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            EntityLivingBase dead = evt.getEntityLiving();
            DamageSource damageSource = evt.getSource();
            if (damageSource.getTrueSource() instanceof EntityLivingBase) {
                this.sendReminder((EntityLivingBase) damageSource.getTrueSource());
            }
            if (RandomUtil.percentageChance(3)) {
                dead.world.newExplosion(dead, dead.posX, dead.posY, dead.posZ, 4, false, false);
                return;
            }
            if (RandomUtil.percentageChance(10)) {
                dead.world.newExplosion(dead, dead.posX, dead.posY, dead.posZ, 3, false, false);
                return;
            }
            if (RandomUtil.percentageChance(30)) {
                dead.world.newExplosion(dead, dead.posX, dead.posY, dead.posZ, 2, false, false);
                return;
            }
            dead.world.newExplosion(dead, dead.posX, dead.posY, dead.posZ, 1, false, false);
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
        return "self_explosion";
    }
}
