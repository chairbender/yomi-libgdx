package com.chairbender.yomi.api.gamevent;

/**
 * Handler which handles a game event.
 */
public abstract class EventHandler<T extends GameEvent> {

    private final Class<T> type;

    /**
     *
     * @param eventType the type of event that this event handler will handle
     */
    public EventHandler(Class<T> eventType) {
        this.type = eventType;
    }

    /**
     * Do some processing in response to the event.
     * @param event event to handle
     */
    public abstract void handle(T event);

    public Class<T> getEventType() {
        return type;
    }

}
