package pers.roinflam.futurediary.base.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.MinecraftForge;
import pers.roinflam.futurediary.init.FutureDiaryPotion;
import pers.roinflam.futurediary.utils.util.PotionUtil;

import javax.annotation.Nonnull;

public abstract class PotionBase extends Potion {

    protected PotionBase(boolean isBadEffectIn, int liquidColorIn, @Nonnull String name) {
        super(isBadEffectIn, liquidColorIn);
        MinecraftForge.EVENT_BUS.register(this);
        PotionUtil.registerPotion(this, name);
        FutureDiaryPotion.POTIONS.add(this);
    }

    @Override
    public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier) {

    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
