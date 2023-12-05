package hu.telcat.servercore.listener;

import hu.telcat.servercore.ServerCore;
import hu.telcat.servercore.command.CommandType;
import hu.telcat.servercore.entity.Item;
import hu.telcat.servercore.entity.list.ItemList;
import hu.telcat.servercore.log.Log;
import hu.telcat.servercore.utils.ServerConstants;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteractListener(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if ((event.getAction() == Action.PHYSICAL) && (event.getClickedBlock().getType() == Material.FARMLAND) && ServerConstants.disableCropTrampling) {
            event.setCancelled(true);
            return;
        }
        if(event.getItem() == null){
            return;
        }
        ItemStack usedItem = event.getItem();
        if(!usedItem.hasItemMeta()){
            return;
        }
        if(usedItem.getItemMeta().getPersistentDataContainer().has(ServerConstants.getHashCommandKey(), PersistentDataType.STRING)){
            String hashCode = usedItem.getItemMeta().getPersistentDataContainer().get(ServerConstants.getHashCommandKey(), PersistentDataType.STRING);
            String[] hash = hashCode.split("#");
            if(!hash[0].equalsIgnoreCase("U")){
                event.setCancelled(true);
                return;
            }
            if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR){
                Item item = ItemList.getItem(hash[1]);
                item.runCommand(player);
                event.setCancelled(true);
            }else{
                event.setCancelled(true);
                return;
            }
        }
    }
}
