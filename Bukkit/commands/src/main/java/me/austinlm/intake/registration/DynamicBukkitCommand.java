package me.austinlm.intake.registration;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.sun.jdi.InvalidTypeException;
import me.austinlm.intake.registration.provider.CommandArgumentProviderWrapper;
import me.austinlm.intake.registration.provider.CommandProviderWrapper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

/**
 * An implementation of a Bukkit command that is dynamically registered at runtime.
 *
 * @author Austin Mayes
 */
public class DynamicBukkitCommand extends Command implements PluginIdentifiableCommand {
    protected final Plugin owningPlugin;
    protected String[] permissions = new String[0];
    protected CommandProviderWrapper wrapper;

    /**
     * Constructor
     *
     * @param aliases all possible aliases for the command.
     * @param desc    description of the command.
     * @param usage   usage of the command.
     * @param plugin  plugin that owns the command.
     * @param wrapper wrapper that is associated with the command.
     */
    public DynamicBukkitCommand(String[] aliases, String desc, String usage, Plugin plugin, CommandProviderWrapper wrapper) {
        super(aliases[0], desc, usage, Arrays.asList(aliases));
        this.owningPlugin = plugin;
        this.wrapper = wrapper;
    }

    /**
     * Pass commands from bukkit to the owning plugin.
     */
    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        return owningPlugin.onCommand(sender, this, label, args);
    }

    /**
     * Dynamic tab completer associated with the {@link com.sk89q.intake.parametric.Provider#getSuggestions} results.
     */
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (this.wrapper.getWrappers().isEmpty()) return ImmutableList.of();
        String lastWord = args[args.length - 1];
        CommandArgumentProviderWrapper currentArg = this.wrapper.getWrappers().get(args.length - 1);
        List<?> results = currentArg.getBinding().getProvider().getSuggestions(lastWord);
        if (!(results.get(0) instanceof String)) throw new IllegalArgumentException("Suggestions must be a list of strings.");
        return (List<String>)results;
    }

    /**
     * @return permissions required to execute the command.
     */
    public String[] getPermissions() {
        return permissions;
    }

    /**
     * Set the permissions required to execute the command.
     *
     * @param permissions string, by permission node, of permissions that are required to execute the command.
     */
    public void setPermissions(String[] permissions) {
        this.permissions = permissions;
        if (permissions != null) {
            super.setPermission(Joiner.on(";").useForNull("").join(permissions));
        }
    }

    /**
     * @return the plugin that owns the command.
     */
    @Override
    public Plugin getPlugin() {
        return owningPlugin;
    }
}
