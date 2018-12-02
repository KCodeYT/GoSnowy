package de.kcodeyt.gosnowy.manager;

import de.kcodeyt.gosnowy.GoSnowy;
import de.kcodeyt.gosnowy.config.PlayerConfig;
import io.gomint.config.InvalidConfigurationException;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PlayerManager {

    private GoSnowy goSnowy;

    private Map<String, PlayerConfig> playerConfigs;

    private File playerDirectory;

    public PlayerManager(GoSnowy goSnowy) {
        this.goSnowy = goSnowy;
        this.playerConfigs = new HashMap<>();

        this.playerDirectory = new File(this.goSnowy.getDataFolder(), "players");
    }

    private boolean playerExists(String playerName) {
        return this.playerConfigs.containsKey(playerName);
    }

    private PlayerConfig loadPlayer(String playerName) {
        File playerFile = new File(this.playerDirectory, playerName + ".yml");

        PlayerConfig playerConfig;
        if(this.playerExists(playerName)) {
            playerConfig = this.playerConfigs.get(playerName);

            try {
                playerConfig.load(playerFile);
            } catch(InvalidConfigurationException e) {
                this.goSnowy.getLogger().error("Error whilst loading the file from " + playerName + ": ", e);
            }

        } else {
            playerConfig = new PlayerConfig(true);

            try {
                playerConfig.init(playerFile);
            } catch(InvalidConfigurationException e) {
                this.goSnowy.getLogger().error("Error whilst initialising the file from " + playerName + ": ", e);
            }
        }

        this.playerConfigs.put(playerName, playerConfig);
        return playerConfig;
    }

    public PlayerConfig getPlayerConfig(String playerName) {
        if(this.playerExists(playerName))
            return this.playerConfigs.get(playerName);
        return this.loadPlayer(playerName);
    }

    public void saveConfig(String playerName) {
        if(this.playerExists(playerName)) {
            PlayerConfig playerConfig = this.playerConfigs.get(playerName);

            this.goSnowy.getScheduler().executeAsync(() -> {
                try {
                    playerConfig.save(new File(this.playerDirectory, playerName + ".yml"));
                } catch(InvalidConfigurationException e) {
                    this.goSnowy.getLogger().error("Error whilst initialising the file from " + playerName + ": ", e);
                }
            });
        }
    }

}
