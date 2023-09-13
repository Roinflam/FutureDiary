package pers.roinflam.futurediary.event.catastrophic;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.base.potion.hide.PrivateHideBase;
import pers.roinflam.futurediary.event.FutureEvent;

import javax.annotation.Nonnull;

//ÑªÈâ»û±ä
public class BloodBodyVariationEvent extends FutureEvent {

    @SubscribeEvent
    public void onBreakSpeed(@Nonnull PlayerEvent.BreakSpeed evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            evt.setNewSpeed(evt.getNewSpeed() * 0.5f);
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(@Nonnull LivingEvent.LivingUpdateEvent evt) {
        if (!evt.getEntityLiving().world.isRemote) {
            World world = evt.getEntityLiving().getEntityWorld();
            if (!this.isEnableEvent(world)) {
                return;
            }
            EntityLivingBase entityLivingBase = evt.getEntityLiving();
            if (entityLivingBase instanceof EntityPlayer && world.getTotalWorldTime() % 20 == 0) {
                entityLivingBase.addPotionEffect(new PotionEffect(MobEffectBloodBodyVariation.BLOOD_BODY_VARIATION, 30, 0));
                this.sendReminder(entityLivingBase);
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
        return "blood_body_variation";
    }

    static class MobEffectBloodBodyVariation extends PrivateHideBase {
        public static final MobEffectBloodBodyVariation BLOOD_BODY_VARIATION = new MobEffectBloodBodyVariation(true, 0, "blood_body_variation_MobEffectBloodBodyVariation");

        protected MobEffectBloodBodyVariation(boolean isBadEffectIn, int liquidColorIn, @Nonnull String name) {
            super(isBadEffectIn, liquidColorIn, name);
            this.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "3b9c60b7-0b6a-0058-7a6e-3b4c7e9c5fc0", 0.5, 2);
            this.registerPotionAttributeModifier(SharedMonsterAttributes.MAX_HEALTH, "597613b5-26ce-d05f-56b3-2e7c70abea97", -0.5, 2);
            this.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE, "28957f32-806e-1a71-5091-96adcb595fe3", -0.5, 2);
            this.registerPotionAttributeModifier(SharedMonsterAttributes.ARMOR, "4098f3c7-cfea-1686-c4b5-0dc3648b59a1", -0.5, 2);
            this.registerPotionAttributeModifier(SharedMonsterAttributes.ARMOR_TOUGHNESS, "aedfa849-b179-3497-e7a7-7ff4e21bd4f0", -0.5, 2);
        }

    }
}
