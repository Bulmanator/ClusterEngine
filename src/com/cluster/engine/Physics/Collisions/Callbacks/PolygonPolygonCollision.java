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
package com.cluster.engine.Physics.Collisions.Callbacks;

import com.cluster.engine.Physics.Collisions.Manifold;
import com.cluster.engine.Physics.Shapes.Polygon;
import com.cluster.engine.Physics.Shapes.Shape;
import com.cluster.engine.Physics.Transform;
import com.cluster.engine.Utilities.MUtil;
import com.cluster.engine.Utilities.VUtil;
import org.jsfml.system.Vector2f;

class PolygonPolygonCollision implements CollisionHandler {

    /**
     * a helper class to store the vertices
     */
    public static class Simplex {
        public Vector2f[] vertices;
        public int vertexCount;

        public Vector2f direction;

        public Simplex() {
            vertices = new Vector2f[3];
            vertexCount = 0;

            direction = null;
        }
    }

    /**
     * a helper class to store an edge representation for EPA
     */
    public static class Edge {
        public Vector2f normal;
        public int index;
        public float distance;

        public Edge() {
            normal = null;
            index = 0;
            distance = Float.MAX_VALUE;
        }
    }

    // TODO(James): Maybe this can go elsewhere and be modifiable
    static final int MAX_ITERATIONS = 30;

    /**
     * Performs narrow-phase GJK collision detection for two convex polygons
     * @param manifold The manifold to hold information about the collision
     * @param a The first shape to test for collision
     * @param b The second shape to test for collision
     */
    public void handleCollision(Manifold manifold, Shape a, Shape b) {

        Polygon polygonA = (Polygon) a;
        Polygon polygonB = (Polygon) b;

        Transform txA = manifold.a.getTransform();
        Transform txB = manifold.b.getTransform();

        Simplex simplex = new Simplex();

        simplex.direction = Vector2f.sub(txB.getPosition(), txA.getPosition());


        Vector2f pA = support(polygonA,
                txA.applyRotation(simplex.direction));

        pA = txA.apply(pA);

        Vector2f pB = support(polygonB,
                txB.applyRotation(Vector2f.neg(simplex.direction)));

        pB = txB.apply(pB);

        simplex.vertices[simplex.vertexCount] = Vector2f.sub(pA, pB);
        simplex.vertexCount++;


        if(VUtil.dot(simplex.vertices[0], simplex.direction) < 0) {
            manifold.collided = false;
            manifold.normal = null;
            manifold.overlap = 0;

            return;
        }

        simplex.direction = Vector2f.neg(simplex.direction);

        for(int iter = 0; iter < MAX_ITERATIONS; iter++) {
            pA = support(polygonA, txA.applyRotation(simplex.direction));
            pA = txA.apply(pA);

            pB = support(polygonB, txB.applyRotation(Vector2f.neg(simplex.direction)));
            pB = txB.apply(pB);

            simplex.vertices[simplex.vertexCount] = Vector2f.sub(pA, pB);
            simplex.vertexCount++;

            if(VUtil.dot(simplex.vertices[simplex.vertexCount - 1],
                    simplex.direction) < 0) {

                manifold.collided = false;
                manifold.normal = null;
                manifold.overlap = 0;

                return;
            }
            else {
                if(contains(simplex)) {
                    manifold.collided = true;
                    handleEPA(manifold, simplex, polygonA, polygonB);
                    return;
                }
            }
        }
    }

    /**
     * The support method for GJK, gets the farthest point on a polygon in the given direction
     * @param polygon The polygon which is part of the detection
     * @param direction The direction to search in
     * @return The farthest vertex along the given direction
     */
    private Vector2f support(Polygon polygon, Vector2f direction) {

        Vector2f[] vertices = polygon.getVertices();
        int count = polygon.getVertexCount();

        float max = VUtil.dot(vertices[0], direction);
        int bestIndex = 0;

        for(int i = 1; i < count; i++) {
            float distance = VUtil.dot(vertices[i], direction);
            if(distance > max) {
                max = distance;
                bestIndex = i;
            }
        }

        return vertices[bestIndex];
    }

