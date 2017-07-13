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

import com.cluster.engine.Physics.Collisions.Manifold;
import com.cluster.engine.Physics.Shapes.AABB;
import com.cluster.engine.Utilities.Interfaces.EntityRenderable;
import com.cluster.engine.Utilities.Interfaces.Updateable;
import com.cluster.engine.Utilities.MUtil;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

import java.util.Vector;

/**
 * a Class which represents the physics world and updates any body added to it
 * @author James
 */
public class World implements Updateable, EntityRenderable {

    /** Whether or not the debug rendering should draw AABBs */
    public static boolean DRAW_AABB = false;
    /** Whether or not the debug rendering should draw bodies */
    public static boolean DRAW_BODIES = true;
    /** Whether or not the debug rendering should draw velocities */
    public static boolean DRAW_VELOCITIES = false;

    /** The colour of the bodies drawn by the debug rendering */
    public static Color BODY_COLOUR = Color.RED;
    /** The colour of the AABBs drawn by the debug rendering */
    public static Color AABB_COLOUR = Color.BLUE;
    /** The colour of the velocities drawn by the debug rendering */
    public static Color VELOCITY_COLOUR = Color.GREEN;

    /** The gravity of the World that should act on the bodies */
    private Vector2f gravity;

    /** a vector of all of the bodies currently registered to the World */
    private Vector<RigidBody> bodies;

    /** a vector which contains all manifolds which require narrow phase collision checking */
    private Vector<Manifold> manifolds;

    /**
     * Constructs a new physics world with the gravity applied
     * @param gravity The gravity of the world
     */
    public World(Vector2f gravity) {
        this.gravity = gravity;
        bodies = new Vector<>();
        manifolds = new Vector<>();
    }

    /**
     * Updates all rigid bodies which are present in the world and tests for collisions
     * @param dt The amount of time passed since last frame
     */
    public void update(float dt) {

        // Evaluate manifolds of bodies to test for collisions
        for (int i = 0; i < bodies.size(); i++) {
            RigidBody a = bodies.get(i);
            if (!a.isAlive()) continue;

            for (int j = i + 1; j < bodies.size(); j++) {
                RigidBody b = bodies.get(j);
                if (!b.isAlive()) continue;

                Manifold manifold = new Manifold(a, b);
                manifold.solve();

                if (manifold.collided) {
                    manifolds.add(manifold);
                }
            }
        }

        // Apply any manifolds which did collide
        for (int i = 0; i < 6; i++) {
            for (Manifold manifold : manifolds) {
                manifold.apply();
            }
        }

        // Update body forces and positions
        for (RigidBody body : bodies) {
            // Apply gravity
            body.applyForce(new Vector2f(gravity.x * body.getMassData().mass,
                    gravity.y * body.getMassData().mass));
            body.update(dt);
            // Reset any forces being applied to the body
            body.resetForces();
        }

        // Correct positions of any bodies which collided
        for (Manifold manifold : manifolds) {
            manifold.correctPosition();
        }

        // Clear all collision manifolds
        manifolds.clear();
    }

    /**
     * Draws bodies to the screen, used for debugging
     * @param renderer The {@link RenderWindow} to draw the entity to
     */
    public void render(RenderWindow renderer) {
        if(!(DRAW_AABB || DRAW_BODIES || DRAW_VELOCITIES)) return;

        for(RigidBody body : bodies) {
            if(DRAW_BODIES) {
                Shape shape = (Shape) body.getShape().getDrawable();
                shape.setFillColor(Color.TRANSPARENT);
                shape.setOutlineColor(BODY_COLOUR);
                shape.setOutlineThickness(-1f);

                shape.setPosition(body.getTransform().getPosition());
                shape.setRotation(body.getTransform().getAngle() * MUtil.RAD_TO_DEG);

                renderer.draw(shape);
            }

            if(DRAW_AABB) {
                AABB aabb = new AABB(body.getShape().getVertices(), body.getShape().getVertexCount());
                RectangleShape shape = new RectangleShape(Vector2f.sub(aabb.getMaximum(), aabb.getMinimum()));
                shape.setPosition(body.getTransform().getPosition());
                shape.setFillColor(Color.TRANSPARENT);
                shape.setOutlineColor(AABB_COLOUR);
                shape.setOutlineThickness(1);

                renderer.draw(shape);
            }

            if(DRAW_VELOCITIES) {
                Vector2f vel = body.getVelocity();
                Vector2f pos = body.getTransform().getPosition();

                Vector2f length = new Vector2f(pos.x + (vel.x), pos.y + (vel.y));

                Vertex[] line = new Vertex[] { new Vertex(pos, VELOCITY_COLOUR), new Vertex(length, VELOCITY_COLOUR) };

                renderer.draw(line, PrimitiveType.LINES);
            }
        }
    }

    /**
     * Removes a body from the world
     * @param body The body to remove
     * @return True if the body is successfully removed, otherwise false
     */
    public boolean removeBody(RigidBody body) {
        return bodies.remove(body);
    }

    /**
     * Creates a new body from the given configuration
     * @param config The configuration to make the body from
     * @return The body instance which was created
     */
    public RigidBody createBody(BodyConfig config) {
        RigidBody rb = new RigidBody(config, this);
        bodies.add(rb);

        return rb;
    }

    /**
     * Clears all of the bodies in the world
     */
    public void clearBodies() {
        manifolds.clear();
        bodies.clear();
    }
}
