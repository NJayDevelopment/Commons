package net.njay.commons.debug.filter;

/**
 * Class to represent a filter.
 *
 * @author Austin Mayes
 */
public interface Filter {
    public FilterResponse check(Object toCheck) throws InvalidSuppliedValueException;

    public enum FilterResponse {
        SHOW,
        HIDE;

        public boolean shouldShow() {
            return this.equals(SHOW);
        }

        public boolean shouldHide() {
            return !shouldShow();
        }
    }
}
