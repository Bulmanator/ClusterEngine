package com.cluster.engine.Components;


import com.cluster.engine.Utilities.Interfaces.EntityRenderable;
import com.cluster.engine.Utilities.Interfaces.Updateable;
import org.jsfml.graphics.RenderWindow;

public abstract class Component implements Updateable, EntityRenderable {

    /**
     * The type of component
     */
    public enum Type {
        /** Transform component, for positional and rotational information */
        Transform,
        /** Rigid Body component, for physics */
        RigidBody,
        /** Particle emitter component, for the particle systems */
        ParticleEmitter,
        /** Animation component */
        Animation,
        /** User created components */
        User
    }

    // The name of the component
    public final String name;
    // The GameObject which the component is attached to
    private GameObject gameObject;

    /**
     * Sets the name of the component so it can be retrieved later
     * @param name The name of the component
     */
    protected Component(String name) {
        this.name = name;
    }

    public void update(float dt) {}
    public void render(RenderWindow renderer) {}

    public void setGameObject(GameObject object) {
        gameObject = object;
    }

    public GameObject getGameObject() { return gameObject; }

    public Type getType() { return Type.User; }

    public String getName() { return name; }
}
