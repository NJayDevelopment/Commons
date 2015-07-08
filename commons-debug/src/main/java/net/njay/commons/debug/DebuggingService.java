package net.njay.commons.debug;

import com.google.common.collect.Maps;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A utility class to make debugging cleaner.
 *
 * @author Nick (Original Class)
 * @author Austin Mayes (Revamp)
 */
public class DebuggingService {

    private LinkedHashMap<FilterMode, List<Class<?>>> filters = Maps.newLinkedHashMap();
    private Logger logger;
    
    /**
     * Constructor
     *
     * @param logger to log to.
     */
    public DebuggingService(Logger logger) {
        this.logger = logger;
    }

    /**
     * Add a flileter to the service.
     *
     * @param mode    of the filter.
     * @param classes to be effected by the filter.
     */
    public void addFilter(FilterMode mode, List<Class<?>> classes) {
        this.filters.put(mode, classes);
    }

    /**
     * Log to the logger
     *
     * @param e to log.
     */
    public void log(Exception e) {
        Validate.notNull(e, "e cannot be null");
        log(LogLevel.WARNING, ExceptionUtils.getFullStackTrace(e));
    }

    /**
     * Log to the logger
     *
     * @param level  of the error.
     * @param string to log.
     */
    public void log(LogLevel level, String string) {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        String className = elements[elements.length - 1].getClassName();
        if (this.filters != null) {
            boolean foundMatch = (this.filters.containsKey(FilterMode.BLACKLIST));
            for (Class<?> clazz : this.filters.get(FilterMode.BLACKLIST))
                if (clazz.getName().equals(className)) {
                    foundMatch = !foundMatch;
                    break;
                }
            if (!foundMatch) return;
        }
        this.logger.log(Level.ALL, "[" + level.name() + "]" + string);
    }

    /**
     * Represents the modes of a filter.
     * WHITELIST: Passes the filter and can be logged.
     * BLACKLIST: Blocks the classes from being logged.
     */
    public enum FilterMode {
        WHITELIST, BLACKLIST
    }

    /**
     * Enum to represent a log level.
     */
    public enum LogLevel {
        INFO(0), WARNING(1), SEVERE(2);

        int level;

        LogLevel(int level) {
            this.level = level;
        }

        public int getLevel() {
            return this.level;
        }
    }
}