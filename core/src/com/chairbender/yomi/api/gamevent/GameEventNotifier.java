package com.chairbender.yomi.api.gamevent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles being notified that events have occurred by the game, and alerting the UI about those
 * events. Should be a member of the Game class, so you can
 */
public class GameEventNotifier {


    /**
     * Map from game events to the handlers that want to know about those events
     */
    private Map<Class<?extends GameEvent>,List<EventHandler>> handlerMap;

    public GameEventNotifier() {
        this.handlerMap = new HashMap<Class<?extends GameEvent>,List<EventHandler>>();
    }


    public void addEventHandler(EventHandler handler) {
        if (!handlerMap.containsKey(handler.getEventType())) {
            handlerMap.put(handler.getEventType(),new ArrayList<EventHandler>());
        }

        handlerMap.get(handler.getEventType()).add(handler);
    }

    /**
     * game should call this to notify this class about events that happened
     * @param event event that happened
     */
    public void notifyEvent(GameEvent event) {
        if (handlerMap.containsKey(event.getClass())) {
            for (EventHandler handler : handlerMap.get(event.getClass())) {
                handler.handle(event);
            }
        }
    }




}
