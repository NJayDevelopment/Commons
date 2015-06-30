package me.austinlm.commons.commands.ioc.modules;

import com.sk89q.intake.parametric.AbstractModule;
import me.austinlm.commons.commands.Annotations;
import me.austinlm.commons.commands.ioc.providers.PlayerProvider;
import org.bukkit.entity.Player;

/**
 * Module that aids in the parsing of a {@link org.bukkit.entity.Player}.
 *
 * @author Austin Mayes
 */
public class PlayerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Player.class).annotatedWith(Annotations.Sender.class).toProvider(new PlayerProvider(true));
        bind(Player.class).toProvider(new PlayerProvider(false));
    }

}
