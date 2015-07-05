package net.njay.commons.nms;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtils {
    /**
     * Get a field from a class.
     *
     * @param cl         class to get the field from.
     * @param field_name name of the field.
     * @return the field.
     */
    public static Field getField(Class<?> cl, String field_name) {
        try {
            Field field = cl.getDeclaredField(field_name);
            return field;
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get a method from a class with the matching parameter types.
     *
     * @param cl     class to get the method from.
     * @param method name of the method.
     * @param args   array of classes that the method accepts as parameters (in order).
     * @return the method.
     */
    public static Method getMethod(Class<?> cl, String method, Class<?>[] args) {
        for (Method m : cl.getMethods()) {
            if (m.getName().equals(method) && classListEqual(args, m.getParameterTypes())) {
                return m;
            }
        }
        return null;
    }

    /**
     * Get a method from a class with the matching parameter types.
     *
     * @param cl     class to get the method from.
     * @param method name of the method.
     * @param args   array of classes that the method accepts as parameters (in order).
     * @return the method.
     */
    public static Method getMethod_(Class<?> cl, String method, Class... args) {
        for (Method m : cl.getMethods()) {
            if (m.getName().equals(method) && classListEqual(args, m.getParameterTypes())) {
                return m;
            }
        }
        return null;
    }

    /**
     * Get a method from a class with the specified number of arguments.
     *
     * @param cl     class to get the method from.
     * @param method name of the method.
     * @param args   number of arguments the method has.
     * @return the method.
     */
    public static Method getMethod(Class<?> cl, String method, Integer args) {
        for (Method m : cl.getMethods()) {
            if (m.getName().equals(method) && args.equals(new Integer(m.getParameterTypes().length))) {
                return m;
            }
        }
        return null;
    }

    /**
     * Get a method from a class.
     *
     * @param cl     class to get the method from.
     * @param method name of the method.
     * @return the method.
     */
    public static Method getMethod(Class<?> cl, String method) {
        for (Method m : cl.getMethods()) {
            if (m.getName().equals(method)) {
                return m;
            }
        }
        return null;
    }

    /**
     * Used to check if a list of classes is equal.
     *
     * @param l1 list #1
     * @param l2 list #2
     * @return if the lists have the same values.
     */
    public static boolean classListEqual(Class<?>[] l1, Class<?>[] l2) {
        boolean equal = true;

        if (l1.length != l2.length)
            return false;
        for (int i = 0; i < l1.length; i++) {
            if (l1[i] != l2[i]) {
                equal = false;
                break;
            }
        }

        return equal;
    }
}
