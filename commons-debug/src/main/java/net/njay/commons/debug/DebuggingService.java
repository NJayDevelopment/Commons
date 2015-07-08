package net.njay.commons.debug;

import com.google.common.collect.Lists;
import net.njay.commons.debug.filter.ClassFilter;
import net.njay.commons.debug.filter.Filter;
import net.njay.commons.debug.filter.MethodFilter;
import net.njay.commons.debug.filter.NoneFilter;
import net.njay.commons.nms.ReflectionUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.lang.reflect.Method;
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

    private List<Filter> filters = Lists.newArrayList();
    private Logger logger;

    /**
     * Constructor
     *
     * @param logger to log to.
     */
    public DebuggingService(Logger logger) {
        this.logger = logger;

        this.filters.add(new ClassFilter(ClassFilter.class));
        this.filters.add(new ClassFilter(MethodFilter.class));
        this.filters.add(new ClassFilter(DebuggingService.class));
    }

    /**
     * Add a filter to the service.
     *
     * @param filter to add.
     */
    public void addFilter(Filter filter) {
        this.filters.add(filter);
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

    public void log(LogLevel level, String string, Object instance) {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        Class clazz = elements[elements.length - 1].getClass();
        Method method = ReflectionUtils.getMethod(clazz, elements[elements.length - 1].getMethodName());
        if (this.filters != null) {
            for (Filter filter : this.filters) {
                if (filter.getClass().equals(NoneFilter.class)) return;
                else if (filter.getClass().equals(MethodFilter.class) && filter.check(method).shouldHide()) return;
                else if (filter.getClass().equals(ClassFilter.class) && filter.check(clazz).shouldHide()) return;
                else if (filter.check(instance).shouldHide()) return;
            }
        }
        this.logger.log(Level.ALL, "[" + level.name() + "]" + (instance == null ? "" : " Object Instance: " + instance) + (string == null ? "" : " Message:" + string));
    }

    public void log(LogLevel level, Object instance) {
        log(level, null, instance);
    }

    public void log(LogLevel level, String string) {
        log(level, string, null);
    }

    public Logger getLogger() {
        return logger;
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