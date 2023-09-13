package pers.roinflam.futurediary.event.bad;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.event.FutureEvent;

import javax.annotation.Nonnull;

//¼¬´Ì
public class ThornEvent extends FutureEvent {

    @SubscribeEvent
    public void onLivingHurt(@Nonnull LivingHurtEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            if (evt.getSource() instanceof EntityDamageSource) {
                EntityDamageSource entityDamageSource = (EntityDamageSource) evt.getSource();
                if (!entityDamageSource.getIsThornsDamage() && entityDamageSource.getImmediateSource() instanceof EntityLivingBase) {
                    EntityLivingBase hurter = evt.getEntityLiving();
                    EntityLivingBase attacker = (EntityLivingBase) entityDamageSource.getImmediateSource();
                    attacker.attackEntityFrom(DamageSource.causeThornsDamage(hurter), hurter.getMaxHealth() * 0.05f + attacker.getMaxHealth() * 0.05f);
                    this.sendReminder(hurter);
                }
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
        return "thorn";
    }
}
