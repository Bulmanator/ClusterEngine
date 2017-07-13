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
package com.cluster.engine.Physics.Collisions;

import com.cluster.engine.Physics.Collisions.Callbacks.CollisionJumpTable;
import com.cluster.engine.Physics.MassData;
import com.cluster.engine.Physics.Material;
import com.cluster.engine.Physics.RigidBody;
import com.cluster.engine.Physics.Shapes.AABB;
import com.cluster.engine.Physics.Shapes.Polygon;
import com.cluster.engine.Physics.Transform;
import com.cluster.engine.Utilities.MUtil;
import com.cluster.engine.Utilities.VUtil;
import org.jsfml.system.Vector2f;

/**
 * A small structure to hold relevant contact information
 */
public class Manifold {

    /** The first {@link RigidBody} to be involved in the collision */
    public RigidBody a;
    /** The second {@link RigidBody} to be involved in the collision */
    public RigidBody b;

    /** Whether or not there was a collision */
    public boolean collided;

    /** The penetration normal used for resolution */
    public Vector2f normal;
    /** The amount the two bodies overlapped */
    public float overlap;

    // TODO(James): Contact points via clipping
    private Vector2f[] contactPoints;
    private int contactPointCount;

    public Manifold(RigidBody a, RigidBody b) {
        this.a = a;
        this.b = b;

        collided = false;

        normal = null;
        overlap = 0;

        contactPoints = new Vector2f[2];
        contactPointCount = 0;
    }

    public void solve() {
        Polygon a = this.a.getShape();
        Polygon b = this.b.getShape();

        AABB aabbA = new AABB(a.getVertices(), a.getVertexCount());
        AABB aabbB = new AABB(b.getVertices(), b.getVertexCount());

        aabbA.transform(this.a.getTransform());
        aabbB.transform(this.b.getTransform());

        if(!AABB.overlaps(aabbA, aabbB)) {
            collided = false;
            return;
        }

        int ia = a.getType().index;
        int ib = b.getType().index;

        CollisionJumpTable.handlers[ia][ib].handleCollision(this, a, b);
    }

    public void apply() {
        Material materialA = a.getMaterial();
        Material materialB = b.getMaterial();

        MassData massA = a.getMassData();
        MassData massB = b.getMassData();

        if (normal == null)
            return;

        Transform txA = a.getTransform();
        Transform txB = b.getTransform();

        Vector2f centre = Vector2f.sub(txA.getPosition(), txB.getPosition());
        if(VUtil.dot(centre, normal) > 0) {
            normal = Vector2f.neg(normal);
        }

        // Choose which restitution value to use
        float e = Math.min(materialA.restitution, materialB.restitution);

        // Calculate the relative velocity between both bodies
        Vector2f rv = Vector2f.sub(b.getVelocity(), a.getVelocity());

        // If they are not moving towards each other then don't apply collision
        if(VUtil.dot(rv, normal) > 0) return;

        // Work out the magnitude of the impulse
        float impulseMag = -(1 + e) * (VUtil.dot(rv, normal));
        impulseMag /= (massA.invMass + massB.invMass);

        // Work out the actual impulse to apply
        Vector2f impulse = new Vector2f(normal.x * impulseMag, normal.y * impulseMag);

        // Apply Impulse to body a
        a.applyImpulse(Vector2f.neg(impulse));

        // Apply Impulse to body b
        b.applyImpulse(impulse);

        Vector2f tangent = Vector2f.sub(rv, Vector2f.mul(normal, VUtil.dot(rv, normal)));
        tangent = VUtil.normalise(tangent);

        float frictionMag = -VUtil.dot(rv, tangent);
        frictionMag /= (massA.invMass + massB.invMass);

        float staticFriction = MUtil.sqrt((materialA.staticFriction * materialA.staticFriction)
                + (materialB.staticFriction * materialB.staticFriction));

        Vector2f frictionImpulse;
        if(Math.abs(frictionMag) < impulseMag * staticFriction) {
            frictionImpulse = Vector2f.mul(tangent, frictionMag);
        }
        else {
            float dynamicFriction = MUtil.sqrt((materialA.dynamicFriction * materialA.dynamicFriction)
                    + (materialB.dynamicFriction * materialB.dynamicFriction));

            frictionImpulse = Vector2f.mul(tangent, -impulseMag * dynamicFriction);
        }

        a.applyImpulse(Vector2f.neg(frictionImpulse));
        b.applyImpulse(frictionImpulse);
    }

    /**
     * This deals with objects that sink into each other<br>
     * More visible when dealing with very small bodies colliding with very large bodies, or bodies resting on static ones
     */
    public void correctPosition() {
        if(normal == null)
            return;

        MassData massA = a.getMassData();
        MassData massB = b.getMassData();

        float correctionVal = (Math.max(overlap - 0.01f, 0.0f) / (massA.invMass + massB.invMass)) * 0.2f;

        Vector2f correction = new Vector2f(correctionVal * normal.x, correctionVal * normal.y);

        Vector2f positionA = Vector2f.sub(a.getTransform().getPosition(),
                new Vector2f(correction.x * massA.invMass, correction.y * massA.invMass));

        Vector2f positionB = Vector2f.add(b.getTransform().getPosition(),
                new Vector2f(correction.x * massB.invMass, correction.y * massB.invMass));

        a.setTransform(positionA, a.getTransform().getAngle());
        b.setTransform(positionB, b.getTransform().getAngle());
    }
}
