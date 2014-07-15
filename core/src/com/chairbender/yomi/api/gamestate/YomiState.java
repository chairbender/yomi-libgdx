package com.chairbender.yomi.api.gamestate;

/**
 * By implementing this, the implementing class simply indicates that
 * it is a state with action methods. It's up to the using class to know what
 * methods are actions. All "action" methods are defined as methods which return
 * a class of type action, which, when executed, returns a new YomiState representing
 * the new state. YomiStates should be completely self-contained - that is, one
 * could feasibly start a game from any point by just initializing a new YomiState.
 */
public interface YomiState {
}
