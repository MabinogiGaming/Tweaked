package com.mabinogi.tweaked.commands;

import static com.mabinogi.tweaked.Tweaked.LOG;
import static com.mabinogi.tweaked.logging.LogHandler.TAB;

import java.util.Collections;
import java.util.List;

import com.mabinogi.tweaked.api.annotations.TweakedCommand;
import com.mabinogi.tweaked.api.commands.ICommand;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

@TweakedCommand("hand")
public class CommandHand implements ICommand
{
	@Override
	public boolean isHidden()
	{
		return false;
	}
	
	@Override
	public void execute(MinecraftServer server, EntityPlayer player, String[] args)
	{
		//get held item
        ItemStack heldItem = player.getHeldItemMainhand();
        
        // Tries to get name of held item first
        if(!heldItem.isEmpty()) 
        {
        	//dump the message
    		LOG.dump("/tweaked hand");
        	
        	//get item registry name
            String output = "<" + heldItem.getItem().getRegistryName();
            
            //add extra arguments if required
        	int meta = heldItem.getMetadata();
        	int count = heldItem.getCount();
        	int nbt = heldItem.getTagCompound() == null ? 0 : heldItem.getTagCompound().getSize();
        	
        	//metadata first
        	if (meta != 0 || count > 1 || nbt > 0)
        	{
        		output += ":" + meta;
        	}
        	
        	//count next
        	if (count > 1 || nbt > 0)
        	{
        		output += ":" + count;
        	}
        	
        	//finally nbt
        	if (nbt > 0)
        	{
        		output += ":" + heldItem.getTagCompound().toString();
        	}
        	
        	//close brackets
        	output += ">";
            
            //create the text component
            TextComponentString txtComponent = new TextComponentString(output);
            
            //add a copy on click event
            ClickEvent click = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tweaked copy " + output);
            txtComponent.getStyle().setClickEvent(click);
            
            //add a hover event
            HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Click to copy"));
            txtComponent.getStyle().setHoverEvent(hoverEvent);
            
            //send the message
            player.sendMessage(txtComponent);
			
            //dump the message
			LOG.dump(TAB + output);
        }
        else
        {
        	//create the text component
            TextComponentString txtComponent = new TextComponentString("Requires an item in the main hand");
            
            //send the message
            player.sendMessage(txtComponent);
        }
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, EntityPlayer player, String[] args, BlockPos targetPos)
	{
		return Collections.<String>emptyList();
	}

}
