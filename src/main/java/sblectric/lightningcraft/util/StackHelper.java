package sblectric.lightningcraft.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import sblectric.lightningcraft.recipes.LightningInfusionRecipes;

/** Helps with ItemStack functionality */
public class StackHelper {

	private static Map<String, Integer> oreMap = new HashMap();
	private static Map<String, Integer> subMap = new HashMap();
	public static Random rand = new Random();

	/** Animate an ItemStack based on makeItemStackFromString (clientside only) */
	@SideOnly(Side.CLIENT)
	public static ItemStack animateItemStackFromString(String stackString, boolean change) {
		int ores;
		if(OreDictionary.doesOreNameExist(stackString)) {
			ores = OreDictionary.getOres(stackString).size();
		} else {
			ores = 1;
		}
		int oreIndex = 0;
		int subIndex = 0;
		ItemStack result;
		
		if(oreMap.containsKey(stackString))	oreIndex = oreMap.get(stackString);
		if(change) {
			if(ores > 0) {
				oreIndex = rand.nextInt(ores);
			} else {
				oreIndex = 0;
			}
			oreMap.put(stackString, oreIndex);
		}

		ItemStack primary = makeItemStackFromString(stackString, oreIndex);
		if(primary == null) return null;

		List<ItemStack> subItems = new JointList();
		primary.getItem().getSubItems(primary.getItem(), null, subItems);

		if(subMap.containsKey(stackString))	subIndex = subMap.get(stackString);
		if(primary.getItemDamage() == OreDictionary.WILDCARD_VALUE && !subItems.isEmpty()) {
			if(change) {
				subIndex = rand.nextInt(subItems.size());
				subMap.put(stackString, subIndex);
			}
			result = subItems.get(subIndex);
		} else {
			result = primary;
		}
		return result;
	}

	/** Creates a new ItemStack from the string acquired from makeStringFromItemStack or an oredict name, with an oredict index option */
	public static ItemStack makeItemStackFromString(String stackString, int oreIndex) {
		if(stackString == LightningInfusionRecipes.nullIdentifier) return null;
		if(stackString.contains("@")) { // it's a stack
			try {
				String[] a = stackString.split("#");
				int stackSize = Integer.parseInt(a[0]);
				String[] b = a[1].split("@");
				String name = b[0]; // get the registry name
				int meta = Integer.parseInt(b[1]);
				ItemStack output = GameRegistry.makeItemStack(name, meta, stackSize, null);
				return output;
			} catch(Exception e) {
				return null; // parsing error
			}
		} else { // it's an oredict value (maybe)
			List<ItemStack> list;
			if(OreDictionary.doesOreNameExist(stackString) && oreIndex < (list = OreDictionary.getOres(stackString)).size()) {
				return list.get(oreIndex); // yep
			} else {
				return null; // guess not
			}
		}
	}
	
	/** Creates a new ItemStack from the string acquired from makeStringFromItemStack or an oredict name */
	public static ItemStack makeItemStackFromString(String stackString) {
		return makeItemStackFromString(stackString, 0);
	}
	
	/** Get metadata from a string version of an ItemStack */
	public static int getMetaFromString(String stackString) {
		if(stackString.contains("@")) {
			String[] a = stackString.split("@");
			return Integer.parseInt(a[1]);
		} else {
			return -1; // oredict flag
		}
	}
	
	/** Strip a stack of its metadata information */
	public static String stripMetaFromString(String stackString) {
		if(stackString.contains("@")) {
			String[] a = stackString.split("@");
			return a[0];
		} else {
			return stackString;
		}
	}
	
	/** Change a string's metadata information */
	public static String changeStringMeta(String stackString, int newMeta) {
		if(getMetaFromString(stackString) > -1) {
			return stripMetaFromString(stackString) + "@" + newMeta;
		} else {
			return stackString;
		}
	}

	/** Turn an ItemStack or oredict name into a String */
	public static String makeStringFromItemStack(Object stack) {
		if(stack instanceof ItemStack) {
			ItemStack s = (ItemStack)stack;
			return s.stackSize + "#" + s.getItem().getRegistryName() + "@" + s.getItemDamage();
		} else if(stack instanceof String) {
			return (String)stack;
		} else {
			throw new IllegalArgumentException("Parameter must be a String or an ItemStack, was given " + stack.getClass());
		}
	}

	/** An unlimited type of areItemStacksEqual for crafting recipes (non-amount sensitive, cannot be null) */
	public static boolean areItemStacksEqualForCrafting(ItemStack... stacks) {
		ItemStack comp = stacks[0];
		if(comp == null) return false;
		ItemStack comp1 = comp.copy(); comp1.stackSize = 1;
		for(int n = 1; n < stacks.length; n++) {
			if(stacks[n] == null) return false;
			ItemStack comp2 = stacks[n].copy(); comp2.stackSize = 1;
			if(!ItemStack.areItemStacksEqual(comp1, comp2)) { // try regular and oredict
				if(comp1.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
					comp2.setItemDamage(OreDictionary.WILDCARD_VALUE);
					if(!ItemStack.areItemStacksEqual(comp1, comp2)) return false;
				} else {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Sets the color for the specified armor ItemStack.
	 */
	public static boolean setStackColor(ItemStack stack, Color color) {
		if(!(stack.getItem() instanceof ItemArmor))
			return false;
		Item armor = stack.getItem();

		if (!(armor == Items.LEATHER_BOOTS || armor == Items.LEATHER_LEGGINGS || armor == Items.LEATHER_CHESTPLATE || armor == Items.LEATHER_HELMET))
		{
			return false;
		}
		else
		{
			NBTTagCompound nbttagcompound = stack.getTagCompound();
			if(nbttagcompound == null) nbttagcompound = new NBTTagCompound();
			NBTTagCompound disp = new NBTTagCompound();
			disp.setInteger("color", color.getRGB());
			nbttagcompound.setTag("display", disp);
			stack.setTagCompound(nbttagcompound);
			return true;
		}
	}

	/** Gets the enchantments that exist on a specified item stack */
	public static List<NBTTagCompound> getEnchantments(ItemStack s) {
		List enchList = new ArrayList<NBTTagCompound>();

		if(s != null && s.hasTagCompound()) {
			NBTTagList enchants;
			String ench = s.getItem() == Items.ENCHANTED_BOOK ? "StoredEnchantments" : "ench";
			enchants = (NBTTagList) s.getTagCompound().getTag(ench);

			if(enchants != null && enchants.tagCount() > 0) {
				for(int i = 0; i < enchants.tagCount(); i++) {
					NBTTagCompound enchant = enchants.getCompoundTagAt(i);
					enchList.add(enchant);
				}
			}
		}

		return enchList;
	}

	/** Adds a list of enchantments to a specified item stack */
	public static boolean addEnchantments(ItemStack s, List<NBTTagCompound> enchList) {
		if(s != null) {
			String ench = s.getItem() == Items.ENCHANTED_BOOK ? "StoredEnchantments" : "ench";
			if(!s.hasTagCompound()) s.setTagCompound(new NBTTagCompound());
			NBTTagList currentEnchants = (NBTTagList) s.getTagCompound().getTag(ench);

			if(currentEnchants == null) {
				currentEnchants = new NBTTagList();
			}

			for(int i = 0; i < enchList.size(); i++) {
				currentEnchants.appendTag(enchList.get(i));
			}

			s.getTagCompound().setTag(ench, currentEnchants);
			return true;
		}
		return false;
	}

}