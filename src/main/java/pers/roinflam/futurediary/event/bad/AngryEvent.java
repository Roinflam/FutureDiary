package pers.roinflam.futurediary.event.bad;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.base.potion.hide.PrivateHideBase;
import pers.roinflam.futurediary.event.FutureEvent;
import pers.roinflam.futurediary.utils.java.random.RandomUtil;

import javax.annotation.Nonnull;

import static pers.roinflam.futurediary.event.bad.AngryEvent.MobEffectMovementSpeedReduced.MOVEMENT_SPEED_REDUCED;

//дуе╜
public class AngryEvent extends FutureEvent {

    @SubscribeEvent
    public void onLivingAttack(@Nonnull LivingAttackEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            DamageSource damageSource = evt.getSource();
            if (!(damageSource.getImmediateSource() instanceof EntityPlayer) && damageSource.getTrueSource() instanceof EntityPlayer && RandomUtil.percentageChance(10)) {
                EntityLivingBase attacker = (EntityLivingBase) damageSource.getTrueSource();
                EntityLivingBase hurter = evt.getEntityLiving();
                attacker.attackEntityFrom(DamageSource.causeMobDamage(hurter), attacker.getHealth() * 0.33f);
                hurter.setPositionAndUpdate(attacker.posX, attacker.posY, attacker.posZ);
                attacker.addPotionEffect(new PotionEffect(MOVEMENT_SPEED_REDUCED, 60, 0));
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
        return "angry";
    }

    static class MobEffectMovementSpeedReduced extends PrivateHideBase {
        public static final MobEffectMovementSpeedReduced MOVEMENT_SPEED_REDUCED = new MobEffectMovementSpeedReduced(true, 0, "angry_MobEffectMovementSpeedReduced");

        protected MobEffectMovementSpeedReduced(boolean isBadEffectIn, int liquidColorIn, @Nonnull String name) {
            super(isBadEffectIn, liquidColorIn, name);
            this.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "BE155388-F24D-9CDC-A238-BB4E709D2936", -0.4, 2);
        }

    }
}
