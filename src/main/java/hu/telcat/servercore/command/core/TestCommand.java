package hu.telcat.servercore.command.core;

import org.bukkit.entity.Player;

public class TestCommand implements CoreCommand{

    @Override
    public void executeCommand(Player player, String[] args) {
        player.sendMessage("MÚKODIK");
    }
}
