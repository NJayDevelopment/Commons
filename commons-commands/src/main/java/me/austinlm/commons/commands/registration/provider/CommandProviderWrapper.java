package me.austinlm.commons.commands.registration.provider;

import com.sk89q.intake.Command;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Object that is used to represent a command's method, annotation, and arguments.
 *
 * @author Austin Mayes
 */

public class CommandProviderWrapper {
    protected Method baseMethod;
    protected Command declaration;
    protected List<CommandArgumentProviderWrapper> wrappers;

    public CommandProviderWrapper(Method baseMethod, Command declaration, List<CommandArgumentProviderWrapper> wrappers) {
        this.baseMethod = baseMethod;
        this.declaration = declaration;
        this.wrappers = wrappers;
    }

    public Method getBaseMethod() {
        return baseMethod;
    }

    public Command getDeclaration() {
        return declaration;
    }

    public List<CommandArgumentProviderWrapper> getWrappers() {
        return wrappers;
    }
}
