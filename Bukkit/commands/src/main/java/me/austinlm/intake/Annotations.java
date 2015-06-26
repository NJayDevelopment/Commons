package me.austinlm.intake;

import com.sk89q.intake.parametric.annotation.Classifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotations for parameter values.
 *
 * @author Austin Mayes
 */
public class Annotations {
    /**
     * A supplied value that is provided in the {@link com.sun.org.apache.xml.internal.utils.NameSpace}.
     */
    @Classifier()
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER, ElementType.FIELD})
    public @interface Sender {
    }
}
