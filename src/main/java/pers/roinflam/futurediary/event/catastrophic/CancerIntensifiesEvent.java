package pers.roinflam.futurediary.event.catastrophic;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.event.FutureEvent;

import javax.annotation.Nonnull;

//»û±ä¼Ó¾ç
public class CancerIntensifiesEvent extends FutureEvent {

    @SubscribeEvent
    public void onLivingUpdate(@Nonnull LivingEvent.LivingUpdateEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntityLiving().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            if (world.getTotalWorldTime() % 200 == 0) {
                EntityLivingBase entityLivingBase = evt.getEntityLiving();
                entityLivingBase.heal(entityLivingBase.getMaxHealth() * 0.1f);
            }
        }
    }

    @SubscribeEvent
    public void onLivingHurt(@Nonnull LivingHurtEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            EntityLivingBase hurter = evt.getEntityLiving();
            DamageSource damageSource = evt.getSource();
            if (!(hurter instanceof EntityPlayer)) {
                evt.setAmount(evt.getAmount() * 0.5f);
                if (damageSource.getTrueSource() instanceof EntityPlayer) {
                    this.sendReminder((EntityLivingBase) damageSource.getTrueSource());
                }
            }
            if (damageSource.getTrueSource() instanceof EntityLivingBase && !(damageSource.getTrueSource() instanceof EntityPlayer)) {
                EntityLivingBase attacker = (EntityLivingBase) damageSource.getTrueSource();
                damageSource.setDamageBypassesArmor();
                evt.setAmount(evt.getAmount() * 2);
                if (hurter instanceof EntityPlayer) {
                    this.sendReminder(hurter);
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
        return false;
    }

    @Override
    public boolean isCatastrophicEvent() {
        return true;
    }

    @Nonnull
    @Override
    public String getEventName() {
        return "cancer_intensifies";
    }
}
