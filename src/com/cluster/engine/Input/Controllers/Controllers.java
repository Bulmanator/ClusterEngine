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

package com.cluster.engine.Input.Controllers;

import org.jsfml.window.Joystick;

/**
 * A class which is used to construct all of the controller instances
 */
public class Controllers {

    public enum Type {
        PS3(0x054c, 0x0268),
        PS4(0x054c, 0x09cc),
        Xbox_360(0x045e, 0x028e),
        Xbox_One(0x045e, 0x02dd | 0x02ff),
        Xbox_Elite(0x045e, 0x02e3),
        Unknown(0, 0);

        private int vendorID;
        private int productID;

        Type(int vendorID, int productID) {
            this.vendorID = vendorID;
            this.productID = productID;
        }

        static Type value(int vendorID, int productID) {
            for(Type type : values()) {
                if((type.productID & productID) == productID) {
                    if((type.vendorID & vendorID) == vendorID) {
                        return type;
                    }
                }
            }
            return Unknown;
        }
    }


    private static final Controllers instance = new Controllers();

    private Controller[] controllers;

    private Controllers() {
        controllers = new Controller[Joystick.JOYSTICK_COUNT];
        for(int i = 0; i < Joystick.JOYSTICK_COUNT; i++) {
            controllers[i] = new Controller(PlayerNumber.values()[i]);
        }
    }

    /**
     * Gets whether or not the specified player is connected
     * @param player The player to test for connection
     * @return True if the controller is connected, otherwise false
     */
    public static boolean isConnected(PlayerNumber player) {
        return instance.controllers[player.ordinal()].isConnected();
    }

    /**
     * Gets the current state of a specified controller
     * @param player The controller number
     * @return The button, trigger and thumbstick state of the controller
     */
    public static ControllerState getState(PlayerNumber player) {
        instance.controllers[player.ordinal()].setConnected(Joystick.isConnected(player.ordinal()));
        return new ControllerState(instance.controllers[player.ordinal()]);
    }
}
