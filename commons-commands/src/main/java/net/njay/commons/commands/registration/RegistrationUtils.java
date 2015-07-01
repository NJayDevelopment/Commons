package net.njay.commons.commands.registration;

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