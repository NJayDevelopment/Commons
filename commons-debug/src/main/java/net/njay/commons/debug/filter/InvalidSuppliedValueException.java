package net.njay.commons.debug.filter;

/**
 * Thrown when a filter is sent an invalid object to check.
 *
 * @author Austin Mayes
 */
public class InvalidSuppliedValueException extends Exception {
    Filter filter;
    Object sent;

    public InvalidSuppliedValueException(Object sent, Filter filter) {
        super("Invalid object sent to filter: " + filter.toString() + ". Sent: " + sent.toString());
        this.sent = sent;
        this.filter = filter;
    }
}
