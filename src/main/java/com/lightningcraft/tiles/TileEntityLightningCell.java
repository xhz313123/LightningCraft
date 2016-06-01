package com.lightningcraft.tiles;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;

import com.lightningcraft.achievements.LCAchievements;
import com.lightningcraft.blocks.BlockAirTerminal;
import com.lightningcraft.entities.EntityLCLightningBolt;
import com.lightningcraft.ref.Metal.Rod;
import com.lightningcraft.util.Effect;
import com.lightningcraft.util.WeatherUtils;
import com.lightningcraft.util.WorldUtils;

/** The power cell tile entity */
public class TileEntityLightningCell extends TileEntityBase {
	
	// public fields
	public double storedPower;
	public double maxPower;
	public double efficiency;
	public int cooldownTime;
	public boolean isUpgraded;
	public String cellName;
	
	// private fields
	private boolean topTierTerminal = false;
	private boolean didCheck = false;
	
	/** The power cell tile entity */
	public TileEntityLightningCell(double mp, String name) {
		this.maxPower = mp;
		this.cellName = name;
	}
	
	public TileEntityLightningCell() {}
	
	@Override
	public void update() {
		// variables
		EntityLCLightningBolt lightning;
		boolean dosave = false;
		
		// clientside first
		if (worldObj.isRemote) {
			isAirTerminalPresent(); // update efficiency info
			return;
		}
		
		// now serverside
		
		// normalize stored power
		if(this.storedPower > this.maxPower) this.storedPower = this.maxPower;
		if(this.storedPower < 0) this.storedPower = 0;
		
		// top tier achievement
		if(this.didCheck == false && this.maxPower == 30000 && this.topTierTerminal) {
			EntityPlayerMP player = (EntityPlayerMP)WorldUtils.getClosestPlayer(worldObj, getX(), getY(), getZ(), 16);
			if(player != null && player.getStatFile().canUnlockAchievement(LCAchievements.perfectCell)) {
				player.addStat(LCAchievements.perfectCell, 1);
				this.didCheck = true;
			}
		}
		
		// cooldown time
		if(this.cooldownTime > 0) { 
			this.cooldownTime--;
			dosave = true;
		}
		
		// make sure there's an air terminal above the cell
		if(isAirTerminalPresent()) {
			
			// random chance of lightning strike (1/100000 of a chance per tick)
			// goes up to 1/1000 when it's storming
			double chance;
			chance = 1D/100000D;
			if(this.worldObj.isThundering()) chance = 1D/1000D;
			if(random.nextDouble() <= chance && this.storedPower < this.maxPower - 100D * this.efficiency) {
				Effect.lightningGen(this.worldObj, pos.up());
			}
			
			// get lightning strikes near it (internal / external source)
			// near = within 5 block radius XZ / 3 block radius Y
			AxisAlignedBB box = new AxisAlignedBB(this.getX() - 5, this.getY() - 2, this.getZ() - 5, this.getX() + 5, this.getY() + 4, this.getZ() + 5);
			List<EntityLightningBolt> bolts = WeatherUtils.getLightningBoltsWithinAABB(this.worldObj, box);
			
			// lightning has struck the air terminal, now charge the lightning cell!
			// then comes the 2 second cooldown
			// don't process if the cell is full!
			if(!bolts.isEmpty() && this.cooldownTime <= 0 && this.storedPower < this.maxPower - 100D * this.efficiency) {
				
				// remove the entities from the world (don't charge multiple cells!!!)
				for(EntityLightningBolt bolt : bolts) {
					if(bolt.isDead) return; // stop executing the function now!
					this.worldObj.removeEntity(bolt);
				}
				
				this.storedPower += 100D * this.efficiency;
				this.cooldownTime = 40;
				dosave = true;
			}
		}
		
		// force a save if the state changes
		if(dosave) this.markDirty();
		
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound par1) {
		super.writeToNBT(par1);
		par1.setDouble("storedPower", this.storedPower);
		par1.setDouble("maxPower", this.maxPower);
		par1.setInteger("cooldownTime", this.cooldownTime);
		par1.setBoolean("isUpgraded", this.isUpgraded);
		par1.setBoolean("topTierCheck", this.didCheck);
		par1.setString("customName", this.cellName);
		return par1;
	}

	@Override
	public void readFromNBT(NBTTagCompound par1) {
		super.readFromNBT(par1);
		this.storedPower = par1.getDouble("storedPower");
		this.maxPower = par1.getDouble("maxPower");
		this.cooldownTime = par1.getInteger("cooldownTime");
		this.isUpgraded = par1.getBoolean("isUpgraded");
		this.didCheck = par1.getBoolean("topTierCheck");
		this.cellName = par1.getString("customName");
	}
	
	/** checks to see if there is an air terminal on top (also sets the efficiency) */
	public boolean isAirTerminalPresent() {
		// get the block above
		this.topTierTerminal = false;
		IBlockState state = this.worldObj.getBlockState(getPos().add(0, 1, 0));
		Block test = state.getBlock();
		int meta = test.getMetaFromState(state);
		boolean flag;
		
		if(test instanceof BlockAirTerminal) {
			flag = true;
			this.efficiency = ((BlockAirTerminal)test).getEfficiency(state);
			if(meta == Rod.MYSTIC) this.topTierTerminal = true;
		} else {
			flag = false;
			this.efficiency = 0;
		}
		return flag;
	}
	
}
