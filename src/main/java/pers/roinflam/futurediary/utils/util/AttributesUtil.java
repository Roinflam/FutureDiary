package pers.roinflam.futurediary.utils.util;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class AttributesUtil {
    public static float getAttributeValue(@Nonnull EntityLivingBase entityLivingBase, @Nonnull IAttribute attribute) {
        return getAttributeValue(entityLivingBase, attribute, 0);
    }

    public static float getAttributeValue(@Nonnull EntityLivingBase entityLivingBase, @Nonnull IAttribute attribute, double value) {
        double base = attribute.getDefaultValue() + value;
        if (entityLivingBase.getEntityAttribute(attribute) != null) {
            base = entityLivingBase.getEntityAttribute(attribute).getAttributeValue();
        }

        List<Double> zero = new ArrayList<>();
        List<Double> one = new ArrayList<>();
        List<Double> two = new ArrayList<>();

        Multimap<String, AttributeModifier> attributeInstance = entityLivingBase.getHeldItemMainhand().getAttributeModifiers(EntityEquipmentSlot.MAINHAND);
        for (AttributeModifier attributeModifier : attributeInstance.get(attribute.getName())) {
            if (attributeModifier.getOperation() == 0) {
                zero.add(attributeModifier.getAmount());
            } else if (attributeModifier.getOperation() == 1) {
                one.add(attributeModifier.getAmount());
            } else {
                two.add(attributeModifier.getAmount());
            }
        }

        attributeInstance = entityLivingBase.getHeldItemOffhand().getAttributeModifiers(EntityEquipmentSlot.OFFHAND);
        for (AttributeModifier attributeModifier : attributeInstance.get(attribute.getName())) {
            if (attributeModifier.getOperation() == 0) {
                zero.add(attributeModifier.getAmount());
            } else if (attributeModifier.getOperation() == 1) {
                one.add(attributeModifier.getAmount());
            } else {
                two.add(attributeModifier.getAmount());
            }
        }
        int index = 0;
        for (ItemStack i : entityLivingBase.getArmorInventoryList()) {
            switch (index) {
                case 0: {
                    attributeInstance = i.getAttributeModifiers(EntityEquipmentSlot.FEET);
                    for (AttributeModifier attributeModifier : attributeInstance.get(attribute.getName())) {
                        if (attributeModifier.getOperation() == 0) {
                            zero.add(attributeModifier.getAmount());
                        } else if (attributeModifier.getOperation() == 1) {
                            one.add(attributeModifier.getAmount());
                        } else {
                            two.add(attributeModifier.getAmount());
                        }
                    }
                    break;
                }
                case 1: {
                    attributeInstance = i.getAttributeModifiers(EntityEquipmentSlot.LEGS);
                    for (AttributeModifier attributeModifier : attributeInstance.get(attribute.getName())) {
                        if (attributeModifier.getOperation() == 0) {
                            zero.add(attributeModifier.getAmount());
                        } else if (attributeModifier.getOperation() == 1) {
                            one.add(attributeModifier.getAmount());
                        } else {
                            two.add(attributeModifier.getAmount());
                        }
                    }
                    break;
                }
                case 2: {
                    attributeInstance = i.getAttributeModifiers(EntityEquipmentSlot.CHEST);
                    for (AttributeModifier attributeModifier : attributeInstance.get(attribute.getName())) {
                        if (attributeModifier.getOperation() == 0) {
                            zero.add(attributeModifier.getAmount());
                        } else if (attributeModifier.getOperation() == 1) {
                            one.add(attributeModifier.getAmount());
                        } else {
                            two.add(attributeModifier.getAmount());
                        }
                    }
                    break;
                }
                case 3: {
                    attributeInstance = i.getAttributeModifiers(EntityEquipmentSlot.HEAD);
                    for (AttributeModifier attributeModifier : attributeInstance.get(attribute.getName())) {
                        if (attributeModifier.getOperation() == 0) {
                            zero.add(attributeModifier.getAmount());
                        } else if (attributeModifier.getOperation() == 1) {
                            one.add(attributeModifier.getAmount());
                        } else {
                            two.add(attributeModifier.getAmount());
                        }
                    }
                    break;
                }
                default:
                    break;
            }
            index++;
        }

        for (double amount : zero) {
            base += amount;
        }

        double number = base;

        for (double amount : one) {
            number += base * amount;
        }

        for (double amount : two) {
            number *= 1.0D + amount;
        }

        return (float) number;
    }

    @Nonnull
    public static Multimap<String, AttributeModifier> getAnyAttributeModifiers(@Nonnull ItemStack itemStack) {
        Multimap<String, AttributeModifier> multimap = HashMultimap.create();
        NBTTagCompound nbtTagCompound = itemStack.getTagCompound();
        if (itemStack.hasTagCompound() && nbtTagCompound.hasKey("AttributeModifiers", 9)) {
            NBTTagList nbttaglist = nbtTagCompound.getTagList("AttributeModifiers", 10);
            for (int i = nbttaglist.tagCount() - 1; i >= 0; --i) {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                AttributeModifier attributemodifier = SharedMonsterAttributes.readAttributeModifierFromNBT(nbttagcompound);
                multimap.put(nbttagcompound.getString("AttributeName"), attributemodifier);
            }
        }
        return multimap;
    }
}
