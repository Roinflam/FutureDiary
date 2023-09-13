package pers.roinflam.futurediary.event.catastrophic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.event.FutureEvent;
import pers.roinflam.futurediary.init.FutureDiaryPotion;
import pers.roinflam.futurediary.utils.helper.task.SynchronizationTask;

import javax.annotation.Nonnull;

//¸ÐÖªÁ÷ÊÅ
public class PerceivedLapseEvent extends FutureEvent {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLivingDamage(@Nonnull LivingDamageEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().getEntityWorld();
            if (!this.isEnableEvent(world)) {
                return;
            }
            if (evt.getEntityLiving() instanceof EntityPlayer) {
                EntityPlayer entityPlayer = (EntityPlayer) evt.getEntityLiving();
                new SynchronizationTask(30 * 20) {

                    @Override
                    public void run() {
                        entityPlayer.addPotionEffect(new PotionEffect(FutureDiaryPotion.PERCEIVED_LAPSE, 200, 0));
                        PerceivedLapseEvent.this.sendReminder(entityPlayer);
                    }

                }.start();
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
        return "perceived_lapse";
    }

}
