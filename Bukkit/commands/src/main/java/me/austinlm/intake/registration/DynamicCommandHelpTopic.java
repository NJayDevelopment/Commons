package me.austinlm.intake.registration;

import com.google.common.base.Joiner;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicFactory;

/**
 * Help topic that is dynamically created at runtime.
 *
 * @author molen
 */
public class DynamicCommandHelpTopic extends HelpTopic {
    private final DynamicBukkitCommand cmd;

    /**
     * Constructor
     *
     * @param cmd command o create the topic for.
     */
    public DynamicCommandHelpTopic(DynamicBukkitCommand cmd) {
        this.cmd = cmd;
        this.name = "/" + cmd.getName();

        StringBuilder fullText = new StringBuilder();
        this.shortText = cmd.getDescription();

        fullText.append(ChatColor.BOLD).append(ChatColor.GOLD).append("Description: ").append(ChatColor.WHITE);
        fullText.append(cmd.getDescription()).append("\n");
        fullText.append(ChatColor.BOLD).append(ChatColor.GOLD).append("Usage: ").append(ChatColor.WHITE);
        fullText.append(cmd.getUsage()).append("\n");

        if (cmd.getAliases().size() > 0) {
            fullText.append(ChatColor.BOLD).append(ChatColor.GOLD).append("Aliases: ").append(ChatColor.WHITE);
            fullText.append(Joiner.on(", ").join(cmd.getAliases())).append("\n");
        }

        this.fullText = fullText.toString();
    }

    /**
     * Determine if a player can see help information.
     *
     * @param player player to check permissions against.
     * @return if the player can see the help topic.
     */
    @Override
    public boolean canSee(CommandSender player) {
        if (cmd.getPermissions() != null && cmd.getPermissions().length > 0) {
            for (String perm : cmd.getPermissions()) {
                if (perm != null && player.hasPermission(perm)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    /**
     * Get the help string for this topic.
     *
     * @param forWho sender to send the message to.
     * @return the help string for this topic.
     */
    @Override
    public String getFullText(CommandSender forWho) {
        if (this.fullText == null || this.fullText.length() == 0) {
            return getShortText();
        } else {
            return this.fullText;
        }
    }

    /**
     * Topic factory.
     */
    public static class Factory implements HelpTopicFactory<DynamicBukkitCommand> {
        /**
         * Create a new help topic.
         *
         * @param command command to get help information of.
         * @return a new topic for the command.
         */
        @Override
        public HelpTopic createTopic(DynamicBukkitCommand command) {
            return new DynamicCommandHelpTopic(command);
        }
    }
}
