package it.mauro.mobpointspremium.commands;

import it.mauro.mobpointspremium.MobPointsPremium;
import it.mauro.mobpointspremium.api.ChatAPI;
import it.mauro.mobpointspremium.utils.CC;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class MobPointsStaff implements CommandExecutor {

    private final MobPointsPremium pl;

    public MobPointsStaff(MobPointsPremium pl) {
        this.pl = pl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(!sender.hasPermission("mobpointspremium.command.admin") && args.length == 0){
            sender.sendMessage(ChatAPI.translateBase("&5Running &d&lMobPoints+&5 v1 by &d@maurof00"));
        }else if(args.length == 0 && sender.hasPermission("mobpointspremium.command.help")) {
            sender.sendMessage(" ");
            sender.sendMessage(CC.color("&7<&m----&6&lMobPoints&b&l+ &bHelp&7&m----&7>"));
            sender.sendMessage(CC.color("&cStaff commands"));
            sender.sendMessage(CC.color("&b- /mobpoints help: &eShows this message."));
            sender.sendMessage(CC.color("&b- /mobpoints setpoints <user> <points>: &eSets player points."));
            sender.sendMessage(CC.color("&b- /mobpoints reload: &eReloads configuration file."));
            sender.sendMessage("");
        } else if(args.length == 1 && args[0].equals("help") && sender.hasPermission("mobpointspremium.command.help")) {
            sender.sendMessage(" ");
            sender.sendMessage(CC.color("&7<&m----&6&lMobPoints&b&l+ &bHelp&7&m----&7>"));
            sender.sendMessage(CC.color("&cStaff commands"));
            sender.sendMessage(CC.color("&b- /mobpoints help: &eShows this message."));
            sender.sendMessage(CC.color("&b- /mobpoints setpoints <user> <points>: &eSets player points."));
            sender.sendMessage(CC.color("&b- /mobpoints reload: &eReloads configuration file."));
            sender.sendMessage("");
        } else if(args.length == 1 && args[0].equals("reload") && sender.hasPermission("mobpointspremium.command.reload")) {
            pl.getConfig().options().copyDefaults(false);
            pl.reloadConfig();
            pl.saveConfig();
            MobPointsPremium.getMessage();
            sender.sendMessage(ChatAPI.translateA(MobPointsPremium.getMessage().getString("messages.plugin.reload_config")));
        }else if(args.length == 2 && sender.hasPermission("mobpointspremium.command.admin")) {
            sender.sendMessage(CC.color(MobPointsPremium.getMessage().getString(CC.color("messages.errors.incorrect_command").replaceAll("%usage%", "&e/mobpoints setpoints <user> <points>"))));
        } else if(args.length == 3 && args[0].equals("setpoints") && sender.hasPermission("mobpointspremium.command.setpoints")) {
            String playerName = args[1];
            Player player = Bukkit.getPlayer(playerName);
            if (player == null){
                sender.sendMessage(ChatAPI.translateBase(MobPointsPremium.getMessage().getString("messages.errors.player_not_found")));
                return true;
            }

            int amount = 0;
            try {
                amount = Integer.parseInt(args[2]);
            }catch (NumberFormatException e){
                sender.sendMessage(ChatAPI.translateBase(MobPointsPremium.getMessage().getString("messages.errors.invalid_amount")));
                return true;
            }

            try {
                pl.getPointsDatabase().updatePlayerPoints(player, amount);
                sender.sendMessage(ChatAPI.translate(MobPointsPremium.getMessage().getString("messages.points.set_points").replace("%player%", playerName).replaceAll("%amount%", formatNumero(amount))));
            } catch (SQLException e) {
                e.printStackTrace();
                sender.sendMessage(ChatColor.RED + "An error occurred while updating the player's points. Try again later.");
                return true;
            }
        } else if(args.length == 3 && args[0].equals("resetpoints") && sender.hasPermission("mobpointspremium.command.resetpoints")) {
            String playerName = args[1];
            Player player = Bukkit.getPlayer(playerName);
            if (player == null){
                sender.sendMessage(CC.color(MobPointsPremium.getMessage().getString("messages.errors.player_not_found")));
                return true;
            }

            try {
                pl.getPointsDatabase().updatePlayerPoints(player, 0);
                sender.sendMessage(ChatAPI.translate(MobPointsPremium.getMessage().getString("messages.points.reset_points").replace("%player%", playerName)));
            } catch (SQLException e) {
                e.printStackTrace();
                sender.sendMessage(ChatColor.RED + "An error occurred while updating the player's points. Try again later.");
                return true;
            }
        } else if (args.length == 0) {
            sender.sendMessage(ChatAPI.translateBase(MobPointsPremium.getMessage().getString("messages.errors.incorrect_command").replaceAll("%usage%", "&e/mobpoints setpoints/reload")));
        }
        return false;
    }

    private String formatNumero(int n) {
        if (n >= 1_000_000_000) {
            double billionTaglia = (double) n / 1_000_000_000;
            return billionTaglia + "B";
        } else if (n >= 1_000_000) {
            double millionTaglia = (double) n / 1_000_000;
            return millionTaglia + "M";
        } else if (n >= 1_000) {
            double thousandTaglia = (double) n / 1_000;
            return thousandTaglia + "K";
        } else {
            return String.valueOf(n);
        }
    }
}
