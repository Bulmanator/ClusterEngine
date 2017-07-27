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

package com.cluster.engine.Graphics.Particles;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;

/**
 * A class used to configure a particle
 */
public class ParticleConfig {

    /** The offset from the GameObject it is attached to, default = (0, 0) */
    public Vector2f offset;
    /** The speed of the particle, default = 0 */
    public float speed;
    /** The minimum angle to generate between, in degrees, default = 0 */
    public float minAngle;
    /** The maximum angle to generate between, in degrees, default = 0 */
    public float maxAngle;
    /** The rotational speed of the particles, in degrees per second, default = 0 */
    public float rotationalSpeed;

    /** The point count of the display shape, use 0 for circle, default = 0 */
    public int pointCount;
    /** The texture to apply to the particle, will not be used if null, default = null */
    public Texture texture;
    /** Whether the particle will fade out or disappear, default = false */
    public boolean fadeOut;

    /** The colours transition through, up to 8 colours are supported */
    public final Color[] colours;

    /** The beginning size of the particle, default = 5 */
    public float startSize;
    /** The ending size of the particle, default = 5 */
    public float endSize;

    /** The minimum length of a particles life, in seconds, default = 0.1 */
    public float minLifetime;
    /** The maximum length of a particles life, in seconds, default = 1 */
    public float maxLifetime;


    /**
     * Instantiates a default particle configuration
     */
    public ParticleConfig() {
        // Positional
        offset = new Vector2f(0, 0);
        speed = 0;
        minAngle = 0;
        maxAngle = 0;
        rotationalSpeed = 0;

        // Display
        pointCount = 0;
        texture = null;
        fadeOut = false;

        // Colour
        colours = new Color[8];
        colours[0] = Color.WHITE;

        // Size
        startSize = 5;
        endSize = 5;

        // Lifetime
        minLifetime = 0.1f;
        maxLifetime = 1;
    }

    /**
     * Constructs a new EngineConfig, copying the values from the one given
     * @param config The {@link ParticleConfig} to copy values from
     */
    ParticleConfig(ParticleConfig config) {
        // Positional
        offset = new Vector2f(config.offset.x, config.offset.y);
        speed = config.speed;
        minAngle = config.minAngle;
        maxAngle = config.maxAngle;
        rotationalSpeed = config.rotationalSpeed;

        // Display
        pointCount = config.pointCount;
        texture = config.texture;
        fadeOut = config.fadeOut;

        // Colour
        colours = new Color[8];
        for(int i = 0; i < config.colours.length; i++) {
            colours[i] = config.colours[i] == null ? null : new Color(config.colours[i], 255);
        }

        // Size
        startSize = config.startSize;
        endSize = config.endSize;

        // Lifetime
        minLifetime = config.minLifetime;
        maxLifetime = config.maxLifetime;
    }
}
