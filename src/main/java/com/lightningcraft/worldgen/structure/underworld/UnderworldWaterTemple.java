package com.lightningcraft.worldgen.structure.underworld;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

import com.lightningcraft.blocks.BlockSlabLC;
import com.lightningcraft.blocks.BlockStone;
import com.lightningcraft.blocks.LCBlocks;
import com.lightningcraft.items.LCItems;
import com.lightningcraft.ref.RefMisc;
import com.lightningcraft.util.WeightedRandomChestContent;
import com.lightningcraft.util.WorldUtils;
import com.lightningcraft.worldgen.structure.Feature;
import com.lightningcraft.worldgen.structure.LootChestGroup;

/** The underworld water temple structure */
public class UnderworldWaterTemple extends Feature {
	
	// loot chests
	private static final int nChests = 1;
	private static final int minStacks = 5;
	private static final int maxStacks = 10;

	// Blocks to use
	private static final Block stairBlock = LCBlocks.underStairs;
	private static final Block slabBlock = LCBlocks.slabBlock;
	private static final int slabMeta = BlockSlabLC.UNDER;
	private static final Block mainBlock = LCBlocks.stoneBlock;
	private static final int mainMeta = BlockStone.UNDER_BRICK;
	private static final int chiselMeta = BlockStone.UNDER_BRICK_CHISELED; // metadata for chiseled bricks
	private static final Block lightBlock = LCBlocks.lightBlock;
	private static final Block glassBlock = Blocks.STAINED_GLASS;
	private static final int glassMeta = 15; // metadata for stained glass
	private static final Block waterBlock = Blocks.FLOWING_WATER;
	private static final Block lapisBlock = Blocks.LAPIS_BLOCK;
	private static final Block wallBlock = LCBlocks.wallBlock;

	public UnderworldWaterTemple() {
		this(new Random(), 0, 0);
	}

	public UnderworldWaterTemple(Random rand, int par2, int par3) {
		super(rand, par2, 64, par3, 13, 17, 13, false); // no rotation
		this.spawnMinY = 31;
		this.spawnMaxY = 32;
		
		lootChests = new LootChestGroup(nChests, minStacks, maxStacks, new WeightedRandomChestContent[]
			{new WeightedRandomChestContent(Items.WATER_BUCKET, 0, 1, 1, 5), new WeightedRandomChestContent(Items.DYE, 4, 5, 10, 30), 
			new WeightedRandomChestContent(Items.GOLD_INGOT, 0, 2, 7, 20), new WeightedRandomChestContent(Items.EMERALD, 0, 1, 3, 20), 
			new WeightedRandomChestContent(Items.BOAT, 0, 1, 1, 15), new WeightedRandomChestContent(Items.IRON_INGOT, 0, 3, 7, 20), 
			new WeightedRandomChestContent(LCItems.soulSword, 0, 1, 1, 1), new WeightedRandomChestContent(LCItems.zombieSword, 0, 1, 1, 1), 
			new WeightedRandomChestContent(LCItems.featherSword, 0, 1, 1, 1), new WeightedRandomChestContent(LCItems.enderSword, 0, 1, 1, 1),
			new WeightedRandomChestContent(LCItems.elecSword, 0, 1, 1, 1)});
	}

	/** Water temple spawn mechanics */
	@Override
	protected boolean findSpawnPosition(World world, StructureBoundingBox box, int yoff) {
		if(this.structY >= 0) {
			if(RefMisc.DEBUG) System.out.println("Structure already exists @ (" + this.boundingBox.minX + ", " + this.boundingBox.minZ + ")");
			return true;
		} else {
			if(RefMisc.DEBUG) System.out.println("Spawn check initiated at (" + this.boundingBox.minX + ", " + this.boundingBox.minZ + ")");

			// check each corner
			if(WorldUtils.canSpawnAtPosition(world, this.boundingBox.minX, this.spawnMaxY, this.boundingBox.minZ, Blocks.WATER, this.scatteredFeatureSizeY) &&
					WorldUtils.canSpawnAtPosition(world, this.boundingBox.maxX, this.spawnMaxY, this.boundingBox.minZ, Blocks.WATER, this.scatteredFeatureSizeY) &&
					WorldUtils.canSpawnAtPosition(world, this.boundingBox.minX, this.spawnMaxY, this.boundingBox.maxZ, Blocks.WATER, this.scatteredFeatureSizeY) && 
					WorldUtils.canSpawnAtPosition(world, this.boundingBox.maxX, this.spawnMaxY, this.boundingBox.maxZ, Blocks.WATER, this.scatteredFeatureSizeY)) {
				this.structY = this.spawnMinY;
				if(RefMisc.DEBUG) System.out.println("Spawned success at position (" + this.boundingBox.minX + ", " + this.spawnMinY + ", " + this.boundingBox.minZ + ")");
				this.boundingBox.offset(0, this.structY - this.boundingBox.minY + yoff, 0);
				return true;
			} else {
				if(RefMisc.DEBUG) System.out.println("Spawned failed at position (" + this.boundingBox.minX + ", " + this.spawnMinY + ", " + this.boundingBox.minZ + ")");
				return false;
			}
		}
	}

