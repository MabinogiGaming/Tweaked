package com.mabinogi.tweaked.annotations;

import static com.mabinogi.tweaked.Tweaked.LOG;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.mabinogi.tweaked.script.arguments.iface.IArgument;
import com.mabinogi.tweaked.script.variables.iface.IVariable;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;

public class Annotations {
	
	public static final Map<String, Object> ACTIONS = new HashMap<>();
	
	public static final Map<String, IArgument> ARGUMENTS = new HashMap<>();
	
	public static final Map<String, IVariable> VARIABLES = new HashMap<>();
	
	public static void build(ASMDataTable asm)
	{
		try
		{
			buildVariables(asm);
			buildArguments(asm);
			buildActions(asm);
		} 
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException e)
		{
			e.printStackTrace();
		}
	}
	
	private static void buildActions(ASMDataTable asm) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException
	{
		Set<ASMData> asmSet = asm.getAll("com.mabinogi.tweaked.annotations.TweakedAction");
    	for (ASMData data : asmSet)
    	{
    		Object modid = data.getAnnotationInfo().get("modid");
    		if (modid != null && modid instanceof String)
			{
    			if (!Loader.isModLoaded(((String) modid)))
				{
					LOG.debug("Disabled Action : " + (data.getClassName().contains(".") ? data.getClassName().substring(data.getClassName().lastIndexOf(".") + 1) : data.getClassName()) + " as \"" + data.getAnnotationInfo().get("value") + "\"");
					continue;
				}
			}
    		
    		LOG.debug("Registered Action : " + (data.getClassName().contains(".") ? data.getClassName().substring(data.getClassName().lastIndexOf(".") + 1) : data.getClassName()) + " as \"" + data.getAnnotationInfo().get("value") + "\"");
    		
    		//invoke the class
    		Class<?> clazz = Class.forName(data.getClassName());
			Object invokedClazz = clazz.newInstance();
			
			//get the input token
			Object value = data.getAnnotationInfo().get("value");
			
			if (value instanceof String)
			{
				ACTIONS.put((String) data.getAnnotationInfo().get("value"), invokedClazz);
			}
    	}
	}
	
	private static void buildArguments(ASMDataTable asm) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException
	{
		Set<ASMData> asmSet = asm.getAll("com.mabinogi.tweaked.annotations.TweakedArgument");
    	for (ASMData data : asmSet)
    	{
    		//invoke the class
    		Class<?> clazz = Class.forName(data.getClassName());
			Object invokedClazz = clazz.newInstance();
			
			//get the input token
			Object value = data.getAnnotationInfo().get("value");
			
			if (value instanceof ArrayList<?> && invokedClazz instanceof IArgument)
			{
				ArrayList<?> tokenList = (ArrayList<?>) value;
				
				for (Object o : tokenList)
				{
					if (o instanceof String)
					{
						ARGUMENTS.put((String) o, (IArgument) invokedClazz);
						
						LOG.debug("Registered Argument : " + (data.getClassName().contains(".") ? data.getClassName().substring(data.getClassName().lastIndexOf(".") + 1) : data.getClassName()) + " as \"" + o + "\"");
					}
				}
			}
    	}
	}
	
	private static void buildVariables(ASMDataTable asm) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException
	{
		Set<ASMData> asmSet = asm.getAll("com.mabinogi.tweaked.annotations.TweakedVariable");
    	for (ASMData data : asmSet)
    	{
    		LOG.debug("Registered Variable : " + (data.getClassName().contains(".") ? data.getClassName().substring(data.getClassName().lastIndexOf(".") + 1) : data.getClassName()) + " as \"" + data.getAnnotationInfo().get("value") + "\"");
    		
    		//invoke the class
    		Class<?> clazz = Class.forName(data.getClassName());
			Object invokedClazz = clazz.newInstance();
			
			//get the input token
			Object value = data.getAnnotationInfo().get("value");
			
			if (value instanceof String && invokedClazz instanceof IVariable)
			{
				VARIABLES.put((String) data.getAnnotationInfo().get("value"), (IVariable) invokedClazz);
			}
    	}
	}

}
