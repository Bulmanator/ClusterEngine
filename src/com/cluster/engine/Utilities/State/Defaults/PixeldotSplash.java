package com.cluster.engine.Utilities.State.Defaults;

import com.cluster.engine.Utilities.ContentManager;
import com.cluster.engine.Utilities.MathUtil;
import com.cluster.engine.Utilities.State.GameStateManager;
import com.cluster.engine.Utilities.State.State;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Mouse;

/**
 * @author Matthew Threlfall
 */
public class PixeldotSplash extends State{
    private float logoTime;
    private float duration;
    private float angle;
    private int textAlpha;
    private State next;
    private Font f;

    public PixeldotSplash(GameStateManager gsm, Vector2f worldSize) {
        super(gsm, worldSize);
        logoTime = 0.05f;
        textAlpha = 0;
        duration = 1f;
        angle = 0;
        ContentManager.getInstance().loadFont("BigNoodle","big_noodle_titling.ttf");
        f = ContentManager.getInstance().getFont("BigNoodle");
        //next = nextState;
    }

    @Override
    public void update(float dt) {

        if (logoTime < duration) {
            angle = -180*logoTime/duration;
            logoTime += dt* MathUtil.sin(180*MathUtil.DEG_TO_RAD*logoTime/duration);
        }
        if (logoTime > duration*0.8f){
            textAlpha += 255*dt;
            textAlpha = Math.min(textAlpha, 255);
        }
        if (textAlpha == 255) {
            //gsm.setState(next);
        }
        if(Mouse.isButtonPressed(Mouse.Button.LEFT) || Keyboard.isKeyPressed(Keyboard.Key.SPACE)) {
            textAlpha = 255;
            logoTime = duration;
            angle = -180;
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

        Text t = new Text("Pixeldot", f);
        t.setCharacterSize(180);
        t.setColor(new Color(0,0,0,textAlpha));
        t.setPosition(worldSize.x/2 - t.getLocalBounds().width/2-10, worldSize.y/2 - t.getLocalBounds().height*1.5f);
        window.draw(t);

        t = new Text("studios", f);
        t.setCharacterSize(180);
        t.setColor(new Color(0,0,0,textAlpha));
        t.setPosition(worldSize.x/2 - t.getLocalBounds().width/2-10, worldSize.y/2);
        window.draw(t);
    }

    @Override
    public void dispose() {

    }
}
