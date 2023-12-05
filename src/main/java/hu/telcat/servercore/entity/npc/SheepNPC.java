package hu.telcat.servercore.entity.npc;

import hu.telcat.servercore.ServerCore;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Chunk;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.Sheep;
import org.bukkit.metadata.FixedMetadataValue;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class SheepNPC extends NPC{

    private final ArrayList<EntityArmorStand> npcHolograms = new ArrayList<>();
    private Sheep sheep;

    public SheepNPC(String name, String skin, JSONArray text, String location) {
        super(name, skin, text, location);
    }

    public SheepNPC(String name, String skin, JSONArray text, String location, JSONObject json) {
        super(name, skin, text, location, json);
    }

    @Override
    public void spawn(){
        Chunk chunk = getLocation().getChunk();
        if(!chunk.isLoaded()){
            chunk.load();
        }
        sheep = SheepNMS.spawnSheep(getName(), getLocation());
        sheep.setColor(DyeColor.valueOf(getSkin()));
        sheep.setSilent(true);
        sheep.setRotation(getLocation().getYaw(), getLocation().getPitch());
        if(isCommandEnabled()){
            sheep.setMetadata("CORE#CUSTOM_COMMAND", new FixedMetadataValue(ServerCore.getInstance(), this.getName()));
        }
        double armorHeight = -0.7;
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
        sheep.remove();
        Iterator<EntityArmorStand> it = npcHolograms.iterator();
        while(it.hasNext()){
            it.next().killEntity();
            it.remove();
        }
    }
}
