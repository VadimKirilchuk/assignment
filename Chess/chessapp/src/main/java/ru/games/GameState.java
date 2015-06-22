package ru.games;

/**
 * Created by Андрей on 21.06.2015.
 */
public class GameState {
    private State state = State.WHITE_MOVING;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
