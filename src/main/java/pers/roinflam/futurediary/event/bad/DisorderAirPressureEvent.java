package pers.roinflam.futurediary.event.bad;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.base.potion.hide.PrivateHideBase;
import pers.roinflam.futurediary.event.FutureEvent;

import javax.annotation.Nonnull;

import static pers.roinflam.futurediary.event.bad.DisorderAirPressureEvent.MobEffectDisorderAirPressure.DISORDER_AIR_PRESSURE;

//ÆøÑ¹Ê§Ðò
public class DisorderAirPressureEvent extends FutureEvent {

    @SubscribeEvent
    public void onBreakSpeed(@Nonnull PlayerEvent.BreakSpeed evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            evt.setNewSpeed(evt.getNewSpeed() * 0.75f);
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
            if (world.getTotalWorldTime() % 20 == 0) {
                entityLivingBase.addPotionEffect(new PotionEffect(DISORDER_AIR_PRESSURE, 30, 0));
                this.sendReminder(entityLivingBase);
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
        return "disorder_air_pressure";
    }


    static class MobEffectDisorderAirPressure extends PrivateHideBase {
        public static final MobEffectDisorderAirPressure DISORDER_AIR_PRESSURE = new MobEffectDisorderAirPressure(true, 0, "disorder_air_pressure_MobEffectDisorderAirPressure");

        protected MobEffectDisorderAirPressure(boolean isBadEffectIn, int liquidColorIn, @Nonnull String name) {
            super(isBadEffectIn, liquidColorIn, name);
            this.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "c52dbc40-277f-f2ee-1d38-3753d8df1c65", -0.2, 2);
            this.registerPotionAttributeModifier(SharedMonsterAttributes.FLYING_SPEED, "098fa6c2-8550-03bc-ccd3-485b65198cb2", -0.2, 2);
            this.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, "12548c0a-5efe-9ab5-1ca5-6017674f97e7", -0.2, 2);
        }

    }
}
