package main.java.me.austinlm.intake;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.sk89q.intake.CommandException;
import com.sk89q.intake.InvalidUsageException;
import com.sk89q.intake.InvocationCommandException;
import com.sk89q.intake.argument.Namespace;
import com.sk89q.intake.dispatcher.Dispatcher;
import com.sk89q.intake.util.auth.AuthorizationException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;

/**
 * Misc. Utilities
 *
 * @author molen
 */
public class CommandUtils {

    /**
     * Handle all intake exceptions
     *
     * @param sender sender of the command.
     * @param e      exception from intake.
     */
    public static void handleException(CommandSender sender, Throwable e) {
        if (e instanceof InvocationCommandException) {
            handleException(sender, e.getCause());
            e.printStackTrace();
        } else if (e instanceof NumberFormatException) {
            sender.sendMessage(ChatColor.RED + "Number expected, string received instead.");
        } else if (e instanceof IllegalArgumentException) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            e.printStackTrace();
        } else if (e instanceof NullPointerException && e.getStackTrace()[0].getClassName().equals(Preconditions.class.getName())) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
        } else if (e instanceof AuthorizationException) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
        } else if (e instanceof InvalidUsageException) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            sender.sendMessage(ChatColor.RED + ((InvalidUsageException) e).getSimpleUsageString("/"));
            e.printStackTrace();
        } else if (e instanceof CommandException) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
        } else {
            if (e.getCause() == null) {
                sender.sendMessage(ChatColor.RED + "An error has occurred, see console.");
                e.printStackTrace();
            } else {
                handleException(sender, e.getCause());
            }
        }
    }

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
            handleException(sender, e);
        }
    }
}
