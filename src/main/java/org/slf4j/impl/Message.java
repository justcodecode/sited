package org.slf4j.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * @author chi
 */
final class Message {
    final Level level;
    final long time;
    final String logger;
    final String message;
    final Object[] arguments;
    final Throwable exception;
    String logMessage;

    Message(Level level, long time, String logger, String message, Object[] arguments, Throwable exception) {
        this.level = level;
        this.time = time;
        this.logger = logger;
        this.message = message;
        this.arguments = arguments;
        this.exception = exception;
    }

    public String toString() {
        if (logMessage == null) {
            StringBuilder builder = new StringBuilder(64);
            builder.append(DateTimeFormatter.ISO_INSTANT.format(Instant.ofEpochMilli(time)))
                    .append(" [")
                    .append(Thread.currentThread().getName())
                    .append("] ")
                    .append(level.name())
                    .append(' ')
                    .append(logger)
                    .append(" - ");

            if (arguments == null)
                builder.append(message);
            else
                builder.append(String.format(message, arguments));

            builder.append(System.lineSeparator());
            if (exception != null) {
                StringWriter stackTrace = new StringWriter();
                exception.printStackTrace(new PrintWriter(stackTrace));
                builder.append(stackTrace.toString());
            }

            logMessage = builder.toString();
        }
        return logMessage;
    }

    enum Level {
        TRACE, DEBUG, INFO, ERROR, WARNING
    }
}
