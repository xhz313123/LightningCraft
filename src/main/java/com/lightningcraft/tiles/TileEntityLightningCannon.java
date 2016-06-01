package com.lightningcraft.tiles;

import java.util.UUID;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.lightningcraft.entities.EntityLCElectricAttack;
import com.lightningcraft.util.JointList;
import com.lightningcraft.util.WorldUtils;

/** The Underworld cannon */
public class TileEntityLightningCannon extends TileEntityLightningUser {

	public static final int maxIdleTicks = 400;
	public static final int minIdleTicks = 40;
	public static final double lePerAttack = 20;

	private int ticksUntilNextAttack;
	private int currentTicks;
	private double tileX, tileY, tileZ;
	public EnumMode mode = EnumMode.DEFAULT;

	/** Set up the cannon */
	public TileEntityLightningCannon() {
		this.ticksUntilNextAttack = reload();
		this.currentTicks = 0;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public double getMaxRenderDistanceSquared() {
		return 16384.0D;
	}

	private int reload() {
		return random.nextInt(maxIdleTicks - minIdleTicks) + minIdleTicks;
	}

	/** The main operational loop */
	@Override
	public void update() {
		if(!this.worldObj.isRemote) {

			// keep the coordinates updated
			tileX = getX() + 0.5;
			tileY = getY() + 0.5;
			tileZ = getZ() + 0.5;
			IBlockState state = worldObj.getBlockState(pos);
			int meta = state.getBlock().getMetaFromState(state);

			// set target defaults
			EntityLivingBase target = null;
			boolean doAttack = false;
			boolean hasCell = false;
			boolean drawPower = false;

			// increase counter
			this.currentTicks++;
			
			// check for a cell
			if(meta == 1) hasCell = this.hasLPCell();

			// acquire a target for firing
			if(this.currentTicks > this.ticksUntilNextAttack) {
				doAttack = true;
				if(meta == 0) { // normal Underworld cannon (players)
					target = WorldUtils.getClosestVulnerablePlayer(this.worldObj, tileX, tileY, tileZ, 32.0D, new JointList<UUID>());
				} else {
					if(hasCell && this.canDrawCellPower(lePerAttack)) {
						drawPower = true;
						switch(mode) {
						case MOBS: // all mobs
							target = (EntityLivingBase)WorldUtils.getClosestVulnerableEntityOfType(this.worldObj, tileX, tileY, tileZ, 32.0D, 
									IMob.class);
							break;
						case ALL: // all EntityLivings
							target = (EntityLivingBase)WorldUtils.getClosestVulnerableEntityOfType(this.worldObj, tileX, tileY, tileZ, 32.0D, 
									EntityLiving.class);
							break;
						default: // nothing (default)
						}
					}
				}
			}

			// perform the attack
			if(doAttack) {
				if(target != null && this.canEntityBeSeen(target)) {
					EntityLCElectricAttack bolt = new EntityLCElectricAttack(worldObj, tileX, tileY, tileZ, target);
					this.worldObj.spawnEntityInWorld(bolt);
					this.worldObj.playSound(null, tileX, tileY, tileZ, SoundEvents.ENTITY_FIREWORK_BLAST, SoundCategory.BLOCKS, 3.0F, 0.1F);
					if(drawPower) this.drawCellPower(lePerAttack);
				}

				this.ticksUntilNextAttack = reload();
				this.currentTicks = 0;
				this.markDirty();
			}
		}
	}
	
	/** Rotate the operating mode */
	public void rotateOperatingMode() {
		this.mode = EnumMode.assignMode((mode.id + 1) % EnumMode.values().length);
		this.markDirty();
	}

	/**
	 * returns true if the entity provided in the argument can be seen. (Raytrace)
	 */
	public boolean canEntityBeSeen(Entity e) {
		return this.worldObj.rayTraceBlocks(new Vec3d(tileX + 1, tileY, tileZ + 0), new Vec3d(e.posX, e.posY + e.getEyeHeight(), e.posZ)) == null || 
				this.worldObj.rayTraceBlocks(new Vec3d(tileX + 0, tileY, tileZ + 1), new Vec3d(e.posX, e.posY + e.getEyeHeight(), e.posZ)) == null || 
				this.worldObj.rayTraceBlocks(new Vec3d(tileX - 1, tileY, tileZ + 0), new Vec3d(e.posX, e.posY + e.getEyeHeight(), e.posZ)) == null || 
				this.worldObj.rayTraceBlocks(new Vec3d(tileX + 0, tileY, tileZ - 1), new Vec3d(e.posX, e.posY + e.getEyeHeight(), e.posZ)) == null; 

	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);

		// basic firing
		this.ticksUntilNextAttack = tagCompound.getInteger("ticksUntilNextAttack");
		this.currentTicks = tagCompound.getInteger("currentTicks");
		
		// operating mode
		this.mode = EnumMode.assignMode(tagCompound.getInteger("operatingMode"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);

		// basic firing
		tagCompound.setInteger("ticksUntilNextAttack", ticksUntilNextAttack);
		tagCompound.setInteger("currentTicks", currentTicks);
		
		// operating mode
		tagCompound.setInteger("operatingMode", this.mode.id);
		return tagCompound;
	}
	
	/** Operating mode */
	public static enum EnumMode {
		NONE(0, "Cannon Inactive"),
		MOBS(1, "Hostile Mobs Only"),
		ALL(2, "All Non-Player Entities");

		private static EnumMode DEFAULT = NONE;
		private int id;
		private String name;

		private EnumMode(int id, String name) {
			this.id = id;
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public int getID() {
			return this.id;
		}

		public static EnumMode assignMode(int id) {
			for(EnumMode m : EnumMode.values()) {
				if(m.id == id) return m;
			}
			return DEFAULT;
		}

	}

}
