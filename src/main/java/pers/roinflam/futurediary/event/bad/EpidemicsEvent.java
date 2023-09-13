package pers.roinflam.futurediary.event.bad;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.event.FutureEvent;
import pers.roinflam.futurediary.init.FutureDiaryPotion;
import pers.roinflam.futurediary.utils.util.EntityUtil;

import javax.annotation.Nonnull;
import java.util.List;

//Œ¡“ﬂ
public class EpidemicsEvent extends FutureEvent {

    @SubscribeEvent
    public void onLivingDamage(@Nonnull LivingDamageEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            DamageSource damageSource = evt.getSource();
            Entity attacker = damageSource.getImmediateSource();
            if (attacker instanceof EntityLivingBase && !(attacker instanceof EntityPlayer) && evt.getEntityLiving() instanceof EntityPlayer) {
                EntityPlayer hurter = (EntityPlayer) evt.getEntityLiving();
                hurter.addPotionEffect(new PotionEffect(FutureDiaryPotion.EPIDEMICS, 200, 0));
                this.sendReminder(hurter);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLivingDeath(@Nonnull LivingDeathEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            if (evt.getEntityLiving() instanceof EntityPlayer) {
                EntityPlayer entityPlayer = (EntityPlayer) evt.getEntityLiving();
                if (entityPlayer.getActivePotionEffect(FutureDiaryPotion.EPIDEMICS) != null) {
                    @Nonnull List<EntityPlayer> entityPlayers = EntityUtil.getNearbyEntities(
                            EntityPlayer.class,
                            entityPlayer,
                            16,
                            otherPlayer -> !otherPlayer.equals(entityPlayer)
                    );
                    for (@Nonnull EntityPlayer otherPlayer : entityPlayers) {
                        otherPlayer.addPotionEffect(new PotionEffect(FutureDiaryPotion.EPIDEMICS, 200, 0));
                        this.sendReminder(otherPlayer);
                    }
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
        return "epidemics";
    }


}
