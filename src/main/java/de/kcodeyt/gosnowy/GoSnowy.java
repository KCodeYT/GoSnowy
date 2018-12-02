package de.kcodeyt.gosnowy;

import de.kcodeyt.gosnowy.config.PlayerConfig;
import de.kcodeyt.gosnowy.config.PluginConfig;
import de.kcodeyt.gosnowy.listener.PlayerQuitListener;
import de.kcodeyt.gosnowy.manager.PlayerManager;
import io.gomint.config.InvalidConfigurationException;
import io.gomint.entity.EntityPlayer;
import io.gomint.math.Vector;
import io.gomint.plugin.Plugin;
import io.gomint.plugin.PluginName;
import io.gomint.plugin.Version;
import io.gomint.scheduler.Task;
import io.gomint.server.world.WorldAdapter;
import io.gomint.util.random.FastRandom;
import io.gomint.world.Particle;
import io.gomint.world.ParticleData;
import io.gomint.world.block.BlockAir;
import lombok.Getter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@PluginName("GoSnowy")
@Version(major = 1, minor = 0)
public class GoSnowy extends Plugin {

    @Getter
    private PlayerManager playerManager;

    private Task snowTask;

    private File configFile;
    private PluginConfig pluginConfig;

    @Override
    public void onInstall() {
        // Initializing the pluginConfig
        this.pluginConfig = new PluginConfig(250, 1);

        try {
            this.configFile = new File(this.getDataFolder(), "config.yml");

            if(this.configFile.exists()) {
                this.pluginConfig.load(new File(this.getDataFolder(), "config.yml"));
            }
        } catch(InvalidConfigurationException e) {
            this.getLogger().error("Error whilst loading config.yml:", e);
        }

        // Initializing the playerManager
        this.playerManager = new PlayerManager(this);

        // Registering the playerQuitListener
        this.registerListener(new PlayerQuitListener(this));

        // Creating the snowTask which sends the snow particles
        this.snowTask = this.getScheduler().schedule(() -> {

            FastRandom fastRandom = FastRandom.current();

            List<EntityPlayer> players = new ArrayList<>();

            for(EntityPlayer player : this.getServer().getPlayers()) {
                PlayerConfig playerConfig = this.playerManager.getPlayerConfig(player.getName());

                if(playerConfig.isSeeSnow()) {
                    players.add(player);
                }
            }

            for(int i = 0; i <= this.pluginConfig.getSnowflakesAmount(); i++) {
                int randomX = fastRandom.nextInt(51) - 25;
                int randomY = fastRandom.nextInt(14) + 2;
                int randomZ = fastRandom.nextInt(51) - 25;

                for(EntityPlayer player : players) {
                    Vector playerVector = player.getLocation().add(randomX, randomY, randomZ);

                    int blockHeight = 0;
                    for(int blockY = (int) playerVector.getY(); blockY <= 255; blockY++) {
                        if(!(player.getWorld().getBlockAt((int) playerVector.getX(), blockY, (int) playerVector.getZ()) instanceof BlockAir)) {
                            blockHeight = blockY;
                        }
                    }

                    if(blockHeight < playerVector.getY()) {
                        ((WorldAdapter) player.getWorld()).sendParticle(player, playerVector, Particle.FALLING_DUST, ParticleData.color(255, 255, 255));
                    }
                }
            }

        }, 0, 50 * this.pluginConfig.getSchedulerPeriod(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void onUninstall() {
        this.snowTask.cancel();

        try {
            if(!this.configFile.getParentFile().exists()) {
                this.configFile.getParentFile().mkdirs();
            }

            this.pluginConfig.save(this.configFile);
        } catch(InvalidConfigurationException e) {
            this.getLogger().error("Error whilst saving config.yml:", e);
        }
    }

}
