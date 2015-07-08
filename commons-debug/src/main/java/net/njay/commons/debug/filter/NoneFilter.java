package net.njay.commons.debug.filter;

/**
 * Filter that always returns false.
 *
 * @author Austin Mayes
 */
public class NoneFilter implements Filter {
    @Override
    public FilterResponse check(Object toCheck) {
        return FilterResponse.HIDE;
    }
}
