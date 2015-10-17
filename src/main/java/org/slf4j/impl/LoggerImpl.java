package org.slf4j.impl;

import org.slf4j.helpers.MarkerIgnoringBase;

import java.io.PrintStream;

/**
 * @author chi
 */
class LoggerImpl extends MarkerIgnoringBase {
    private final PrintStream output = System.out;
    private final Message.Level level;

    LoggerImpl(String name, Message.Level level) {
        this.name = name;
        this.level = level;
    }

    void log(Message message) {
        output.print(message.toString());
    }

    @Override
    public boolean isTraceEnabled() {
        return false;
    }

    @Override
    public void trace(String msg) {

    }

    @Override
    public void trace(String format, Object arg) {

    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {

    }

    @Override
    public void trace(String format, Object... arguments) {

    }

    @Override
    public void trace(String msg, Throwable t) {

    }

    @Override
    public boolean isDebugEnabled() {
        return Message.Level.DEBUG.ordinal() >= level.ordinal();
    }

    @Override
    public void debug(String msg) {
        debug(msg, new Object[]{});
    }

    @Override
    public void debug(String format, Object arg) {
        debug(format, new Object[]{arg});
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        debug(format, new Object[]{arg1, arg2});
    }

    @Override
    public void debug(String format, Object... arguments) {
        if (isDebugEnabled()) {
            Throwable throwable = arguments.length > 0 && arguments[arguments.length - 1] instanceof Throwable
                ? (Throwable) arguments[arguments.length - 1] : null;
            Message message = new Message(Message.Level.DEBUG, System.currentTimeMillis(), name, format, arguments, throwable);
            log(message);
        }
    }

    @Override
    public void debug(String msg, Throwable t) {
        debug(msg, new Object[]{t});
    }

    @Override
    public boolean isInfoEnabled() {
        return Message.Level.INFO.ordinal() >= level.ordinal();
    }

    @Override
    public void info(String msg) {
        info(msg, new Object[]{});
    }

    @Override
    public void info(String format, Object arg) {
        info(format, new Object[]{arg});
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        info(format, new Object[]{arg1, arg2});
    }

    @Override
    public void info(String format, Object... arguments) {
        if (isInfoEnabled()) {
            Throwable throwable = arguments.length > 0 && arguments[arguments.length - 1] instanceof Throwable
                ? (Throwable) arguments[arguments.length - 1] : null;
            Message message = new Message(Message.Level.INFO, System.currentTimeMillis(), name, format, arguments, throwable);
            log(message);
        }
    }

    @Override
    public void info(String msg, Throwable t) {
        info(msg, new Object[]{t});
    }

    @Override
    public boolean isWarnEnabled() {
        return Message.Level.WARNING.ordinal() >= level.ordinal();
    }

    @Override
    public void warn(String msg) {
        warn(msg, new Object[]{});
    }

    @Override
    public void warn(String format, Object arg) {
        warn(format, new Object[]{arg});
    }

    @Override
    public void warn(String format, Object... arguments) {
        if (isWarnEnabled()) {
            Throwable throwable = arguments.length > 0 && arguments[arguments.length - 1] instanceof Throwable
                ? (Throwable) arguments[arguments.length - 1] : null;
            Message message = new Message(Message.Level.WARNING, System.currentTimeMillis(), name, format, arguments, throwable);
            log(message);
        }
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        warn(format, new Object[]{arg1, arg2});
    }

    @Override
    public void warn(String msg, Throwable t) {
        warn(msg, new Object[]{t});
    }

    @Override
    public boolean isErrorEnabled() {
        return Message.Level.INFO.ordinal() >= level.ordinal();
    }

    @Override
    public void error(String msg) {
        error(msg, new Object[]{});
    }

    @Override
    public void error(String format, Object arg) {
        error(format, new Object[]{arg});
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        error(format, new Object[]{arg1, arg2});
    }

    @Override
    public void error(String format, Object... arguments) {
        if (isErrorEnabled()) {
            Throwable throwable = arguments.length > 0 && arguments[arguments.length - 1] instanceof Throwable
                ? (Throwable) arguments[arguments.length - 1] : null;
            Message message = new Message(Message.Level.ERROR, System.currentTimeMillis(), name, format, arguments, throwable);
            log(message);
        }
    }

    @Override
    public void error(String msg, Throwable t) {
        error(msg, new Object[]{t});
    }
}
