package me.austinlm.commons.countdowns;

/**
 * Base class for countdowns.
 *
 * @author Austin Mayes
 */
public abstract class Countdown {

    public void onStart(int time) {
    }

    public abstract void onEnd();

    public void onTick(int elapsed) {
    }

    public abstract void onCancel();

    public abstract boolean shouldDisplay();

    public void display() {
    }
}
