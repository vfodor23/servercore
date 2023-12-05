package hu.telcat.servercore.command.core;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class NewMailCommand implements CoreCommand{

    @Override
    public void executeCommand(Player player, String[] args) {
        String HR = ChatColor.STRIKETHROUGH + StringUtils.repeat(" ", 80);
        player.sendMessage(HR);
        player.sendMessage("");
        player.sendMessage("§aSzerver §2Posta");
        player.sendMessage("§eÚj üzeneted érkezett. Az elolvasásához kattints §6IDE§e!");
        player.sendMessage("");
        player.sendMessage(HR);
    }
}
