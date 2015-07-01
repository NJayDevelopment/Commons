package net.njay.commons.ui.bossbar;

/**
 * Representation of the bar that is displayed on screen.
 *
 * @author Austin Mayes
 */
public class UIBossBar {
    private float percent;
    private String text;

    /**
     * Constructor
     *
     * @param percent 1-100 value to be displayed in bar meter.
     * @param text    text to be displayed above the bar.
     */
    public UIBossBar(float percent, String text) {
        this.percent = percent;
        this.text = text;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
