package pers.roinflam.futurediary.event.good;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.event.FutureEvent;

import javax.annotation.Nonnull;

//圣光赐予我力量
public class LightPowerEvent extends FutureEvent {

    @SubscribeEvent
    public void onLivingHurt(@Nonnull LivingHurtEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            EntityLivingBase hurter = evt.getEntityLiving();
            DamageSource damageSource = evt.getSource();
            if (damageSource.getTrueSource() instanceof EntityPlayer && hurter.world.getLight(hurter.getPosition()) > 10) {
                EntityPlayer entityPlayer = (EntityPlayer) damageSource.getTrueSource();
                evt.setAmount(evt.getAmount() * 1.375f);
                entityPlayer.heal(entityPlayer.getMaxHealth() * 0.1f);
                this.sendReminder(entityPlayer);
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
        return "light_power";
    }
}
