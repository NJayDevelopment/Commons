package net.njay.commons.countdowns;

import com.google.common.base.Preconditions;
import org.bukkit.plugin.Plugin;

/**
 * Bukkit runnable for countdowns.
 *
 * @author Austin Mayes
 */
public class CountdownRunnable implements Runnable {
    protected Countdown countdown;
    protected int taskId = 0;
    protected int timeLeft = 0;
    protected int totalTime;
    protected Plugin plugin;

    /**
     * Constructor
     *
     * @param plugin    plugin that will be scheduled with.
     * @param countdown countdown associated with this runnable.
     */
    public CountdownRunnable(Plugin plugin, Countdown countdown) {
        Preconditions.checkNotNull(plugin, "plugin can't be null");
        Preconditions.checkNotNull(countdown, "countdown can't be null");

        this.countdown = countdown;
        this.plugin = plugin;
    }

    /**
     * Cancel the runnable and the countdown, calling onCancel.
     */
    public void cancel() {
        this.plugin.getServer().getScheduler().cancelTask(taskId);
        this.countdown.onCancel();
        this.countdown.setRunning(false);
    }

    /**
     * Called on every tick.
     */
    public void run() {
        if (this.timeLeft <= 0) {
            this.countdown.onEnd();
            this.countdown.setRunning(false);
            stop();
        } else {
            this.countdown.onTick(this.timeLeft);
            this.timeLeft--;
        }
    }

    /**
     * Re(start) the runnable/countdown.
     *
     * @param time time to run for,
     * @return the updated runnable.
     */
    public CountdownRunnable start(int time) {
        Preconditions.checkArgument(time >= 1, "time must be 1 or above");

        this.totalTime = time;
        this.timeLeft = time;
        if (this.taskId != 0) {
            restart(time);
        } else {
            this.taskId = this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, this, 0L, 20L);
            this.countdown.onStart(time);
            this.countdown.setRunning(true);
        }
        return this;
    }

    /**
     * Restart the countdown/runnable.
     *
     * @param time new countdown time.
     */
    public void restart(int time) {
        this.stop();
        this.countdown.setRunning(false);
        this.taskId = this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, this, 0L, 20L);
        this.countdown.onStart(time);
        this.countdown.setRunning(true);
    }

    /**
     * Stop the bukkit task.
     */
    protected void stop() {
        plugin.getServer().getScheduler().cancelTask(taskId);
    }

    /**
     * @return the countdown
     */
    public Countdown getCountdown() {
        return countdown;
    }

    /**
     * @return the task id of the runnable (0 if no task is running).
     */
    public int getTaskId() {
        return taskId;
    }

    /**
     * @return time left (in seconds).
     */
    public int getTimeLeft() {
        return timeLeft;
    }

    /**
     * @return total time the countdown should run.
     */
    public int getTotalTime() {
        return totalTime;
    }

    /**
     * @return the plugin that is running the task.
     */
    public Plugin getPlugin() {
        return plugin;
    }
}