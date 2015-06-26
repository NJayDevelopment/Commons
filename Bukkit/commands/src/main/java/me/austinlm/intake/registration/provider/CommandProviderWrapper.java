package main.java.me.austinlm.intake.registration.provider;

import com.sk89q.intake.Command;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Austin on 6/25/15.
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
