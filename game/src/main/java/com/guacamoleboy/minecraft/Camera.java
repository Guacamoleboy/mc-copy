package com.guacamoleboy.minecraft;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;

public class Camera {

    // Attributes
    private Vector3f position;
    private float yaw, pitch;
    private final float mouseSensitivity = 0.1f;
    private final float movementSpeed = 2.5f;

    // ____________________________________________________________

    public Camera(Vector3f position, float yaw, float pitch) {
        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    // ____________________________________________________________

    public Matrix4f getViewMatrix() {
        Vector3f front = calculateFrontVector();
        Vector3f center = new Vector3f(position).add(front);
        return new Matrix4f().lookAt(position, center, new Vector3f(0, 1, 0));
    }

    // ____________________________________________________________

    private Vector3f calculateFrontVector() {
        float x = (float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        float y = (float) Math.sin(Math.toRadians(pitch));
        float z = (float) (Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        return new Vector3f(x, y, z).normalize();
    }

    // ____________________________________________________________

    public void processKeyboardInput(long window, float deltaTime) {
        Vector3f front = calculateFrontVector();
        Vector3f right = front.cross(new Vector3f(0, 1, 0), new Vector3f()).normalize();
        Vector3f up = right.cross(front, new Vector3f()).normalize();

        if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS)
            position.fma(movementSpeed * deltaTime, front);
        if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS)
            position.fma(-movementSpeed * deltaTime, front);
        if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS)
            position.fma(-movementSpeed * deltaTime, right);
        if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS)
            position.fma(movementSpeed * deltaTime, right);
        if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS)
            position.fma(movementSpeed * deltaTime, up);
        if (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS)
            position.fma(-movementSpeed * deltaTime, up);
    }

    // ____________________________________________________________

    public void processMouseMovement(double xoffset, double yoffset) {
        yaw += xoffset * mouseSensitivity;
        pitch += yoffset * mouseSensitivity;

        if (pitch > 89.0f)
            pitch = 89.0f;
        if (pitch < -89.0f)
            pitch = -89.0f;
    }

} // Class end