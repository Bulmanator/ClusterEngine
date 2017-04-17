/*
    MIT License

    Copyright (c) 2017 James Bulman

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
*/

package com.cluster.engine.Utilities.State;

import com.cluster.engine.Game;
import com.cluster.engine.Utilities.Interfaces.Renderable;
import com.cluster.engine.Utilities.Interfaces.Updateable;

import java.util.Stack;

/**
 * A Class which allows different sections of the game to be loaded without removing others
 * @author James Bulman
 */
public class GameStateManager implements Updateable, Renderable {

    /**
     * The Game used for State construction
     */
    public final Game game;

    // A Stack of States which allowing for level loading etc.
    private Stack<State> states;

    /**
     * Creates a new Game State Manager
     * @param game The Game instance used to get information for the States
     */
    public GameStateManager(Game game) {
        this.game = game;
        states = new Stack<>();
    }

    /**
     * Pushes the given State to the top
     * @param state The State to push to the top
     */
    public void addState(State state) {
        states.push(state);
    }

    /**
     * Removes the active State and then pushes the State given to the top
     * @param state The State to push to the top
     */
    public void setState(State state) {
        popState();
        addState(state);
    }

    /**
     * Removes the active State and disposes it
     */
    public void popState() {
        State s = states.pop();
        if(s != null) s.dispose();
    }

    /**
     * Updates the active State
     * @param dt The amount of time passed since last frame
     */
    public void update(float dt) {
        if(!states.isEmpty())
            states.peek().update(dt);
    }

    /**
     * Renders the active State
     */
    public void render() {
        if(!states.isEmpty())
            states.peek().render();
    }
}
