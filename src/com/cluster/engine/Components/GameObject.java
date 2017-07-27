package com.cluster.engine.Components;

import com.cluster.engine.Physics.RigidBody;

import java.util.HashMap;

public class GameObject {

    public final String name;

    private Transform tx;
    private RigidBody body;
    HashMap<String, Component> components;

    GameObject(String name) {
        this.name = name;

        // Every GameObject has a transform
        tx = new Transform();
        tx.setGameObject(this);

        components = new HashMap<>();
        body = null;
    }

    public void addComponent(Component component) {
        component.setGameObject(this);
        if(component.getType() == Component.Type.RigidBody) {
            if(body != null) {
                removeComponent(body.name);
            }
            body = (RigidBody) component;
        }
        else if(component.getType() != Component.Type.Transform) {
            components.put(component.name, component);
        }
    }

    public Component removeComponent(String name) {
        Component result;

        if(body != null && body.name.equals(name)) {
            body.setAlive(false);
            body.setGameObject(null);
            result = body;
            body = null;
        }
        else {

            if(!components.containsKey(name)) {
                throw new IllegalArgumentException("Error: Failed to remove component with the name \""
                        + name + "\" as the GameObject did not contain it");
            }

            result = components.remove(name);
        }

        return result;
    }

    public Component getComponent(String name) {
        if(!components.containsKey(name))
            throw new IllegalArgumentException("Error: A component with the name \""
                    + name + "\" did not exist on the GameObject");

        return components.get(name);
    }

    public Transform getTransform() { return tx; }
    public RigidBody getBody() { return body; }

    public HashMap<String, Component> getComponents() { return components; }
}
