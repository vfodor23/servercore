package hu.telcat.servercore.utils;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import net.minecraft.server.v1_16_R3.CommandDispatcher;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.command.CraftCommandMap;

import java.util.ArrayList;
import java.util.List;

public class Command {

    private static CraftCommandMap commandMap = (CraftCommandMap) ((CraftServer) Bukkit.getServer()).getCommandMap();
    private static String FALLBACK_PREFIX = "core";

    protected LiteralArgumentBuilder<CommandListenerWrapper> mainCommand;
    protected List<LiteralArgumentBuilder<CommandListenerWrapper>> aliasCommands = new ArrayList<LiteralArgumentBuilder<CommandListenerWrapper>>();
    private String permission = null;
    protected com.mojang.brigadier.Command<CommandListenerWrapper> defaultExecutes = null;

    /**
     * Constructor
     * @param commandName String
     * @param aliases String[]
     * @param permission String
     */
    public Command(String commandName, String[] aliases, String permission) {
        this.mainCommand = CommandDispatcher.a(commandName);
        this.permission = permission;

        if (permission != null) {
            mainCommand.requires((commandlistenerwrapper) -> {
                return commandlistenerwrapper.getBukkitSender().hasPermission(permission);
            });
        }

        for (String alias : aliases) {
            LiteralArgumentBuilder<CommandListenerWrapper> aliasCommand = CommandDispatcher.a(alias);

            if (permission != null) {
                aliasCommand.requires((commandlistenerwrapper) -> {
                    return commandlistenerwrapper.getBukkitSender().hasPermission(permission);
                });
            }

            aliasCommands.add(aliasCommand);
        }
    }

    /**
     * Constructor
     * @param commandName String
     * @param aliases String[]
     */
    public Command(String commandName, String[] aliases) {
        this(commandName, aliases, null);
    }

    /**
     * Constructor
     * @param commandName String
     * @param permission String
     */
    public Command(String commandName, String permission) {
        this(commandName, new String[] {}, permission);
    }

    /**
     *
     */
    public void register() {
        CommandDispatcher dispatcher = NMS.getServer().dataPackResources.commandDispatcher;

        if (defaultExecutes != null) {
            mainCommand.executes(defaultExecutes);
        }

        LiteralCommandNode<CommandListenerWrapper> registeredMainCommand = NMS.registerCommand(mainCommand);

        commandMap.register(FALLBACK_PREFIX, new CoreCommandWrapper(dispatcher, registeredMainCommand));

        for (LiteralArgumentBuilder<CommandListenerWrapper> aliasCommand : aliasCommands) {
            if (defaultExecutes != null) {
                aliasCommand.executes(defaultExecutes);
            }

            aliasCommand.redirect(registeredMainCommand);

            LiteralCommandNode<CommandListenerWrapper> registeredAliasCommand = NMS.registerCommand(aliasCommand);
            commandMap.register(FALLBACK_PREFIX, new CoreCommandWrapper(dispatcher, registeredAliasCommand));
        }
    }
}
