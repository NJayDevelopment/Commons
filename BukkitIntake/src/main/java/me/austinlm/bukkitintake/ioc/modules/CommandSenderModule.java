package me.austinlm.bukkitintake.ioc.modules;

import com.sk89q.intake.parametric.AbstractModule;
import me.austinlm.bukkitintake.Annotations;
import me.austinlm.bukkitintake.ioc.providers.CommandSenderProvider;
import org.bukkit.command.CommandSender;

/**
 * Module that aids in the parsing of a {@link org.bukkit.command.CommandSender}.
 *
 * @author Austin Mayes
 */
public class CommandSenderModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CommandSender.class).annotatedWith(Annotations.Sender.class).toProvider(new CommandSenderProvider(true));
        bind(CommandSender.class).toProvider(new CommandSenderProvider(false));
    }

}
