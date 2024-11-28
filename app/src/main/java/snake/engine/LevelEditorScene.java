package snake.engine;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene {
    private boolean changingScene;
    private float timeToChange = 2.0f;

    public LevelEditorScene() {
        super();
        System.out.println("inside level editor scene");
    }

    @Override
    public void update(float dt) {
        System.out.println("fps:"+1/dt);
        if (!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            changingScene = true;
        }
        if (changingScene && timeToChange > 0) {
            timeToChange -= dt;
            Window.get().r-=dt*5.0f;
            Window.get().b-=dt*5.0f;
            Window.get().g-=dt*5.0f;
        } else if (changingScene) {
            Window.changeScene(1);
        }
    }

}
