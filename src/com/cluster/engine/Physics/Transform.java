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

import com.cluster.engine.Utilities.MUtil;
import org.jsfml.system.Vector2f;

/**
 * A simple Transform class which can hold the position, rotation and scale of an object
 * @author James Bulman
 */
public class Transform {

    // TODO(James): Redo this to provide inverse transformations

    /** The position of the object on the screen */
    private Vector2f position;
    /** The angle of the object, in radians */
    private float angle;
    /** The sine and cosine of the current angle */
    private float sin, cos;

    /**
     * Creates a default Transform, at (0, 0) with a rotation of 0 and scale of (1, 1)
     */
    public Transform() {
        position = Vector2f.ZERO;
        angle = 0;
        sin = 0;
        cos = 1;
    }

    /**
     * Creates a Transform from the position, rotation and scale specified
     * @param position The position for the Transform to be placed at
     * @param angle The angle for the Transform to be rotated to
     */
    public Transform(Vector2f position, float angle) {
        this.position = position;
        this.angle = angle;
        sin = MUtil.sin(angle);
        cos = MUtil.cos(angle);
    }

    /**
     * Applies the transformation to the vector given
     * @param vector The vector to apply this transformation to
     * @return The transformed vector
     */
    public Vector2f apply(Vector2f vector) {
        float x = (vector.x * cos) - (vector.y * sin);
        float y = (vector.x * sin) + (vector.y * cos);

        return Vector2f.add(new Vector2f(x, y), position);
    }

    /**
     * Applies only the position portion of the transform
     * @param vector The vector to transform
     * @return The vector translated via the position of the transform
     */
    public Vector2f applyPosition(Vector2f vector) { return Vector2f.add(vector, position); }

    /**
     * Applies only the rotation portion of the transform
     * @param vector The vector to transform
     * @return The vector rotated by the angle of this transform
     */
    public Vector2f applyRotation(Vector2f vector) {
        float x = (vector.x * cos) - (vector.y * sin);
        float y = (vector.x * sin) + (vector.y * cos);

        return new Vector2f(x, y);
    }

    /**
     * Gets the current position of the Transform
     * @return The position of the Transform
     */
    public Vector2f getPosition() { return position; }

    /**
     * Gets the current rotation of the Transform
     * @return The current rotation of the Transform, in radians
     */
    public float getAngle() { return angle; }

    /**
     * Moves the Transform to the given position
     * @param position The new position to move the Transform to
     */
    void setPosition(Vector2f position) { this.position = position; }

    /**
     * Rotates the Transform to the given angle
     * @param angle The angle to set the rotation to, in radians
     */
    void setAngle(float angle) {
        this.angle = angle;
        sin = MUtil.sin(angle);
        cos = MUtil.cos(angle);
    }
}
