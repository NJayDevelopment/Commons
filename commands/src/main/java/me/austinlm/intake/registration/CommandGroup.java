package me.austinlm.intake.registration;

import java.util.List;

/**
 * Group of objects that have commands in them.
 *
 * @author Austin Mayes
 */
public class CommandGroup {
    protected List<Object> members;
    protected String name;
    protected List<CommandGroup> children;

    /**
     * Constructor
     *
     * @param members  members of the group.
     * @param name     name of the group.
     * @param children any children of the group
     */
    public CommandGroup(List<Object> members, String name, List<CommandGroup> children) {
        this.members = members;
        this.name = name;
        this.children = children;
    }

    /**
     * @return the members of the group.
     */
    public List<Object> getMembers() {
        return members;
    }

    /**
     * @return the name of the group.
     */
    public String getName() {
        return name;
    }

    /**
     * @return children of the group.
     */
    public List<CommandGroup> getChildren() {
        return children;
    }
}
