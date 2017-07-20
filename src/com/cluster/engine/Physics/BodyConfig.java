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

package com.cluster.engine.Physics;

import com.cluster.engine.Physics.Shapes.Polygon;
import org.jsfml.system.Vector2f;

/**
 * a class used to initialise a {@link RigidBody}
 */
public class BodyConfig {

    /** The shape used to represent the body */
    public Polygon shape;
    /** The position the body should start at, default = (0, 0) */
    public Vector2f position;

    /** The starting linear velocity of the body, default = (0, 0) */
    public Vector2f velocity;
    /** The starting angular velocity of the body, default = 0 */
    public float angularVelocity;

    /** How bouncy the body should be, default = 0.2 */
    public float restitution;
    /** The density of the body, default = 0.5 */
    public float density;

    /** The friction coefficient between this and an object moving relative, default = 0.1f */
    public float dynamicFriction;
    /** The friction coefficient between this and a non-moving object, default = 0.1f */
    public float staticFriction;

    /** The mask of bits which can collide with this body */
    public int mask;
    /** The mask of bits which this body can collide with */
    public int category;
    /** Whether the body is static or not, default = false */
    public boolean isStatic;

    /**
     * Creates a default body configuration
     */
    public BodyConfig() {
        // Positional
        shape = null;
        position = Vector2f.ZERO;

        // Movement
        velocity = Vector2f.ZERO;
        angularVelocity = 0;

        // Material
        restitution = 0.2f;
        density = 0.5f;

        dynamicFriction = 0.1f;
        staticFriction = 0.1f;

        // Other
        mask = 0xFFFF;
        category = 0x0001;
        isStatic = false;
    }

}
