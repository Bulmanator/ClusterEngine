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

import com.cluster.engine.Utilities.MUtil;
import com.cluster.engine.Utilities.VUtil;
import org.jsfml.graphics.ConvexShape;
import org.jsfml.graphics.Drawable;
import org.jsfml.system.Vector2f;

/**
 * a class to represent an arbitrary polygon to be used within the physics system. These are read-only once created
 */
public class Polygon extends Shape {

    /** The maximum number of vertices which */
    public static final int MAX_VERTICES = 8;

    /** The vertices of the polygon, in local space */
    private Vector2f[] vertices;

    /** The number of vertices in the polygon */
    private int vertexCount;

    /** The Drawable representation of the shape */
    private ConvexShape shape;

    /**
     * Creates a random convex polygon with a radius between 10 and 100
     */
    public Polygon() { this(MUtil.randomFloat(10, 45)); }

    /**
     * Creates a random convex polygon with the radius provided
     * @param radius The radius to generate vertices within
     */
    public Polygon(float radius) {

        radius = Math.abs(radius);

        vertices = new Vector2f[MAX_VERTICES];
        this.radius = radius;

        // Generate all of the vertices needed
        vertexCount = 0;
        for(float angle = 0; angle < MUtil.PI2;) {

            vertices[vertexCount] = new Vector2f((MUtil.cos(angle) * radius),
                    (MUtil.sin(angle) * radius));

            vertexCount++;
            if(vertexCount == MAX_VERTICES) break;
            angle += MUtil.randomFloat(40, 80) * MUtil.DEG_TO_RAD;
        }

        shape = new ConvexShape(vertexCount);
        for(int i = 0; i < vertexCount; i++) {
            shape.setPoint(i, vertices[i]);
        }
    }

    /**
     * Creates a polygon with the vertices supplied
     * @param vertices The vertices which make up the polygon
     */
    public Polygon(Vector2f[] vertices) {
        if(vertices.length > MAX_VERTICES) {
            throw new IllegalArgumentException("Error: a maximum of 8 vertices are allowed");
        }

        // Copy the vertices into an array of MAX_VERTICES for consistency
        this.vertices = new Vector2f[MAX_VERTICES];
        System.arraycopy(vertices, 0, this.vertices, 0, vertices.length);
        vertexCount = vertices.length;

        // Move the vertices into local space
        Vector2f centroid = findCentroid(vertices, vertexCount);
        for (int i = 0; i < vertexCount; i++) {
            vertices[i] = Vector2f.sub(vertices[i], centroid);
            float length = VUtil.lengthSq(vertices[i]);
            radius = length > radius ? length : radius;
        }

        radius = MUtil.sqrt(radius);
        shape = new ConvexShape(vertices);
    }

    /**
     * Gets the farthest vertex in the given direction
     * @param direction The direction to search in
     * @return The farthest point along the direction
     */
    public Vector2f getFarthestPoint(Vector2f direction) {
        int bestIndex = 0;
        float best = VUtil.dot(vertices[0], direction);

        for(int i = 1; i < vertexCount; i++) {
            float dist = VUtil.dot(vertices[i], direction);

            if(dist > best) {
                bestIndex = i;
                best = dist;
            }
        }

        return vertices[bestIndex];
    }

    /**
     * Gets the vertices of the polygon
     * @return An array containing the vertices
     */
    public Vector2f[] getVertices() { return vertices; }

    /**
     * Gets the number of vertices the polygon has
     * @return The vertex count
     */
    public int getVertexCount() { return vertexCount; }

    /**
     * Gets the drawable representation of the shape
     * @return The drawable polygon
     */
    public Drawable getDrawable() {
        return shape;
    }

    /**
     * Gets the type of the polygon shape
     * @return Always returns {@link Shape.Type#Polygon}
     */
    public Type getType() {
        return Type.Polygon;
    }

    /**
     * Calculates the centroid of a convex polygon using the polygonal centroid formula
     * @param vertices The vertices that make up the polygon
     * @param count The number of vertices within the polygon
     * @return The centroid of the polygon
     */
    private static Vector2f findCentroid(Vector2f[] vertices, int count) {
        float x = 0, y = 0;
        float area = 0;

        for(int i = 0, j = count - 1; i < count; j = i++) {
            Vector2f v0 = vertices[i];
            Vector2f v1 = vertices[j];

            float a = VUtil.cross(v0, v1);
            area += a;
            x += (v0.x + v1.x) * a;
            y += (v0.y + v1.y) * a;
        }

        area *= 0.5f;

        x /= (6f * area);
        y /= (6f * area);

        return new Vector2f(x, y);
    }

}
