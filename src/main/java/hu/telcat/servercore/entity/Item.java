package hu.telcat.servercore.entity;

import java.util.ArrayList;

import hu.telcat.servercore.ServerCore;
import hu.telcat.servercore.command.CommandType;
import hu.telcat.servercore.entity.list.ItemList;
import hu.telcat.servercore.log.Log;
import hu.telcat.servercore.utils.Language;
import hu.telcat.servercore.utils.ServerConstants;
import hu.telcat.servercore.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.json.JSONArray;
import org.json.JSONObject;

public class Item {

    private String name;
    private Material material;
    private String displayName;
    private int amount;
    private ArrayList<String> lore = new ArrayList<String>();
    private ArrayList<String> pages = new ArrayList<String>();
    private ArrayList<JSONObject> commands = new ArrayList<>();
    private InteractionType interactionType = InteractionType.NONE;
    private boolean hideAttributes = false;
    private boolean hideEnchants = false;

    public Item(String name, Material material, String displayName, int amount, JSONObject attributes, String interaction, JSONArray command) {
        this.name = name;
        this.material = material;
        this.displayName = displayName;
        this.amount = amount;
        if(attributes.has("lore")){
            JSONArray jsonLore = new JSONArray(attributes.getJSONArray("lore"));
            for(Object obj : jsonLore){
                lore.add(obj.toString());
            }
        }
        if(attributes.has("hideAttributes")){
            hideAttributes = attributes.getBoolean("hideAttributes");
        }
        if(attributes.has("hideEnchants")){
            hideEnchants = attributes.getBoolean("hideEnchants");
        }
        for(Object obj : command){
            JSONObject json = new JSONObject(obj.toString());
            commands.add(json);
        }
        loadInteraction(interaction);
    }

    public Item(String name, Material material, String displayName, int amount,  String interaction, JSONArray command) {
        this.name = name;
        this.material = material;
        this.displayName = displayName;
        this.amount = amount;
        for(Object obj : command){
            JSONObject json = new JSONObject(obj.toString());
            commands.add(json);
        }
        loadInteraction(interaction);
    }

    public Item(String name, Material material, String displayName, int amount,  String interaction) {
        this.name = name;
        this.material = material;
        this.displayName = displayName;
        this.amount = amount;
        loadInteraction(interaction);
    }

    public Item(Material material, String displayName, int amount) {
        this.material = material;
        this.displayName = displayName;
        this.amount = amount;
    }

    public Item(Material material, int amount) {
        this.material = material;
        this.amount = amount;
    }

    public void addLore(String s) {
        this.lore.add(s.replaceAll("&", "§"));
    }

    public void clearLore() {
        this.lore.clear();
    }

    public void removeLore(int i) {
        this.lore.remove(i);
    }

    public ArrayList<String> getLore(){
        return this.lore;
    }

    public Material getType() {
        return this.material;
    }

    public void setType(Material m) {
        this.material = m;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int i) {
        this.amount = i;
    }

    public void setPages(ArrayList<String> pages) {
        if(!pages.isEmpty()) {
            this.pages = pages;
        }
    }

    public ItemStack getItem() {
        ItemStack is = new ItemStack(this.material, this.amount);
        if(this.material.equals(Material.WRITTEN_BOOK)) {
            BookMeta im = (BookMeta) is.getItemMeta();
            im.setAuthor("ThePower");
            if(this.displayName != null) {
                im.setTitle(this.displayName);
            }
            if(!this.pages.isEmpty()) {
                for(String s : pages) {
                    im.addPage(s);
                }
            }
            is.setItemMeta(im);
            return is;
        }
        ItemMeta im = is.getItemMeta();
        if(this.displayName != null) {
            im.setDisplayName(this.displayName.replaceAll("&", "§"));
        }
        if(hideAttributes) im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        if(hideEnchants) im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        if(interactionType != InteractionType.NONE){
            ArrayList<String> buildedLore = new ArrayList<>();
            String identifactionHash = interactionType.toString().charAt(0) + "#" + this.name;
            if(!this.lore.isEmpty()){
                for(String line : lore){
                    buildedLore.add(line.replaceAll("&", "§"));
                }
            }
            if(interactionType == InteractionType.USE){
                buildedLore.add("  ");
                buildedLore.add(Language.ITEM_HOW_TO_USE);
            }else if(interactionType == InteractionType.CLICK){
                buildedLore.add("  ");
                buildedLore.add(Language.ITEM_HOW_TO_CLICK);
            }
            im.setLore(buildedLore);
            im.getPersistentDataContainer().set(ServerConstants.getHashCommandKey(), PersistentDataType.STRING, identifactionHash);
        }
        is.setItemMeta(im);
        return is;
    }

    public void runCommand(Player player){
        for(JSONObject json : commands){
            CommandType commandType = CommandType.valueOf(json.getString("type"));
            String command = json.getString("command");
            if(commandType == CommandType.CORE){
                String commandIdentifier = command;
                String[] args;
                if(command.contains(" ")){
                    String[] splitedCommand = command.split(" ");
                    commandIdentifier = splitedCommand[0];
                    args = new String[splitedCommand.length-1];
                    for(int i = 0; i< args.length; i++){
                        args[i] = splitedCommand[i+1];
                    }
                }else{
                    args = new String[0];
                }
                ServerCore.getInstance().getCoreCommand(commandIdentifier).executeCommand(player, args);
            }
        }
    }


    private void loadInteraction(String interaction) {
        if(interaction.contains("onUse") || interaction.contains("onClick") || interaction.contains("disableClick")){
            if(interaction.contains("disableClick")){
                interactionType = InteractionType.DISABLE_CLICK;
            }else if(interaction.contains("onUse")){
                interactionType = InteractionType.USE;
            }else{
                interactionType = InteractionType.CLICK;
            }
        }
    }

    public enum InteractionType {
        USE, CLICK, DISABLE_CLICK, NONE;
    }
}

