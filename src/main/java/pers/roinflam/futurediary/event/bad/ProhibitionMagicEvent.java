package pers.roinflam.futurediary.event.bad;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.event.FutureEvent;

import javax.annotation.Nonnull;

//½ûÄ§
public class ProhibitionMagicEvent extends FutureEvent {

    @SubscribeEvent
    public void onLivingAttack(@Nonnull LivingHurtEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            DamageSource damageSource = evt.getSource();
            if (damageSource.isMagicDamage()) {
                if (damageSource.getTrueSource() instanceof EntityLivingBase) {
                    EntityLivingBase entityLivingBase = (EntityLivingBase) damageSource.getTrueSource();
                    entityLivingBase.attackEntityFrom(DamageSource.MAGIC, evt.getAmount() * 0.33f);
                    this.sendReminder(entityLivingBase);
                    evt.setAmount(evt.getAmount() * 0.66f);
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
        return "prohibition_magic";
    }

}
