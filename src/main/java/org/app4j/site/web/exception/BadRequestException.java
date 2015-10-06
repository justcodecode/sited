package org.app4j.site.web.exception;

import com.google.common.collect.Maps;
import org.app4j.site.util.Asserts;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author chi
 */
public class BadRequestException extends RuntimeException {
    private final Map<String, Asserts.AssertionErrorMessage> errors = Maps.newHashMap();

    public BadRequestException(String name, Object value, String message) {
        super(message);
        errors.put(name, new Asserts.AssertionErrorMessage(name, value, message));
    }

    public BadRequestException(List<Asserts.AssertionErrorMessage> errors) {
        errors.stream().forEach(new Consumer<Asserts.AssertionErrorMessage>() {
            @Override
            public void accept(Asserts.AssertionErrorMessage assertionErrorMessage) {
                BadRequestException.this.errors.put(assertionErrorMessage.getName(), assertionErrorMessage);
            }
        });
    }
}
