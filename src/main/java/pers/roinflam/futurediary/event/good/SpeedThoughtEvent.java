package pers.roinflam.futurediary.event.good;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.base.potion.hide.PrivateHideBase;
import pers.roinflam.futurediary.event.FutureEvent;

import javax.annotation.Nonnull;

import static pers.roinflam.futurediary.event.good.SpeedThoughtEvent.MobEffectSpeedBoost.SPEED_BOOST;
import static pers.roinflam.futurediary.event.good.SpeedThoughtEvent.MobEffectSpeedReduction.SPEED_REDUCTION;


//Ë¼Ð÷¼ÓËÙ
public class SpeedThoughtEvent extends FutureEvent {

    @SubscribeEvent
    public void onLivingUpdate(@Nonnull LivingEvent.LivingUpdateEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().getEntityWorld();
            if (!this.isEnableEvent(world)) {
                return;
            }
            EntityLivingBase entityLivingBase = evt.getEntityLiving();
            if (world.getTotalWorldTime() % 20 == 0) {
                if (entityLivingBase instanceof EntityPlayer) {
                    entityLivingBase.addPotionEffect(new PotionEffect(SPEED_BOOST, 30, 0));
                    this.sendReminder(entityLivingBase);
                } else {
                    entityLivingBase.addPotionEffect(new PotionEffect(SPEED_REDUCTION, 30, 0));
                }
            }
        }
    }

    @Override
    public boolean isBadEvent() {
        return false;
    }

    @Override
    public boolean isGoodEvent() {
        return true;
    }

    @Override
    public boolean isCatastrophicEvent() {
        return false;
    }

    @Nonnull
    @Override
    public String getEventName() {
        return "speed_thought";
    }

    static class MobEffectSpeedReduction extends PrivateHideBase {
        public static final MobEffectSpeedReduction SPEED_REDUCTION = new MobEffectSpeedReduction(true, 0, "speed_thought_MobEffectSpeedReduction");

        protected MobEffectSpeedReduction(boolean isBadEffectIn, int liquidColorIn, @Nonnull String name) {
            super(isBadEffectIn, liquidColorIn, name);
            this.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "4846a40c-00c5-e83a-e00b-cb4a014d3234", -0.2, 2);
            this.registerPotionAttributeModifier(SharedMonsterAttributes.FLYING_SPEED, "3a844b06-c189-291a-2d0f-fe4246ac3751", -0.2, 2);
            this.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, "68ab4508-b5be-e2c7-d46f-7815f2f3a957", -0.2, 2);
        }

    }

    static class MobEffectSpeedBoost extends PrivateHideBase {
        public static final MobEffectSpeedBoost SPEED_BOOST = new MobEffectSpeedBoost(false, 0, "speed_thought_MobEffectSpeedBoost");

        protected MobEffectSpeedBoost(boolean isBadEffectIn, int liquidColorIn, @Nonnull String name) {
            super(isBadEffectIn, liquidColorIn, name);
            this.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "2e280a0e-c8f6-ba5a-c8be-4657d3438d8a", 0.2, 2);
            this.registerPotionAttributeModifier(SharedMonsterAttributes.FLYING_SPEED, "8c90ee61-2cb0-8acd-98cb-53c43334d351", 0.2, 2);
            this.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, "9da09699-c478-ee3d-1976-2d8ec7a5c371", 0.2, 2);
        }

    }
}
