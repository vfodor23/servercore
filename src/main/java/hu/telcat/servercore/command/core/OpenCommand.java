package hu.telcat.servercore.command.core;

import hu.telcat.servercore.entity.CoreInventory;
import hu.telcat.servercore.entity.list.CoreInventoryList;
import hu.telcat.servercore.utils.Language;
import org.bukkit.entity.Player;

public class OpenCommand implements CoreCommand{

    @Override
    public void executeCommand(Player player, String[] args) {
        String inventory = args[0];
        if(!CoreInventoryList.isInventoryExist(inventory)){
            player.sendMessage(Language.INVENTORY_DO_NOT_EXIST.replaceAll("%inventory_name%", inventory));
            return;
        }
        CoreInventory coreInventory = CoreInventoryList.getInventory(inventory);
        player.openInventory(coreInventory.getBukkitInventory());
    }
}
