package net.njay.commons.ui.actionbar;

import com.google.common.collect.Lists;
import net.njay.commons.countdowns.Countdown;
import net.njay.commons.countdowns.CountdownManager;
import net.njay.commons.nms.PacketUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * Class to manage all of the action bars being displayed by a plugin.
 *
 * @author Austin Mayes
 */
public class ActionBarManager {
    private List<PacketRepeater> active = Lists.newArrayList();
    private JavaPlugin owner;
    private CountdownManager countdownManager;

    /**
     * Constructor
     *
     * @param owner plugin that runs the bars.
     */
    public ActionBarManager(JavaPlugin owner) {
        this.owner = owner;
        this.countdownManager = new CountdownManager(owner);
    }

    /**
     * Permanently display a bar to all of the players on the server.
     *
     * @param bar to display.
     */
    public void displayPermanent(UIActionBar bar) {
        for (Player player : owner.getServer().getOnlinePlayers()) {
            displayPermanent(bar, player);
        }
    }

    /**
     * Permanently display a bar to a player.
     *
     * @param bar    to display.
     * @param player to display to.
     */
    public void displayPermanent(UIActionBar bar, Player player) {
        PacketRepeater repeater = new PacketRepeater(player, bar);
        this.countdownManager.addCountdown(repeater, Integer.MAX_VALUE);
    }

    /**
     * Hide all of the bars.
     */
    public void hide() {
        for (PacketRepeater repeater : this.active) {
            repeater.onEnd();
        }
    }

    /**
     * Hide a bar for all players.
     *
     * @param bar to hide.
     */
    public void hide(UIActionBar bar) {
        for (PacketRepeater repeater : this.active) {
            if (repeater.getBar().equals(bar)) repeater.onEnd();
        }
    }

    /**
     * Hide a bar for a player.
     *
     * @param bar    to hide.
     * @param player to hide for.
     */
    public void hide(UIActionBar bar, Player player) {
        for (PacketRepeater repeater : this.active) {
            if (repeater.getBar().equals(bar) && repeater.getPlayer().equals(player)) repeater.onEnd();
        }
    }

    /**
     * Hide all of a player's bars.
     *
     * @param player to hide for.
     */
    public void hide(Player player) {
        for (PacketRepeater repeater : this.active) {
            if (repeater.getPlayer().equals(player)) repeater.onEnd();
        }
    }

    /**
     * Display a bar to all players.
     *
     * @param bar     to display.
     * @param seconds how long to display.
     */
    public void display(UIActionBar bar, int seconds) {
        for (Player player : owner.getServer().getOnlinePlayers()) {
            display(bar, player, seconds);
        }
    }

    /**
     * Display a bar to a player.
     *
     * @param bar     to display.
     * @param player  to display to.
     * @param seconds how long to display.
     */
    public void display(final UIActionBar bar, final Player player, int seconds) {
        Countdown displayCountdown = new PacketRepeater(player, bar);
        this.countdownManager.addCountdown(displayCountdown, seconds);
    }

    /**
     * Display a bar to all players.
     *
     * @param bar to display.
     */
    public void display(UIActionBar bar) {
        for (Player player : owner.getServer().getOnlinePlayers()) {
            display(bar, player);
        }
    }

    /**
     * Display a bar to a player.
     *
     * @param bar    to display.
     * @param player to display to.
     */
    public void display(UIActionBar bar, Player player) {
        PacketUtils.sendPacket(player, bar.getPacket());
    }

    public JavaPlugin getOwner() {
        return owner;
    }

    /**
     * Class to handle the repeated sending of packets.
     */
    private class PacketRepeater extends Countdown {
        private Player player;
        private UIActionBar bar;

        /**
         * Constructor
         *
         * @param player to send packets to.
         * @param bar    to display.
         */
        public PacketRepeater(Player player, UIActionBar bar) {
            this.player = player;
            this.bar = bar;
        }

        @Override
        public void onStart(int time) {
            active.add(this);
        }

        @Override
        public void onEnd() {
            active.remove(this);
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void display(int left, int total) {
            PacketUtils.sendPacket(player, bar.getPacket());
        }

        public Player getPlayer() {
            return player;
        }

        public UIActionBar getBar() {
            return bar;
        }
    }
}
