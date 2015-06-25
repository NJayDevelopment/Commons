package me.austinlm.bukkitintake.registration.provider;

import com.sk89q.intake.parametric.Binding;


/**
 * Created by Austin on 6/25/15.
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
