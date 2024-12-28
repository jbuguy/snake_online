package snake.components;

import org.joml.Vector2f;
import org.joml.Vector3f;

import snake.engine.Window;
import snake.renderer.DrawLine;
import snake.util.Settings;

public class GridLines extends Component {
    @Override
    public void update(float dt) {
        Vector2f cameraPos = Window.getScene().getCamera().position;
        Vector2f projectionSize = Window.getScene().getCamera().getProjectionSize();
        int firstX = ((int) cameraPos.x / Settings.GRID_WIDTH -1) * Settings.GRID_WIDTH;
        int firstY = ((int) cameraPos.y / Settings.GRID_WIDTH -1) * Settings.GRID_HEIGHT;
        int numVLines = (int) (projectionSize.x / Settings.GRID_WIDTH)+2;
        int numHLines = (int) (projectionSize.y / Settings.GRID_HEIGHT)+2;
        int height = (int) projectionSize.y+Settings.GRID_HEIGHT*2;
        int width = (int) projectionSize.x+Settings.GRID_WIDTH*2;
        int maxLines=Math.max(numHLines, numVLines);
        Vector3f color=new Vector3f(0.5f,0.5f,0.5f);
        for (int i = 0; i < maxLines; i++) {
            int x=firstX+(Settings.GRID_WIDTH*i);
            int y=firstY+(Settings.GRID_HEIGHT*i);
            if (i<numVLines) {
                DrawLine.addLine2D(new Vector2f(x,firstY), new Vector2f(x,y+height),color);
            }
            if (i<numVLines) {
                DrawLine.addLine2D(new Vector2f(firstX, y), new Vector2f(firstX+width, y),color);
            }
        }
    }

}
