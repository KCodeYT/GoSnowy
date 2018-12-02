package de.kcodeyt.gosnowy.commad;

import de.kcodeyt.gosnowy.GoSnowy;
import de.kcodeyt.gosnowy.config.PlayerConfig;
import io.gomint.command.Command;
import io.gomint.command.CommandOutput;
import io.gomint.command.CommandSender;
import io.gomint.command.ConsoleCommandSender;
import io.gomint.command.annotation.Description;
import io.gomint.command.annotation.Name;
import io.gomint.entity.EntityPlayer;
import io.gomint.plugin.injection.InjectPlugin;

import java.util.Map;

@Name("snow")
@Description("Set the see snow status to on/off")
public class SnowCommand extends Command {

    @InjectPlugin
    private GoSnowy goSnowy;

    @Override
    public CommandOutput execute(CommandSender commandSender, String s, Map<String, Object> map) {
        CommandOutput commandOutput = new CommandOutput();

        if(commandSender instanceof ConsoleCommandSender) {
            return commandOutput.fail("You need to be a player to execute this command");
        }

        EntityPlayer player = (EntityPlayer) commandSender;

        PlayerConfig playerConfig = this.goSnowy.getPlayerManager().getPlayerConfig(player.getName());
        playerConfig.setSeeSnow(!playerConfig.isSeeSnow());

        if(playerConfig.isSeeSnow())
            return commandOutput.success("You see snow particles again.");
        else
            return commandOutput.success("You dont see snow particles anymore.");
    }

}
