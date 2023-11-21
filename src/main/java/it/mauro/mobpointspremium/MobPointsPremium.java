package it.mauro.mobpointspremium;

import it.mauro.mobpointspremium.commands.GetPoints;
import it.mauro.mobpointspremium.commands.MobPointsStaff;
import it.mauro.mobpointspremium.database.PointsDatabase;
import it.mauro.mobpointspremium.events.MobKillListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.SQLException;

public final class MobPointsPremium extends JavaPlugin {


    private PointsDatabase pointsDatabase;

    public static @NotNull MobPointsPremium getPlugin() {
        return getPlugin(MobPointsPremium.class);
    }

    public static void createDefaultConfigurations() {
        getPlugin().saveDefaultConfig();
        File messageFile = new File(getPlugin().getDataFolder(), "messages.yml");
        if (!messageFile.exists()) {
            getPlugin().saveResource("messages.yml", false);
        }
    }

    public static @NotNull FileConfiguration getMessage() {
        return YamlConfiguration.loadConfiguration(new File(getPlugin().getDataFolder(), "messages.yml"));
    }

    public static @NotNull Plugin getInstance() {
        return getPlugin();
    }

    @Override
    public void onEnable() {
        createDefaultConfigurations();
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            pointsDatabase = new PointsDatabase(getDataFolder().getAbsolutePath() + "/points.db");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to database! " + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
        }
        saveDefaultConfig();
        getCommand("points").setExecutor(new GetPoints(this));
        getCommand("mobpoints").setExecutor(new MobPointsStaff(this));
        getServer().getPluginManager().registerEvents(new MobKillListener(this), this);

    }

    @Override
    public void onDisable() {
        try {
            pointsDatabase.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PointsDatabase getPointsDatabase() {
        return pointsDatabase;
    }
}
