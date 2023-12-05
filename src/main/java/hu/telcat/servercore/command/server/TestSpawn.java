package hu.telcat.servercore.command.server;

import hu.telcat.servercore.entity.npc.NPC;
import hu.telcat.servercore.entity.npc.NPCList;
import hu.telcat.servercore.entity.npc.SheepNMS;
import hu.telcat.servercore.entity.npc.SheepNPC;
import hu.telcat.servercore.utils.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestSpawn extends Command {

    public TestSpawn() {
        super("test-spawn", "core.test.spawn");
    }

    @Override
    public void register() {
        mainCommand.executes((commandContext -> {
            CommandSender commandSender = commandContext.getSource().getBukkitSender();
            if(commandSender instanceof Player){
                Player player = (Player) commandSender;

                player.sendMessage("§eSPAWNED");
            }
            return 0;
        }));
        super.register();
    }
}
