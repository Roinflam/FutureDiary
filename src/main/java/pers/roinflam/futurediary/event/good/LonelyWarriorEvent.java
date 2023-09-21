package pers.roinflam.futurediary.event.good;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.event.FutureEvent;
import pers.roinflam.futurediary.utils.util.EntityUtil;

import javax.annotation.Nonnull;
import java.util.List;

//µÇ½¢Ð¡×é
public class LonelyWarriorEvent extends FutureEvent {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLivingHurt(@Nonnull LivingHurtEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            if (evt.getEntityLiving() instanceof EntityPlayer) {
                EntityPlayer entityPlayer = (EntityPlayer) evt.getEntityLiving();
                @Nonnull List<EntityPlayer> entityPlayers = EntityUtil.getNearbyEntities(
                        EntityPlayer.class,
                        entityPlayer,
                        32,
                        otherPlayer -> !otherPlayer.equals(entityPlayer)
                );
                if (entityPlayers.size() == 0) {
                    evt.setAmount(evt.getAmount() * 0.625f);
                    this.sendReminder(entityPlayer);
                }
            }
            DamageSource damageSource = evt.getSource();
            if (damageSource.getTrueSource() instanceof EntityPlayer) {
                EntityPlayer entityPlayer = (EntityPlayer) damageSource.getTrueSource();
                @Nonnull List<EntityPlayer> entityPlayers = EntityUtil.getNearbyEntities(
                        EntityPlayer.class,
                        entityPlayer,
                        32,
                        otherPlayer -> !otherPlayer.equals(entityPlayer)
                );
                if (entityPlayers.size() == 0) {
                    evt.setAmount(evt.getAmount() * 1.75f);
                    this.sendReminder(entityPlayer);
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
        return "lonely_warrior";
    }

}
