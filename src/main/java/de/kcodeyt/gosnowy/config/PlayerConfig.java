package de.kcodeyt.gosnowy.config;

import io.gomint.config.YamlConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class PlayerConfig extends YamlConfig {

    private boolean seeSnow;

}
