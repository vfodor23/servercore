package hu.telcat.servercore.listener;

import hu.telcat.servercore.utils.ChatUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.text.MessageFormat;

public class PlayerChatListener implements Listener {

    @EventHandler
    public void onPlayerChatEvent(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        MessageFormat format = ChatUtils.getChatFormat(player);
        event.setFormat(format.format(new Object[]{}));
    }
}
