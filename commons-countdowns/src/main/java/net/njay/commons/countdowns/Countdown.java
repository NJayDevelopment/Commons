package net.njay.commons.countdowns;

/**
 * Base class for countdowns.
 *
 * @author Austin Mayes
 */
public abstract class Countdown {

    private boolean running = false;

    /**
     * Called when the countdown is started.
     * Implementers should be aware that this is also called on restart.
     *
     * @param time time the countdown will last.
     */
    public void onStart(int time) {
    }

    /**
     * Called when the countdown ends normally.
     * This is not called during a restart.
     */
    public abstract void onEnd();

    /**
     * Called every second.
     *
     * @param left time left (in seconds) until the countdown ends.
     */
    public void onTick(int left) {
    }

    /**
     * Called when the countdown is canceled.
     * This is not called on restart.
     */
    public abstract void onCancel();

    /**
     * If the countdown should be displayed.
     */
    public boolean shouldDisplay() {
        return true;
    }

    /**
     * If the countdown is running.
     */
    public boolean isRunning() {
        return this.running;
    }

    /**
     * Set if the countdown is currently running.
     *
     * @param running if the countdown is running.
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * Take any measures to display the countdown to users.
     * Implementers need to check {@link #shouldDisplay}
     *
     * @param left time left.
     * @param total total time the countdown will run.
     */
    public void display(int left, int total) {
    }
}
