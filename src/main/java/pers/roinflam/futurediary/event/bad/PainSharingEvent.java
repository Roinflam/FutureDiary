package pers.roinflam.futurediary.event.bad;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.event.FutureEvent;
import pers.roinflam.futurediary.utils.util.EntityUtil;

import javax.annotation.Nonnull;
import java.util.List;

//Í´¿àÆ½Ì¯
public class PainSharingEvent extends FutureEvent {
    public static final DamageSource PAIN_SHARING = new DamageSource("pain_sharing").setDamageBypassesArmor();

    @SubscribeEvent
    public void onLivingDamage(@Nonnull LivingDamageEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            if (evt.getEntityLiving() instanceof EntityPlayer && !evt.getSource().getDamageType().equals("pain_sharing")) {
                EntityPlayer hurter = (EntityPlayer) evt.getEntityLiving();
                @Nonnull List<EntityPlayer> entityPlayers = EntityUtil.getNearbyEntities(
                        EntityPlayer.class,
                        hurter,
                        32,
                        entityPlayer -> !entityPlayer.equals(hurter)
                );
                float damage = evt.getAmount() * 0.33f;
                for (@Nonnull EntityPlayer entityPlayer : entityPlayers) {
                    entityPlayer.attackEntityFrom(PAIN_SHARING, damage);
                    this.sendReminder(entityPlayer);
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
        return "pain_sharing";
    }

}
