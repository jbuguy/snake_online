package snake.engine;

import org.joml.Vector2f;
import org.joml.Vector4f;

import snake.components.SpriteRenderer;

public class LevelEditorScene extends Scene {


    public LevelEditorScene() {
        super();
    }

    @Override
    public void init() {
        this.camera=new Camera(new Vector2f());
        int xOffset=0;
        int yOffset=0;

        float totalWidth=(float)(1600-xOffset*2);
        float totalHeight=(float)(900-yOffset*2);
        float sizeX=totalWidth/100.0f;
        float sizeY=totalHeight/100.0f;
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                float xPos=xOffset+(i*sizeX);
                float yPos=yOffset+(j*sizeY);
                GameObject gObject = new GameObject("obj"+i+""+j,new Transform(new Vector2f(xPos, yPos),new Vector2f(sizeX, sizeY)));
                gObject.addComponent(new SpriteRenderer(new Vector4f(xPos/totalWidth, yPos/totalHeight, 1, 1)));
                this.addGameObjectToScene(gObject);
            }
        }
    }


    @Override
    public void update(float dt) {
        System.out.println("fps:"+(1.0f/dt));
        this.gameObjects.forEach(go -> go.update(dt));
        this.renderer.render();
    }

}