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
