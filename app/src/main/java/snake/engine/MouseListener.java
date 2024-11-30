package snake.engine;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double xPos, yPos, lastX, lastY;
    private boolean mouseBtnPressed[] = new boolean[3];
    private boolean dragging;

    private MouseListener() {
        this.scrollX = 0;
        this.scrollY = 0;
        this.lastX = 0;
        this.lastY = 0;
        this.xPos = 0;
        this.yPos = 0;
    }

    public static MouseListener get() {
        if (MouseListener.instance == null) {
            MouseListener.instance = new MouseListener();
        }
        return MouseListener.instance;
    }

    public static void mousePosCallBack(long window, double xPos, double yPos) {
        get().lastX = get().xPos;
        get().lastY = get().yPos;
        get().xPos = xPos;
        get().yPos = yPos;
    }

    public static void mouseButtonCallBack(long window, int button, int action, int mod) {
        if (action == GLFW_PRESS) {
            if (button < get().mouseBtnPressed.length) {
                get().mouseBtnPressed[button] = true;
            }
        } else if (action == GLFW_RELEASE) {
            if (button < get().mouseBtnPressed.length) {
                get().mouseBtnPressed[button] = false;
                get().dragging = false;
            }
        }
    }

    public static void mouseScrollCallBack(long window, double xOffSet, double yOffSet) {
        get().scrollX = xOffSet;
        get().scrollY = yOffSet;
    }

    public static void endFrame() {
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
        get().dragging = get().mouseBtnPressed[0] || get().mouseBtnPressed[1] || get().mouseBtnPressed[2];
    }

    public static float getX() {
        return (float) get().xPos;
    }

    public static float getY() {
        return (float) get().yPos;
    }

    public static float getDx() {
        return (float) (get().lastX - get().xPos);
    }

    public static float getDy() {
        return (float) (get().lastY - get().yPos);
    }

    public static float getScrollX() {
        return ((float) get().scrollX);
    }

    public static float getScrollY() {
        return ((float) get().scrollY);
    }

    public static boolean isDragging() {
        return get().dragging;
    }

    public static boolean mouseButtonDown(int button) {
        if (button < get().mouseBtnPressed.length) {
            return get().mouseBtnPressed[button];
        }
        return false;
    }
    
}
