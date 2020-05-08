package net.comorevi.np.ace;

import cn.nukkit.Server;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

import java.io.File;

public class ACEPlugin extends PluginBase {
    private static final int CONFIG_VERSION = 1;
    private Config config;

    @Override
    public void onEnable() {
        saveResource("config.yml", false);
        config = new Config(new File("./plugins/AutoCommandExecutor", "config.yml"), Config.YAML);
        if (config.getInt("version") < CONFIG_VERSION) {
            getServer().getLogger().warning("[AutoCommandExecutor] Please delete old config file.");
            getServer().getPluginManager().disablePlugin(this);
        }
        registerTasks();
    }

    private void registerTasks() {
        config.getKeys().forEach(key -> {
            getServer().getScheduler().scheduleRepeatingTask(this,
                    new Runnable() {
                        @Override
                        public void run() {
                            Server.getInstance().dispatchCommand(Server.getInstance().getConsoleSender(), config.getString(key+".command"));
                        }
                    }
            , config.getInt(key+".interval") * 20);
        });
    }
}
