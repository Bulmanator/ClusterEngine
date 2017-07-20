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
package com.cluster.engine.Physics.Shapes;

import org.jsfml.graphics.CircleShape;
import org.jsfml.graphics.Drawable;
import org.jsfml.system.Vector2f;

/**
 * A class to represent a Circle shape to be used by the physics system
 * @author James Bulman
 */
public class Circle extends Shape {

    /** The drawable representation of the circle */
    private CircleShape shape;

    /**
     * Creates a circle with the given radius
     * @param radius The radius of the circle
     */
    public Circle(float radius) {
        this.radius = radius;
        shape = new CircleShape(radius);
    }

    /**
     * Gets the farthest point in any given direction on the circle
     * @param direction The direction to search in
     * @return The farthest point in the given direction
     */
    public Vector2f getFarthestPoint(Vector2f direction) {
        return Vector2f.mul(direction, radius);
    }

    /**
     * Gets the drawable representation of the shape
     * @return The drawable circle shape
     */
    public Drawable getDrawable() { return shape; }

    /**
     * Gets the type of the circle shape
     * @return Always returns {@link Shape.Type#Circle}
     */
    public Type getType() {
        return Type.Circle;
    }
}
