package pers.roinflam.futurediary.event.bad;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.FoodStats;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import pers.roinflam.futurediary.base.potion.hide.PrivateHideBase;
import pers.roinflam.futurediary.event.FutureEvent;

import javax.annotation.Nonnull;

import static pers.roinflam.futurediary.event.bad.AbnormalAirPressureEvent.MobEffectMovementSpeedReduced.MOVEMENT_SPEED_REDUCED;

//ÆøÑ¹Òì³£
public class AbnormalAirPressureEvent extends FutureEvent {

    @SubscribeEvent
    public void onPlayerTick(@Nonnull TickEvent.PlayerTickEvent evt) {
        if (!evt.player.world.isRemote && evt.phase.equals(TickEvent.Phase.START)) {
            World world = evt.player.getEntityWorld();
            if (!this.isEnableEvent(world)) {
                return;
            }
            if (world.getTotalWorldTime() % 5 == 0) {
                EntityPlayer entityPlayer = evt.player;
                FoodStats foodStats = entityPlayer.getFoodStats();
                if (foodStats.needFood()) {
                    int foodLevel = entityPlayer.getFoodStats().getFoodLevel();
                    entityPlayer.addPotionEffect(new PotionEffect(MOVEMENT_SPEED_REDUCED, 6, 20 - (foodLevel + 1)));
                    if (20 - (foodLevel + 1) >= 10) {
                        this.sendReminder(entityPlayer);
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
        return "abnormal_air_pressure";
    }


    static class MobEffectMovementSpeedReduced extends PrivateHideBase {
        public static final MobEffectMovementSpeedReduced MOVEMENT_SPEED_REDUCED = new MobEffectMovementSpeedReduced(true, 0, "abnormal_air_pressure_MobEffectMovementSpeedReduced");

        protected MobEffectMovementSpeedReduced(boolean isBadEffectIn, int liquidColorIn, @Nonnull String name) {
            super(isBadEffectIn, liquidColorIn, name);
            this.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "b815cef8-d561-64de-4735-d72286148b00", -0.033, 2);
        }
    }
}
