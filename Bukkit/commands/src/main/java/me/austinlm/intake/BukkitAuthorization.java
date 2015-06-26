package main.java.me.austinlm.intake;

import com.sk89q.intake.argument.Namespace;
import com.sk89q.intake.util.auth.Authorizer;
import org.bukkit.command.CommandSender;

/**
 * Authorizer that check bukkit permissions.
 *
 * @author Austin Mayes
 */
public class BukkitAuthorization implements Authorizer {

    /**
     * Check if a sender has the correct permission.
     *
     * @param namespace  namespace that the sender is in.
     * @param permission permission to check for.
     * @return if a sender has the correct permission.
     */
    @Override
    public boolean testPermission(Namespace namespace, String permission) {
        return namespace.get(CommandSender.class).hasPermission(permission);
    }
}
