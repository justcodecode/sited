package org.app4j.site.internal.error;

/**
 * @author chi
 */
public interface ErrorConfig {
    ErrorConfig on(Class<? extends Throwable> error, ErrorHandler errorHandler);

    ErrorHandler handler(Class<? extends Throwable> error);
}
