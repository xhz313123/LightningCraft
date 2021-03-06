package sblectric.lightningcraft.gui.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import sblectric.lightningcraft.gui.ShortSender;
import sblectric.lightningcraft.tiles.TileEntityRF;

/** RF provider container */
public class ContainerRFProvider extends ContainerLightningUser {
	
	private TileEntityRF tileRF;
	private Short low = null;
	private Short high = null;
	
	public ContainerRFProvider(InventoryPlayer player, TileEntityRF tile) {
		super(player, tile);
		this.tileRF = tile;
		
		// player inventory placement
		int i;
		
		for(i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(player, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		
		for(i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(player, i, 8 + i * 18, 142));
		}
	}
	
	/** Send out the two shorts */
	@Override
	public void sendInfo(IContainerListener craft) {
		int storedRF = this.tileRF.getEnergyStored(null);
		craft.sendProgressBarUpdate(this, 0, ShortSender.getLowShort(storedRF));
		craft.sendProgressBarUpdate(this, 1, ShortSender.getHighShort(storedRF));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getInfo(short par1, short par2) {
		if(par1 == 0) low = par2;
		if(par1 == 1) high = par2;
		if(low != null && high != null) {
			int storedRF = ShortSender.getInt(low, high);
			this.tileRF.setEnergyStored(storedRF);
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.tileRF.isUseableByPlayer(player);
	}
	
	/**
	* Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
	*/
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
	{
		ItemStack itemstack = null;
		Slot slot = this.inventorySlots.get(par2);

		int INPUT = -1;

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			// itemstack is in player inventory, try to place in slot
			if (par2 != INPUT)
			{
				// item in player's inventory, but not in action bar
				if (par2 >= INPUT+1 && par2 < INPUT+28)
				{
					// place in action bar
					if (!this.mergeItemStack(itemstack1, INPUT+28, INPUT+37, false))
					{
						return null;
					}
				}
				// item in action bar - place in player inventory
				else if (par2 >= INPUT+28 && par2 < INPUT+37 && !this.mergeItemStack(itemstack1, INPUT+1, INPUT+28, false))
				{
					return null;
				}
			}

			if (itemstack1.stackSize == 0)
			{
				slot.putStack((ItemStack)null);
			}
			else
			{
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize)
			{
				return null;
			}

			slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
		}
		return itemstack;
	}

}
