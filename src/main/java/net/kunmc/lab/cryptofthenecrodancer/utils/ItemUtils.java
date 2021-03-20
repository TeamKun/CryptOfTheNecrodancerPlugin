package net.kunmc.lab.cryptofthenecrodancer.utils;

import org.bukkit.Material;

public class ItemUtils
{
    public static ToolType toType(Material mat)
    {
        switch (mat)
        {
            case STONE_AXE:
            case WOODEN_AXE:
            case IRON_AXE:
            case GOLDEN_AXE:
            case DIAMOND_AXE:
                return ToolType.AXE;
            case STONE_HOE:
            case WOODEN_HOE:
            case IRON_HOE:
            case GOLDEN_HOE:
            case DIAMOND_HOE:
                return ToolType.HOE;
            case STONE_PICKAXE:
            case WOODEN_PICKAXE:
            case IRON_PICKAXE:
            case GOLDEN_PICKAXE:
            case DIAMOND_PICKAXE:
                return ToolType.PICKAXE;
            case STONE_SHOVEL:
            case WOODEN_SHOVEL:
            case IRON_SHOVEL:
            case GOLDEN_SHOVEL:
            case DIAMOND_SHOVEL:
                return ToolType.SHOVEL;
            default:
                return null;
        }
    }


    public static ToolMaterial toMaterial(Material mat)
    {

        switch (mat)
        {
            case WOODEN_AXE:
            case WOODEN_HOE:
            case WOODEN_PICKAXE:
            case WOODEN_SHOVEL:
                return ToolMaterial.WOODEN;
            case STONE_AXE:
            case STONE_HOE:
            case STONE_PICKAXE:
            case STONE_SHOVEL:
                return ToolMaterial.WOODEN;
            case IRON_AXE:
            case IRON_HOE:
            case IRON_PICKAXE:
            case IRON_SHOVEL:
                return ToolMaterial.IRON;
            case GOLDEN_AXE:
            case GOLDEN_HOE:
            case GOLDEN_PICKAXE:
            case GOLDEN_SHOVEL:
                return ToolMaterial.GOLDEN;
            case DIAMOND_AXE:
            case DIAMOND_HOE:
            case DIAMOND_PICKAXE:
            case DIAMOND_SHOVEL:
                return ToolMaterial.DIAMOND;
            default:
                return null;
        }
    }

    public static boolean isTool(Material type)
    {
        switch (type)
        {
            case WOODEN_AXE:
            case WOODEN_HOE:
            case WOODEN_PICKAXE:
            case WOODEN_SHOVEL:
            case IRON_AXE:
            case IRON_HOE:
            case IRON_PICKAXE:
            case IRON_SHOVEL:
            case GOLDEN_AXE:
            case GOLDEN_HOE:
            case GOLDEN_PICKAXE:
            case GOLDEN_SHOVEL:
            case DIAMOND_AXE:
            case DIAMOND_HOE:
            case DIAMOND_PICKAXE:
            case DIAMOND_SHOVEL:
            case SHEARS:
                return true;
            default:
                return false;
        }
    }

    enum ToolMaterial
    {
        WOODEN,
        STONE,
        IRON,
        GOLDEN,
        DIAMOND
    }

    enum ToolType
    {
        AXE,
        HOE,
        PICKAXE,
        SHOVEL,
        SHEAR,

    }
}
