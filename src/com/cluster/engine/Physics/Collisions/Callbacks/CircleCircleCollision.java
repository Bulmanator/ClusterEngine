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

import com.cluster.engine.Components.Transform;
import com.cluster.engine.Physics.Collisions.Manifold;
import com.cluster.engine.Physics.Shapes.Circle;
import com.cluster.engine.Physics.Shapes.Shape;
import com.cluster.engine.Utilities.Maths.MUtil;
import com.cluster.engine.Utilities.Maths.VUtil;
import org.jsfml.system.Vector2f;

class CircleCircleCollision implements CollisionHandler {

    public void handleCollision(Manifold manifold, Shape a, Shape b) {

        Circle circleA = (Circle) a;
        Circle circleB = (Circle) b;

        Transform txA = manifold.a.getTransform();
        Transform txB = manifold.b.getTransform();

        Vector2f pos = Vector2f.sub(txB.getPosition(), txA.getPosition());
        float dist = VUtil.lengthSq(pos);

        float radiusSum = circleA.getRadius() + circleB.getRadius();

        if(dist <= radiusSum * radiusSum) {
            manifold.collided = true;
            manifold.normal = VUtil.normalise(pos);
            manifold.overlap = radiusSum - MUtil.sqrt(dist);
        }
        else {
            manifold.collided = false;
            manifold.normal = null;
            manifold.overlap = 0;
        }
    }
}
