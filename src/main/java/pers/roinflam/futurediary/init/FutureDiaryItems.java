package pers.roinflam.futurediary.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import pers.roinflam.futurediary.item.RandomDiary;
import pers.roinflam.futurediary.item.YukiDiary;

import java.util.ArrayList;
import java.util.List;

public class FutureDiaryItems {
    public static final List<Item> ITEMS = new ArrayList<Item>();

    public static final RandomDiary RANDOM_DIARY = new RandomDiary("random_diary", CreativeTabs.TOOLS);
    public static final YukiDiary YUKI_DIARY = new YukiDiary("yuki_diary", CreativeTabs.TOOLS);
}
