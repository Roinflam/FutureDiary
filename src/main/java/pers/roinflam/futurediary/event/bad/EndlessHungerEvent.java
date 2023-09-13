package pers.roinflam.futurediary.event.bad;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.event.FutureEvent;
import pers.roinflam.futurediary.utils.java.random.RandomUtil;

import javax.annotation.Nonnull;

//无尽饥饿
public class EndlessHungerEvent extends FutureEvent {

    @SubscribeEvent
    public void onLivingAttack(@Nonnull LivingAttackEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            DamageSource damageSource = evt.getSource();
            if (damageSource.getTrueSource() instanceof EntityLivingBase && evt.getEntityLiving() instanceof EntityPlayer && RandomUtil.percentageChance(25)) {
                EntityPlayer entityPlayer = (EntityPlayer) evt.getEntityLiving();
                FoodStats foodStats = entityPlayer.getFoodStats();
                if (foodStats.getFoodLevel() > 0) {
                    foodStats.setFoodLevel(foodStats.getFoodLevel() - 1);
                    this.sendReminder(entityPlayer);
                }
            }
            if (damageSource.getTrueSource() instanceof EntityPlayer && RandomUtil.percentageChance(20)) {
                EntityPlayer entityPlayer = (EntityPlayer) damageSource.getTrueSource();
                FoodStats foodStats = entityPlayer.getFoodStats();
                if (foodStats.getFoodLevel() > 0) {
                    foodStats.setFoodLevel(foodStats.getFoodLevel() - 1);
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
        return "endless_hunger";
    }

}
