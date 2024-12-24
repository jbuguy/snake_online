package snake.components;

import snake.engine.Component;

public class SpriteRenderer extends Component{
    private boolean firstTime=true;

    @Override
    public void start() {
        
    }

    @Override
    public void update(float dt) {
        if (firstTime) {
            firstTime=false;
        }
    }

}
