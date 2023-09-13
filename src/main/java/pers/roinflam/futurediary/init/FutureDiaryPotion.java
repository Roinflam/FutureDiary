package pers.roinflam.futurediary.init;

import net.minecraft.potion.Potion;
import pers.roinflam.futurediary.potion.MobEffectArrogant;
import pers.roinflam.futurediary.potion.MobEffectEmotionalEntity;
import pers.roinflam.futurediary.potion.MobEffectEpidemics;
import pers.roinflam.futurediary.potion.MobEffectPerceivedLapse;

import java.util.ArrayList;
import java.util.List;

public class FutureDiaryPotion {
    public static final List<Potion> POTIONS = new ArrayList<Potion>();

    public static final MobEffectEmotionalEntity EMOTIONAL_ENTITY = new MobEffectEmotionalEntity(true, 0x7c1500);
    public static final MobEffectEpidemics EPIDEMICS = new MobEffectEpidemics(true, 0x007023);
    public static final MobEffectArrogant ARROGANT = new MobEffectArrogant(true, 0);
    public static final MobEffectPerceivedLapse PERCEIVED_LAPSE = new MobEffectPerceivedLapse(true, 0);
}