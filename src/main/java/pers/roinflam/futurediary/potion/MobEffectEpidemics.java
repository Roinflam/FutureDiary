package pers.roinflam.futurediary.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import pers.roinflam.futurediary.base.potion.hide.HideBase;

import javax.annotation.Nonnull;


public class MobEffectEpidemics extends HideBase {

    public MobEffectEpidemics(boolean isBadEffectIn, int liquidColorIn) {
        super(isBadEffectIn, liquidColorIn, "epidemics");
        this.registerPotionAttributeModifier(SharedMonsterAttributes.MAX_HEALTH, "0964b81d-4b85-4e95-080e-f666756040ba", -0.33, 2);
    }


    @Override
    public void performEffect(@Nonnull EntityLivingBase entityLivingBaseIn, int amplifier) {
        entityLivingBaseIn.attackEntityFrom(DamageSource.GENERIC, entityLivingBaseIn.getHealth() * 0.075f);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
