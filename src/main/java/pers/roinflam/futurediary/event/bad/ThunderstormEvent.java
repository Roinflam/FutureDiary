package pers.roinflam.futurediary.event.bad;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import pers.roinflam.futurediary.event.FutureEvent;
import pers.roinflam.futurediary.utils.helper.task.SynchronizationTask;
import pers.roinflam.futurediary.utils.java.random.RandomUtil;

import javax.annotation.Nonnull;
import java.util.Random;

//±©À×
public class ThunderstormEvent extends FutureEvent {

    @SubscribeEvent
    public void onWorldTick(@Nonnull TickEvent.WorldTickEvent evt) {
        if (!evt.world.isRemote && evt.phase.equals(TickEvent.Phase.START)) {
            World world = evt.world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            world.getWorldInfo().setRaining(true);
            world.getWorldInfo().setRainTime(1200);
            world.getWorldInfo().setThundering(true);
            world.getWorldInfo().setThunderTime(1200);
        }
    }

    @SubscribeEvent
    public void onPlayerTick(@Nonnull TickEvent.PlayerTickEvent evt) {
        if (!evt.player.world.isRemote && evt.phase.equals(TickEvent.Phase.START)) {
            World world = evt.player.getEntityWorld();
            if (!this.isEnableEvent(world)) {
                return;
            }
            EntityPlayer entityPlayer = evt.player;
            BlockPos blockPos = entityPlayer.getPosition();
            Random random = new Random();
            if (world.getTotalWorldTime() % 600 == 0) {
                int offsetX = random.nextInt(33) - 16;
                int offsetZ = random.nextInt(33) - 16;
                int x = blockPos.getX() + offsetX;
                int z = blockPos.getZ() + offsetZ;
                world.addWeatherEffect(new EntityLightningBolt(world, x, world.getPrecipitationHeight(blockPos).getY(), z, false));
            } else if (world.getTotalWorldTime() % 6000 == 0) {
                if (RandomUtil.percentageChance(5) && world.canSeeSky(blockPos)) {
                    world.addWeatherEffect(new EntityLightningBolt(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), false));
                }
                if (RandomUtil.percentageChance(50)) {
                    for (int i = 0; i < 64; i++) {
                        new SynchronizationTask(10 * i + random.nextInt(i * 5 + 25)) {

                            @Override
                            public void run() {
                                int offsetX = random.nextInt(49) - 24;
                                int offsetZ = random.nextInt(49) - 24;
                                int x = blockPos.getX() + offsetX;
                                int z = blockPos.getZ() + offsetZ;
                                world.addWeatherEffect(new EntityLightningBolt(world, x, world.getPrecipitationHeight(blockPos).getY(), z, false));
                            }

                        }.start();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onLivingHurt(@Nonnull LivingHurtEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            if (evt.getSource().equals(DamageSource.LIGHTNING_BOLT)) {
                EntityLivingBase hurter = evt.getEntityLiving();
                hurter.setAbsorptionAmount(0);
                evt.setAmount(evt.getAmount() * 2 + hurter.getMaxHealth() * 0.5f);
                this.sendReminder(hurter);
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
        return "thunderstorm";
    }

}