    /**
     * Works out if the simplex contains the origin or not, if not augments the vertices and changes direction
     * @param simplex The simplex created by GJK
     * @return True if the origin is contained within the vertices, otherwise False
     */
    private boolean contains(Simplex simplex) {

        Vector2f a = simplex.vertices[simplex.vertexCount - 1];
        Vector2f ao = Vector2f.neg(a);

        if(simplex.vertexCount == 3) {
            Vector2f b = simplex.vertices[1];
            Vector2f c = simplex.vertices[0];

            Vector2f ab = Vector2f.sub(b, c);
            Vector2f ac = Vector2f.sub(c, a);

            if(VUtil.dot(VUtil.tripleCross(ab, ac, ac), ao) > 0) {
                if(VUtil.dot(ac, ao) > 0) {
                    simplex.vertices[1] = a;
                    simplex.vertexCount--;
                    simplex.direction = VUtil.tripleCross(ac, ao, ac);
                }
                else {
                    if(VUtil.dot(ab, ao) > 0) {
                        simplex.vertices[0] = b;
                        simplex.vertices[1] = a;
                        simplex.vertexCount--;
                        simplex.direction = VUtil.tripleCross(ab, ao, ab);
                    }
                    else {
                        simplex.vertices[0] = a;
                        simplex.vertexCount = 1;
                        simplex.direction = ao;
                    }
                }
            }
            else {
                if(VUtil.dot(VUtil.tripleCross(ab, ab, ac), ao) > 0) {
                    if(VUtil.dot(ab, ao) > 0) {
                        simplex.vertices[0] = b;
                        simplex.vertices[1] = a;
                        simplex.vertexCount--;
                        simplex.direction = VUtil.tripleCross(ab, ao, ab);
                    }
                    else {
                        simplex.vertices[0] = a;
                        simplex.vertexCount = 1;
                        simplex.direction = ao;
                    }
                }
                else {
                    return true;
                }

            }
        }
        else {
            Vector2f b = simplex.vertices[0];
            Vector2f ab = Vector2f.sub(b, a);

            if(VUtil.dot(ab, ao) > 0) {
                simplex.direction = VUtil.tripleCross(ab, ao, ab);
            }
            else {
                simplex.vertices[0] = a;
                simplex.vertexCount = 1;
                simplex.direction = ao;
            }
        }

        return false;
    }

    /**
     * Perform EPA to find the MTV and penetration distance
     * @param manifold The manifold to store the calculated information
     * @param simplex The simplex created by GJK
     */
    private void handleEPA(Manifold manifold, Simplex simplex, Polygon a, Polygon b) {

        Transform txA = manifold.a.getTransform();
        Transform txB = manifold.b.getTransform();

        for(int iter = 0; iter < MAX_ITERATIONS; iter++) {

            Edge edge = getClosestEdge(simplex);
            if(edge.normal.equals(Vector2f.ZERO)) {
                manifold.collided = false;
                return;
            }

            Vector2f pA = support(a, edge.normal);
            pA = txA.apply(pA);

            Vector2f pB = support(b, Vector2f.neg(edge.normal));
            pB = txB.apply(pB);

            Vector2f p = Vector2f.sub(pA, pB);

            float dist = VUtil.dot(p, edge.normal);
            if(MUtil.isZero(dist)) {
                manifold.collided = false;
                manifold.normal = null;
                manifold.overlap = 0;
                return;
            }

            if(MUtil.isZero(dist - edge.distance, 0.00001f)) {
                manifold.normal = edge.normal;
                manifold.overlap = dist;
                return;
            }
            else {
                Vector2f[] tmp = simplex.vertices;
                simplex.vertices = new Vector2f[++simplex.vertexCount];

                for(int i = 0; i < simplex.vertexCount; i++) {
                    if(i == edge.index) {
                        simplex.vertices[i] = p;
                    }
                    else if(i > edge.index) {
                        simplex.vertices[i] = tmp[i - 1];
                    }
                    else {
                        simplex.vertices[i] = tmp[i];
                    }
                }
            }
        }
    }

    /**
     * Finds the edge of the simplex which is closest to the bounding area of the Minkowski sum
     * @param simplex The simplex created by GJK
     * @return The edge which is closest to the bounding area
     */
    private Edge getClosestEdge(Simplex simplex) {
        Edge result = new Edge();

        for(int i = 0; i < simplex.vertexCount; i++) {

            int j = i + 1 == simplex.vertexCount ? 0 : i + 1;

            Vector2f a = simplex.vertices[i];
            Vector2f b = simplex.vertices[j];

            if(a.equals(b)) continue;

            Vector2f e = Vector2f.sub(b, a);

            Vector2f norm = new Vector2f(e.y, -e.x);
            norm = VUtil.normalise(norm);


            float d = VUtil.dot(norm, a);
            if(d < result.distance) {
                result.normal = norm;
                result.index = j;
                result.distance = d;
            }
        }

        return result;
    }
}
