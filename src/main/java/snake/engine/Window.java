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
import org.lwjgl.opengl.GL;

import snake.scenes.MainMenuScene;
import snake.renderer.DrawShape;
import snake.scenes.LevelScene;
import snake.scenes.Scene;

public class Window {
    private static Window instance = null;
    private static Scene currentScene;

    public static void changeScene(int scene) {
        switch (scene) {
            case 0:
                currentScene = new MainMenuScene();
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

    public static Scene getScene() {
        return currentScene;
    }

    public static int getWidth() {
        return get().width;
    }

    public static int getHeight() {
        return get().height;
    }

    public static void setWidth(int newWidth) {
        get().width = newWidth;
    }

    public static void setHeight(int newHeight) {
        get().height = newHeight;
    }

    private int width, height;
    private String title;
    private long glfwWindow;

    float r, g, b, a;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "online snake";
        this.r = 0.87f;
        this.g = 0.92f;
        this.b = 0.98f;
        this.a = 1.0f;
    }

    public void name() {
        glfwSetWindowShouldClose(GLFW_NO_WINDOW_CONTEXT, true);
    }

    public void run() {
        System.out.println("hello lwjgl " + Version.getVersion() + "!");
        init();

        loop();
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
            DrawShape.beginFrame();
            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);
            if (dt > 0) {
                DrawShape.draw();
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
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("failed to create window");
        }
        // mouse listener
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        // key listener
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallBack);
        // resize callback
        glfwSetWindowSizeCallback(glfwWindow, (window, newWidth, newHeight) -> {
            Window.setWidth(newWidth);
            Window.setHeight(newHeight);
        });
        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(0);
        glfwShowWindow(glfwWindow);

        GL.createCapabilities();
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        // ImGui

        Window.changeScene(0);
    }
}