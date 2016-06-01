package com.lightningcraft.render;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.lightningcraft.blocks.BlockAirTerminal;
import com.lightningcraft.blocks.BlockWireless;
import com.lightningcraft.ref.Metal.Ingot;
import com.lightningcraft.ref.Metal.Rod;

/** Block coloring for the mod */
@SideOnly(Side.CLIENT)
public class BlockColoring implements IBlockColor, IItemColor {
	
	private static final int DEFAULT = 0xFFFFFF;
	private static final BlockColoring instance = new BlockColoring();
	
	/** Register the block to be handled by this color handler */
	public static void registerBlock(Block block) {
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(instance, block);
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(instance, block);
	}

	/** The color multiplier for the block */
	@Override
	public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
		Block block = state.getBlock();
		int meta = block.getMetaFromState(state);
		if(block instanceof BlockAirTerminal) { // rod coloring
			return Rod.getColorFromMeta(meta);
		} else if(block instanceof BlockWireless) { // ingot coloring
			return Ingot.getColorFromMeta(meta % 3);
		} else {
			return DEFAULT;
		}
	}

	/** The color multiplier for the block's item */
	@Override
	public int getColorFromItemstack(ItemStack stack, int tintIndex) {
		Block block;
		if((block = Block.getBlockFromItem(stack.getItem())) != null) {
			return colorMultiplier(block.getStateFromMeta(stack.getItemDamage()), null, null, tintIndex);
		} else {
			return DEFAULT;
		}
	}

}
