package net.njay.commons.ui.actionbar;

import net.njay.commons.chat.JSONChatMessageBuilder;
import net.njay.commons.nms.NMSUtils;
import org.bukkit.ChatColor;

/**
 * Class to represent an action bar.
 *
 * @author Austin Mayes
 */
public class UIActionBar {
    private Object baseComponent;
    private ActionBarManager manager;

    /**
     * Constructor
     *
     * @param baseComponent to display.
     */
    public UIActionBar(Object baseComponent) {
        this.baseComponent = baseComponent;
    }

    /**
     * Constructor
     *
     * @param json raw json to be converted for display.
     */
    public UIActionBar(String json) {
        try {
            this.baseComponent = JSONChatMessageBuilder.deserialize(json).getBaseComponent();
        } catch (Exception e) {
            manager.getService().log(e);
        }
    }

    /**
     * Constructor
     *
     * @param text  to display.
     * @param color color of the text; if null, white.
     */
    public UIActionBar(String text, ChatColor color) {
        JSONChatMessageBuilder builder = new JSONChatMessageBuilder(text).color(color == null ? ChatColor.WHITE : color);
        try {
            this.baseComponent = builder.getBaseComponent();
        } catch (Exception e) {
            manager.getService().log(e);
        }
    }

    /**
     * Get the formatted base component.
     *
     * @return
     */
    public Object getBaseComponent() {
        return baseComponent;
    }

    /**
     * Get the packet that will display the bar.
     */
    public Object getPacket() {
        try {
            Class<?> PacketPlayOutChat = NMSUtils.getNMSClass("PacketPlayOutChat");
            Object packet = null;
            packet = PacketPlayOutChat.getConstructor(new Class<?>[]{NMSUtils.getNMSClass("IChatBaseComponent"), byte.class}).newInstance(this.getBaseComponent(), (byte) 2);
            return packet;
        } catch (Exception e) {
            manager.getService().log(e);
        }
        return null;
    }

    public ActionBarManager getManager() {
        return manager;
    }

    public void setManager(ActionBarManager manager) {
        this.manager = manager;
    }
}
