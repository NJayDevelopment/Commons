package net.njay.commons.countdowns;

import com.google.common.collect.Maps;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

/**
 * Manages a set of countdowns.
 *
 * @author Austin Mayes
 */
public class CountdownManager {
    HashMap<Countdown, CountdownRunnable> countdowns = Maps.newHashMap();
    Plugin owner;

    /**
     * Constructor
     *
     * @param owner Bukkit plugin that tasks should be scheduled with.
     */
    public CountdownManager(Plugin owner) {
        this.owner = owner;
    }

    /**
     * Add a countdown to the manager and start it.
     *
     * @param countdown countdown to be added.
     * @param time      time for the countdown to run.
     */
    public void addCountdown(Countdown countdown, int time) {
        addCountdown(countdown, time, true);
    }

    /**
     * Add a countdown to the manager.
     *
     * @param countdown countdown to be added.
     * @param time      time for the countdown to run.
     * @param startNow  if the countdown should start now.
     */
    public void addCountdown(Countdown countdown, int time, boolean startNow) {
        CountdownRunnable runnable = new CountdownRunnable(this.owner, countdown);
        if (startNow) runnable = runnable.start(time);
        this.countdowns.put(countdown, runnable);
    }

    /**
     * (Re)start a countdown.
     *
     * @param countdown countdown to work with.
     * @param time      time for the countdown to run.
     * @param isRestart if the countdown should be restarted. If this is false, and the countdown is running, nothing will happen.
     */
    public void start(Countdown countdown, int time, boolean isRestart) {
        if (isRestart || !countdown.isRunning()) this.countdowns.get(countdown).start(time);
    }

    /**
     * Cancel all countdowns of a certain class.
     *
     * @param clazz class of the countdown(s).
     */
    public void cancelAll(Class<? extends Countdown> clazz) {
        for (Countdown countdown : this.countdowns.keySet()) {
            if (countdown.getClass().equals(clazz)) {
                this.countdowns.get(countdown).cancel();
                this.countdowns.remove(countdown);
            }
        }
    }

    /**
     * Cancel all running countdowns.
     */
    public void cancelAll() {
        for (CountdownRunnable runnable : this.countdowns.values()) {
            runnable.cancel();
        }
        this.countdowns.clear();
    }

    public void cancel(Countdown countdown) {
        CountdownRunnable runnable = this.countdowns.remove(countdown);
        if (runnable != null) {
            runnable.cancel();
        }
    }
}
