package it.mauro.mobpointspremium.api;

import org.bukkit.ChatColor;

public class ChatAPI {

    public static String translate(String s) {
        String prefix = translateBase("&b&lMobPoints&6&l+ &b» ");
        return ChatColor.translateAlternateColorCodes('&', prefix+s);
    }
    public static String translateA(String s) {
        String prefix = translateBase("&b&lMB&6&l+ &b» ");
        return ChatColor.translateAlternateColorCodes('&', prefix+s);
    }
    public static String translateBase(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
