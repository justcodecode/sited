package org.app4j.site.runtime.event;

/**
 * @author chi
 */
public abstract class Task implements Runnable {
    private final String name;
    private State state;
    private double progress;

    public Task(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public State state() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public double progress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public enum State {
        WAIT, PROCESSING, FINISHED, CANCELED, FAILED
    }
}
