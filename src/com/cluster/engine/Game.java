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

package com.cluster.engine;

import com.cluster.engine.Utilities.Interfaces.Disposable;
import com.cluster.engine.Utilities.Interfaces.Renderable;
import com.cluster.engine.Utilities.Interfaces.Updateable;
import org.jsfml.graphics.RenderWindow;

/**
 * The base class for a Game instance, the {@link Engine} will use this to update and render your game
 * @author James Bulman
 */
public abstract class Game implements Updateable, Renderable, Disposable {

    /** The Engine instance this Game belongs to */
    protected Engine engine;
    /** The Window used for drawing to */
    protected RenderWindow window;

    /** Default Constructor */
    protected Game() {}

    /**
     * Runs once at the beginning of the game, this should be used for setup
     */
    protected abstract void initialise();

    /**
     * Runs once per frame, should be used to update all of your game objects
     * @param dt The amount of time passed since last frame
     */
    public abstract void update(float dt);

    /**
     * Runs once per frame, should be used to draw all of your game objects to the screen
     */
    public abstract void render();

    /**
     * Called when the Window is resized
     * @param width The new width of the Window
     * @param height The new Height of the Window
     */
    public void resize(int width, int height) {}

    /**
     * Runs when the Window loses focus, used to pause the game
     */
    public void pause() {}

    /**
     * Runs when the Window regains focus, used to resume the game
     */
    public void resume() {}

    /**
     * Runs when the game has been finished with, used to destroy disposable objects
     */
    public abstract void dispose();

    /**
     * Gets the Engine instance associated with this game
     * @return The Engine instance
     */
    public final Engine getEngine() { return engine; }

    /**
     * Gets the Window associated with this game
     * @return The Window
     */
    public final RenderWindow getWindow() { return window; }

    /**
     * Sets the Engine instance of the Game<br>
     * @param engine The Engine to associate with the Game
     */
    final void setEngine(Engine engine) {
        this.engine = engine;
        window = engine.getWindow();
    }
}