	/**
	 * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
	 * Mineshafts at the end, it adds Fences...
	 */
	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox box) {
		if (!this.findSpawnPosition(world, box, 0)) {
			return false;
		} else {

			// some blocks to add to the temple
			this.addBlock(world, 0, 1, 4, mainBlock, chiselMeta);
			this.addBlock(world, 0, 1, 8, mainBlock, chiselMeta);
			this.addBlock(world, 0, 2, 4, mainBlock, chiselMeta);
			this.addBlock(world, 0, 2, 8, mainBlock, chiselMeta);
			this.addBlock(world, 0, 3, 4, stairBlock, 0);
			this.addBlock(world, 0, 3, 8, stairBlock, 0);
			this.addBlock(world, 1, 0, 6, mainBlock, mainMeta);
			this.addBlock(world, 1, 1, 3, mainBlock, chiselMeta);
			this.addBlock(world, 1, 1, 4, mainBlock, mainMeta);
			this.addBlock(world, 1, 1, 5, mainBlock, mainMeta);
			this.addBlock(world, 1, 1, 7, mainBlock, mainMeta);
			this.addBlock(world, 1, 1, 8, mainBlock, mainMeta);
			this.addBlock(world, 1, 1, 9, mainBlock, chiselMeta);
			this.addBlock(world, 1, 2, 3, mainBlock, chiselMeta);
			this.addBlock(world, 1, 2, 4, mainBlock, mainMeta);
			this.addBlock(world, 1, 2, 5, mainBlock, mainMeta);
			this.addBlock(world, 1, 2, 7, mainBlock, mainMeta);
			this.addBlock(world, 1, 2, 8, mainBlock, mainMeta);
			this.addBlock(world, 1, 2, 9, mainBlock, chiselMeta);
			this.addBlock(world, 1, 3, 3, stairBlock, 2);
			this.addBlock(world, 1, 3, 4, mainBlock, mainMeta);
			this.addBlock(world, 1, 3, 5, stairBlock, 3);
			this.addBlock(world, 1, 3, 7, stairBlock, 2);
			this.addBlock(world, 1, 3, 8, mainBlock, mainMeta);
			this.addBlock(world, 1, 3, 9, stairBlock, 3);
			this.addBlock(world, 1, 4, 4, mainBlock, mainMeta);
			this.addBlock(world, 1, 4, 8, mainBlock, mainMeta);
			this.addBlock(world, 1, 5, 4, mainBlock, chiselMeta);
			this.addBlock(world, 1, 5, 5, stairBlock, 7);
			this.addBlock(world, 1, 5, 7, stairBlock, 6);
			this.addBlock(world, 1, 5, 8, mainBlock, chiselMeta);
			this.addBlock(world, 1, 6, 4, mainBlock, mainMeta);
			this.addBlock(world, 1, 6, 5, mainBlock, mainMeta);
			this.addBlock(world, 1, 6, 6, stairBlock, 4);
			this.addBlock(world, 1, 6, 7, mainBlock, mainMeta);
			this.addBlock(world, 1, 6, 8, mainBlock, mainMeta);
			this.addBlock(world, 1, 7, 4, stairBlock, 0);
			this.addBlock(world, 1, 7, 5, mainBlock, mainMeta);
			this.addBlock(world, 1, 7, 6, lapisBlock, 0);
			this.addBlock(world, 1, 7, 7, mainBlock, mainMeta);
			this.addBlock(world, 1, 7, 8, stairBlock, 0);
			this.addBlock(world, 1, 8, 5, slabBlock, slabMeta);
			this.addBlock(world, 1, 8, 6, slabBlock, slabMeta);
			this.addBlock(world, 1, 8, 7, slabBlock, slabMeta);
			this.addBlock(world, 2, 0, 4, mainBlock, mainMeta);
			this.addBlock(world, 2, 0, 5, mainBlock, mainMeta);
			this.addBlock(world, 2, 0, 6, mainBlock, mainMeta);
			this.addBlock(world, 2, 0, 7, mainBlock, mainMeta);
			this.addBlock(world, 2, 0, 8, mainBlock, mainMeta);
			this.addBlock(world, 2, 1, 2, lapisBlock, 0);
			this.addBlock(world, 2, 1, 3, mainBlock, mainMeta);
			this.addBlock(world, 2, 1, 9, mainBlock, mainMeta);
			this.addBlock(world, 2, 1, 10, lapisBlock, 0);
			this.addBlock(world, 2, 2, 2, wallBlock, 0);
			this.addBlock(world, 2, 2, 3, glassBlock, glassMeta);
			this.addBlock(world, 2, 2, 9, glassBlock, glassMeta);
			this.addBlock(world, 2, 2, 10, wallBlock, 0);
			this.addBlock(world, 2, 3, 2, wallBlock, 0);
			this.addBlock(world, 2, 3, 3, glassBlock, glassMeta);
			this.addBlock(world, 2, 3, 9, glassBlock, glassMeta);
			this.addBlock(world, 2, 3, 10, wallBlock, 0);
			this.addBlock(world, 2, 4, 2, wallBlock, 0);
			this.addBlock(world, 2, 4, 3, glassBlock, glassMeta);
			this.addBlock(world, 2, 4, 9, glassBlock, glassMeta);
			this.addBlock(world, 2, 4, 10, wallBlock, 0);
			this.addBlock(world, 2, 5, 2, wallBlock, 0);
			this.addBlock(world, 2, 5, 3, glassBlock, glassMeta);
			this.addBlock(world, 2, 5, 6, stairBlock, 4);
			this.addBlock(world, 2, 5, 9, glassBlock, glassMeta);
			this.addBlock(world, 2, 5, 10, wallBlock, 0);
			this.addBlock(world, 2, 6, 2, slabBlock, slabMeta);
			this.addBlock(world, 2, 6, 3, mainBlock, mainMeta);
			this.addBlock(world, 2, 6, 6, stairBlock, 1);
			this.addBlock(world, 2, 6, 9, mainBlock, mainMeta);
			this.addBlock(world, 2, 6, 10, slabBlock, slabMeta);
			this.addBlock(world, 2, 7, 3, stairBlock, 0);
			this.addBlock(world, 2, 7, 4, mainBlock, mainMeta);
			this.addBlock(world, 2, 7, 5, lightBlock, 0);
			this.addBlock(world, 2, 7, 6, lightBlock, 0);
			this.addBlock(world, 2, 7, 7, lightBlock, 0);
			this.addBlock(world, 2, 7, 8, mainBlock, mainMeta);
			this.addBlock(world, 2, 7, 9, stairBlock, 0);
			this.addBlock(world, 2, 8, 4, slabBlock, slabMeta);
			this.addBlock(world, 2, 8, 6, stairBlock, 0);
			this.addBlock(world, 2, 8, 8, slabBlock, slabMeta);
			this.addBlock(world, 2, 11, 5, stairBlock, 4);
			this.addBlock(world, 2, 11, 7, stairBlock, 4);
			this.addBlock(world, 2, 12, 5, slabBlock, slabMeta);
			this.addBlock(world, 2, 12, 6, slabBlock, slabMeta);
			this.addBlock(world, 2, 12, 7, slabBlock, slabMeta);
			this.addBlock(world, 3, 0, 3, mainBlock, mainMeta);
			this.addBlock(world, 3, 0, 4, mainBlock, mainMeta);
			this.addBlock(world, 3, 0, 5, mainBlock, mainMeta);
			this.addBlock(world, 3, 0, 6, mainBlock, mainMeta);
			this.addBlock(world, 3, 0, 7, mainBlock, mainMeta);
			this.addBlock(world, 3, 0, 8, mainBlock, mainMeta);
			this.addBlock(world, 3, 0, 9, mainBlock, mainMeta);
			this.addBlock(world, 3, 1, 1, mainBlock, chiselMeta);
			this.addBlock(world, 3, 1, 2, mainBlock, mainMeta);
			this.addBlock(world, 3, 1, 10, mainBlock, mainMeta);
			this.addBlock(world, 3, 1, 11, mainBlock, chiselMeta);
			this.addBlock(world, 3, 2, 1, mainBlock, chiselMeta);
			this.addBlock(world, 3, 2, 2, glassBlock, glassMeta);
			this.addBlock(world, 3, 2, 10, glassBlock, glassMeta);
			this.addBlock(world, 3, 2, 11, mainBlock, chiselMeta);
			this.addBlock(world, 3, 3, 1, stairBlock, 0);
			this.addBlock(world, 3, 3, 2, glassBlock, glassMeta);
			this.addBlock(world, 3, 3, 10, glassBlock, glassMeta);
			this.addBlock(world, 3, 3, 11, stairBlock, 0);
			this.addBlock(world, 3, 4, 2, glassBlock, glassMeta);
			this.addBlock(world, 3, 4, 6, stairBlock, 4);
			this.addBlock(world, 3, 4, 10, glassBlock, glassMeta);
			this.addBlock(world, 3, 5, 2, glassBlock, glassMeta);
			this.addBlock(world, 3, 5, 6, stairBlock, 1);
			this.addBlock(world, 3, 5, 10, glassBlock, glassMeta);
			this.addBlock(world, 3, 6, 2, mainBlock, mainMeta);
			this.addBlock(world, 3, 6, 10, mainBlock, mainMeta);
			this.addBlock(world, 3, 7, 2, stairBlock, 2);
			this.addBlock(world, 3, 7, 3, mainBlock, mainMeta);
			this.addBlock(world, 3, 7, 4, lightBlock, 0);
			this.addBlock(world, 3, 7, 5, mainBlock, mainMeta);
			this.addBlock(world, 3, 7, 6, mainBlock, mainMeta);
			this.addBlock(world, 3, 7, 7, mainBlock, mainMeta);
			this.addBlock(world, 3, 7, 8, lightBlock, 0);
			this.addBlock(world, 3, 7, 9, mainBlock, mainMeta);
			this.addBlock(world, 3, 7, 10, stairBlock, 3);
			this.addBlock(world, 3, 8, 3, slabBlock, slabMeta);
			this.addBlock(world, 3, 8, 5, mainBlock, mainMeta);
			this.addBlock(world, 3, 8, 6, waterBlock, 0);
			this.addBlock(world, 3, 8, 7, mainBlock, mainMeta);
			this.addBlock(world, 3, 8, 9, slabBlock, slabMeta);
			this.addBlock(world, 3, 9, 5, mainBlock, chiselMeta);
			this.addBlock(world, 3, 9, 7, mainBlock, chiselMeta);
			this.addBlock(world, 3, 10, 5, mainBlock, chiselMeta);
			this.addBlock(world, 3, 10, 7, mainBlock, chiselMeta);
			this.addBlock(world, 3, 11, 4, stairBlock, 6);
			this.addBlock(world, 3, 11, 5, mainBlock, mainMeta);
			this.addBlock(world, 3, 11, 7, mainBlock, mainMeta);
			this.addBlock(world, 3, 11, 8, stairBlock, 7);
			this.addBlock(world, 3, 12, 4, slabBlock, slabMeta);
			this.addBlock(world, 3, 12, 5, lightBlock, 0);
			this.addBlock(world, 3, 12, 6, lightBlock, 0);
			this.addBlock(world, 3, 12, 7, lightBlock, 0);
			this.addBlock(world, 3, 12, 8, slabBlock, slabMeta);
			this.addBlock(world, 3, 13, 5, stairBlock, 0);
			this.addBlock(world, 3, 13, 6, lapisBlock, 0);
			this.addBlock(world, 3, 13, 7, stairBlock, 0);
			this.addBlock(world, 3, 14, 6, stairBlock, 0);
			this.addBlock(world, 4, 0, 2, mainBlock, mainMeta);
			this.addBlock(world, 4, 0, 3, mainBlock, mainMeta);
			this.addBlock(world, 4, 0, 4, mainBlock, mainMeta);
			this.addBlock(world, 4, 0, 5, mainBlock, mainMeta);
			this.addBlock(world, 4, 0, 6, mainBlock, mainMeta);
			this.addBlock(world, 4, 0, 7, mainBlock, mainMeta);
			this.addBlock(world, 4, 0, 8, mainBlock, mainMeta);
			this.addBlock(world, 4, 0, 9, mainBlock, mainMeta);
			this.addBlock(world, 4, 0, 10, mainBlock, mainMeta);
			this.addBlock(world, 4, 1, 0, mainBlock, chiselMeta);
			this.addBlock(world, 4, 1, 1, mainBlock, mainMeta);
			this.addBlock(world, 4, 1, 6, mainBlock, mainMeta);
			this.addBlock(world, 4, 1, 11, mainBlock, mainMeta);
			this.addBlock(world, 4, 1, 12, mainBlock, chiselMeta);
			this.addBlock(world, 4, 2, 0, mainBlock, chiselMeta);
			this.addBlock(world, 4, 2, 1, mainBlock, mainMeta);
			this.addBlock(world, 4, 2, 6, mainBlock, mainMeta);
			this.addBlock(world, 4, 2, 11, mainBlock, mainMeta);
			this.addBlock(world, 4, 2, 12, mainBlock, chiselMeta);
			this.addBlock(world, 4, 3, 0, stairBlock, 2);
			this.addBlock(world, 4, 3, 1, mainBlock, mainMeta);
			this.addBlock(world, 4, 3, 6, mainBlock, mainMeta);
			this.addBlock(world, 4, 3, 11, mainBlock, mainMeta);
			this.addBlock(world, 4, 3, 12, stairBlock, 3);
			this.addBlock(world, 4, 4, 1, mainBlock, mainMeta);
			this.addBlock(world, 4, 4, 5, stairBlock, 6);
			this.addBlock(world, 4, 4, 7, stairBlock, 7);
			this.addBlock(world, 4, 4, 11, mainBlock, mainMeta);
			this.addBlock(world, 4, 5, 1, mainBlock, chiselMeta);
			this.addBlock(world, 4, 5, 11, mainBlock, chiselMeta);
			this.addBlock(world, 4, 6, 1, mainBlock, mainMeta);
			this.addBlock(world, 4, 6, 11, mainBlock, mainMeta);
			this.addBlock(world, 4, 7, 1, stairBlock, 2);
			this.addBlock(world, 4, 7, 2, mainBlock, mainMeta);
			this.addBlock(world, 4, 7, 3, lightBlock, 0);
			this.addBlock(world, 4, 7, 4, mainBlock, mainMeta);
			this.addBlock(world, 4, 7, 8, mainBlock, mainMeta);
			this.addBlock(world, 4, 7, 9, lightBlock, 0);
			this.addBlock(world, 4, 7, 10, mainBlock, mainMeta);
			this.addBlock(world, 4, 7, 11, stairBlock, 3);
			this.addBlock(world, 4, 8, 2, slabBlock, slabMeta);
			this.addBlock(world, 4, 8, 4, slabBlock, slabMeta);
			this.addBlock(world, 4, 8, 8, slabBlock, slabMeta);
			this.addBlock(world, 4, 8, 10, slabBlock, slabMeta);
			this.addBlock(world, 4, 11, 3, stairBlock, 4);
			this.addBlock(world, 4, 11, 5, stairBlock, 5);
			this.addBlock(world, 4, 11, 7, stairBlock, 5);
			this.addBlock(world, 4, 11, 9, stairBlock, 4);
			this.addBlock(world, 4, 12, 3, slabBlock, slabMeta);
			this.addBlock(world, 4, 12, 4, lightBlock, 0);
			this.addBlock(world, 4, 12, 5, mainBlock, mainMeta);
			this.addBlock(world, 4, 12, 6, stairBlock, 5);
			this.addBlock(world, 4, 12, 7, mainBlock, mainMeta);
			this.addBlock(world, 4, 12, 8, lightBlock, 0);
			this.addBlock(world, 4, 12, 9, slabBlock, slabMeta);
			this.addBlock(world, 4, 13, 4, slabBlock, slabMeta);
			this.addBlock(world, 4, 13, 5, mainBlock, mainMeta);
			this.addBlock(world, 4, 13, 6, mainBlock, mainMeta);
			this.addBlock(world, 4, 13, 7, mainBlock, mainMeta);
			this.addBlock(world, 4, 13, 8, slabBlock, slabMeta);
			this.addBlock(world, 4, 14, 5, stairBlock, 0);
			this.addBlock(world, 4, 14, 6, mainBlock, mainMeta);
			this.addBlock(world, 4, 14, 7, stairBlock, 0);
			this.addBlock(world, 5, 0, 2, mainBlock, mainMeta);
			this.addBlock(world, 5, 0, 3, mainBlock, mainMeta);
			this.addBlock(world, 5, 0, 4, mainBlock, mainMeta);
			this.addBlock(world, 5, 0, 6, mainBlock, chiselMeta);
			this.addBlock(world, 5, 0, 8, mainBlock, mainMeta);
			this.addBlock(world, 5, 0, 9, mainBlock, mainMeta);
			this.addBlock(world, 5, 0, 10, mainBlock, mainMeta);
			this.addBlock(world, 5, 1, 1, mainBlock, mainMeta);
			this.addBlock(world, 5, 1, 5, slabBlock, slabMeta);
			this.addBlock(world, 5, 1, 7, slabBlock, slabMeta);
			this.addBlock(world, 5, 1, 11, mainBlock, mainMeta);
			this.addBlock(world, 5, 2, 1, mainBlock, chiselMeta);
			this.addBlock(world, 5, 2, 11, mainBlock, chiselMeta);
			this.addBlock(world, 5, 3, 1, stairBlock, 1);
			this.addBlock(world, 5, 3, 11, stairBlock, 1);
			this.addBlock(world, 5, 4, 4, stairBlock, 4);
			this.addBlock(world, 5, 4, 8, stairBlock, 4);
			this.addBlock(world, 5, 5, 1, stairBlock, 5);
			this.addBlock(world, 5, 5, 11, stairBlock, 5);
			this.addBlock(world, 5, 6, 1, mainBlock, mainMeta);
			this.addBlock(world, 5, 6, 11, mainBlock, mainMeta);
			this.addBlock(world, 5, 7, 1, mainBlock, mainMeta);
			this.addBlock(world, 5, 7, 2, lightBlock, 0);
			this.addBlock(world, 5, 7, 3, mainBlock, mainMeta);
			this.addBlock(world, 5, 7, 9, mainBlock, mainMeta);
			this.addBlock(world, 5, 7, 10, lightBlock, 0);
			this.addBlock(world, 5, 7, 11, mainBlock, mainMeta);
			this.addBlock(world, 5, 8, 1, slabBlock, slabMeta);
			this.addBlock(world, 5, 8, 3, mainBlock, mainMeta);
			this.addBlock(world, 5, 8, 9, mainBlock, mainMeta);
			this.addBlock(world, 5, 8, 11, slabBlock, slabMeta);
			this.addBlock(world, 5, 9, 3, mainBlock, chiselMeta);
			this.addBlock(world, 5, 9, 9, mainBlock, chiselMeta);
			this.addBlock(world, 5, 10, 3, mainBlock, chiselMeta);
			this.addBlock(world, 5, 10, 9, mainBlock, chiselMeta);
			this.addBlock(world, 5, 11, 2, stairBlock, 6);
			this.addBlock(world, 5, 11, 3, mainBlock, mainMeta);
			this.addBlock(world, 5, 11, 4, stairBlock, 7);
			this.addBlock(world, 5, 11, 8, stairBlock, 6);
			this.addBlock(world, 5, 11, 9, mainBlock, mainMeta);
			this.addBlock(world, 5, 11, 10, stairBlock, 7);
			this.addBlock(world, 5, 12, 2, slabBlock, slabMeta);
			this.addBlock(world, 5, 12, 3, lightBlock, 0);
			this.addBlock(world, 5, 12, 4, mainBlock, mainMeta);
			this.addBlock(world, 5, 12, 8, mainBlock, mainMeta);
			this.addBlock(world, 5, 12, 9, lightBlock, 0);
			this.addBlock(world, 5, 12, 10, slabBlock, slabMeta);
			this.addBlock(world, 5, 13, 3, stairBlock, 2);
			this.addBlock(world, 5, 13, 4, mainBlock, mainMeta);
			this.addBlock(world, 5, 13, 8, mainBlock, mainMeta);
			this.addBlock(world, 5, 13, 9, stairBlock, 3);
			this.addBlock(world, 5, 14, 4, stairBlock, 2);
			this.addBlock(world, 5, 14, 5, stairBlock, 5);
			this.addBlock(world, 5, 14, 6, stairBlock, 5);
			this.addBlock(world, 5, 14, 7, stairBlock, 6);
			this.addBlock(world, 5, 14, 8, stairBlock, 3);
			this.addBlock(world, 5, 15, 5, stairBlock, 2);
			this.addBlock(world, 5, 15, 6, stairBlock, 0);
			this.addBlock(world, 5, 15, 7, stairBlock, 3);
			this.addBlock(world, 6, 0, 1, mainBlock, mainMeta);
			this.addBlock(world, 6, 0, 2, mainBlock, mainMeta);
			this.addBlock(world, 6, 0, 3, mainBlock, mainMeta);
			this.addBlock(world, 6, 0, 4, mainBlock, mainMeta);
			this.addBlock(world, 6, 0, 5, mainBlock, chiselMeta);
			this.addBlock(world, 6, 0, 7, mainBlock, chiselMeta);
			this.addBlock(world, 6, 0, 8, mainBlock, mainMeta);
			this.addBlock(world, 6, 0, 9, mainBlock, mainMeta);
			this.addBlock(world, 6, 0, 10, mainBlock, mainMeta);
			this.addBlock(world, 6, 0, 11, mainBlock, mainMeta);
			this.addBlock(world, 6, 1, 4, mainBlock, mainMeta);
			this.addBlock(world, 6, 1, 8, mainBlock, mainMeta);
			this.addBlock(world, 6, 2, 4, mainBlock, mainMeta);
			this.addBlock(world, 6, 2, 8, mainBlock, mainMeta);
			this.addBlock(world, 6, 3, 4, mainBlock, mainMeta);
			this.addBlock(world, 6, 3, 8, mainBlock, mainMeta);
			this.addBlock(world, 6, 4, 3, stairBlock, 6);
			this.addBlock(world, 6, 4, 9, stairBlock, 7);
			this.addBlock(world, 6, 5, 2, stairBlock, 6);
			this.addBlock(world, 6, 5, 3, stairBlock, 3);
			this.addBlock(world, 6, 5, 9, stairBlock, 2);
			this.addBlock(world, 6, 5, 10, stairBlock, 7);
			this.addBlock(world, 6, 6, 1, stairBlock, 6);
			this.addBlock(world, 6, 6, 2, stairBlock, 3);
			this.addBlock(world, 6, 6, 10, stairBlock, 2);
			this.addBlock(world, 6, 6, 11, stairBlock, 7);
			this.addBlock(world, 6, 7, 1, lapisBlock, 0);
			this.addBlock(world, 6, 7, 2, lightBlock, 0);
			this.addBlock(world, 6, 7, 3, mainBlock, mainMeta);
			this.addBlock(world, 6, 7, 9, mainBlock, mainMeta);
			this.addBlock(world, 6, 7, 10, lightBlock, 0);
			this.addBlock(world, 6, 7, 11, lapisBlock, 0);
			this.addBlock(world, 6, 8, 1, slabBlock, slabMeta);
			this.addBlock(world, 6, 8, 2, stairBlock, 2);
			this.addBlock(world, 6, 8, 3, waterBlock, 0);
			this.addBlock(world, 6, 8, 9, waterBlock, 0);
			this.addBlock(world, 6, 8, 10, stairBlock, 3);
			this.addBlock(world, 6, 8, 11, slabBlock, slabMeta);
			this.addBlock(world, 6, 12, 2, slabBlock, slabMeta);
			this.addBlock(world, 6, 12, 3, lightBlock, 0);
			this.addBlock(world, 6, 12, 4, stairBlock, 7);
			this.addBlock(world, 6, 12, 8, stairBlock, 6);
			this.addBlock(world, 6, 12, 9, lightBlock, 0);
			this.addBlock(world, 6, 12, 10, slabBlock, slabMeta);
			this.addBlock(world, 6, 13, 3, lapisBlock, 0);
			this.addBlock(world, 6, 13, 4, mainBlock, mainMeta);
			this.addBlock(world, 6, 13, 8, mainBlock, mainMeta);
			this.addBlock(world, 6, 13, 9, lapisBlock, 0);
			this.addBlock(world, 6, 14, 3, stairBlock, 2);
			this.addBlock(world, 6, 14, 4, mainBlock, mainMeta);
			this.addBlock(world, 6, 14, 5, stairBlock, 7);
			this.addBlock(world, 6, 14, 6, waterBlock, 0);
			this.addBlock(world, 6, 14, 7, stairBlock, 6);
			this.addBlock(world, 6, 14, 8, mainBlock, mainMeta);
			this.addBlock(world, 6, 14, 9, stairBlock, 3);
			this.addBlock(world, 6, 15, 5, stairBlock, 2);
			this.addBlock(world, 6, 15, 6, lightBlock, 0);
			this.addBlock(world, 6, 15, 7, stairBlock, 3);
			this.addBlock(world, 6, 16, 6, slabBlock, slabMeta);
			this.addBlock(world, 7, 0, 2, mainBlock, mainMeta);
			this.addBlock(world, 7, 0, 3, mainBlock, mainMeta);
			this.addBlock(world, 7, 0, 4, mainBlock, mainMeta);
			this.addBlock(world, 7, 0, 6, mainBlock, chiselMeta);
			this.addBlock(world, 7, 0, 8, mainBlock, mainMeta);
			this.addBlock(world, 7, 0, 9, mainBlock, mainMeta);
			this.addBlock(world, 7, 0, 10, mainBlock, mainMeta);
			this.addBlock(world, 7, 1, 1, mainBlock, mainMeta);
			this.addBlock(world, 7, 1, 5, slabBlock, slabMeta);
			this.addBlock(world, 7, 1, 7, slabBlock, slabMeta);
			this.addBlock(world, 7, 1, 11, mainBlock, mainMeta);
			this.addBlock(world, 7, 2, 1, mainBlock, chiselMeta);
			this.addBlock(world, 7, 2, 11, mainBlock, chiselMeta);
			this.addBlock(world, 7, 3, 1, stairBlock, 0);
			this.addBlock(world, 7, 3, 11, stairBlock, 0);
			this.addBlock(world, 7, 4, 4, stairBlock, 5);
			this.addBlock(world, 7, 4, 8, stairBlock, 5);
			this.addBlock(world, 7, 5, 1, stairBlock, 4);
			this.addBlock(world, 7, 5, 11, stairBlock, 4);
			this.addBlock(world, 7, 6, 1, mainBlock, mainMeta);
			this.addBlock(world, 7, 6, 11, mainBlock, mainMeta);
			this.addBlock(world, 7, 7, 1, mainBlock, mainMeta);
			this.addBlock(world, 7, 7, 2, lightBlock, 0);
			this.addBlock(world, 7, 7, 3, mainBlock, mainMeta);
			this.addBlock(world, 7, 7, 9, mainBlock, mainMeta);
			this.addBlock(world, 7, 7, 10, lightBlock, 0);
			this.addBlock(world, 7, 7, 11, mainBlock, mainMeta);
			this.addBlock(world, 7, 8, 1, slabBlock, slabMeta);
			this.addBlock(world, 7, 8, 3, mainBlock, mainMeta);
			this.addBlock(world, 7, 8, 9, mainBlock, mainMeta);
			this.addBlock(world, 7, 8, 11, slabBlock, slabMeta);
			this.addBlock(world, 7, 9, 3, mainBlock, chiselMeta);
			this.addBlock(world, 7, 9, 9, mainBlock, chiselMeta);
			this.addBlock(world, 7, 10, 3, mainBlock, chiselMeta);
			this.addBlock(world, 7, 10, 9, mainBlock, chiselMeta);
			this.addBlock(world, 7, 11, 2, stairBlock, 6);
			this.addBlock(world, 7, 11, 3, mainBlock, mainMeta);
			this.addBlock(world, 7, 11, 4, stairBlock, 7);
			this.addBlock(world, 7, 11, 8, stairBlock, 6);
			this.addBlock(world, 7, 11, 9, mainBlock, mainMeta);
			this.addBlock(world, 7, 11, 10, stairBlock, 7);
			this.addBlock(world, 7, 12, 2, slabBlock, slabMeta);
			this.addBlock(world, 7, 12, 3, lightBlock, 0);
			this.addBlock(world, 7, 12, 4, mainBlock, mainMeta);
			this.addBlock(world, 7, 12, 8, mainBlock, mainMeta);
			this.addBlock(world, 7, 12, 9, lightBlock, 0);
			this.addBlock(world, 7, 12, 10, slabBlock, slabMeta);
			this.addBlock(world, 7, 13, 3, stairBlock, 2);
			this.addBlock(world, 7, 13, 4, mainBlock, mainMeta);
			this.addBlock(world, 7, 13, 8, mainBlock, mainMeta);
			this.addBlock(world, 7, 13, 9, stairBlock, 3);
			this.addBlock(world, 7, 14, 4, stairBlock, 2);
			this.addBlock(world, 7, 14, 5, stairBlock, 4);
			this.addBlock(world, 7, 14, 6, stairBlock, 4);
			this.addBlock(world, 7, 14, 7, stairBlock, 6);
			this.addBlock(world, 7, 14, 8, stairBlock, 3);
			this.addBlock(world, 7, 15, 5, stairBlock, 1);
			this.addBlock(world, 7, 15, 6, stairBlock, 1);
			this.addBlock(world, 7, 15, 7, stairBlock, 3);
			this.addBlock(world, 8, 0, 2, mainBlock, mainMeta);
			this.addBlock(world, 8, 0, 3, mainBlock, mainMeta);
			this.addBlock(world, 8, 0, 4, mainBlock, mainMeta);
			this.addBlock(world, 8, 0, 5, mainBlock, mainMeta);
			this.addBlock(world, 8, 0, 6, mainBlock, mainMeta);
			this.addBlock(world, 8, 0, 7, mainBlock, mainMeta);
			this.addBlock(world, 8, 0, 8, mainBlock, mainMeta);
			this.addBlock(world, 8, 0, 9, mainBlock, mainMeta);
			this.addBlock(world, 8, 0, 10, mainBlock, mainMeta);
			this.addBlock(world, 8, 1, 0, mainBlock, chiselMeta);
			this.addBlock(world, 8, 1, 1, mainBlock, mainMeta);
			this.addBlock(world, 8, 1, 6, mainBlock, mainMeta);
			this.addBlock(world, 8, 1, 11, mainBlock, mainMeta);
			this.addBlock(world, 8, 1, 12, mainBlock, chiselMeta);
			this.addBlock(world, 8, 2, 0, mainBlock, chiselMeta);
			this.addBlock(world, 8, 2, 1, mainBlock, mainMeta);
			this.addBlock(world, 8, 2, 6, mainBlock, mainMeta);
			this.addBlock(world, 8, 2, 11, mainBlock, mainMeta);
			this.addBlock(world, 8, 2, 12, mainBlock, chiselMeta);
			this.addBlock(world, 8, 3, 0, stairBlock, 2);
			this.addBlock(world, 8, 3, 1, mainBlock, mainMeta);
			this.addBlock(world, 8, 3, 6, mainBlock, mainMeta);
			this.addBlock(world, 8, 3, 11, mainBlock, mainMeta);
			this.addBlock(world, 8, 3, 12, stairBlock, 3);
			this.addBlock(world, 8, 4, 1, mainBlock, mainMeta);
			this.addBlock(world, 8, 4, 5, stairBlock, 6);
			this.addBlock(world, 8, 4, 7, stairBlock, 7);
			this.addBlock(world, 8, 4, 11, mainBlock, mainMeta);
			this.addBlock(world, 8, 5, 1, mainBlock, chiselMeta);
			this.addBlock(world, 8, 5, 11, mainBlock, chiselMeta);
			this.addBlock(world, 8, 6, 1, mainBlock, mainMeta);
			this.addBlock(world, 8, 6, 11, mainBlock, mainMeta);
			this.addBlock(world, 8, 7, 1, stairBlock, 2);
			this.addBlock(world, 8, 7, 2, mainBlock, mainMeta);
			this.addBlock(world, 8, 7, 3, lightBlock, 0);
			this.addBlock(world, 8, 7, 4, mainBlock, mainMeta);
			this.addBlock(world, 8, 7, 8, mainBlock, mainMeta);
			this.addBlock(world, 8, 7, 9, lightBlock, 0);
			this.addBlock(world, 8, 7, 10, mainBlock, mainMeta);
			this.addBlock(world, 8, 7, 11, stairBlock, 3);
			this.addBlock(world, 8, 8, 2, slabBlock, slabMeta);
			this.addBlock(world, 8, 8, 4, slabBlock, slabMeta);
			this.addBlock(world, 8, 8, 8, slabBlock, slabMeta);
			this.addBlock(world, 8, 8, 10, slabBlock, slabMeta);
			this.addBlock(world, 8, 11, 3, stairBlock, 5);
			this.addBlock(world, 8, 11, 5, stairBlock, 4);
			this.addBlock(world, 8, 11, 7, stairBlock, 4);
			this.addBlock(world, 8, 11, 9, stairBlock, 5);
			this.addBlock(world, 8, 12, 3, slabBlock, slabMeta);
			this.addBlock(world, 8, 12, 4, lightBlock, 0);
			this.addBlock(world, 8, 12, 5, mainBlock, mainMeta);
			this.addBlock(world, 8, 12, 6, stairBlock, 4);
			this.addBlock(world, 8, 12, 7, mainBlock, mainMeta);
			this.addBlock(world, 8, 12, 8, lightBlock, 0);
			this.addBlock(world, 8, 12, 9, slabBlock, slabMeta);
			this.addBlock(world, 8, 13, 4, slabBlock, slabMeta);
			this.addBlock(world, 8, 13, 5, mainBlock, mainMeta);
			this.addBlock(world, 8, 13, 6, mainBlock, mainMeta);
			this.addBlock(world, 8, 13, 7, mainBlock, mainMeta);
			this.addBlock(world, 8, 13, 8, slabBlock, slabMeta);
			this.addBlock(world, 8, 14, 5, stairBlock, 1);
			this.addBlock(world, 8, 14, 6, mainBlock, mainMeta);
			this.addBlock(world, 8, 14, 7, stairBlock, 1);
			this.addBlock(world, 9, 0, 3, mainBlock, mainMeta);
			this.addBlock(world, 9, 0, 4, mainBlock, mainMeta);
			this.addBlock(world, 9, 0, 5, mainBlock, mainMeta);
			this.addBlock(world, 9, 0, 6, mainBlock, mainMeta);
			this.addBlock(world, 9, 0, 7, mainBlock, mainMeta);
			this.addBlock(world, 9, 0, 8, mainBlock, mainMeta);
			this.addBlock(world, 9, 0, 9, mainBlock, mainMeta);
			this.addBlock(world, 9, 1, 1, mainBlock, chiselMeta);
			this.addBlock(world, 9, 1, 2, mainBlock, mainMeta);
			this.addBlock(world, 9, 1, 10, mainBlock, mainMeta);
			this.addBlock(world, 9, 1, 11, mainBlock, chiselMeta);
			this.addBlock(world, 9, 2, 1, mainBlock, chiselMeta);
			this.addBlock(world, 9, 2, 2, glassBlock, glassMeta);
			this.addBlock(world, 9, 2, 10, glassBlock, glassMeta);
			this.addBlock(world, 9, 2, 11, mainBlock, chiselMeta);
			this.addBlock(world, 9, 3, 1, stairBlock, 1);
			this.addBlock(world, 9, 3, 2, glassBlock, glassMeta);
			this.addBlock(world, 9, 3, 10, glassBlock, glassMeta);
			this.addBlock(world, 9, 3, 11, stairBlock, 1);
			this.addBlock(world, 9, 4, 2, glassBlock, glassMeta);
			this.addBlock(world, 9, 4, 6, stairBlock, 5);
			this.addBlock(world, 9, 4, 10, glassBlock, glassMeta);
			this.addBlock(world, 9, 5, 2, glassBlock, glassMeta);
			this.addBlock(world, 9, 5, 6, stairBlock, 0);
			this.addBlock(world, 9, 5, 10, glassBlock, glassMeta);
			this.addBlock(world, 9, 6, 2, mainBlock, mainMeta);
			this.addBlock(world, 9, 6, 3, slabBlock, 8 + slabMeta);
			this.addBlock(world, 9, 6, 10, mainBlock, mainMeta);
			this.addBlock(world, 9, 7, 2, stairBlock, 2);
			this.addBlock(world, 9, 7, 3, mainBlock, mainMeta);
			this.addBlock(world, 9, 7, 4, lightBlock, 0);
			this.addBlock(world, 9, 7, 5, mainBlock, mainMeta);
			this.addBlock(world, 9, 7, 6, mainBlock, mainMeta);
			this.addBlock(world, 9, 7, 7, mainBlock, mainMeta);
			this.addBlock(world, 9, 7, 8, lightBlock, 0);
			this.addBlock(world, 9, 7, 9, mainBlock, mainMeta);
			this.addBlock(world, 9, 7, 10, stairBlock, 3);
			this.addBlock(world, 9, 8, 3, slabBlock, slabMeta);
			this.addBlock(world, 9, 8, 5, mainBlock, mainMeta);
			this.addBlock(world, 9, 8, 6, waterBlock, 0);
			this.addBlock(world, 9, 8, 7, mainBlock, mainMeta);
			this.addBlock(world, 9, 8, 9, slabBlock, slabMeta);
			this.addBlock(world, 9, 9, 5, mainBlock, chiselMeta);
			this.addBlock(world, 9, 9, 7, mainBlock, chiselMeta);
			this.addBlock(world, 9, 10, 5, mainBlock, chiselMeta);
			this.addBlock(world, 9, 10, 7, mainBlock, chiselMeta);
			this.addBlock(world, 9, 11, 4, stairBlock, 6);
			this.addBlock(world, 9, 11, 5, mainBlock, mainMeta);
			this.addBlock(world, 9, 11, 7, mainBlock, mainMeta);
			this.addBlock(world, 9, 11, 8, stairBlock, 7);
			this.addBlock(world, 9, 12, 4, slabBlock, slabMeta);
			this.addBlock(world, 9, 12, 5, lightBlock, 0);
			this.addBlock(world, 9, 12, 6, lightBlock, 0);
			this.addBlock(world, 9, 12, 7, lightBlock, 0);
			this.addBlock(world, 9, 12, 8, slabBlock, slabMeta);
			this.addBlock(world, 9, 13, 5, stairBlock, 1);
			this.addBlock(world, 9, 13, 6, lapisBlock, 0);
			this.addBlock(world, 9, 13, 7, stairBlock, 1);
			this.addBlock(world, 9, 14, 6, stairBlock, 1);
			this.addBlock(world, 10, 0, 4, mainBlock, mainMeta);
			this.addBlock(world, 10, 0, 5, mainBlock, mainMeta);
			this.addBlock(world, 10, 0, 6, mainBlock, mainMeta);
			this.addBlock(world, 10, 0, 7, mainBlock, mainMeta);
			this.addBlock(world, 10, 0, 8, mainBlock, mainMeta);
			this.addBlock(world, 10, 1, 2, lapisBlock, 0);
			this.addBlock(world, 10, 1, 3, mainBlock, mainMeta);
			this.addBlock(world, 10, 1, 9, mainBlock, mainMeta);
			this.addBlock(world, 10, 1, 10, lapisBlock, 0);
			this.addBlock(world, 10, 2, 2, wallBlock, 0);
			this.addBlock(world, 10, 2, 3, glassBlock, glassMeta);
			this.addBlock(world, 10, 2, 9, glassBlock, glassMeta);
			this.addBlock(world, 10, 2, 10, wallBlock, 0);
			this.addBlock(world, 10, 3, 2, wallBlock, 0);
			this.addBlock(world, 10, 3, 3, glassBlock, glassMeta);
			this.addBlock(world, 10, 3, 9, glassBlock, glassMeta);
			this.addBlock(world, 10, 3, 10, wallBlock, 0);
			this.addBlock(world, 10, 4, 2, wallBlock, 0);
			this.addBlock(world, 10, 4, 3, glassBlock, glassMeta);
			this.addBlock(world, 10, 4, 9, glassBlock, glassMeta);
			this.addBlock(world, 10, 4, 10, wallBlock, 0);
			this.addBlock(world, 10, 5, 2, wallBlock, 0);
			this.addBlock(world, 10, 5, 3, glassBlock, glassMeta);
			this.addBlock(world, 10, 5, 6, stairBlock, 5);
			this.addBlock(world, 10, 5, 9, glassBlock, glassMeta);
			this.addBlock(world, 10, 5, 10, wallBlock, 0);
			this.addBlock(world, 10, 6, 2, slabBlock, slabMeta);
			this.addBlock(world, 10, 6, 3, mainBlock, mainMeta);
			this.addBlock(world, 10, 6, 6, stairBlock, 0);
			this.addBlock(world, 10, 6, 9, mainBlock, mainMeta);
			this.addBlock(world, 10, 6, 10, slabBlock, slabMeta);
			this.addBlock(world, 10, 7, 3, stairBlock, 1);
			this.addBlock(world, 10, 7, 4, mainBlock, mainMeta);
			this.addBlock(world, 10, 7, 5, lightBlock, 0);
			this.addBlock(world, 10, 7, 6, lightBlock, 0);
			this.addBlock(world, 10, 7, 7, lightBlock, 0);
			this.addBlock(world, 10, 7, 8, mainBlock, mainMeta);
			this.addBlock(world, 10, 7, 9, stairBlock, 1);
			this.addBlock(world, 10, 8, 4, slabBlock, slabMeta);
			this.addBlock(world, 10, 8, 6, stairBlock, 1);
			this.addBlock(world, 10, 8, 8, slabBlock, slabMeta);
			this.addBlock(world, 10, 11, 5, stairBlock, 5);
			this.addBlock(world, 10, 11, 7, stairBlock, 5);
			this.addBlock(world, 10, 12, 5, slabBlock, slabMeta);
			this.addBlock(world, 10, 12, 6, slabBlock, slabMeta);
			this.addBlock(world, 10, 12, 7, slabBlock, slabMeta);
			this.addBlock(world, 11, 0, 6, mainBlock, mainMeta);
			this.addBlock(world, 11, 1, 3, mainBlock, chiselMeta);
			this.addBlock(world, 11, 1, 4, mainBlock, mainMeta);
			this.addBlock(world, 11, 1, 5, mainBlock, mainMeta);
			this.addBlock(world, 11, 1, 7, mainBlock, mainMeta);
			this.addBlock(world, 11, 1, 8, mainBlock, mainMeta);
			this.addBlock(world, 11, 1, 9, mainBlock, chiselMeta);
			this.addBlock(world, 11, 2, 3, mainBlock, chiselMeta);
			this.addBlock(world, 11, 2, 4, mainBlock, mainMeta);
			this.addBlock(world, 11, 2, 5, mainBlock, chiselMeta);
			this.addBlock(world, 11, 2, 7, mainBlock, chiselMeta);
			this.addBlock(world, 11, 2, 8, mainBlock, mainMeta);
			this.addBlock(world, 11, 2, 9, mainBlock, chiselMeta);
			this.addBlock(world, 11, 3, 3, stairBlock, 2);
			this.addBlock(world, 11, 3, 4, mainBlock, mainMeta);
			this.addBlock(world, 11, 3, 5, stairBlock, 3);
			this.addBlock(world, 11, 3, 7, stairBlock, 2);
			this.addBlock(world, 11, 3, 8, mainBlock, mainMeta);
			this.addBlock(world, 11, 3, 9, stairBlock, 3);
			this.addBlock(world, 11, 4, 4, mainBlock, mainMeta);
			this.addBlock(world, 11, 4, 8, mainBlock, mainMeta);
			this.addBlock(world, 11, 5, 4, mainBlock, chiselMeta);
			this.addBlock(world, 11, 5, 5, stairBlock, 7);
			this.addBlock(world, 11, 5, 7, stairBlock, 6);
			this.addBlock(world, 11, 5, 8, mainBlock, chiselMeta);
			this.addBlock(world, 11, 6, 4, mainBlock, mainMeta);
			this.addBlock(world, 11, 6, 5, mainBlock, mainMeta);
			this.addBlock(world, 11, 6, 6, stairBlock, 5);
			this.addBlock(world, 11, 6, 7, mainBlock, mainMeta);
			this.addBlock(world, 11, 6, 8, mainBlock, mainMeta);
			this.addBlock(world, 11, 7, 4, stairBlock, 1);
			this.addBlock(world, 11, 7, 5, mainBlock, mainMeta);
			this.addBlock(world, 11, 7, 6, lapisBlock, 0);
			this.addBlock(world, 11, 7, 7, mainBlock, mainMeta);
			this.addBlock(world, 11, 7, 8, stairBlock, 1);
			this.addBlock(world, 11, 8, 5, slabBlock, slabMeta);
			this.addBlock(world, 11, 8, 6, slabBlock, slabMeta);
			this.addBlock(world, 11, 8, 7, slabBlock, slabMeta);
			this.addBlock(world, 12, 1, 4, mainBlock, chiselMeta);
			this.addBlock(world, 12, 1, 8, mainBlock, chiselMeta);
			this.addBlock(world, 12, 2, 4, mainBlock, chiselMeta);
			this.addBlock(world, 12, 2, 8, mainBlock, chiselMeta);
			this.addBlock(world, 12, 3, 4, stairBlock, 1);
			this.addBlock(world, 12, 3, 8, stairBlock, 1);
			
			// place a spawner
			Class e = EntityGuardian.class;
			this.addSpawner(world, 6, -1, 6, e);

			// place the loot chest
			if(!lootChests.getChestPlaced(0)) lootChests.setChestPlaced(0, this.generateStructureChestContents(world, box, rand, 6, 0, 6, 4));
			
			return true;
		}
	}
}