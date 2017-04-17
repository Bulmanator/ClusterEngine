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

package com.cluster.engine.Input;

import org.jsfml.system.Vector2i;
import org.jsfml.window.Joystick;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Mouse;

/**
 * An interface used to handle all types of user input
 * @author James Bulman
 */
public interface InputProcessor {

    // Keyboard Methods

    /**
     * Called when a key on the keyboard is pressed
     * @param key The {@link Keyboard.Key} which was pressed
     */
    void keyPressed(Keyboard.Key key);

    /**
     * Called when a key on the keyboard is released
     * @param key The {@link Keyboard.Key} which was released
     */
    void keyReleased(Keyboard.Key key);

    // Mouse Methods

    /**
     * Called when a button on the mouse is pressed
     * @param button The {@link Mouse.Button} which was pressed
     * @param position The position of the mouse on the screen when the button was pressed
     */
    void mouseButtonPressed(Mouse.Button button, Vector2i position);

    /**
     * Called when a button on the mouse is released
     * @param button The {@link Mouse.Button} which was released
     * @param position The position of the mouse on the screen when the button was released
     */
    void mouseButtonReleased(Mouse.Button button, Vector2i position);

    /**
     * Called when the mouse wheel is scrolled
     * @param amount The mouse that the scroll wheel was moved by
     */
    void mouseWheelMoved(int amount);

    /**
     * Called when the mouse is moved
     * @param position The new position of the mouse on the screen
     */
    void mouseMoved(Vector2i position);

    // Controller Methods

    /**
     * Called when a controller is connected
     * @param id The numeric ID of the controller connected
     */
    void controllerConnected(int id);

    /**
     * Called when a controller is disconnected
     * @param id The numeric ID of the controller disconnected
     */
    void controllerDisconnected(int id);

    /**
     * Called when a button on the controller is pressed
     * @param id The numeric ID of the controller
     * @param button The button which was pressed
     */
    void controllerButtonPressed(int id, int button);

    /**
     * Called when a button on the controller is released
     * @param id The numeric ID of the controller
     * @param button The button which was released
     */
    void controllerButtonReleased(int id, int button);

    /**
     * Called when an axis on the controller moved
     * @param id The numeric ID of the controller
     * @param axis The {@link Joystick.Axis} which was moved
     * @param position The new position of the axis
     */
    void controllerAxisMoved(int id, Joystick.Axis axis, float position);

}
