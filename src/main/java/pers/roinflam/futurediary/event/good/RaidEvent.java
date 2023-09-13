package pers.roinflam.futurediary.event.good;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.event.FutureEvent;

import javax.annotation.Nonnull;

//出其不意
public class RaidEvent extends FutureEvent {

    @SubscribeEvent
    public void onLivingHurt(@Nonnull LivingHurtEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            DamageSource damageSource = evt.getSource();
            if (evt.getEntityLiving() instanceof EntityLiving && damageSource.getTrueSource() instanceof EntityPlayer) {
                EntityLiving hurter = (EntityLiving) evt.getEntityLiving();
                EntityPlayer attacker = (EntityPlayer) damageSource.getTrueSource();
                if (hurter.getAttackTarget() == null || !hurter.getAttackTarget().equals(attacker)) {
                    damageSource.setDamageBypassesArmor();
                    evt.setAmount(evt.getAmount() * 1.5f);
                    this.sendReminder(attacker);
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
        return "raid";
    }
}
