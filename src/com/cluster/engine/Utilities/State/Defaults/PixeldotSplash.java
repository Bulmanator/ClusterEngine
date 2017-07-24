package com.cluster.engine.Utilities.State.Defaults;

import com.cluster.engine.Utilities.State.GameStateManager;
import com.cluster.engine.Utilities.State.State;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

/**
 * @author Matthew Threlfall
 */
public class PixeldotSplash extends State{
    private float logoTime;
    private float duration;
    private float angle;
    private int textAlpha;
    private State next;

    public PixeldotSplash(GameStateManager gsm, Vector2f worldSize) {
        super(gsm, worldSize);
        logoTime = 0;
        textAlpha = 0;
        duration = 1f;
        angle = 0;
        //next = nextState;
    }

    @Override
    public void update(float dt) {

        if (logoTime < duration) {
            angle = -90*logoTime/duration;
            logoTime += dt;
        }
        else {
            textAlpha += 200*dt;
            textAlpha = Math.min(textAlpha, 255);
            System.out.println(textAlpha);
        }
        if (textAlpha == 255) {
            //gsm.setState(next);
        }
    }


    @Override
    public void render() {
        window.clear();
        RenderStates rs = new RenderStates(BlendMode.ADD);
        RectangleShape r = new RectangleShape();
        Vector2f size = new Vector2f(600,600);

        r.setPosition(worldSize.x/2, worldSize.y/2);
        r.setOrigin(size.x/2, size.y/2);
        r.setSize(size);
        r.setFillColor(new Color(255,0,0, 200));
        r.setRotation(angle);
        window.draw(r, rs);

        r = new RectangleShape();
        r.setFillColor(new Color(0,0,255, 200));
        r.setPosition(worldSize.x/2, worldSize.y/2);
        r.setOrigin(size.x/2, size.y/2);
        r.setSize(size);
        r.setRotation(angle - (90*(logoTime/duration)/3));
        window.draw(r, rs);

        r = new RectangleShape();
        r.setFillColor(new Color(0,255,0,200));
        r.setPosition(worldSize.x/2, worldSize.y/2);
        r.setOrigin(size.x/2, size.y/2);
        r.setSize(size);
        r.setRotation(angle - (90*(logoTime/duration)/3*2));
        window.draw(r, rs);
    }

    @Override
    public void dispose() {

    }
}
