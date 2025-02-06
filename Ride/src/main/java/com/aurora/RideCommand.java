package com.aurora;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class RideCommand implements CommandExecutor {
    private final JavaPlugin plugin;

    public RideCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        String permission = plugin.getConfig().getString("command.permission", "ride.use");

        if (!player.hasPermission(permission)) {
            player.sendMessage("§cYou do not have permission to use this command!");
            return true;
        }

        List<String> allowedBlocks = plugin.getConfig().getStringList("allowed-blocks");

        Location loc = player.getLocation();
        Block blockBelow = loc.clone().subtract(0, 1, 0).getBlock();

        if (!allowedBlocks.contains(blockBelow.getType().name())) {
            player.sendMessage("§eYou must be standing on ice to use this command.");
            return true;
        }

        Location boatSpawnLocation = loc.clone().add(0, 0.5, 0);
        Boat boat = (Boat) player.getWorld().spawn(boatSpawnLocation, Boat.class);
        boat.addPassenger(player);

        int despawnTime = plugin.getConfig().getInt("despawn-time", 4);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!boat.getPassengers().contains(player)) {
                    if (despawnTime > 0) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (!boat.isDead()) {
                                    boat.remove();
                                }
                            }
                        }.runTaskLater(plugin, despawnTime * 20L);
                    }
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 10L);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!boat.getPassengers().contains(player)) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (!boat.isDead()) {
                                boat.remove();
                            }
                        }
                    }.runTaskLater(plugin, despawnTime * 20L);
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 10L);

        return true;
    }
}