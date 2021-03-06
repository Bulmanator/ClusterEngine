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

import com.cluster.engine.Utilities.Interfaces.EntityRenderable;
import com.cluster.engine.Utilities.Interfaces.Initialisable;
import com.cluster.engine.Utilities.Interfaces.Updateable;
import com.cluster.engine.Utilities.MUtil;
import org.jsfml.graphics.CircleShape;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;

/**
 * Represents a single particle inside the particle system
 * @author James Bulman
 */
class Particle implements Initialisable<ParticleConfig>, Updateable, EntityRenderable {

    /** The position of the particle */
    private Vector2f position;
    /** The velocity of the particle */
    private Vector2f velocity;
    /** The rotational speed of the particle */
    private float rotationalSpeed;

    /** The shape which is drawn to the screen */
    private CircleShape display;
    /** Whether the particle will fade out or disappear */
    private boolean fadeOut;

    /** The colours to transition through */
    private Color[] colours;
    /** The time each colour is present for */
    private float colourTime;

    /** The size of the particle at the beginning of its lifetime */
    private float startSize;
    /** The size of the particle at the end of its life */
    private float endSize;

    /** How much time has passed since the particle was initialised, in seconds */
    private float accumulator;
    /** How long the particle will live for, in seconds */
    private float lifetime;
    /** Whether or not the particle is alive */
    private boolean alive;

    /**
     * Constructs a Particle, Use {@link Particle#initialise(ParticleConfig)} to configure the particle
     */
    Particle() {
        // Positional
        position = new Vector2f(0, 0);
        velocity = new Vector2f(0, 0);
        rotationalSpeed = 0;

        // Display
        display = new CircleShape(5f);
        fadeOut = false;

        // Colour
        colours = new Color[1];

        // Size
        startSize = 5f;
        endSize = 5f;

        // Lifetime
        accumulator = 0;
        lifetime = 0;
        alive = false;
    }

    /**
     * Initialises the particle from the given configuration
     * @param config The {@link ParticleConfig} to initialise the particle with
     */
    public void initialise(ParticleConfig config) {
        // Set the position
        position = new Vector2f(config.position.x, config.position.y);

        // Work out the (x, y) velocity using the angle and speed
        float angle = MUtil.randomFloat(config.minAngle, config.maxAngle);
        angle *= MUtil.DEG_TO_RAD;

        velocity = new Vector2f(
                config.speed * MUtil.cos(angle),
                -config.speed * MUtil.sin(angle)
        );

        // Set the rotational speed
        rotationalSpeed = config.rotationalSpeed;

        // Set the begin and end sizes
        startSize = config.startSize;
        endSize = config.endSize;

        // Work out what shape to use
        if(config.pointCount < 3 || config.pointCount > 16) {
            display = new CircleShape(startSize);
        }
        else {
            display = new CircleShape(startSize, config.pointCount);
        }

        display.setRotation(angle * MUtil.RAD_TO_DEG);

        // Set the texture if one is present
        if(config.texture != null) display.setTexture(config.texture);

        int colourCount = 0;
        for(int i = 0; i < config.colours.length; i++) {
            if(config.colours[i] == null) break;
            colourCount++;
        }

        if(colourCount == 0) {
            colourCount = 1;
            colours = new Color[colourCount];
            colours[0] = Color.WHITE;
        }
        else {
            colours = new Color[colourCount];
            System.arraycopy(config.colours, 0, colours, 0, colourCount);
        }

        // Set display position
        display.setOrigin(startSize, startSize);
        display.setPosition(position);
        display.setFillColor(colours[0]);

        // Set whether to fade out or not
        fadeOut = config.fadeOut;

        // Reset the accumulator and set the lifetime
        accumulator = 0;
        lifetime = MUtil.randomFloat(config.minLifetime, config.maxLifetime);
        lifetime = MUtil.round(lifetime, 4);

        colourTime = lifetime / (colourCount - 1);

        // Mark the particle as alive
        alive = true;
    }

    /**
     * Updates the particle for a single frame
     * @param dt The amount of time passed since last frame
     */
    public void update(float dt) {
        if(!alive) return;

        accumulator += dt;
        float timeLeft = lifetime - accumulator;

        if(timeLeft > 0) {

            float ratio = accumulator / lifetime;

            display.setRadius(MUtil.lerp(startSize, endSize, ratio));
            display.setOrigin(display.getRadius(), display.getRadius());


            position = new Vector2f(
                position.x + (velocity.x * dt),
                position.y + (velocity.y * dt)
            );


            display.setPosition(position);
            display.rotate(rotationalSpeed * dt);

            int a = 255;
            if (fadeOut) a = (int) (255 * (timeLeft / lifetime));

            if(colours.length > 1) {

                Color start, end;

                int colourIndex = (int) (accumulator / colourTime);

                start = colours[colourIndex];
                end = colours[colourIndex + 1];

                float colRatio = (accumulator - (colourTime * colourIndex)) / colourTime;

                Color colour = MUtil.lerpColour(start, end, colRatio);

                display.setFillColor(new Color(colour, a));
            }
            else {
                display.setFillColor(new Color(colours[0], a));
            }
        }
        else {
            alive = false;
        }
    }

    /**
     * Draws the particle to the screen
     * @param renderer The {@link RenderWindow} to draw the entity to
     */
    public void render(RenderWindow renderer) {
        if(!alive) return;
        renderer.draw(display);
    }

    /**
     * Whether or not the particle is alive
     * @return True if the particle is alive, otherwise False
     */
    public boolean isAlive() { return alive; }

    /**
     * Kills the particle to prevent it from being updated or rendered
     */
    void kill() { alive = false; }
}
