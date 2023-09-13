package pers.roinflam.futurediary.data;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import pers.roinflam.futurediary.utils.Reference;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FutureData {
    @Nonnull
    private final File worldDirectory;
    private boolean first;
    private NBTTagList badEvents;
    private NBTTagList catastrophicEvents;
    private NBTTagList goodEvents;
    private int totalSecond;

    public FutureData(@Nonnull World world) {
        this.worldDirectory = world.getSaveHandler().getWorldDirectory();
        first = true;
        badEvents = new NBTTagList();
        catastrophicEvents = new NBTTagList();
        goodEvents = new NBTTagList();
        totalSecond = 0;

        loadFutureData();
    }

    public boolean loadFutureData() {
        File file = new File(worldDirectory, Reference.MOD_ID + "_data.dat");
        if (file.exists()) {
            try {
                NBTTagCompound nbt = CompressedStreamTools.readCompressed(Files.newInputStream(file.toPath()));
                this.readFromNBT(nbt);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public NBTTagList getBadEvents() {
        return badEvents;
    }

    public void setBadEvents(NBTTagList badEvents) {
        this.badEvents = badEvents;
    }

    public NBTTagList getCatastrophicEvents() {
        return catastrophicEvents;
    }

    public void setCatastrophicEvents(NBTTagList catastrophicEvents) {
        this.catastrophicEvents = catastrophicEvents;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public NBTTagList getGoodEvents() {
        return goodEvents;
    }

    public void setGoodEvents(NBTTagList goodEvents) {
        this.goodEvents = goodEvents;
    }

    public boolean saveFutureData() {
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeToNBT(nbt);
        File file = new File(worldDirectory, Reference.MOD_ID + "_data.dat");
        try {
            CompressedStreamTools.writeCompressed(nbt, Files.newOutputStream(file.toPath()));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getTotalSecond() {
        return totalSecond;
    }

    public void setTotalSecond(int totalSecond) {
        this.totalSecond = totalSecond;
    }

    @Nonnull
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound nbt) {
        nbt.setInteger("totalSecond", totalSecond);
        nbt.setBoolean("first", first);
        nbt.setTag("badEvent", badEvents);
        nbt.setTag("catastrophicEvents", catastrophicEvents);
        nbt.setTag("goodEvents", goodEvents);
        return nbt;
    }

    public void readFromNBT(@Nonnull NBTTagCompound nbt) {
        totalSecond = nbt.getInteger("totalSecond");
        first = nbt.getBoolean("first");
        badEvents = nbt.getTagList("badEvent", Constants.NBT.TAG_STRING);
        catastrophicEvents = nbt.getTagList("catastrophicEvents", Constants.NBT.TAG_STRING);
        goodEvents = nbt.getTagList("goodEvents", Constants.NBT.TAG_STRING);
    }
}
