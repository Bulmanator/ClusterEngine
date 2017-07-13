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

import com.cluster.engine.Physics.Transform;
import org.jsfml.system.Vector2f;

/**
 * a class which represents an Axis-Aligned Bounding Box, used within the broad phase collision detection
 */
public class AABB {

    private Vector2f minimum;
    private Vector2f maximum;

    public AABB(Vector2f minimum, Vector2f maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    /**
     * Finds the minimum and maximum bounds of the given vertices and creates an AABB using them
     * @param vertices The vertices to create the AABB from
     */
    public AABB(Vector2f[] vertices, int count) {
        float minX, maxX;
        float minY, maxY;

        // Initial Values
        minX = maxX = vertices[0].x;
        minY = maxY = vertices[0].y;

        for(int i = 1; i < count; i++) {
            // Find min and max x values
            if(vertices[i].x < minX) {
                minX = vertices[i].x;
            }
            else if(vertices[i].x > maxX) {
                maxX = vertices[i].x;
            }

            // Find min and max y values
            if(vertices[i].y < minY) {
                minY = vertices[i].y;
            }
            else if(vertices[i].y > maxY) {
                maxY = vertices[i].y;
            }
        }

        // Find the centre
        minimum = new Vector2f(minX, minY);
        // Find the half width and height
        maximum = new Vector2f(maxX,maxY);
    }

    public void transform(Transform tx) {
        minimum = tx.applyPosition(minimum);
        maximum = tx.applyPosition(maximum);
    }

    /**
     * Gets the minimum corner of the AABB
     * @return The minimum corner
     */
    public Vector2f getMinimum() { return minimum; }

    /**
     * Gets the maximum corner of the AABB
     * @return The maximum corner
     */
    public Vector2f getMaximum() { return maximum; }

    /**
     * Checks if two AABBs overlap
     * @param one The first AABB to check
     * @param two The second AABB to check
     * @return If the two AABBs overlap then true, otherwise false
     */
    public static boolean overlaps(AABB one, AABB two) {
        // Check X axis
        if(two.minimum.x > one.maximum.x || one.minimum.x > two.maximum.x) {
            return false;
        }

        // Check Y axis
        if(two.minimum.y > one.maximum.y || one.minimum.y > two.maximum.y) {
            return false;
        }

        // They overlap
        return true;
    }
}
