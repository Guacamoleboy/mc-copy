package com.guacamoleboy.minecraft;

import org.lwjgl.glfw.GLFWErrorCallback;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFWErrorCallback.*;

public class WindowManager {

    // Attributes
    private long window;
    private int width, height;
    private String title;
    private boolean firstMouse = true;
    private double lastMouseX, lastMouseY;
    private Camera camera;

    // ____________________________________________________________

    public WindowManager(int width, int height, String title, Camera camera) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.camera = camera;
    }

    // ____________________________________________________________

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(width, height, title, 0, 0);
        if (window == 0)
            throw new RuntimeException("Failed to create GLFW window");

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);

        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        // Set mouse callback
        glfwSetCursorPosCallback(window, (window, xpos, ypos) -> {
            if (firstMouse) {
                lastMouseX = xpos;
                lastMouseY = ypos;
                firstMouse = false;
            }

            double xoffset = xpos - lastMouseX;
            double yoffset = lastMouseY - ypos; // reversed y

            lastMouseX = xpos;
            lastMouseY = ypos;

            camera.processMouseMovement(xoffset, yoffset);
        });
    }

    // ____________________________________________________________

    public void pollEvents() {
        glfwPollEvents();
    }

    // ____________________________________________________________

    public void swapBuffers() {
        glfwSwapBuffers(window);
    }

    // ____________________________________________________________

    public void processInput(float deltaTime) {
        if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS) {
            glfwSetWindowShouldClose(window, true);
        }
        camera.processKeyboardInput(window, deltaTime);
    }

    // ____________________________________________________________

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    // ____________________________________________________________

    public void cleanup() {
        glfwTerminate();
    }

    // ____________________________________________________________

    public long getWindow() {
        return window;
    }

} // Class end