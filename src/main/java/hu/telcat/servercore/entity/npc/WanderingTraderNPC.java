package hu.telcat.servercore.entity.npc;

import hu.telcat.servercore.ServerCore;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Chunk;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.metadata.FixedMetadataValue;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class WanderingTraderNPC extends NPC{

    private final ArrayList<EntityArmorStand> npcHolograms = new ArrayList<>();
    private WanderingTrader villager;

    public WanderingTraderNPC(String name, String skin, JSONArray text, String location) {
        super(name, skin, text, location);
    }

    public WanderingTraderNPC(String name, String skin, JSONArray text, String location, JSONObject json) {
        super(name, skin, text, location, json);
    }

    @Override
    public void spawn(){
        Chunk chunk = getLocation().getChunk();
        if(!chunk.isLoaded()){
            chunk.load();
        }
        villager = WanderingTraderNMS.spawnWanderingTrader(getLocation());
        villager.setSilent(true);
        villager.setRotation(getLocation().getYaw(), getLocation().getPitch());
        if(isCommandEnabled()){
            villager.setMetadata("CORE#CUSTOM_COMMAND", new FixedMetadataValue(ServerCore.getInstance(), this.getName()));
        }
        double armorHeight = -0.2;
        for(Object obj : getText()){
            WorldServer worldServer = ((CraftWorld)getLocation().getWorld()).getHandle();
            EntityArmorStand stand = new EntityArmorStand(EntityTypes.ARMOR_STAND, worldServer);
            stand.setLocation(getLocation().getX(), getLocation().getY() + armorHeight, getLocation().getZ(),
                    getLocation().getYaw(), getLocation().getPitch());
            IChatBaseComponent chatBaseComponent = new ChatMessage(obj.toString().replaceAll("&", "§"));
            stand.setCustomName(chatBaseComponent);
            stand.setCustomNameVisible(true);
            stand.setInvisible(true);
            stand.setNoGravity(true);
            worldServer.addEntity(stand);
            npcHolograms.add(stand);
            armorHeight += 0.3;
        }
    }

    public void remove() {
        villager.remove();
        Iterator<EntityArmorStand> it = npcHolograms.iterator();
        while(it.hasNext()){
            it.next().killEntity();
            it.remove();
        }
    }
}
