import com.cluster.engine.Engine;
import com.cluster.engine.Game;
import com.cluster.engine.Network.Server;
import com.cluster.engine.Utilities.EngineConfig;
import com.cluster.engine.Utilities.State.Defaults.PixeldotSplash;
import com.cluster.engine.Utilities.State.GameStateManager;
import org.jsfml.system.Vector2f;

import java.io.IOException;


/**
 *
 */

public class driver extends Game {
    GameStateManager gsm;

    public static void main(String[] args) {
        EngineConfig c = new EngineConfig();
        c.width = 1920;
        c.height = 1080;
        c.fpsLimit = 60;
        //new Engine(new driver(), c);

        try {
            Server s = new Server(10000, 1);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initialise() {
        gsm = new GameStateManager(this);
        gsm.addState(new PixeldotSplash(gsm, new Vector2f(1920,1080)));

    }

    @Override
    public void update(float dt) {
        gsm.update(dt);

    }

    @Override
    public void render() {
        gsm.render();

    }

    @Override
    public void dispose() {

    }
}
