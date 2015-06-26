package main.java.me.austinlm.intake.registration.provider;

import com.google.common.collect.Lists;
import com.sk89q.intake.Command;
import com.sk89q.intake.parametric.Binding;
import com.sk89q.intake.parametric.IllegalParameterException;
import com.sk89q.intake.parametric.Injector;
import com.sk89q.intake.parametric.Key;
import com.sk89q.intake.parametric.annotation.Classifier;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class CommandProviderFactory {
    protected Injector injector;
    protected Method baseMethod;
    protected Command declaration;
    protected List<CommandArgumentProviderWrapper> providerWrappers = Lists.newArrayList();

    public CommandProviderFactory(Injector injector, Method baseMethod, Command declaration) {
        this.injector = injector;
        this.baseMethod = baseMethod;
        this.declaration = declaration;
    }

    private static String getFriendlyName(Type type, Annotation classifier, int index) {
        if (classifier != null) {
            return classifier.annotationType().getSimpleName().toLowerCase();
        } else {
            return type instanceof Class<?> ? ((Class<?>) type).getSimpleName().toLowerCase() : "unknown" + index;
        }
    }

    public CommandProviderWrapper buildWarappers() {
        Annotation[][] annotations = baseMethod.getParameterAnnotations();
        for (int i = 0; i < this.baseMethod.getParameters().length; i++) {
            CommandArgumentProviderWrapper wrapper = addParameter(this.baseMethod.getParameters()[i].getType(), Arrays.asList(annotations[i]), this.injector);
            if (wrapper != null) this.providerWrappers.add(wrapper);
        }
        return new CommandProviderWrapper(this.baseMethod, this.declaration, this.providerWrappers);
    }

    private CommandArgumentProviderWrapper addParameter(Type type, List<? extends Annotation> annotations, Injector injector) {
        Annotation classifier = null;

        for (Annotation annotation : annotations) {
            if (annotation.annotationType().getAnnotation(Classifier.class) != null) {
                classifier = annotation;
            }
        }

        System.out.println(classifier);
        Key<?> key = Key.get(type, classifier != null ? classifier.annotationType() : null);
        Binding<?> binding = injector.getBinding(key);
        if (binding == null) {
            throw new IllegalParameterException("Can't find a binding for the parameter type '" + type + "'");
        }

        if (binding.getProvider().isProvided()) return null;
        return new CommandArgumentProviderWrapper(type, binding);
    }
}
