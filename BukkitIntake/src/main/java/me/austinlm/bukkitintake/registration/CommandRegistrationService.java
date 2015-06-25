package me.austinlm.bukkitintake.registration;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sk89q.intake.Command;
import com.sk89q.intake.Intake;
import com.sk89q.intake.dispatcher.Dispatcher;
import com.sk89q.intake.fluent.CommandGraph;
import com.sk89q.intake.fluent.DispatcherNode;
import com.sk89q.intake.parametric.Injector;
import com.sk89q.intake.parametric.Module;
import com.sk89q.intake.parametric.ParametricBuilder;
import com.sk89q.intake.util.auth.Authorizer;
import me.austinlm.bukkitintake.registration.provider.CommandProviderFactory;
import me.austinlm.bukkitintake.registration.provider.CommandProviderWrapper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service to register parametric commands with Intake.
 *
 * @author Austin Mayes
 */
public class CommandRegistrationService {
    private CommandGroup defaultGroup;
    private List<CommandGroup> otherGroups;
    private List<Module> modulesToInstall;
    private Authorizer authorizer;

    private Injector injector;
    private CommandGraph graph;
    private Dispatcher dispatcher;
    private ParametricBuilder builder;
    private HashMap<Class, List<CommandProviderWrapper>> providersByClass = Maps.newHashMap();

    /**
     * Constructor
     *
     * @param defaultGroup     default CommandGroup
     * @param otherGroups      any sub CommandGroups
     * @param modulesToInstall modules to install into the injector.
     * @param authorizer       authorizer to use.
     */
    public CommandRegistrationService(CommandGroup defaultGroup, List<CommandGroup> otherGroups, List<Module> modulesToInstall, Authorizer authorizer) {
        this.defaultGroup = defaultGroup;
        this.otherGroups = otherGroups;
        this.modulesToInstall = modulesToInstall;
        this.authorizer = authorizer;

        this.injector = Intake.createInjector();
        this.builder = new ParametricBuilder(injector);
        this.graph = new CommandGraph();
        this.dispatcher = this.graph.getDispatcher();
    }

    /**
     * Register commands with Intake.
     *
     * @return the service with the completed registration state.
     */
    public CommandRegistrationService register() {
        this.registerAuth();
        this.registerBindings();
        this.registerWithDispatch();
        this.createProviders();

        return this;
    }

    /**
     * Create provider wrappers for each command.
     */
    private void createProviders() {
        createProvidersForGroup(this.defaultGroup);

        for (CommandGroup group : this.otherGroups) {
            createProvidersForGroup(group);
        }
    }

    /**
     * Create command providers for the classes (and children classes) of a group.
     *
     * @param group group to register.
     */
    private void createProvidersForGroup(CommandGroup group) {
        for (Object o : group.getMembers()) {
            List<CommandProviderWrapper> wrappersForClass = Lists.newArrayList();
            for (Method m : o.getClass().getMethods()) {
                if (m.getAnnotation(Command.class) == null) continue;
                CommandProviderFactory factory = new CommandProviderFactory(this.injector, m, m.getAnnotation(Command.class));
                wrappersForClass.add(factory.buildWarappers());
            }
            this.providersByClass.put(o.getClass(), wrappersForClass);
        }

        if (group.getChildren() != null) {
            for (CommandGroup group1 : group.getChildren()) {
                createProvidersForGroup(group1);
            }
        }
    }

    /**
     * Install the modules into the injector.
     */
    private void registerBindings() {
        for (Module b : this.modulesToInstall) {
            this.injector.install(b);
        }
    }

    /**
     * Register the authorizer.
     */
    private void registerAuth() {
        this.builder.setAuthorizer(this.authorizer);
    }

    /**
     * Register commands with intake dispatcher.
     */
    private void registerWithDispatch() {
        this.graph.builder(this.builder);

        DispatcherNode mainNode = this.graph.commands();
        for (Object o : this.defaultGroup.getMembers()) {
            mainNode.registerMethods(o);
        }

        for (CommandGroup commandGroup : this.otherGroups) {
            registerGroup(mainNode, commandGroup);
        }
    }

    /**
     * Register a group as a dispatcher node.
     *
     * @param parent     parent dispatcher node.
     * @param toRegister group to register.
     */
    private void registerGroup(DispatcherNode parent, CommandGroup toRegister) {
        DispatcherNode groupNode = parent.group(toRegister.getName());
        for (Object o : toRegister.getMembers()) {
            groupNode.registerMethods(o);
        }

        if (toRegister.getChildren() != null) {
            for (CommandGroup child : toRegister.getChildren()) {
                registerGroup(groupNode, child);
            }
        }
    }

    /**
     * Get a command wrapper from an alias.
     *
     * @param alias alias of the command.
     * @return the wrapper attached to the alias.
     */
    public CommandProviderWrapper getWrapperForCommand(String alias) {
        CommandProviderWrapper result = null;
        for (Map.Entry<Class, List<CommandProviderWrapper>> entry : this.providersByClass.entrySet()) {
            List<CommandProviderWrapper> wrappers = entry.getValue();
            for (CommandProviderWrapper wrapper : wrappers) {
                if (wrapper.getDeclaration().aliases()[0].equalsIgnoreCase(alias)) result = wrapper;
            }
        }
        return result;
    }

    /**
     * @return the default command group.
     */
    public CommandGroup getDefaultGroup() {
        return defaultGroup;
    }

    /**
     * @return the sub-groups.
     */
    public List<CommandGroup> getOtherGroups() {
        return otherGroups;
    }

    /**
     * @return the modules that are being installed to the injector.
     */
    public List<Module> getModulesToInstall() {
        return modulesToInstall;
    }

    /**
     * @return the authorizer to register.
     */
    public Authorizer getAuthorizer() {
        return authorizer;
    }

    /**
     * @return the injector
     */
    public Injector getInjector() {
        return injector;
    }

    /**
     * @return the command graph.
     */
    public CommandGraph getGraph() {
        return graph;
    }

    /**
     * @return the command dispatcher.
     */
    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    /**
     * @return the parametric builder for all commands.
     */
    public ParametricBuilder getBuilder() {
        return builder;
    }

    /**
     * @return a map of providers grouped by class.
     */
    public HashMap<Class, List<CommandProviderWrapper>> getProvidersByClass() {
        return providersByClass;
    }
}
