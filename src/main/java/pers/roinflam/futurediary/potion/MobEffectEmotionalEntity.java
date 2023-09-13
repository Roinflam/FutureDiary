package pers.roinflam.futurediary.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import pers.roinflam.futurediary.base.potion.hide.HideBase;

import javax.annotation.Nonnull;


public class MobEffectEmotionalEntity extends HideBase {

    public MobEffectEmotionalEntity(boolean isBadEffectIn, int liquidColorIn) {
        super(isBadEffectIn, liquidColorIn, "emotional_entity");
    }


    @Override
    public void performEffect(@Nonnull EntityLivingBase entityLivingBaseIn, int amplifier) {
        int level = amplifier + 1;
        entityLivingBaseIn.attackEntityFrom(DamageSource.GENERIC, (entityLivingBaseIn.getHealth() * 0.01f + entityLivingBaseIn.getMaxHealth() * 0.01f) * level);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
