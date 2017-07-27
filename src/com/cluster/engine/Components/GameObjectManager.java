package com.cluster.engine.Components;

import com.cluster.engine.Engine;
import com.cluster.engine.Utilities.Interfaces.EntityRenderable;
import com.cluster.engine.Utilities.Interfaces.Updateable;
import org.jsfml.graphics.CircleShape;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;

import java.util.Collection;
import java.util.Vector;

public final class GameObjectManager implements Updateable, EntityRenderable {

    private Vector<GameObject> gameObjects;

    public GameObjectManager() {
        gameObjects = new Vector<>();
    }

    public void update(float dt) {
        for(GameObject go : gameObjects) {
            Collection<Component> components = go.components.values();
            for(Component c : components) {
                c.update(dt);
            }
        }
    }

    public void render(RenderWindow renderer) {
        for(GameObject go : gameObjects) {
            if(Engine.DEBUG) {
                CircleShape cs = new CircleShape(3);
                cs.setOrigin(3, 3);
                cs.setPosition(go.getTransform().getPosition());
                cs.setFillColor(Color.RED);
                renderer.draw(cs);
            }

            Collection<Component> components = go.components.values();
            for(Component c : components) {
                c.render(renderer);
            }
        }
    }

    public GameObject createGameObject(String name) {
        GameObject result = new GameObject(name);
        gameObjects.add(result);

        return result;
    }

    public boolean removeGameObject(GameObject object) {
        boolean result = gameObjects.remove(object);
        if(!result) {
            System.err.println("Warning: A GameObject of the name \"" + object.name +  "\" was not present");
        }

        return result;
    }


}
