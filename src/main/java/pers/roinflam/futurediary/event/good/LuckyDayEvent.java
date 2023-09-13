package pers.roinflam.futurediary.event.good;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import pers.roinflam.futurediary.base.potion.hide.PrivateHideBase;
import pers.roinflam.futurediary.event.FutureEvent;

import javax.annotation.Nonnull;

import static pers.roinflam.futurediary.event.good.LuckyDayEvent.MobEffectLuckBoost.LUCK_BOOST;

//–“‘À»’
public class LuckyDayEvent extends FutureEvent {

    @SubscribeEvent
    public void onPlayerTick(@Nonnull TickEvent.PlayerTickEvent evt) {
        if (!evt.player.world.isRemote && evt.phase.equals(TickEvent.Phase.START)) {
            World world = evt.player.getEntityWorld();
            if (!this.isEnableEvent(world)) {
                return;
            }
            EntityPlayer entityPlayer = evt.player;
            if (world.getTotalWorldTime() % 20 == 0) {
                entityPlayer.addPotionEffect(new PotionEffect(LUCK_BOOST, 20, 0));
                this.sendReminder(entityPlayer);
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
        return "lucky_day";
    }

    static class MobEffectLuckBoost extends PrivateHideBase {
        public static final MobEffectLuckBoost LUCK_BOOST = new MobEffectLuckBoost(false, 0, "lucky_day_MobEffectLuckBoost");

        protected MobEffectLuckBoost(boolean isBadEffectIn, int liquidColorIn, @Nonnull String name) {
            super(isBadEffectIn, liquidColorIn, name);
            this.registerPotionAttributeModifier(SharedMonsterAttributes.LUCK, "2b7fd3f3-bcd5-e7be-bc69-189efa5e7a2d", 10, 2);
        }

    }
}
