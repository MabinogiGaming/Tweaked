package com.mabinogi.tweaked.script.variables;

import com.mabinogi.tweaked.api.annotations.TweakedVariable;
import com.mabinogi.tweaked.api.objects.IIngredient;
import com.mabinogi.tweaked.api.variables.IVariable;
import com.mabinogi.tweaked.script.ScriptHelper;
import com.mabinogi.tweaked.script.builders.IngredientBuilder;
import com.mabinogi.tweaked.script.holders.VariableHolder;
import com.mabinogi.tweaked.script.objects.ObjDict;

@TweakedVariable("dict")
public class VarDict implements IVariable 
{
	@Override
	public String parse(VariableHolder var, String start, String in)
	{
		if (!in.startsWith("<"))
		{
			ScriptHelper.reportScriptError(start, "Malformed Ingredient, must start with \"<\"");
			return null;
		}
		
		if (!in.endsWith(">"))
		{
			ScriptHelper.reportScriptError(start, "Malformed Ingredient, must end with \">\"");
			return null;
		}
		
		String arg = in;
		
		//attempt to build component
		IIngredient ingredient = IngredientBuilder.build(arg.substring(arg.indexOf("<") + 1, arg.indexOf(">")));
		if (ingredient == null)
		{
			ScriptHelper.reportScriptError(start, "Ingredient \"" + arg + "\" doesn't exist");
			return null;
		}
		
		if (!(ingredient instanceof ObjDict))
		{
			ScriptHelper.reportScriptError(start, "Ingredient \"" + arg + "\" is not a Dict");
			return null;
		}
		
		//add arg
		var.clazz = ObjDict.class;
		var.value = ingredient;
		
		return "";
	}

}