package pers.roinflam.futurediary.utils.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import pers.roinflam.futurediary.FutureDiary;

import javax.annotation.Nonnull;

public class ItemUtil {

    @Nonnull
    public static Item registerItem(@Nonnull Item item, @Nonnull String name, @Nonnull CreativeTabs creativeTabs) {
        item.setUnlocalizedName(name);
        item.setRegistryName(name);
        item.setCreativeTab(creativeTabs);
        FutureDiary.proxy.registerItemRenderer(item, 0, "inventory");
        return item;
    }

}
