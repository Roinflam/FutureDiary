package pers.roinflam.futurediary.event.bad;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import pers.roinflam.futurediary.event.FutureEvent;
import pers.roinflam.futurediary.utils.java.random.RandomUtil;
import pers.roinflam.futurediary.utils.util.EntityUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

//Г№Ко
public class HatredEvent extends FutureEvent {

    @SubscribeEvent
    public void onPlayerTick(@Nonnull TickEvent.PlayerTickEvent evt) {
        if (!evt.player.world.isRemote && evt.phase.equals(TickEvent.Phase.START)) {
            World world = evt.player.getEntityWorld();
            if (!this.isEnableEvent(world)) {
                return;
            }
            if (world.getTotalWorldTime() % 600 == 0) {
                EntityPlayer entityPlayer = evt.player;
                @Nonnull List<EntityMob> entities = EntityUtil.getNearbyEntities(
                        EntityMob.class,
                        entityPlayer,
                        64
                );
                for (@Nonnull EntityMob entityMob : entities) {
                    @Nullable EntityLivingBase attackTarget = entityMob.getAttackTarget();
                    if (attackTarget == null || !entityMob.getAttackTarget().isEntityAlive()) {
                        if (RandomUtil.percentageChance(25)) {
                            entityMob.setAttackTarget(entityPlayer);
                        }
                    } else {
                        if (!attackTarget.equals(entityPlayer)) {
                            if (RandomUtil.percentageChance(50)) {
                                entityMob.setAttackTarget(entityPlayer);
                            }
                        }
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
        return "hatred";
    }


}
