package pers.roinflam.futurediary.event.catastrophic;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.base.potion.hide.PrivateHideBase;
import pers.roinflam.futurediary.event.FutureEvent;

import javax.annotation.Nonnull;

import static pers.roinflam.futurediary.event.catastrophic.DepthDequantizationEvent.MobEffectDepthDequantization.DEPTH_DEQUANTIZATION;

//去量深化
public class DepthDequantizationEvent extends FutureEvent {

    @SubscribeEvent
    public void onBreakSpeed(@Nonnull PlayerEvent.BreakSpeed evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            evt.setNewSpeed(evt.getNewSpeed() * 0.5f);
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(@Nonnull LivingEvent.LivingUpdateEvent evt) {
        if (!evt.getEntityLiving().world.isRemote) {
            World world = evt.getEntityLiving().getEntityWorld();
            if (!this.isEnableEvent(world)) {
                return;
            }
            EntityLivingBase entityLivingBase = evt.getEntityLiving();
            if (world.getTotalWorldTime() % 20 == 0) {
                entityLivingBase.addPotionEffect(new PotionEffect(DEPTH_DEQUANTIZATION, 30, 0));
                this.sendReminder(entityLivingBase);
            }
        }
    }

    @Override
    public boolean isBadEvent() {
        return false;
    }

    @Override
    public boolean isGoodEvent() {
        return false;
    }

    @Override
    public boolean isCatastrophicEvent() {
        return true;
    }

    @Nonnull
    @Override
    public String getEventName() {
        return "depth_dequantization";
    }

    static class MobEffectDepthDequantization extends PrivateHideBase {
        public static final MobEffectDepthDequantization DEPTH_DEQUANTIZATION = new MobEffectDepthDequantization(true, 0, "depth_dequantization_MobEffectDepthDequantization");

        protected MobEffectDepthDequantization(boolean isBadEffectIn, int liquidColorIn, @Nonnull String name) {
            super(isBadEffectIn, liquidColorIn, name);
            this.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE, "0b105dea-61fa-089d-0053-f07cdb963783", 5, 2);
            this.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "4858cb84-bd80-f038-9fdb-92611627212a", -0.5, 2);
            this.registerPotionAttributeModifier(SharedMonsterAttributes.FLYING_SPEED, "270e2b9f-69b6-d8ba-a9d9-74b0972f36a8", -0.5, 2);
            this.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, "d9be1d14-dabd-44c8-42ca-4ecf276449c9", -0.5, 2);
        }

    }
}
