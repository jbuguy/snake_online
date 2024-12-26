package snake.engine;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

public class Window {
    private static Window instance = null;
    private static Scene currentScene;

    public static void changeScene(int scene) {
        switch (scene) {
            case 0:
                currentScene = new GameInterface();
                break;
            case 1:
                currentScene = new LevelScene();
                break;
            default:
                assert false : "Unkown scene" + scene + "!";
                break;
        }
        currentScene.init();
        currentScene.start();
    }

    public static Window get() {
        if (Window.instance == null) {
            Window.instance = new Window();
        }
        return Window.instance;
    }

    private int width, heigth;
    private String title;

    private long glfwWindow;

    float r, g, b, a;

    private Window() {
        this.width = 640;
        this.heigth = 480;
        this.title = "online snake";
        this.a = 0.0f;
        this.b = 0.0f;
        this.g = 0.0f;
        this.r = 0.0f;
    }

    public void run() {
        System.out.println("hello lwjgl " + Version.getVersion() + "!");
        init();
        loop();
        glfwSetKeyCallback(glfwWindow, null);
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        glfwTerminate();
        GLFWErrorCallback tmp = glfwSetErrorCallback(null);
        if (tmp != null) {
            tmp.free();

        }
    }

    public void loop() {
        float beginTime = ((float) glfwGetTime());
        float lastTime = ((float) glfwGetTime());
        float dt = -1;

        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();
            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);
            if (dt > 0) {
                currentScene.update(dt);
            }
            glfwSwapBuffers(glfwWindow);
            lastTime = ((float) glfwGetTime());
            dt = lastTime - beginTime;
            beginTime = lastTime;
        }
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("failed to init glfw");
        }
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if (vidMode != null) {
            this.width = vidMode.width();
            this.heigth = vidMode.height();
        }
        glfwWindow = glfwCreateWindow(this.width, this.heigth, this.title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("failed to create window");
        }
        // mouse listener
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallBack);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallBack);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallBack);
        // key listener
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallBack);
        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1);
        glfwShowWindow(glfwWindow);
        GL.createCapabilities();
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        Window.changeScene(0);
    }

    public static Scene getScene() {
        return currentScene;
    }
}