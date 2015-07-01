package net.njay.commons.ui.bossbar;

/**
 * @author Austin Mayes
 */
public class UIBossBar {
    private float percent;
    private String text;

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
