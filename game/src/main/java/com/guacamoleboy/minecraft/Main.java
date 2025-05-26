package com.guacamoleboy.minecraft;

// Imports
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13C.glActiveTexture;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {

    // Scene initial size
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    // Attributes
    private Shader shader;
    private Camera camera;
    private Mesh cubeMesh;
    private Matrix4f projection;
    private Matrix4f model;
    private WindowManager windowManager;
    private Texture floorTexture;

    // ________________________________________________

    public static void main(String[] args) {
        try {
            new Main().run();
        } catch (Exception e) {
            e.printStackTrace(); // Debug
        }
    }

    // ________________________________________________

    public void run() {
        init();
        loop();
        cleanup();
    }

    // ________________________________________________

    private void init() {

        camera = new Camera(new Vector3f(2, 2, 2), -135f, -30f);
        windowManager = new WindowManager(WIDTH, HEIGHT, "Minecraft Clone", camera);
        windowManager.init();

        GL.createCapabilities();

        glEnable(GL_DEPTH_TEST);
        glClearColor(0.1f, 0.1f, 0.1f, 0.0f);

        projection = new Matrix4f().perspective(
                (float) Math.toRadians(45.0f),
                (float) WIDTH / HEIGHT,
                0.1f,
                100f);

        model = new Matrix4f();

        shader = new Shader("shaders/vertex.glsl", "shaders/fragment.glsl");
        floorTexture = new Texture("src/main/resources/textures/grass.png");

        cubeMesh = new Mesh(Mesh.CUBE_VERTICES, Mesh.CUBE_INDICES);
    }

    // ________________________________________________

    private void loop() {

        float lastFrame = 0f;

        while (!windowManager.shouldClose()) {
            float currentFrame = (float) glfwGetTime();
            float deltaTime = currentFrame - lastFrame;
            lastFrame = currentFrame;

            windowManager.processInput(deltaTime);

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            Matrix4f mvp = new Matrix4f();
            projection.mul(camera.getViewMatrix(), mvp);
            mvp.mul(model);

            shader.use();
            shader.setUniform("u_ModelViewProjection", mvp);
            floorTexture.bind(0);
            cubeMesh.render();

            windowManager.swapBuffers();
            windowManager.pollEvents();
        }
    }

    // ________________________________________________

    private void cleanup() {
        shader.delete();
        cubeMesh.cleanup();
        windowManager.cleanup();
    }

} // Main class end