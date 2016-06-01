package com.lightningcraft.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.lightningcraft.items.blocks.ItemSlabLC;
import com.lightningcraft.ref.RefStrings;
import com.lightningcraft.registry.IRegistryBlock;
import com.lightningcraft.util.JointList;

/** The half-slab block */
public class BlockSlabLC extends BlockSlab implements IRegistryBlock {

	public static final int nVariants = BlockStone.nVariants / 3 + 1;
	public static final int THUNDER = 0;
	public static final int DEMON = 1;
	public static final int UNDER = 2;
	public static final int UNDER_PLANK = 3;
	private static final PropertyInteger VAR = PropertyInteger.create("variant", 0, nVariants - 1);

	public BlockSlabLC(Material mat) {
		super(mat);
		IBlockState blockState = this.blockState.getBaseState();
		blockState = blockState.withProperty(VAR, 0);
		if(!this.isDouble()) {
			blockState = blockState.withProperty(HALF, EnumBlockHalf.BOTTOM);
		}
		setDefaultState(blockState);
	}

	public BlockSlabLC() {
		this(Material.ROCK);
		this.setHardness(10);
		this.setResistance(100);
	}
	
//
//	/** Different tools for different fools */
//	@Override
//	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
//		ItemStack tool = player.getHeldItem(EnumHand.MAIN_HAND);
//		switch(this.getMetaFromState(world.getBlockState(pos))) {
//		case UNDER_PLANK:
//			return true;
//		default:
//			return super.canHarvestBlock(world, pos, player);
//		}
//	}
//	
//	/** Different tools for different fools */
//	@Override
//	public String getHarvestTool(IBlockState state) {
//		switch(this.getMetaFromState(state)) {
//		case UNDER_PLANK:
//			return "axe";
//		default:
//			return super.getHarvestTool(state);
//		}
//	}
//	
//	/** Different tools for different fools */
//	@Override
//	public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World world, BlockPos pos) {
//		switch(this.getMetaFromState(state)) {
//		case UNDER_PLANK:
//			float hardness = this.getBlockHardness(state, world, pos);
//			if (hardness < 0.0F) {
//				return 0.0F;
//			}
//			return player.getBreakSpeed(state, pos) / hardness / 30F;
//		default:
//			return super.getPlayerRelativeBlockHardness(state, player, world, pos);
//		}
//	}
//
	
	@Override
	public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
		switch(this.getMetaFromState(state)) {
		case UNDER_PLANK:
			return LCBlocks.woodPlank.getBlockHardness(LCBlocks.woodPlank.getDefaultState(), world, pos);
		default:
			return super.getBlockHardness(state, world, pos);
		}
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity e, Explosion explosion) {
		switch(this.getMetaFromState(world.getBlockState(pos))) {
		case UNDER_PLANK:
			return LCBlocks.woodPlank.getExplosionResistance(world, pos, e, explosion);
		default:
			return super.getExplosionResistance(world, pos, e, explosion);
		}
	}
	
	/** Different materials */
	@Override
	public Material getMaterial(IBlockState state) {
		switch(this.getMetaFromState(state)) {
		case UNDER_PLANK:
			return LCBlocks.woodPlank.getMaterial(LCBlocks.woodPlank.getDefaultState());
		default:
			return super.getMaterial(state);
		}
	}

	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < nVariants; i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	protected BlockStateContainer createBlockState() {
		if(this.isDouble()) {
			return new BlockStateContainer(this, VAR);
		} else {
			return new BlockStateContainer(this, VAR, HALF);
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState blockState = this.getDefaultState();
		blockState = blockState.withProperty(VAR, meta % 8);
		if (!this.isDouble()) {
			EnumBlockHalf value = EnumBlockHalf.BOTTOM;
			if (meta >= 8) {
				value = EnumBlockHalf.TOP;
			}
			blockState = blockState.withProperty(HALF, value);
		}
		return blockState;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		if(!this.isDouble() && (EnumBlockHalf)state.getValue(HALF) == EnumBlockHalf.TOP) {
			return 8 + state.getValue(VAR);
		} else {
			return state.getValue(VAR);
		}
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(VAR);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(LCBlocks.slabBlock);
	}

	@Override
	public boolean isDouble() {
		return false;
	}

	@Override
	public String getUnlocalizedName(int meta) {
		return this.getUnlocalizedName();
	}

	@Override
	public IProperty getVariantProperty() {
		return VAR;
	}

	@Override
	public Comparable getTypeForItem(ItemStack stack) {
		return stack.getItemDamage();
	}

	@Override
	public Class getItemClass() {
		return ItemSlabLC.class;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerRender(ItemModelMesher mesher) {
		if(!isDouble()) {
			JointList<ResourceLocation> names = new JointList();
			Item item = Item.getItemFromBlock(this);
			for(int meta = 0; meta < nVariants; meta++) {
				names.join(new ResourceLocation(RefStrings.MODID + ":" + this.getShorthandName() + "_" + meta));
			}
			ModelBakery.registerItemVariants(item, names.toArray(new ResourceLocation[names.size()]));
			for(int meta = 0; meta < nVariants; meta++) {
				mesher.register(item, meta, new ModelResourceLocation(RefStrings.MODID + ":" + this.getShorthandName() + "_" + meta, "inventory"));
			}
		}
	}

}
