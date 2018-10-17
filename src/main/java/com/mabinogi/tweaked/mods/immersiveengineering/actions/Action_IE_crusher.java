package com.mabinogi.tweaked.mods.immersiveengineering.actions;

import java.util.ArrayList;
import java.util.List;

import com.mabinogi.tweaked.actions.iface.IAction;
import com.mabinogi.tweaked.annotations.TweakedAction;
import com.mabinogi.tweaked.script.holders.ActionHolder;
import com.mabinogi.tweaked.script.loaders.ActionLoader;
import com.mabinogi.tweaked.script.objects.ObjDict;
import com.mabinogi.tweaked.script.objects.ObjStack;
import com.mabinogi.tweaked.script.objects.ObjStackList;
import com.mabinogi.tweaked.script.objects.ObjAll;

import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;
import net.minecraft.item.ItemStack;

@TweakedAction(value="ie.crusher", modid="immersiveengineering")
public class Action_IE_crusher implements IAction 
{	
	public static final String METHOD_ADD = "add";
	public static final String METHOD_REMOVE = "remove";
	
	public static List<ActionHolder> ACTIONS_ADD = new ArrayList<>();
	public static List<ActionHolder> ACTIONS_REMOVE = new ArrayList<>();
	
	@Override
	public boolean store(String methodName, ActionHolder action)
	{
		switch (methodName)
		{
			case METHOD_ADD:
			{
				ACTIONS_ADD.add(action);
				return true;
			}
			case METHOD_REMOVE:
			{
				ACTIONS_REMOVE.add(action);
				return true;
			}
			default:
				return false;
		}
	}
	
	//**************************************************************************************//
	//										ADD												//
	//**************************************************************************************//
	
	public static List<CrusherRecipe> RECIPES_ADD = new ArrayList<>();
	
	public static void applyAdd()
	{
		//apply scripts
		for (ActionHolder script : ACTIONS_ADD)
		{
			ActionLoader.applyAction(METHOD_ADD, script);
		}
		
		//add recipes
		for (CrusherRecipe recipe : RECIPES_ADD)
		{
			CrusherRecipe.recipeList.add(recipe);
		}
		
		//clean up
		ACTIONS_ADD = null;
		RECIPES_ADD = null;
	}
	
	public void add(ObjStack output, ObjStack input, Integer energy)
	{
		RECIPES_ADD.add(new CrusherRecipe(output.getItemStack(), input.getItem(), energy));
	}
	
	public void add(ObjStack output, ObjDict input, Integer energy)
	{
		RECIPES_ADD.add(new CrusherRecipe(output.getItemStack(), input.getItem(), energy));
	}
	
	//**************************************************************************************//
	//										REMOVE											//
	//**************************************************************************************//
	
	public static Boolean FLAG_CLEAR = false;
	public static List<ItemStack> RECIPES_REMOVE = new ArrayList<>();
	
	public static void applyRemove()
	{
		//apply scripts
		for (ActionHolder script : ACTIONS_REMOVE)
		{
			ActionLoader.applyAction(METHOD_REMOVE, script);
		}
		
		//clear recipes
		if (FLAG_CLEAR)
		{
			CrusherRecipe.recipeList.clear();
		}
		else
		{
			//remove recipes
			for (ItemStack stack : RECIPES_REMOVE)
			{
				CrusherRecipe.removeRecipesForOutput(stack);
			}
		}
		
		//clean up
		ACTIONS_REMOVE = null;
		RECIPES_REMOVE = null;
		FLAG_CLEAR = null;
	}
	
	public void remove(ObjStack stack)
	{
		RECIPES_REMOVE.add(stack.getItemStack());
	}
	
	public void remove(ObjStackList stackList)
	{
		for (ObjStack stack : stackList.list)
		{
			remove(stack);
		}
	}
	
	public void remove(ObjAll all)
	{
		FLAG_CLEAR = true;
	}

}
