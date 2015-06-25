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
 * Provider for a {@link org.bukkit.entity.Player}.
 *
 * @author Austin Mayes
 */
public class PlayerProvider implements Provider<Player> {

    private boolean isProvided;

    public PlayerProvider(boolean isProvided) {
        this.isProvided = isProvided;
    }

    @Override
    public boolean isProvided() {
        return this.isProvided;
    }

    @Nullable
    @Override
    public Player get(CommandArgs arguments, List<? extends Annotation> modifiers) throws ArgumentException, ProvisionException {
        if (isProvided) {
            try {
                return (Player) arguments.getNamespace().get(CommandSender.class);
            } catch (ClassCastException e) {
                e.printStackTrace();
                throw new ProvisionException("This isn't a player");
            }
        }

        String name = arguments.next();
        Player result;

        result = Bukkit.getPlayer(name);
        if (result != null) {
            return result;
        } else {
            throw new ArgumentParseException("Could not find the specified player. NOTE: To reference the console, use 'console'.");
        }
    }

    @Override
    public List<String> getSuggestions(String prefix) {
        List<String> suggestions = Lists.newArrayList();
        for (Player p : Bukkit.getOnlinePlayers()) {
            suggestions.add(p.getName());
        }
        return ImmutableList.copyOf(suggestions);
    }
}
