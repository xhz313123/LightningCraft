package com.lightningcraft.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.lightningcraft.items.blocks.ItemBlockMeta;
import com.lightningcraft.ref.RefMisc;
import com.lightningcraft.ref.RefStrings;
import com.lightningcraft.util.JointList;

/** Block container class, with metadata */
public abstract class BlockContainerLCMeta extends BlockContainerLC {
	
	/** Do some reflection trickery to get non-static variants */
	private static final PropertyInteger DEFAULT = PropertyInteger.create("variant", 0, 1);
	protected int nSubBlocks;
	public boolean sameIcon;
	private JointList<IProperty> variants = new JointList();

	/** A custom render type Tile Entity container. */
	public BlockContainerLCMeta(Block parent, int nSubBlocks, float hardness, float resistance, EnumBlockRenderType renderType, boolean sameIcon) {
		super(parent, hardness, resistance);
		this.renderType = renderType;
		this.isBlockContainer = true;
		if(nSubBlocks > 16) throw new IllegalArgumentException("More than 16 metadata states is unsupported!");
		this.nSubBlocks = nSubBlocks;
		this.sameIcon = sameIcon;
		ReflectionHelper.setPrivateValue(Block.class, this, createBlockState(), RefMisc.DEV ? "blockState" : "field_176227_L");
		setDefaultState();
	}

	/** A standard model Tile Entity container. */
	public BlockContainerLCMeta(Block parent, int nSubBlocks, float hardness, float resistance) {
		this(parent, nSubBlocks, hardness, resistance, EnumBlockRenderType.MODEL, false);
	}
	
	/** Get the properties for this block */
	protected List<IProperty> getProperties() {
		return variants = new JointList().join(PropertyInteger.create("variant", 0, nSubBlocks - 1));
	}
	
	/** Set the default block state */
	protected void setDefaultState() {
		this.setDefaultState(this.blockState.getBaseState().withProperty(variants.get(0), 0));
	}

	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
	    for (int i = 0; i < nSubBlocks; i++) {
	        list.add(new ItemStack(item, 1, i));
	    }
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		if(variants == null) {
			return new BlockStateContainer(this, new IProperty[] { DEFAULT });
		} else {
			List<IProperty> list = this.getProperties();
			return new BlockStateContainer(this, list.toArray(new IProperty[list.size()]));
		}
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
	    return getDefaultState().withProperty(variants.get(0), meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
	    int meta = (int)state.getValue(variants.get(0));
	    return meta;
	}
	
	@Override
	public int damageDropped(IBlockState state) {
	    return getMetaFromState(state);
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
	    return new ItemStack(this, 1, this.getMetaFromState(state));
	}
	
	@Override
	public Class getItemClass() {
		return ItemBlockMeta.class;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerRender(ItemModelMesher mesher) {
		JointList<ResourceLocation> names = new JointList();
		Item item = Item.getItemFromBlock(this);
		if(sameIcon) {
			for(int meta = 0; meta < nSubBlocks; meta++) {
				names.join(new ResourceLocation(RefStrings.MODID + ":" + this.getShorthandName()));
			}
			ModelBakery.registerItemVariants(item, names.toArray(new ResourceLocation[names.size()]));
			for(int meta = 0; meta < nSubBlocks; meta++) {
				mesher.register(item, meta, new ModelResourceLocation(RefStrings.MODID + ":" + this.getShorthandName(), "inventory"));
			}
		} else {
			for(int meta = 0; meta < nSubBlocks; meta++) {
				names.join(new ResourceLocation(RefStrings.MODID + ":" + this.getShorthandName() + "_" + meta));
			}
			ModelBakery.registerItemVariants(item, names.toArray(new ResourceLocation[names.size()]));
			for(int meta = 0; meta < nSubBlocks; meta++) {
				mesher.register(item, meta, new ModelResourceLocation(RefStrings.MODID + ":" + this.getShorthandName() + "_" + meta, "inventory"));
			}
		}
	}

}
