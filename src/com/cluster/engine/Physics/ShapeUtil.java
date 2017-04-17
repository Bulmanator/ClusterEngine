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

import com.cluster.engine.Utilities.MathUtil;
import org.jsfml.system.Vector2f;

/**
 * Some utility methods for shapes
 */
final class ShapeUtil {

    /**
     * Calculates the area of a polygon
     * @param vertices The vertices which make up the polygon
     * @return The area of the polygon
     */
    static float findArea(Vector2f[] vertices) {
        float area = 0;

        for(int i = 0, j = vertices.length - 1; i < vertices.length; j = i++) {
            area += MathUtil.cross(vertices[i], vertices[j]);
        }

        return Math.abs(0.5f * area);
    }

    /**
     * Calculates the centroid of a convex polygon using the polygonal centroid formula
     * @param vertices The vertices that make up the polygon
     * @return The centroid of the polygon
     */
    static Vector2f findCentroid(Vector2f[] vertices) {
        float x = 0, y = 0;
        float area = 0;

        for(int i = 0, j = vertices.length - 1; i < vertices.length; j = i++) {
            Vector2f v0 = vertices[i];
            Vector2f v1 = vertices[j];

            float a = MathUtil.cross(v0, v1);
            area += a;
            x += (v0.x + v1.x) * a;
            y += (v0.y + v1.y) * a;
        }

        area *= 0.5f;

        x /= (6f * area);
        y /= (6f * area);

        return new Vector2f(x, y);
    }

    /**
     * Generates the edge normals from the given vertices which make up a polygon
     * @param vertices The vertices which make up the polygon
     * @return The edge normals
     */
    static Vector2f[] calculateNormals(Vector2f[] vertices) {
        Vector2f[] normals = new Vector2f[vertices.length];
        for(int i = 0, j = vertices.length - 1; i < vertices.length; j = i++) {
            Vector2f nor = Vector2f.sub(vertices[i], vertices[j]);
            normals[i] = MathUtil.normalise(new Vector2f(nor.y, -nor.x));
        }

        return normals;
    }
}
