package pers.roinflam.futurediary.event.good;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.base.potion.hide.PrivateHideBase;
import pers.roinflam.futurediary.event.FutureEvent;

import javax.annotation.Nonnull;

import static pers.roinflam.futurediary.event.good.AdrenalineEvent.MobEffectAdrenaline.ADRENALINE;

//ÉöÉÏÏÙËØì­Éý
public class AdrenalineEvent extends FutureEvent {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLivingDamage(@Nonnull LivingDamageEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            if (evt.getEntityLiving() instanceof EntityPlayer) {
                EntityPlayer entityPlayer = (EntityPlayer) evt.getEntityLiving();
                if (evt.getAmount() >= entityPlayer.getMaxHealth() * 0.33f) {
                    entityPlayer.addPotionEffect(new PotionEffect(ADRENALINE, 200, 0));
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
        return "adrenaline";
    }

    static class MobEffectAdrenaline extends PrivateHideBase {
        public static final MobEffectAdrenaline ADRENALINE = new MobEffectAdrenaline(true, 0, "adrenaline_MobEffectAdrenaline");

        protected MobEffectAdrenaline(boolean isBadEffectIn, int liquidColorIn, @Nonnull String name) {
            super(isBadEffectIn, liquidColorIn, name);
            this.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "6fdf3f06-424c-c57f-828f-e18d14c50605", 0.66, 2);
        }

        @Override
        public void performEffect(@Nonnull EntityLivingBase entityLivingBaseIn, int amplifier) {
            entityLivingBaseIn.heal((entityLivingBaseIn.getMaxHealth() - entityLivingBaseIn.getHealth()) * 0.066f);
        }

        @Override
        public boolean isReady(int duration, int amplifier) {
            return duration % 20 == 0;
        }
    }

}
