package pers.roinflam.futurediary.event.bad;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.event.FutureEvent;
import pers.roinflam.futurediary.utils.helper.task.SynchronizationTask;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;

//Ç÷Í¬ÐÔÏûºÄ
public class ConvergentConsumptionEvent extends FutureEvent {

    @Nonnull
    public static HashMap<String, HashMap<String, Integer>> killNumber = new HashMap<>();

    public ConvergentConsumptionEvent() {
        new SynchronizationTask(1200 * 10, 1200 * 10) {

            @Override
            public void run() {
                for (String uuid : new ArrayList<>(killNumber.keySet())) {
                    HashMap<String, Integer> killTypeList = killNumber.getOrDefault(uuid, new HashMap<>());
                    for (String entityType : new ArrayList<>(killTypeList.keySet())) {
                        killTypeList.put(entityType, killTypeList.get(entityType) - 1);
                        if (killTypeList.get(entityType) <= 0) {
                            killTypeList.remove(entityType);
                        }
                    }
                    if (killTypeList.size() > 0) {
                        killNumber.put(uuid, killTypeList);
                    } else {
                        killNumber.remove(uuid);
                    }
                }
            }

        }.start();
        new SynchronizationTask(1200 * 100, 1200 * 100) {

            @Override
            public void run() {
                killNumber.clear();
            }

        }.start();
    }

    @SubscribeEvent
    public void onLivingHurt(@Nonnull LivingHurtEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            DamageSource damageSource = evt.getSource();
            if (evt.getEntityLiving() instanceof EntityPlayer && damageSource.getTrueSource() instanceof EntityLivingBase) {
                EntityLivingBase attacker = (EntityLivingBase) damageSource.getTrueSource();
                EntityLivingBase hurter = evt.getEntityLiving();
                int number = killNumber.getOrDefault(hurter.getUniqueID().toString(), new HashMap<>()).getOrDefault(EntityList.getEntityString(attacker), 0);
                if (number > 0) {
                    evt.setAmount(evt.getAmount() + evt.getAmount() * 0.25f * number);
                    this.sendReminder(hurter);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLivingDeath(@Nonnull LivingDeathEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            DamageSource damageSource = evt.getSource();
            if (damageSource.getTrueSource() instanceof EntityPlayer) {
                EntityPlayer killer = (EntityPlayer) damageSource.getTrueSource();
                EntityLivingBase dead = evt.getEntityLiving();
                if (!killer.equals(dead)) {
                    String deadName = EntityList.getEntityString(dead);

                    HashMap<String, Integer> killTypeList = killNumber.getOrDefault(killer.getUniqueID().toString(), new HashMap<>());
                    killTypeList.put(deadName, killTypeList.getOrDefault(deadName, 0) + 1);
                    killNumber.put(killer.getUniqueID().toString(), killTypeList);
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
        return "convergent_consumption";
    }
}
