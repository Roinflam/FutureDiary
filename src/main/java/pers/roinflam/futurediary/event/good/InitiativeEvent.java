package pers.roinflam.futurediary.event.good;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.event.FutureEvent;

import javax.annotation.Nonnull;

//先攻
public class InitiativeEvent extends FutureEvent {

    @SubscribeEvent
    public void onLivingDamage(@Nonnull LivingDamageEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            EntityLivingBase hurter = evt.getEntityLiving();
            DamageSource damageSource = evt.getSource();
            if (damageSource.getTrueSource() instanceof EntityPlayer && hurter.getHealth() >= hurter.getMaxHealth()) {
                EntityPlayer attacker = (EntityPlayer) damageSource.getTrueSource();
                float extraDamage = evt.getAmount() * 0.75f;
                evt.setAmount(evt.getAmount() + extraDamage);
                if (extraDamage > hurter.getHealth()) {
                    extraDamage = hurter.getHealth();
                }
                int exp = (int) Math.max(1, Math.min(extraDamage * 0.15, attacker.experienceTotal * 0.075));
                hurter.world.spawnEntity(new EntityXPOrb(world, hurter.posX, hurter.posY, hurter.posZ, exp));
                this.sendReminder(attacker);
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
        return "initiative";
    }
}
