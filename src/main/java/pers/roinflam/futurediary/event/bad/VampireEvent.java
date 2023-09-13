package pers.roinflam.futurediary.event.bad;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import pers.roinflam.futurediary.event.FutureEvent;
import pers.roinflam.futurediary.utils.java.random.RandomUtil;
import pers.roinflam.futurediary.utils.util.EntityLivingUtil;

import javax.annotation.Nonnull;

//СЄЧе
public class VampireEvent extends FutureEvent {

    @SubscribeEvent
    public void onLivingAttack(@Nonnull LivingAttackEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            DamageSource damageSource = evt.getSource();
            if (damageSource.getImmediateSource() instanceof EntityPlayer) {
                EntityPlayer entityPlayer = (EntityPlayer) damageSource.getImmediateSource();
                if (EntityLivingUtil.getTicksSinceLastSwing(entityPlayer) != 1) {
                    return;
                }
                entityPlayer.heal((entityPlayer.getMaxHealth() - entityPlayer.getHealth()) * 0.25f);
                FoodStats foodStats = entityPlayer.getFoodStats();
                if (foodStats.needFood()) {
                    foodStats.setFoodLevel(foodStats.getFoodLevel() + 1);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(@Nonnull TickEvent.PlayerTickEvent evt) {
        if (!evt.player.world.isRemote) {
            World world = evt.player.getEntityWorld();
            if (!this.isEnableEvent(world)) {
                return;
            }
            EntityPlayer entityPlayer = evt.player;
            if (world.getTotalWorldTime() % 20 == 0) {
                if (world.isDaytime() && !world.isRaining()) {
                    BlockPos blockPos = entityPlayer.getPosition();
                    if (world.canSeeSky(blockPos)) {
                        entityPlayer.attackEntityFrom(DamageSource.IN_FIRE.setDamageBypassesArmor(), entityPlayer.getMaxHealth() * 0.2f);
                        entityPlayer.setFire(10);
                        this.sendReminder(entityPlayer);
                    }
                }
            }
            if (world.getTotalWorldTime() % 300 == 0) {
                entityPlayer.attackEntityFrom(DamageSource.GENERIC, entityPlayer.getMaxHealth() * 0.015f);
                if (RandomUtil.percentageChance(20)) {
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
        return "vampire";
    }

}
