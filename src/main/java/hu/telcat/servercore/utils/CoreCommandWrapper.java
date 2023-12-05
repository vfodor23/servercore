package hu.telcat.servercore.utils;

import java.util.Collections;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.command.CraftBlockCommandSender;
import org.bukkit.craftbukkit.v1_16_R3.command.ProxiedNativeCommandSender;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftMinecartCommand;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;

import com.google.common.base.Joiner;
import com.mojang.brigadier.tree.CommandNode;

import net.minecraft.server.v1_16_R3.CommandDispatcher;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import net.minecraft.server.v1_16_R3.DedicatedServer;
import net.minecraft.server.v1_16_R3.EntityMinecartCommandBlock;
import net.minecraft.server.v1_16_R3.MinecraftServer;

public class CoreCommandWrapper extends BukkitCommand {
    private final CommandDispatcher dispatcher;
    public final CommandNode<CommandListenerWrapper> coreCommand;

    protected CoreCommandWrapper(CommandDispatcher dispatcher, CommandNode<CommandListenerWrapper> coreCommand) {
        super(coreCommand.getName(), "A Core provided command.", coreCommand.getUsageText(), Collections.EMPTY_LIST);
        this.dispatcher = dispatcher;
        this.coreCommand = coreCommand;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender))
            return true;

        CommandListenerWrapper icommandlistener = getListener(sender);
        dispatcher.a(icommandlistener, toDispatcher(args, getName()), toDispatcher(args, commandLabel), true);
        return true;
    }

    private CommandListenerWrapper getListener(CommandSender sender) {
        if (sender instanceof Player) {
            return ((CraftPlayer) sender).getHandle().getCommandListener();
        }
        if (sender instanceof BlockCommandSender) {
            return ((CraftBlockCommandSender) sender).getWrapper();
        }
        if (sender instanceof CommandMinecart) {
            return ((EntityMinecartCommandBlock) ((CraftMinecartCommand) sender).getHandle()).getCommandBlock().getWrapper();
        }
        if (sender instanceof RemoteConsoleCommandSender) {
            return ((DedicatedServer) MinecraftServer.getServer()).remoteControlCommandListener.getWrapper();
        }
        if (sender instanceof ConsoleCommandSender) {
            return ((CraftServer) sender.getServer()).getServer().getServerCommandListener();
        }
        if (sender instanceof ProxiedCommandSender) {
            return ((ProxiedNativeCommandSender) sender).getHandle();
        }

        throw new IllegalArgumentException("Cannot make " + sender + " a core command listener");
    }

    public static String getPermission(CommandNode<CommandListenerWrapper> coreCommand) {
        return "core.command." + ((coreCommand.getRedirect() == null) ? coreCommand.getName() : coreCommand.getRedirect().getName());
    }

    private String toDispatcher(String[] args, String name) {
        return "/" + name + ((args.length > 0) ? " " + Joiner.on(' ').join(args) : "");
    }
}
