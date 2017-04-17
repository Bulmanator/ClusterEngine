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

import com.cluster.engine.Utilities.Interfaces.Updateable;
import com.cluster.engine.Utilities.MathUtil;
import org.jsfml.system.Vector2f;

/**
 * Represents a physics body within the world
 * @author James Bulman
 */
public class RigidBody implements Updateable {

    // Positional
    private Polygon shape;
    private Transform transform;

    // Material
    private float density;
    private float restitution;

    // Mass Data
    private float mass;
    private float invMass;

    private float inertia;
    private float invInertia;

    // Movement
    private Vector2f velocity;
    private float angularVelocity;

    // Forces
    private Vector2f force;
    private float torque;

    // Others
    private boolean alive;

    // The world the body is contained in
    private World world;

    // Collision Data
    private int mask;
    private int category;

    /**
     * Creates a new rigid body from the config given
     * @param config The configuration to make to body from
     */
    RigidBody(BodyConfig config, World world) {

        if(config.shape == null)
            throw new IllegalArgumentException("Error: Shape cannot be null");

        transform = new Transform();
        transform.setPosition(config.position);

        density = config.density;
        restitution = config.restitution;

        velocity = config.velocity;
        angularVelocity = config.angularVelocity;

        shape = config.shape;
        shape.initialise(this);

        force = Vector2f.ZERO;
        torque = 0;

        alive = true;

        mask = config.mask;
        category = config.category;

        this.world = world;
    }

    /**
     * Updates the body one iteration
     * @param dt The amount of time passed since last frame
     */
    public void update(float dt) {
        if(!alive) return;

        float dth = dt * 0.5f;

        Vector2f acceleration = Vector2f.mul(force, invMass);

        Vector2f dv = new Vector2f(acceleration.x * dth, acceleration.y * dth);
        velocity = Vector2f.add(velocity, dv);

        float omega = torque * invInertia;
        angularVelocity += (omega * dt);

        Vector2f dx = new Vector2f(velocity.x * dt, velocity.y * dt);
        transform.setPosition(Vector2f.add(transform.getPosition(), dx));

        transform.setAngle(transform.getAngle() + (angularVelocity * dt));

        dv = new Vector2f(acceleration.x * dth, acceleration.y * dth);
        velocity = Vector2f.add(velocity, dv);
    }

    /**
     * Sets the force and the torque being applied to the body to zero
     */
    public void resetForces() {
        force = Vector2f.ZERO;
        torque = 0;
        shape.reset();
    }

    /**
     * Applies the force given to the body
     * @param force The force to apply
     */
    public void applyForce(Vector2f force){
        this.force = Vector2f.add(this.force, force);
    }

    /**
     * Applies an impulse on the body, this directly modifies the velocity
     * @param impulse The impulse to apply
     */
    public void applyImpulse(Vector2f impulse) {
        velocity = Vector2f.add(velocity, new Vector2f(impulse.x * invMass, impulse.y * invMass));
    }

    /**
     * Sets the body to the given position and rotates it to the given angle
     * @param position The position to set the body to
     * @param angle The angle to set the body to
     */
    public void setTransform(Vector2f position, float angle) {
        transform.setPosition(position);
        transform.setAngle(angle);
    }

    /**
     * Takes the current resultant speed of the body and rotates it by the given angle
     * @param angle the angle to rotate the velocity by
     */
    public void rotateVelocity(float angle) {
        float x, y, total;

        total = (float)Math.sqrt(MathUtil.lengthSq(velocity));
        x = total * (float)Math.cos(angle - MathUtil.PI / 2);
        y = total * (float)Math.sin(angle - MathUtil.PI / 2);
        velocity = new Vector2f(x, y);
    }

    /**
     * Will return the speed of the body
     * @return the resultant speed of the body
     */
    public float getSpeed(){
        return (float)Math.sqrt(MathUtil.lengthSq(velocity));
    }

    /**
     * Gets the shape which represents the body
     * @return The shape
     */
    public Polygon getShape() { return shape; }

    /**
     * Gets the transform of the body
     * @return The transform
     */
    public Transform getTransform() { return transform; }

    /**
     * Gets the current linear velocity of the body
     * @return The linear velocity
     */
    public Vector2f getVelocity() { return velocity; }

    /**
     * Gets the current angular velocity of the body
     * @return The angular velocity
     */
    public float getAngularVelocity() { return angularVelocity; }

    /**
     * Gets the density of the body
     * @return The density
     */
    public float getDensity() { return density; }

    /**
     * Gets the the restitution of the body
     * @return How bouncy the body is
     */
    public float getRestitution() { return restitution; }

    /**
     * Gets the mass of the body
     * @return The mass
     */
    public float getMass() { return mass; }

    /**
     * Gets the reciprocal of the mass
     * @return 1 / {@link #getMass()}
     */
    public float getInvMass() { return invMass; }

    /**
     * Gets the inertia of the body
     * @return The inertia
     */
    public float getInertia() { return inertia; }

    /**
     * Gets the reciprocal of the inertia
     * @return 1 / {@link #getInertia()}
     */
    public float getInvInertia() { return invInertia; }

    /**
     * Whether or not the body is alive
     * @return True if the body is alive, otherwise false
     */
    public boolean isAlive() { return alive; }

    /**
     * Gets the mask which defines what can collide with this
     * @return The bit mask
     */
    public int getMask() { return mask; }

    /**
     * Gets the mask which defines what this can collide with
     * @return The category
     */
    public int getCategory() { return category; }

    /**
     * Gets the world the body is contained within
     * @return The world
     */
    public World getWorld() { return world; }

    /**
     * Sets the mass of the body to the value specified
     * @param mass The new mass to set
     */
    void setMass(float mass) {
        this.mass = mass;

        if(mass != 0) {
            invMass = 1f / mass;
        }
        else  {
            invMass = 0;
        }
    }

    /**
     * Sets the inertia to the value specified
     * @param inertia The new inertia to set
     */
    void setInertia(float inertia) {
        this.inertia = inertia;

        if(inertia != 0) {
            invInertia = 1f / inertia;
        }
        else {
            invInertia = 0;
        }
    }

    /**
     * Sets the linear velocity of the body
     * @param velocity The new linear velocity to set
     */
    public void setVelocity(Vector2f velocity) { this.velocity = velocity; }

    /**
     * Sets the angular veloctiy of the body
     * @param angularVelocity The new angular velocity to ser
     */
    public void setAngularVelocity(float angularVelocity) { this.angularVelocity = angularVelocity; }

    /**
     * Sets whether or not the body is alive, does nothing if the data has not been set
     * @param alive True to set the body to alive, false for not alive
     */
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
