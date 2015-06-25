package me.austinlm.bukkitintake.ioc.providers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.ArgumentParseException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Provider for a {@link org.bukkit.command.CommandSender}.
 *
 * @author Austin Mayes
 */
public class CommandSenderProvider implements Provider<CommandSender> {

    private boolean isProvided;

    public CommandSenderProvider(boolean isProvided) {
        this.isProvided = isProvided;
    }

    @Override
    public boolean isProvided() {
        return this.isProvided;
    }

    @Nullable
    @Override
    public CommandSender get(CommandArgs arguments, List<? extends Annotation> modifiers) throws ArgumentException, ProvisionException {
        String name = arguments.next();
        CommandSender result;

        if (isProvided) return arguments.getNamespace().get(CommandSender.class);

        if (name.equalsIgnoreCase("console")) {
            result = Bukkit.getConsoleSender();
        } else result = Bukkit.getPlayer(name);
        if (result != null) {
            return result;
        } else {
            throw new ArgumentParseException("Could not find the specified player. NOTE: To reference the console, use 'console'.");
        }
    }

    @Override
    public List<String> getSuggestions(String prefix) {
        List<String> suggestions = Lists.newArrayList();
        suggestions.add("console");
        for (Player p : Bukkit.getOnlinePlayers()) {
            suggestions.add(p.getName());
        }
        return ImmutableList.copyOf(suggestions);
    }
}
