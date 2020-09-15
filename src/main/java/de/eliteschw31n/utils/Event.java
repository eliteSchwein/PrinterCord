package de.eliteschw31n.utils;

import de.eliteschw31n.Main;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public abstract class Event extends ListenerAdapter {

    private final String eventName;
    private Main printerCord = null;

    public Event(final String eventName) {
        this.eventName = eventName;
    }

    public void setInstance(final Main instance) {
        if (printerCord != null) {
            throw new IllegalStateException("Can only initialize once!");
        }
        printerCord = instance;
    }

    public abstract void execute();

    public Main getMain() {
        return printerCord;
    }

    public String getEventName() {
        return eventName;
    }
}
