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

import com.cluster.engine.Components.Component;
import com.cluster.engine.Components.GameObject;
import com.cluster.engine.Components.Transform;
import com.cluster.engine.Physics.Shapes.Polygon;
import com.cluster.engine.Utilities.Maths.MUtil;
import com.cluster.engine.Utilities.Maths.VUtil;
import org.jsfml.system.Vector2f;

/**
 * Represents a physics body within the world
 * @author James Bulman
 */
public class RigidBody extends Component {

    // TODO(James): Allowing multiple shapes/ fixtures per body
    static final int MAX_FIXTURES = 4;

    // Positional
    private Polygon shape;
    private Transform transform;

    // Material
    private Material material;

    // MassData
    private MassData massData;

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
    private boolean isStatic;

    /**
     * Creates a new rigid body from the config given
     * @param config The configuration to make to body from
     */
    RigidBody(BodyConfig config, World world) {
        super("Body");

        if(config.shape == null)
            throw new IllegalArgumentException("Error: Shape cannot be null");

        isStatic = config.isStatic;
        if(isStatic) {
            material = new Material(config.restitution, 0,
                    config.dynamicFriction, config.staticFriction);

            velocity = new Vector2f(0, 0);
            angularVelocity = 0;
        }
        else {
            material = new Material(config.restitution, config.density,
                    config.dynamicFriction, config.staticFriction);

            velocity = config.velocity;
            angularVelocity = config.angularVelocity;
        }

        shape = config.shape;

        // TODO(James): Make this more robust and support circles
        Vector2f[] vertices = shape.getVertices();
        int vertexCount = shape.getVertexCount();

        float area = 0;

        for(int i = 0, j = vertexCount - 1; i < vertexCount; j = i++) {
            area += VUtil.cross(vertices[i], vertices[j]);
        }

        float radius = shape.getRadius();

        massData = new MassData(material.density * Math.abs(0.5f * area),
                (MUtil.PI / 2f) * (float) Math.pow(radius, 4));

        force = Vector2f.ZERO;
        torque = 0;

        alive = false;

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

        Vector2f acceleration = Vector2f.mul(force, massData.invMass);

        Vector2f dv = new Vector2f(acceleration.x * dth, acceleration.y * dth);
        velocity = Vector2f.add(velocity, dv);

        float omega = torque * massData.invInertia;
        angularVelocity += (omega * dt);

        Vector2f dx = new Vector2f(velocity.x * dt, velocity.y * dt);
        transform.setPosition(Vector2f.add(transform.getPosition(), dx));

        transform.move(dx);
        transform.getRotation().rotate(angularVelocity * dt);

        dv = new Vector2f(acceleration.x * dth, acceleration.y * dth);
        velocity = Vector2f.add(velocity, dv);
    }

    /**
     * Sets the force and the torque being applied to the body to zero
     */
    void resetForces() {
        force = Vector2f.ZERO;
        torque = 0;
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
        velocity = Vector2f.add(velocity, new Vector2f(impulse.x * massData.invMass, impulse.y * massData.invMass));
    }

    /**
     * Will return the speed of the body
     * @return the resultant speed of the body
     */
    public float getSpeed(){
        return (float)Math.sqrt(VUtil.lengthSq(velocity));
    }

    /**
     * Gets the shape which represents the body
     * @return The shape
     */
    public Polygon getShape() { return shape; }

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
     * Gets the material properties of the body
     * @return The material properties
     */
    public Material getMaterial() { return material; }

    /**
     * Gets the mass data of the body
     * @return The mass data
     */
    public MassData getMassData() { return massData; }

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

    public Transform getTransform() {
        return transform;
    }


    /**
     * Sets the linear velocity of the body
     * @param velocity The new linear velocity to set
     */
    public void setVelocity(Vector2f velocity) { this.velocity = velocity; }

    /**
     * Sets the GameObject this RigidBody is attached to and gets its transform
     * @param object The GameObject to attach this to
     */
    public void setGameObject(GameObject object) {
        super.setGameObject(object);
        if(object != null) {
            transform = object.getTransform();
            alive = true;
        }
        else {
            transform = null;
            alive = false;
        }
    }

    /**
     * Gets whether or not the body is static
     * @return True if the body is a static body, otherwise false
     */
    public boolean isStatic() { return isStatic; }

    @Override
    public Type getType() {
        return Type.RigidBody;
    }

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
        if(getGameObject() == null) return;
        this.alive = alive;
    }
}
