package hu.telcat.servercore.entity.npc;

import hu.telcat.servercore.entity.CoreInventory;
import hu.telcat.servercore.log.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;

public class NPCList {

    private static HashMap<String, NPC> npcByName = new HashMap<>();

    public static void addNPC(String name, ResultSet result) {
        npcByName.remove(name);
        try {
            NPC npc = null;
            switch(result.getString("type")){
                case "SHEEP":
                    if(result.getString("command") != null && !Objects.equals(result.getString("command"), "")){
                        npc = new SheepNPC(name, result.getString("skin"),
                                new JSONArray(result.getString("text")),
                                result.getString("location"), new JSONObject(result.getString("command")));
                    }else{
                        npc = new SheepNPC(name, result.getString("skin"),
                                new JSONArray(result.getString("text")), result.getString("location"));
                    }
                    break;
                case "VILLAGER":
                    if(result.getString("command") != null && !Objects.equals(result.getString("command"), "")){
                        npc = new VillagerNPC(name, result.getString("skin"),
                                new JSONArray(result.getString("text")),
                                result.getString("location"), new JSONObject(result.getString("command")));
                    }else{
                        npc = new VillagerNPC(name, result.getString("skin"),
                                new JSONArray(result.getString("text")), result.getString("location"));
                    }
                    break;
                case "TRADER":
                    if(result.getString("command") != null && !Objects.equals(result.getString("command"), "")){
                        npc = new WanderingTraderNPC(name, "",
                                new JSONArray(result.getString("text")),
                                result.getString("location"), new JSONObject(result.getString("command")));
                    }else{
                        npc = new WanderingTraderNPC(name, "",
                                new JSONArray(result.getString("text")), result.getString("location"));
                    }
                    break;
            }
            npcByName.put(name, npc);
        } catch (SQLException e) {
            Log.error("Error during the loading of item " + name + ". Please check log!");
            e.printStackTrace();
        }
    }

    public static NPC getNPC(String name){
        return npcByName.get(name);
    }

    public static boolean isNPCExist(String name){
        return npcByName.containsKey(name);
    }

    public static int npcSize(){
        return npcByName.size();
    }

    public static HashMap<String, NPC> getNPCByName() {
        return npcByName;
    }
}
