package net.njay.commons.commands.registration.provider;

import com.sk89q.intake.parametric.Binding;


/**
 * Object that is used to group command arguments with their respective binders.
 *
 * @author Austin Mayes
 */
public class CommandArgumentProviderWrapper {
    protected Object argument;
    protected Binding binding;

    public CommandArgumentProviderWrapper(Object argument, Binding binding) {
        this.argument = argument;
        this.binding = binding;
    }

    public Object getArgument() {
        return argument;
    }

    public Binding getBinding() {
        return binding;
    }
}
