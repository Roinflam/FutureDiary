package pers.roinflam.futurediary.event.bad;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.event.FutureEvent;

import javax.annotation.Nonnull;

//ÅÔÇÃ²à»÷
public class DetailEvent extends FutureEvent {

    @SubscribeEvent
    public void onLivingHurt(@Nonnull LivingHurtEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            EntityLivingBase entityLivingBase = evt.getEntityLiving();
            evt.setAmount(evt.getAmount() + entityLivingBase.getMaxHealth() * 0.01f);
            this.sendReminder(entityLivingBase);
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
        return "detail";
    }

}
