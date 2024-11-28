package snake.engine;

import java.awt.event.KeyEvent;

public class LevelScene extends Scene{
    private boolean changingScene;
    private float timeToChange = 2.0f;
    public LevelScene(){
        System.out.println("inside level scene");
        Window.get().r=1.0f;
        Window.get().b=1.0f;
        Window.get().g=1.0f;
    }

    @Override
    public void update(float dt) {
        if (!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            changingScene = true;
        }
        if (changingScene && timeToChange > 0) {
            timeToChange -= dt;
            Window.get().r-=dt*5.0f;
            Window.get().b-=dt*5.0f;
            Window.get().g-=dt*5.0f;
        } else if (changingScene) {
            Window.changeScene(0);
        }
    }

}
