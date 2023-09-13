package pers.roinflam.futurediary.event.catastrophic;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.event.FutureEvent;

import javax.annotation.Nonnull;

//¿Ö²ÀÊµÌå
public class TerroristEntityEvent extends FutureEvent {

    @SubscribeEvent
    public void onLivingAttack(@Nonnull LivingAttackEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            EntityLivingBase hurter = evt.getEntityLiving();
            DamageSource damageSource = evt.getSource();
            if (!(hurter instanceof EntityPlayer) && damageSource.getTrueSource() instanceof EntityLivingBase) {
                EntityLivingBase attacker = (EntityLivingBase) damageSource.getTrueSource();
                if (attacker.getHealth() > attacker.getMaxHealth() * 0.025) {
                    attacker.setHealth(attacker.getHealth() - attacker.getMaxHealth() * 0.025f);
                    hurter.heal(attacker.getMaxHealth() * 0.025f);
                    this.sendReminder(attacker);
                }
            }
            if (damageSource.getTrueSource() instanceof EntityLivingBase && !(damageSource.getTrueSource() instanceof EntityPlayer)) {
                EntityLivingBase attacker = (EntityLivingBase) damageSource.getTrueSource();
                attacker.heal(evt.getAmount() * 0.5f);
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
        return "terrorist_entity";
    }
}
