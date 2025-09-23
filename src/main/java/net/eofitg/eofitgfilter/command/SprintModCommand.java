package net.eofitg.eofitgfilter.command;

import net.eofitg.eofitgfilter.SprintMod;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class SprintModCommand extends CommandBase {


    @Override
    public String getCommandName() {
        return "sprintmod";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/sprintmod";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayer)) return;
        SprintMod.config.setEnabled(!SprintMod.config.isEnabled());
        SprintMod.saveConfig();
    }

}
