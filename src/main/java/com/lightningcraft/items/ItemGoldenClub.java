package com.lightningcraft.items;

import net.minecraft.item.ItemStack;

import com.lightningcraft.ref.Metal.Rod;
import com.lightningcraft.util.StackHelper;

/** The return of the golden club */
public class ItemGoldenClub extends ItemGolfClub {
	
	public ItemGoldenClub() {
		super();
		this.setMaxDamage(18); // three times the uses
	}
	
	// repair the item with given item
	@Override
	public boolean getIsRepairable(ItemStack a, ItemStack b) {
		ItemStack repairWith = new ItemStack(LCItems.rod, 0, Rod.GOLD);
		if(StackHelper.areItemStacksEqualForCrafting(repairWith, b)){
			return true;
		} else {
			return false;
		}
	}

}
