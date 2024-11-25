package snake.engine;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_MAXIMIZED;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

public class Window {
    private static Window instance = null;
    private int width, heigth;
    private String title;
    private long glfwWindow;

    private Window() {
        this.width = 1600;
        this.heigth = 900;
        this.title = "online snake";
    }

    public static Window get() {
        if (Window.instance == null) {
            Window.instance = new Window();
        }
        return Window.instance;
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
        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();
            glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            if(MouseListener.isDragging()){
                System.out.println("mouse btn is pressed");
            }
            glfwSwapBuffers(glfwWindow);
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

    }

}
