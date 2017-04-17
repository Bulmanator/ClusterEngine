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
import com.cluster.engine.Utilities.Interfaces.Disposable;
import com.cluster.engine.Utilities.Interfaces.Renderable;
import com.cluster.engine.Utilities.Interfaces.Updateable;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.View;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

/**
 * The base for a State to be used within the {@link GameStateManager}
 * @author James Bulman
 */
public abstract class State implements Renderable, Updateable, Disposable {

    /** The Game instance used to get information for this State */
    protected Game game;
    /** The {@link GameStateManager} this State belongs to */
    protected GameStateManager gsm;

    /** The Window used for rendering */
    protected RenderWindow window;

    /** The View used for moving the Camera */
    protected View view;

    protected Vector2f worldSize;

    /** A Vector2 used for converting mouse coordinates from screen to world */
    protected Vector2i mouse;

    /**
     * Creates a new State
     * @param gsm The {@link GameStateManager} which this State belongs to
     */
    public State(GameStateManager gsm, Vector2f worldSize) {

        // Sets the game to the Game instance in the Game State Manager
        game = gsm.game;
        // Stores a reference to the Game State Manager
        this.gsm = gsm;

        // Gets the Window from the Game
        window = game.getWindow();

        this.worldSize = worldSize;

        // Creates a new View and applies it
        view = new View(new Vector2f(worldSize.x / 2f, worldSize.y / 2f), worldSize);

        // Initialises the Vector to 0, 0
        mouse = new Vector2i(0, 0);
        window.setView(view);
    }

    /**
     * Runs once per frame, used to update the entire State
     * @param dt The amount of time passed since last frame
     */
    public abstract void update(float dt);

    /**
     * Runs once per frame, used to render the entire State
     */
    public abstract void render();

    /**
     * Runs once the State is removed from the {@link GameStateManager}, used to delete unused objects
     */
    public abstract void dispose();

}
