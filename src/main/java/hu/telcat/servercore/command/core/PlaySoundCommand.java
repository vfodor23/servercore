package hu.telcat.servercore.command.core;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PlaySoundCommand implements CoreCommand{
    @Override
    public void executeCommand(Player player, String[] args) {
        player.playSound(player.getLocation(), Sound.valueOf(args[0]), 1.0f, 1.0f);
    }
}
