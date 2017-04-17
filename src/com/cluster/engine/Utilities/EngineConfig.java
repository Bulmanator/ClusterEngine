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

package com.cluster.engine.Utilities;

import org.jsfml.window.WindowStyle;

/**
 * A small data structure used to configure the main engine
 */
public class EngineConfig {

    /** The target width of the window, default = 640 */
    public int width;
    /** The target height of the window, default = 360 */
    public int height;

    /** The FPS limit of updating and rendering, set negative to use V-Sync, default = -1 */
    public int fpsLimit;

    /** The title of the window, default = "Untitled" */
    public String title;
    /** The {@link WindowStyle} of the window, default = WindowStyle.TITLEBAR | WindowStyle.CLOSE */
    public int style;

    /** Whether the engine will automatically start its main loop, default = true */
    public boolean autoStart;

    /** The root folder to all of the content for the game, default = "Content" */
    public String contentRoot;

    /**
     * Creates a default configuration
     */
    public EngineConfig() {
        width = 640;
        height = 360;

        fpsLimit = -1;

        title = "Untitled";
        style = WindowStyle.TITLEBAR | WindowStyle.CLOSE;

        autoStart = true;
        contentRoot = "Content";
    }

}
