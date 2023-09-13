package pers.roinflam.futurediary.event.bad;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.event.FutureEvent;
import pers.roinflam.futurediary.utils.helper.task.SynchronizationTask;
import pers.roinflam.futurediary.utils.java.random.RandomUtil;

import javax.annotation.Nonnull;

//ฒ๚ยั
public class SpawnEvent extends FutureEvent {

    public static void spawnChargedCreeper(@Nonnull World world, double x, double y, double z) {
        EntityCreeper creeper = new EntityCreeper(world);
        creeper.setPosition(x, y, z);
        NBTTagCompound nbt = new NBTTagCompound();
        creeper.writeEntityToNBT(nbt);
        nbt.setBoolean("powered", true);
        creeper.readEntityFromNBT(nbt);
        world.spawnEntity(creeper);
    }


    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLivingDeath(@Nonnull LivingDeathEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            EntityLivingBase dead = evt.getEntityLiving();
            if (dead instanceof EntityMob && RandomUtil.percentageChance(10)) {
                new SynchronizationTask(10) {

                    @Override
                    public void run() {
                        spawnChargedCreeper(world, dead.posX, dead.posY, dead.posZ);
                        if (evt.getSource().getTrueSource() instanceof EntityPlayer) {
                            SpawnEvent.this.sendReminder((EntityLivingBase) evt.getSource().getTrueSource());
                        }
                    }

                }.start();
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
        return "spawn";
    }
}
