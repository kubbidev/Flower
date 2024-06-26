package me.kubbidev.flower.loader;

import me.kubbidev.flower.loader.util.JarInJarClassLoader;
import me.kubbidev.flower.loader.util.LoaderBootstrap;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitLoaderPlugin extends JavaPlugin {
    private static final String JAR_NAME = "flower-bukkit.jarinjar";
    private static final String BOOTSTRAP_CLASS = "me.kubbidev.flower.FBukkitBootstrap";

    private final LoaderBootstrap plugin;

    public BukkitLoaderPlugin() {
        JarInJarClassLoader loader = new JarInJarClassLoader(getClass().getClassLoader(), JAR_NAME);
        this.plugin = loader.instantiatePlugin(BOOTSTRAP_CLASS, JavaPlugin.class, this);
    }

    @Override
    public void onLoad() {
        this.plugin.onLoad();
    }

    @Override
    public void onEnable() {
        this.plugin.onEnable();
    }

    @Override
    public void onDisable() {
        this.plugin.onDisable();
    }

}