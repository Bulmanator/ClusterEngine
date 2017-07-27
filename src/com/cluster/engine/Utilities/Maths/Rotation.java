package com.cluster.engine.Utilities.Maths;

import org.jsfml.system.Vector2f;

/**
 * Represents a given rotation
 */
public class Rotation {

    // The angle the rotation is currently at, in radians
    private float angle;
    // The sine and cosine values for the current angle
    private float sin, cos;

    /**
     * Creates a default rotation at an angle of 0
     */
    public Rotation() {
        angle = 0;
        sin = 0;
        cos = 1;
    }

    /**
     * Creates a rotation at the given angle
     * @param radians The angle of the rotation, in radians
     */
    public Rotation(float radians) {
        angle = radians;
        sin = MUtil.sin(radians);
        cos = MUtil.cos(radians);
    }

    /**
     * Applies the rotation to the given point
     * @param point The point to rotate
     * @return The point rotated by the current angle
     */
    public Vector2f apply(Vector2f point) {
        float x = point.x * cos - point.y * sin;
        float y = point.x * sin + point.y * cos;

        return new Vector2f(x, y);
    }

    public Vector2f applyInverse(Vector2f point) {
        float x =  cos * point.x + sin * point.y;
        float y = -sin * point.x + point.y * cos;

        return new Vector2f(x, y);
    }

    /**
     * Gets the current angle of the rotation, in radians
     * @return The current angle
     */
    public float getAngle() { return angle; }

    /**
     * Gets the sine value of the current angle
     * @return The value of sine
     */
    public float getSin() { return sin; }

    /**
     * Gets the cosine value of the current angle
     * @return The value of cosine
     */
    public float getCos() { return cos; }

    /**
     * Sets the rotation to the given angle
     * @param radians The angle to set to, in radians
     */
    public void set(float radians) {
        angle = radians;
        if(angle < 0) angle += MUtil.PI2;
        else if(angle > MUtil.PI2) angle -= MUtil.PI2;

        sin = MUtil.sin(angle);
        cos = MUtil.cos(angle);
    }

    /**
     * Rotates the rotation by the given angle
     * @param radians The angle to rotate by, in radians
     */
    public void rotate(float radians) {
        angle += radians;
        if(angle < 0) angle += MUtil.PI2;
        else if(angle > MUtil.PI2) angle -= MUtil.PI2;

        sin = MUtil.sin(angle);
        cos = MUtil.cos(angle);
    }
}
