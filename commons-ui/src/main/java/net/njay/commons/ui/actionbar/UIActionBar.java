package net.njay.commons.ui.actionbar;

import net.njay.commons.chat.JSONChatMessageBuilder;
import net.njay.commons.nms.NMSUtils;
import org.bukkit.ChatColor;

import java.lang.reflect.InvocationTargetException;

/**
 * Class to represent an action bar.
 *
 * @author Austin Mayes
 */
public class UIActionBar {
    private Object baseComponent;

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
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
        Class<?> PacketPlayOutChat = NMSUtils.getNMSClass("PacketPlayOutChat");
        Object packet = null;

        try {
            packet = PacketPlayOutChat.getConstructor(new Class<?>[]{NMSUtils.getNMSClass("IChatBaseComponent"), byte.class}).newInstance(this.getBaseComponent(), (byte) 2);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return packet;
    }
}
