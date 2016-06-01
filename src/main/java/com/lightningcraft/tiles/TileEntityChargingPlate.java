package com.lightningcraft.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.lightningcraft.items.ItemBattery;
import com.lightningcraft.items.LCItems;
import com.lightningcraft.util.IntList;
import com.lightningcraft.util.LCMisc;
import com.lightningcraft.util.WorldUtils;

/** The charging plate tile entity */
public class TileEntityChargingPlate extends TileEntityLightningUser {
	
	public static final double chargeRate = 0.25F;
	
	/** The main operational loop */
	@Override
	public void update() {
		if(!this.worldObj.isRemote && this.hasLPCell()) {
			if(this.getBlockMetadata() != 0) {
				
				// check for a user
				EntityPlayer user = WorldUtils.getClosestPlayer(worldObj, getX(), getY(), getZ(), 1.5);
				if(user != null && user.inventory.hasItemStack(new ItemStack(LCItems.battery)) && this.tileCell.storedPower > 0) {
					int invPos = 0;
					IntList avoid = new IntList();
					while((invPos = LCMisc.posInInventory(user, LCItems.battery, avoid)) >= 0) {
						// check this inventory position
						if(invPos >= 0) {
							ItemStack battery = user.inventory.mainInventory[invPos];
							
							if(ItemBattery.getStoredPower(battery) < ItemBattery.getMaxPower(battery)) {
								
								// discharge the lightning cell
								if(this.drawCellPower(chargeRate)) {
										
									// charge the battery
									ItemBattery.addStoredPower(battery, chargeRate);
									
									// avoid overcharging
									if(ItemBattery.getStoredPower(battery) > ItemBattery.getMaxPower(battery)) {
										ItemBattery.setStoredPower(battery, ItemBattery.getMaxPower(battery));
									}
								}
								break; // escape the loop after adding power
							} else {
								avoid.add(invPos);
							}
						}
					}
				}
				
			}
		}
	}

}
