package com.mabinogi.tweaked.mods.vanilla.actions;

import com.mabinogi.tweaked.Tweaked;
import com.mabinogi.tweaked.api.actions.ActionAbstract;
import com.mabinogi.tweaked.api.annotations.TweakedAction;
import com.mabinogi.tweaked.script.objects.ObjStack;
import net.minecraft.util.text.translation.LanguageMap;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import static com.mabinogi.tweaked.Tweaked.LOG;


public class Action_Vanilla_Lang
{	
	public static Action_Lang_Set SET = null;
	
	
	//**************************************************************************************//
	//										setName											//
	//**************************************************************************************//

	@TweakedAction("lang.set")
	public static class Action_Lang_Set extends ActionAbstract
	{
		private Map<String, String> NAMES = new HashMap<>();
		
		public Action_Lang_Set()
		{
			SET = this;
		}

		public void build(String string, String translation)
		{
			NAMES.put(string, translation);
		}
		
		public void build(ObjStack stack, String translation)
		{
			NAMES.put(stack.getItemStack().getUnlocalizedName() + ".name", translation);
		}
		
		@Override
		protected void run()
		{
			if (!NAMES.isEmpty())
			{
				for (Entry<String, String> translation : NAMES.entrySet())
				{
					//inject into language map
					try
					{
						LanguageMap.inject(new ByteArrayInputStream((translation.getKey() + "=" + translation.getValue()).getBytes("utf-8")));
						
						//debug
						LOG.debug("lang.setname : " + translation.getKey() + " to " + translation.getValue());
					} 
					catch (UnsupportedEncodingException e)
					{
						Tweaked.LOG.warn("Warning : Error translating \"" + translation.getKey() + "\"");
					}
				}
			}
			
			//cleanup
			NAMES = null;
		}
	}

}
