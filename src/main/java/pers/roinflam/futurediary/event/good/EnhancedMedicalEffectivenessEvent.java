package pers.roinflam.futurediary.event.good;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.event.FutureEvent;

import javax.annotation.Nonnull;

//√Ó ÷ªÿ¥∫
public class EnhancedMedicalEffectivenessEvent extends FutureEvent {

    @SubscribeEvent
    public void onLivingHeal(@Nonnull LivingHealEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            if (evt.getEntityLiving() instanceof EntityPlayer) {
                EntityLivingBase healer = evt.getEntityLiving();
                evt.setAmount(evt.getAmount() * 1.75f);
                this.sendReminder(healer);
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
        return "enhanced_medical_effectiveness";
    }
}
