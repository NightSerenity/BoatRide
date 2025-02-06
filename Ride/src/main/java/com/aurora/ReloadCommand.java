package com.aurora;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;

public class ReloadCommand implements CommandExecutor {
    private final Main plugin;

    public ReloadCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String reloadPermission = plugin.getConfig().getString("command.admin-permission", "ride.admin");

        if (!sender.hasPermission(reloadPermission)) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to reload the plugin.");
            return true;
        }

        plugin.reloadPluginConfig();
        sender.sendMessage(ChatColor.GREEN + "RidePlugin config has been reloaded!");
        return true;
    }
}