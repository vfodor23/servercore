package hu.telcat.servercore.utils;

import hu.telcat.servercore.log.Log;
import net.minecraft.server.v1_16_R3.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftFurnaceRecipe;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftShapedRecipe;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftShapelessRecipe;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;

public class NMS {
    private static MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    /**
     * Returns Minecraft/Spigot version (Format: 1.12)
     * @return String
     */
    public static String getMinecraftVersion() {
        // 1.12: git-Spigot-xxxxxx-xxxxxxx (MC: 1.12)
        return Bukkit.getVersion().split(" ")[2].split("\\)")[0];
    }

    /**
     * Returns the Player object EntityPlayer origins
     * @param player Player
     * @return EntityPlayer
     */
    public static EntityPlayer getHandle(Player player) {
        return ((CraftPlayer) player).getHandle();
    }

    /**
     * Returns the LivingEntity object EntityLiving origins
     * @param entity LivingEntity
     * @return EntityLiving
     */
    public static EntityLiving getHandle(LivingEntity entity) {
        return ((CraftLivingEntity) entity).getHandle();
    }

    /**
     * Returns the Entity object Entity origins
     * @param entity Entity
     * @return net.minecraft.server.v1_16_R3.Entity
     */
    public static net.minecraft.server.v1_16_R3.Entity getHandle(Entity entity) {
        return ((CraftEntity) entity).getHandle();
    }

    /**
     * Returns the World object WorldServer origins
     * @param world World
     * @return WorldServer
     */
    public static WorldServer getHandle(World world) {
        return ((CraftWorld) world).getHandle();
    }

    /**
     * Returns the Server object MinecraftServer origins
     * @return MinecraftServer
     */
    public static MinecraftServer getServer() {
        Server server = Bukkit.getServer();
        return ((CraftServer) server).getServer();
    }

    /**
     * Send packet to the specified player
     * @param player Player
     * @param packet Packet<?>
     */
    public static void sendPacket(Player player, Packet<?> packet) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

        if (entityPlayer.playerConnection == null ) {
            return;
        }

        entityPlayer.playerConnection.sendPacket(packet);
    }

    /**
     * Send packet to the specified players
     * @param players List<Player>
     * @param packet Packet<?>
     */
    public static void sendPacket(List<Player> players, Packet<?> packet) {
        for (Player player : players) {
            sendPacket(player, packet);
        }
    }

    /**
     * Send packet to the all connected players
     * @param packet Packet<?>
     */
    public static void sendPacketToAll(Packet<?> packet) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendPacket(player, packet);
        }
    }

    public static LiteralCommandNode<CommandListenerWrapper> registerCommand(LiteralArgumentBuilder<CommandListenerWrapper> command) {
        return getServer().dataPackResources.commandDispatcher.a().register(command);
    }

    public static Field getField(Class<?> clazz, String field) {
        if (clazz == null)
            return null;
        Field f = null;
        try {
            f = clazz.getDeclaredField(field);
            f.setAccessible(true);
            return f;
        } catch (Exception e) {
            Log.error(e.getLocalizedMessage());
            return null;
        }
    }

    public static void clearGoals(PathfinderGoalSelector... goalSelectors) {
        if (goalSelectors == null)
            return;
        int i = 0;
        for (PathfinderGoalSelector selector : goalSelectors) {
            try {
                Iterator it = selector.d().iterator();
                while(it.hasNext()){
                    it.next();
                    it.remove();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            i++;
        }
    }
}
