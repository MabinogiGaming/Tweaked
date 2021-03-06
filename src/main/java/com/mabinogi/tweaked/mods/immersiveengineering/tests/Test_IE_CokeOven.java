package com.mabinogi.tweaked.mods.immersiveengineering.tests;

import blusunrize.immersiveengineering.api.crafting.CokeOvenRecipe;
import com.mabinogi.tweaked.api.annotations.TweakedTest;
import com.mabinogi.tweaked.api.test.ITweakedTest;
import net.minecraft.init.Items;
import net.minecraft.world.World;

@SuppressWarnings("unused")
public class Test_IE_CokeOven
{
	//**************************************************************************************//
	//										add												//
	//**************************************************************************************//

	@TweakedTest(modid="immersiveengineering")
	public static class Test_IE_CokeOven_Add implements ITweakedTest
	{
		@Override
		public String getFilename()
		{
			return "ie";
		}

		@Override
		public String getTestDescription()
		{
			return "ie (coke oven) - add";
		}

		@Override
		public String[] getVariables()
		{
			return new String[0];
		}

		@Override
		public String[] getActions()
		{
			return new String[] { "tweak.ie.cokeoven.add(<minecraft:emerald>, <minecraft:coal>, 1000, 500);" };
		}

		@Override
		public boolean runTest(World world)
		{
			for (CokeOvenRecipe recipe : CokeOvenRecipe.recipeList)
			{
				if (recipe.output.getItem() == Items.EMERALD)
				{
					return true;
				}
			}
			return false;
		}
	}


	//**************************************************************************************//
	//										remove											//
	//**************************************************************************************//

	@TweakedTest(modid="immersiveengineering")
	public static class Test_IE_CokeOven_Remove implements ITweakedTest
	{
		@Override
		public String getFilename()
		{
			return "ie";
		}

		@Override
		public String getTestDescription()
		{
			return "ie (coke oven) - remove";
		}

		@Override
		public String[] getVariables()
		{
			return new String[0];
		}

		@Override
		public String[] getActions()
		{
			return new String[] { "tweak.ie.cokeoven.remove(*);" };
		}

		@Override
		public boolean runTest(World world)
		{
			return CokeOvenRecipe.recipeList.size() <= 1;
		}
	}
}
