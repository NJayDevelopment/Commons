package net.njay.commons.debug.filter;

/**
 * Filter to match classes.
 *
 * @author Austin Mayes
 */
public class ClassFilter implements Filter {
    Class toCheckAgainst;

    public ClassFilter(Class toCheckAgainst) {
        this.toCheckAgainst = toCheckAgainst;
    }

    @Override
    public FilterResponse check(Object toCheck) {
        try {
            Class c = (Class) toCheck;
            if (c.equals(this.toCheckAgainst)) return FilterResponse.SHOW;
            else return FilterResponse.HIDE;
        } catch (ClassCastException e) {
            // Not the correct filter, silently ignore.
        }
        return FilterResponse.ABSTAIN;
    }
}
