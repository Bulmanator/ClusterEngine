package com.cluster.engine.Components;

import com.cluster.engine.Utilities.Maths.Rotation;
import org.jsfml.system.Vector2f;

/**
 * The transform component which every GameObject will have
 */
public class Transform extends Component {

    // NOTE(James): Transform order is (Translate * Rotation * Scale)

    // The offset of the GameObject
    private Vector2f position;
    // The rotation of the GameObject
    private Rotation rotation;
    // TODO(James): Will it be possible to have scaling with rigid bodies
    // private Vector2f scale;

    /**
     * Creates a default transform, positioned at (0, 0) with a rotation of 0
     */
    public Transform() {
        super("Transform");

        position = Vector2f.ZERO;
        rotation = new Rotation();
    }

    /**
     * Applies the transform to the given point
     * @param point The point to apply the transform to
     * @return The transformed point
     */
    public Vector2f apply(Vector2f point) {
        return Vector2f.add(rotation.apply(point), position);
    }

    /**
     * Applies the inverse transform to the given point
     * @param point The point to apply the inverse transform to
     * @return The inversely transformed point
     */
    public Vector2f applyInverse(Vector2f point) {
        return rotation.applyInverse(Vector2f.sub(point, position));
    }

    // TODO(James): Scale getters & setters

    /**
     * Gets the current offset of the transform
     * @return The offset
     */
    public Vector2f getPosition() { return position; }

    /**
     * Gets the current rotation of the transform
     * @return The rotation
     */
    public Rotation getRotation() { return rotation; }

    /**
     * Sets the transform to the given offset and the given angle
     * @param position The offset to set the transform to
     * @param radians The angle to set the transform to, in radians
     */
    public void set(Vector2f position, float radians) {
        this.position = position;
        rotation.set(radians);
    }

    /**
     * Sets the offset to the given point
     * @param position The offset to set
     */
    public void setPosition(Vector2f position) { this.position = position; }

    /**
     * Moves the offset of the transform by the given distance
     * @param distance The distance to move
     */
    public void move(Vector2f distance) {
        position = Vector2f.add(position, distance);
    }

    /**
     * Formats the transform into a string
     * @return The string containing the position and rotation
     */
    public String toString() {
        return "Position: [" + position.x + ":" + position.y + "]\nRotation: "
                + rotation.getAngle();
    }
}
