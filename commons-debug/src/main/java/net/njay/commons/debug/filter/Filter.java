package net.njay.commons.debug.filter;

/**
 * Class to represent a filter.
 *
 * @author Austin Mayes
 */
public interface Filter {
    public FilterResponse check(Object toCheck);

    public enum FilterResponse {
        SHOW,
        HIDE,
        ABSTAIN;

        public boolean shouldShow() {
            return this.equals(SHOW);
        }

        public boolean shouldHide() {
            return !shouldShow();
        }
    }
}
