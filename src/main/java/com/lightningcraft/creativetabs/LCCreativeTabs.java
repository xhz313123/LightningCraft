package com.lightningcraft.creativetabs;

import net.minecraft.init.Items;

import com.lightningcraft.blocks.LCBlocks;
import com.lightningcraft.items.LCItems;

public class LCCreativeTabs {
	
	public static LCCreativeTab blocks;
	public static LCCreativeTab items;
	
	public static void mainRegistry() {
		registerCreativeTabs();
	}
	
	private static void registerCreativeTabs() {
		blocks = new LCCreativeTab("lcBlocks");
		items = new LCCreativeTab("lcItems");
	}
	
	public static void updateCreativeTabs() {
		blocks.updateItem(LCBlocks.metalBlock);
		items.updateItem(LCItems.ingot);
	}

}
