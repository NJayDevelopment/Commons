package net.njay.commons.debug.filter;

import java.lang.reflect.Method;

/**
 * Filter to compare methods.
 *
 * @author Austin Mayes
 */
public class MethodFilter implements Filter {
    Method toCheckAgainst;

    public MethodFilter(Method toCheckAgainst) {
        this.toCheckAgainst = toCheckAgainst;
    }

    @Override
    public FilterResponse check(Object toCheck) {
        try {
            Method m = (Method) toCheck;
            if (m.equals(this.toCheckAgainst)) return FilterResponse.SHOW;
            else return FilterResponse.HIDE;
        } catch (ClassCastException e) {
            // Not the correct filter, silently ignore.
        }
        return FilterResponse.ABSTAIN;
    }
}
