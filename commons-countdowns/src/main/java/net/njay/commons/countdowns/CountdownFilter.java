package net.njay.commons.countdowns;

import net.njay.commons.debug.filter.Filter;

/**
 * @author Austin Mayes
 */
public class CountdownFilter implements Filter {
    Countdown toCheckAgainst;

    public CountdownFilter(Countdown toCheckAgainst) {
        this.toCheckAgainst = toCheckAgainst;
    }

    @Override
    public FilterResponse check(Object toCheck) {
        try {
            Countdown countdown = (Countdown) toCheck;
            if (countdown.equals(this.toCheckAgainst)) return FilterResponse.SHOW;
            else return FilterResponse.HIDE;
        } catch (ClassCastException e) {
            // Not the correct filter, silently ignore.
        }
        return FilterResponse.ABSTAIN;
    }
}
