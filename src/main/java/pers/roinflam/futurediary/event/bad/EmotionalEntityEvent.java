package pers.roinflam.futurediary.event.bad;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.base.potion.hide.PrivateHideBase;
import pers.roinflam.futurediary.event.FutureEvent;
import pers.roinflam.futurediary.init.FutureDiaryPotion;

import javax.annotation.Nonnull;

import static pers.roinflam.futurediary.event.bad.EmotionalEntityEvent.MobEffectAttackDamageBoost.ATTACK_DAMAGE_BOOST;

//ÇéÐ÷ÊµÌå
public class EmotionalEntityEvent extends FutureEvent {

    @SubscribeEvent
    public void onLivingDamage(@Nonnull LivingDamageEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            DamageSource damageSource = evt.getSource();
            Entity attacker = damageSource.getImmediateSource();
            if (attacker instanceof EntityLivingBase && !(attacker instanceof EntityPlayer) && evt.getEntityLiving() instanceof EntityPlayer) {
                EntityPlayer hurter = (EntityPlayer) evt.getEntityLiving();
                if (hurter.getActivePotionEffect(FutureDiaryPotion.EMOTIONAL_ENTITY) != null) {
                    hurter.addPotionEffect(new PotionEffect(FutureDiaryPotion.EMOTIONAL_ENTITY, 100, Math.min(hurter.getActivePotionEffect(FutureDiaryPotion.EMOTIONAL_ENTITY).getAmplifier() + 1, 5)));
                    if (hurter.getActivePotionEffect(FutureDiaryPotion.EMOTIONAL_ENTITY).getAmplifier() >= 5) {
                        ((EntityLivingBase) attacker).addPotionEffect(new PotionEffect(ATTACK_DAMAGE_BOOST, 100, 0));
                        this.sendReminder(hurter);
                    }
                } else {
                    hurter.addPotionEffect(new PotionEffect(FutureDiaryPotion.EMOTIONAL_ENTITY, 100, 0));
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
        return "emotional_entity";
    }


    static class MobEffectAttackDamageBoost extends PrivateHideBase {
        public static final MobEffectAttackDamageBoost ATTACK_DAMAGE_BOOST = new MobEffectAttackDamageBoost(false, 0, "emotional_entity_MobEffectAttackDamageBoost");

        protected MobEffectAttackDamageBoost(boolean isBadEffectIn, int liquidColorIn, @Nonnull String name) {
            super(isBadEffectIn, liquidColorIn, name);
            this.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE, "1e3dc400-8612-a4cc-5f9c-de95ea181abc", 0.5, 2);
        }

    }
}
