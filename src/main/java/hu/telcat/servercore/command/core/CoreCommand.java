package hu.telcat.servercore.command.core;

import org.bukkit.entity.Player;

public interface CoreCommand {

    public void executeCommand(Player player, String[] args);
}
