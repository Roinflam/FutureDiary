package pers.roinflam.futurediary.event.catastrophic;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import pers.roinflam.futurediary.event.FutureEvent;
import pers.roinflam.futurediary.utils.helper.task.SynchronizationTask;
import pers.roinflam.futurediary.utils.java.random.RandomUtil;

import javax.annotation.Nonnull;
import java.util.HashSet;

//发配到沙勒
public class DeliverySchaleEvent extends FutureEvent {
    private final HashSet<String> deliveryCoolding = new HashSet<>();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onLivingSetAttackTarget(@Nonnull LivingSetAttackTargetEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            if (evt.getTarget() != null) {
                if (evt.getEntityLiving() instanceof EntityLiving) {
                    EntityLivingBase target = evt.getTarget();
                    EntityLiving entityLiving = (EntityLiving) evt.getEntityLiving();
                    String uuidString = target.getUniqueID().toString();
                    if (deliveryCoolding.contains(uuidString)) {
                        entityLiving.setAttackTarget(null);
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onLivingAttack(@Nonnull LivingAttackEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            DamageSource damageSource = evt.getSource();
            if (damageSource.getTrueSource() instanceof EntityLivingBase) {
                EntityLivingBase attacker = (EntityLivingBase) damageSource.getTrueSource();
                EntityLivingBase hurter = evt.getEntityLiving();
                String uuidString = attacker.getUniqueID().toString();
                if (deliveryCoolding.contains(uuidString)) {
                    this.sendReminder(attacker, 0.1);
                    evt.setCanceled(true);
                    return;
                }
                uuidString = hurter.getUniqueID().toString();
                if (deliveryCoolding.contains(uuidString)) {
                    this.sendReminder(hurter, 0.1);
                    evt.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(@Nonnull TickEvent.PlayerTickEvent evt) {
        if (!evt.player.world.isRemote && evt.phase.equals(TickEvent.Phase.START)) {
            World world = evt.player.getEntityWorld();
            if (!this.isEnableEvent(world)) {
                return;
            }
            if (world.getTotalWorldTime() % 150 == 0 && RandomUtil.percentageChance(25)) {
                EntityPlayer entityPlayer = evt.player;
                String uuidString = entityPlayer.getUniqueID().toString();
                if (!deliveryCoolding.contains(uuidString)) {
                    deliveryCoolding.add(uuidString);
                    this.sendReminder(entityPlayer, 0.1);
                    new SynchronizationTask(1200) {

                        @Override
                        public void run() {
                            deliveryCoolding.remove(entityPlayer.getUniqueID().toString());
                        }

                    }.start();
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
        return "delivery_schale";
    }
}
