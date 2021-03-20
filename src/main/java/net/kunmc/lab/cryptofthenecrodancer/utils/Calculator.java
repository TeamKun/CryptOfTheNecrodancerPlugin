package net.kunmc.lab.cryptofthenecrodancer.utils;

import net.minecraft.server.v1_15_R1.ItemTool;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class Calculator
{
    private static final Field ITEMTOOL_A;
    private static Method getBlock;
    private static Method getMaterial;
    private static Method getBlockData;
    private static Method isReplaceable;
    private static Method isRequiresSpecialTool;
    private static Method getItem;
    private static Method canDestroySpecialBlock;

    static
    {
        try
        {
            getBlock = ReflectionUtils.getMethod(
                    "CraftMagicNumbers",
                    ReflectionUtils.PackageType.CRAFTBUKKIT_UTIL,
                    "getBlock",
                    Material.class
            );

            getBlockData = ReflectionUtils.getMethod(
                    "Block",
                    ReflectionUtils.PackageType.MINECRAFT_SERVER,
                    "getBlockData"
            );


            getMaterial = ReflectionUtils.getMethod(
                    "IBlockData",
                    ReflectionUtils.PackageType.MINECRAFT_SERVER,
                    "getMaterial"
            );


            isReplaceable = ReflectionUtils.getMethod(
                    "Material",
                    ReflectionUtils.PackageType.MINECRAFT_SERVER,
                    "isReplaceable"
            );


            isRequiresSpecialTool = ReflectionUtils.getMethod(
                    "IBlockData",
                    ReflectionUtils.PackageType.MINECRAFT_SERVER,
                    "g"
            );

            canDestroySpecialBlock = ReflectionUtils.getMethod(
                    "Item",
                    ReflectionUtils.PackageType.MINECRAFT_SERVER,
                    "canDestroySpecialBlock",
                    Class.forName(ReflectionUtils.PackageType.MINECRAFT_SERVER + ".IBlockData")
            );

            getItem = ReflectionUtils.getMethod(
                    "CraftMagicNumbers",
                    ReflectionUtils.PackageType.CRAFTBUKKIT_UTIL,
                    "getItem",
                    Material.class
            );
        }
        catch (NoSuchMethodException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    static
    {
        Field fieldItemToolA;
        try
        {
            // This is just a helper that also throws a NoSuchFieldException if field type doesn't match.
            // I find it useful to ensure that NMS changes break more smoothly.
            fieldItemToolA = ReflectionUtils.getField("ItemTool", ReflectionUtils.PackageType.MINECRAFT_SERVER, true, "a");
            fieldItemToolA.setAccessible(true);
        }
        catch (NoSuchFieldException | ClassNotFoundException e)
        {
            e.printStackTrace();
            fieldItemToolA = null;
        }
        ITEMTOOL_A = fieldItemToolA;
    }

    public static boolean isToolRequired(Material blockType)
    {
        try
        {
            Object blockData = getBlockData.invoke(getBlock.invoke(null, blockType));

            return !((boolean) isReplaceable.invoke(getMaterial.invoke(blockData)))
                    && !((boolean) isRequiresSpecialTool.invoke(blockData));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isCorrectTool(ItemStack tool, Material blockType)
    {
        try
        {
            Object item;
            if (tool == null
                    || tool.getType().isAir()
                    || !(Class.forName(ReflectionUtils.PackageType.MINECRAFT_SERVER + ".ItemTool").isInstance((item = getItem.invoke(null, tool.getType())))))
            {
                return !isToolRequired(blockType);
            }

            if (ITEMTOOL_A == null)
            {
                return isUsableTool(tool, blockType);
            }

            Set<?> toolBlocks = (Set<?>) ITEMTOOL_A.get(item);
            return toolBlocks.contains(getBlock.invoke(null, blockType));
        }
        catch (IllegalAccessException | ClassCastException | InvocationTargetException | ClassNotFoundException e)
        {
            e.printStackTrace();
            return isUsableTool(tool, blockType);
        }
    }

    private static boolean isUsableTool(ItemStack tool, Material blockType)
    {
        try
        {

            Object block = getBlock.invoke(blockType);

            Object data = getBlockData.invoke(block);

            if ((boolean) isReplaceable.invoke(getMaterial.invoke(data)) || (boolean) isRequiresSpecialTool.invoke(data))
            {
                // Instant break or always breakable
                return true;
            }


            return tool != null
                    && tool.getType() != Material.AIR
                    && (boolean) canDestroySpecialBlock.invoke(getItem.invoke(null, tool.getType()), data);
        }
        catch (IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
        return true;
    }

    public static int calculateBlockAll(Block block, ItemStack tool)
    {
        int base = calculateBlock(block);

        int toolCt = calculateTool(tool);
        if (!isCorrectTool(tool, block.getType()))
            toolCt -= 10;
        base -= toolCt;

        return base;
    }

    public static int calculateBlock(Block block)
    {
        if (block.getType() == Material.AIR)
            return 0;
        return (int) Math.ceil(block.getType().getHardness() * 5f);
    }

    public static int calculateTool(ItemStack item)
    {
        if (item == null || item.getType() == Material.AIR)
            return 0;

        if (!ItemUtils.isTool(item.getType()))
            return 0;

        int baseTool = calculatePlainTool(item);

        if (item.getType() == Material.SHEARS)
            baseTool = 1;

        baseTool += item.getEnchantmentLevel(Enchantment.DIG_SPEED);
        return baseTool;
    }

    public static int calculatePlainTool(ItemStack item)
    {
        if (item.getType() == Material.SHEARS)
            return 2;

        switch (ItemUtils.toMaterial(item.getType()))
        {
            case WOODEN:
                return 1;
            case STONE:
                return 2;
            case IRON:
                return 3;
            case DIAMOND:
                return 4;
            case GOLDEN:
                return 5;
            default:
                return 0;
        }
    }
}
