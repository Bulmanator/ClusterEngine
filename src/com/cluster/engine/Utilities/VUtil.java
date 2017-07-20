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

import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector3f;

/**
 * A class which contains useful Vector methods which JSFML does not provide by default
 * @author James
 */
public final class VUtil {

    /**
     * a unit vector in the x-axis
     */
    public static final Vector2f X = new Vector2f(1, 0);

    /**
     * a unit vector in the y-axis
     */
    public static final Vector2f Y = new Vector2f(0, 1);

    // Private constructor
    private VUtil() {}

    /**
     * Gets the length of the given vector
     * @param vector The vector to find the length of
     * @return The length of the vector
     */
    public static float length(Vector2f vector) {
        return (float) Math.sqrt((vector.x * vector.x) + (vector.y * vector.y));
    }

    /**
     * Gets the length of the vector given square
     * @param vector The vector to find the length of
     * @return The square length of the vector
     */
    public static float lengthSq(Vector2f vector) {
        return (vector.x * vector.x) + (vector.y * vector.y);
    }

    /**
     * Returns the normalised vector of the given one
     * @param vector The vector to normalise
     * @return The normalised vector
     */
    public static Vector2f normalise(Vector2f vector) {
        float len = length(vector);

        if(len != 0) {
            return new Vector2f(vector.x / len, vector.y / len);
        }

        return Vector2f.ZERO;
    }

    /**
     * Calculates the resultant dot product of the vectors supplied
     * @param a The first vector to perform the dot product on
     * @param b The second vector to perform the dot product on
     * @return The resultant dot product between the two vectors
     */
    public static float dot(Vector2f a, Vector2f b) {
        return (a.x * b.x) + (a.y * b.y);
    }

    /**
     * Calculates the cross product of the vectors supplied
     * @param a The first vector to perform the cross product on
     * @param b The second vector to perform the cross product on
     * @return The resultant cross product between the two vectors
     */
    public static float cross(Vector2f a, Vector2f b) { return (a.x * b.y) - (a.y * b.x); }

    /**
     * Performs the 2D cross product on the given vector with the scalar supplied
     * @param a The vector to perform the cross product on
     * @param s The scalar to perform the cross product with
     * @return The resulting vector with crossed values
     */
    public static Vector2f cross(Vector2f a, float s) { return new Vector2f(a.y * s, a.x * -s); }

    /**
     * Converts a Vector3f to a Vector2f using just the x and y components
     * @param v The Vector3f to convert
     * @return The x and y components as a Vector2f
     */
    public static Vector2f toVector2f(Vector3f v) { return new Vector2f(v.x, v.y); }

    /**
     * Performs the triple 2D cross product between the vectors supplied.
     * @param a The first vector
     * @param b The second vector
     * @param c The third vector
     * @return The result of (b * Dot(a, C)) - (a * Dot(b, C))
     */
    public static Vector2f tripleCross(Vector2f a, Vector2f b, Vector2f c) {

        float ac = dot(a, c);
        float bc = dot(b, c);

        return new Vector2f(b.x * ac - a.x * bc, b.y * ac - a.y * bc);
    }

    /**
     * Projects Vector a onto Vector b
     * @param a The vector to be projected
     * @param b The vector to project onto
     * @return The projected vector
     */
    public static Vector2f project(Vector2f a, Vector2f b) {
        float dotA = dot(a, b);
        float dotB = dot(b, b);

        return Vector2f.mul(b, dotA / dotB);
    }

    /**
     * returns the mid point of two vectors
     * @param a the first vector
     * @param b the second vector
     * @return the mid point vector
     */
    public static Vector2f midPoint(Vector2f a, Vector2f b){
        return new Vector2f((a.x + b.x) / 2f, (a.y + b.y) / 2f);
    }
}
