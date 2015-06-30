package me.austinlm.intake.registration;

import com.sk89q.intake.CommandMapping;
import me.austinlm.intake.registration.provider.CommandProviderWrapper;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;

/**
 * Service to register intake commands with Bukkit
 *
 * @author Austin Mayes
 * @author molen
 */
public class BukkitCommandRegistrationService {
    private Plugin owner;
    private CommandRegistrationService registrationService;

    static {
        Bukkit.getServer().getHelpMap().registerHelpTopicFactory(DynamicBukkitCommand.class, new DynamicCommandHelpTopic.Factory());
    }

    /**
     * Create the service.
     *
     * @param owner               plugin that should own the commands.
     * @param registrationService CommandRegistrationService to pull information from.
     */
    public BukkitCommandRegistrationService(Plugin owner, CommandRegistrationService registrationService) {
        this.owner = owner;
        this.registrationService = registrationService;
    }

    /**
     * Register commands with the Bukkit command map.
     */
    public void register() {
        CommandMap map = getCommandMap();

        for (CommandMapping command : this.registrationService.getDispatcher().getCommands()) {
            CommandProviderWrapper wrapper = this.registrationService.getWrapperForCommand(command.getAllAliases()[0]);
            DynamicBukkitCommand cmd = new DynamicBukkitCommand(command.getAllAliases(),
                    command.getDescription().getShortDescription(), "/" + command.getAllAliases()[0] + " " + command.getDescription().getUsage(), owner, wrapper);
            cmd.setPermissions(command.getDescription().getPermissions().toArray(new String[1]));
            map.register(owner.getDescription().getName(), cmd);
        }
    }

    /**
     * Get the Bukkit command map.
     *
     * @return the Bukkit command map.
     */
    private CommandMap getCommandMap() {
        CommandMap commandMap = getField(Bukkit.getServer().getPluginManager(), "commandMap");
        if (commandMap == null)
            throw new RuntimeException();
        return commandMap;
    }

    /**
     * Get a Field from a class.
     *
     * @param from object's class to get the field from.
     * @param name name of the field.
     * @param <T>  type that field.get() should return
     * @return the value of the field.
     */
    @SuppressWarnings("unchecked")
    public <T> T getField(Object from, String name) {
        for (Class<?> checkClass = from.getClass(); checkClass != null; checkClass = checkClass.getSuperclass()) {
            try {
                Field field = checkClass.getDeclaredField(name);
                field.setAccessible(true);
                return (T) field.get(from);
            } catch (Exception e) {
                return null; // Silence
            }
        }
        return null;
    }
}
