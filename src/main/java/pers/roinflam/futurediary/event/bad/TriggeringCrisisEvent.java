package pers.roinflam.futurediary.event.bad;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.base.potion.hide.PrivateHideBase;
import pers.roinflam.futurediary.event.FutureEvent;

import javax.annotation.Nonnull;

import static pers.roinflam.futurediary.event.bad.TriggeringCrisisEvent.MobEffectMovementSpeedReduced.MOVEMENT_SPEED_REDUCED;

//´¥·¢ÐÔÎ£´ù
public class TriggeringCrisisEvent extends FutureEvent {

    @SubscribeEvent
    public void onLivingDamage(@Nonnull LivingDamageEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            DamageSource damageSource = evt.getSource();
            if (damageSource.getImmediateSource() instanceof EntityLivingBase && !(damageSource.getImmediateSource() instanceof EntityPlayer)) {
                EntityLivingBase hurter = evt.getEntityLiving();

                hurter.addPotionEffect(new PotionEffect(MOVEMENT_SPEED_REDUCED, 20, 0));

                this.sendReminder(hurter);
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
        return "triggering_crisis";
    }

    static class MobEffectMovementSpeedReduced extends PrivateHideBase {
        public static final MobEffectMovementSpeedReduced MOVEMENT_SPEED_REDUCED = new MobEffectMovementSpeedReduced(true, 0xdd7e6b, "triggering_crisis_MobEffectMovementSpeedReduced");

        protected MobEffectMovementSpeedReduced(boolean isBadEffectIn, int liquidColorIn, @Nonnull String name) {
            super(isBadEffectIn, liquidColorIn, name);
            this.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "1793a9f7-d981-1821-b79c-3b0f337badcf", -0.90, 2);
        }

    }
}
