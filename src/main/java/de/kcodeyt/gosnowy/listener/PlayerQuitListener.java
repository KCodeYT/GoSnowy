package de.kcodeyt.gosnowy.listener;

import de.kcodeyt.gosnowy.GoSnowy;
import io.gomint.entity.EntityPlayer;
import io.gomint.event.EventHandler;
import io.gomint.event.EventListener;
import io.gomint.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements EventListener {

    private GoSnowy goSnowy;

    public PlayerQuitListener(GoSnowy goSnowy) {
        this.goSnowy = goSnowy;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        EntityPlayer player = event.getPlayer();

        this.goSnowy.getPlayerManager().saveConfig(player.getName());
    }

}
