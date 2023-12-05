package hu.telcat.servercore.listener;

import hu.telcat.servercore.utils.ServerConstants;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class MiscListener implements Listener {

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event){
        if(ServerConstants.disableWeatherChange) event.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevelChangeEvent(FoodLevelChangeEvent event) {
        if(ServerConstants.disableFoodLevelChange) event.setCancelled(true);
    }

    @EventHandler
    public void onLeafDecayEvent(LeavesDecayEvent event){
        if(ServerConstants.disableLeafDecay) event.setCancelled(true);
    }
}
