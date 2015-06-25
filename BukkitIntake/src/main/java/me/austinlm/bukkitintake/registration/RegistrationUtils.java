package me.austinlm.bukkitintake.registration;

import com.google.common.base.Joiner;
import com.sk89q.intake.argument.Namespace;
import com.sk89q.intake.dispatcher.Dispatcher;
import me.austinlm.bukkitintake.CommandUtils;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class RegistrationUtils {

    /**
     * Pass commands from bukkit to intake.
     *
     * @param commands dispatcher to pass the command to.
     * @param sender   sender of the command.
     * @param cmd      wrapped Command that the sender sent.
     * @param args     arguments attached to the command.
     */
    public void passToFramework(Dispatcher commands, CommandSender sender, org.bukkit.command.Command cmd, String[] args) {
        try {
            Namespace namespace = new Namespace();
            namespace.put(CommandSender.class, sender); //save sender
            commands.call(cmd.getName() + " " + Joiner.on(" ").join(args), namespace, Collections.<String>emptyList());
        } catch (Exception e) {
            CommandUtils.handleException(sender, e);
        }
    }

    /**
     * Construct a CommandGroup with the specified parameters.
     *
     * @param members  members of the group
     * @param name     name of the group.
     * @param children any children that the group may have.
     * @return A constructed group
     */
    public CommandGroup construct(List<Object> members, String name, List<CommandGroup> children) {
        return new CommandGroup(members, name, children);
    }
}
