package hu.telcat.servercore.listener;

import hu.telcat.servercore.ServerCore;
import hu.telcat.servercore.command.CommandType;
import hu.telcat.servercore.entity.Item;
import hu.telcat.servercore.entity.list.ItemList;
import hu.telcat.servercore.utils.ServerConstants;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if(event.getWhoClicked() instanceof Player){
            Player player = (Player) event.getWhoClicked();
            if(event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()){
                return;
            }
            ItemStack clickedItem = event.getCurrentItem();
            if(clickedItem.getItemMeta().getPersistentDataContainer().has(ServerConstants.getHashCommandKey(), PersistentDataType.STRING)) {
                String hashCode = clickedItem.getItemMeta().getPersistentDataContainer().get(ServerConstants.getHashCommandKey(), PersistentDataType.STRING);
                String[] hash = hashCode.split("#");
                if (hash[0].equalsIgnoreCase("D")) {
                    event.setCancelled(true);
                    return;
                }
                if (!hash[0].equalsIgnoreCase("C")) {
                    event.setCancelled(true);
                    return;
                }
                Item item = ItemList.getItem(hash[1]);
                item.runCommand(player);
                event.setCancelled(true);
            }
        }
    }
}
