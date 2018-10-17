package com.mabinogi.tweaked.script.variables;

import java.util.ArrayList;
import java.util.List;

import com.mabinogi.tweaked.annotations.TweakedVariable;
import com.mabinogi.tweaked.script.ScriptHelper;
import com.mabinogi.tweaked.script.builders.IngredientBuilder;
import com.mabinogi.tweaked.script.holders.VariableHolder;
import com.mabinogi.tweaked.script.objects.ObjStack;
import com.mabinogi.tweaked.script.objects.ObjStackList;
import com.mabinogi.tweaked.script.objects.iface.IIngredient;
import com.mabinogi.tweaked.script.variables.iface.IVariable;

@TweakedVariable("StackList")
public class VarStackList implements IVariable
{
	@Override
	public String parse(VariableHolder var, String start, String in)
	{
		//create list
		List<ObjStack> ingredients = new ArrayList<>();
		
		while (in.length() > 0)
    	{
			if (!in.startsWith("<"))
			{
				ScriptHelper.reportScriptError(start, "Malformed Ingredient, must start with \"<\"");
				return null;
			}
			
			if (!in.contains(">"))
			{
				ScriptHelper.reportScriptError(start, "Malformed Ingredient, must end with \">\"");
				return null;
			}
			
			//get arg
			String arg = in.substring(0, in.indexOf(">") + 1);
			
			//attempt to build ingredient
			IIngredient ingredient = IngredientBuilder.build(arg.substring(arg.indexOf("<") + 1, arg.indexOf(">")));
			if (ingredient == null)
			{
				ScriptHelper.reportScriptError(start, "Ingredient \"" + arg + "\" doesn't exist");
				return null;
			}
			
			if (!(ingredient instanceof ObjStack))
			{
				ScriptHelper.reportScriptError(start, "Ingredient \"" + arg + "\" is not a Stack");
				return null;
			}
			
			ingredients.add((ObjStack) ingredient);
			
			//clean line
			in = in.substring(in.indexOf(">") + 1);
			if (in.startsWith(",")) in = in.substring(1);
    	}
		
		//add arg
		var.clazz = ObjStackList.class;
		var.value = new ObjStackList(ingredients);
		
		//list consumes all tokens
		return "";
	}
}