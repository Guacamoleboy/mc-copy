package com.guacamoleboy.minecraft;

import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {

    // Attributes
    private int vao, vbo, ebo;
    private int vertexCount;

    // Each vertex
    public static final float[] CUBE_VERTICES = {
            // Positions           // UVs
            // Front face
            -0.5f, -0.5f,  0.5f, 0.0f, 0.0f,
            0.5f, -0.5f,  0.5f, 1.0f, 0.0f,
            0.5f,  0.5f,  0.5f, 1.0f, 1.0f,
            -0.5f,  0.5f,  0.5f, 0.0f, 1.0f,
            // Back face
            -0.5f, -0.5f, -0.5f, 1.0f, 0.0f,
            0.5f, -0.5f, -0.5f, 0.0f, 0.0f,
            0.5f,  0.5f, -0.5f, 0.0f, 1.0f,
            -0.5f,  0.5f, -0.5f, 1.0f, 1.0f,
            // Left face
            -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,
            -0.5f, -0.5f,  0.5f, 1.0f, 0.0f,
            -0.5f,  0.5f,  0.5f, 1.0f, 1.0f,
            -0.5f,  0.5f, -0.5f, 0.0f, 1.0f,
            // Right face
            0.5f, -0.5f, -0.5f, 1.0f, 0.0f,
            0.5f, -0.5f,  0.5f, 0.0f, 0.0f,
            0.5f,  0.5f,  0.5f, 0.0f, 1.0f,
            0.5f,  0.5f, -0.5f, 1.0f, 1.0f,
            // Top face
            -0.5f,  0.5f,  0.5f, 0.0f, 1.0f,
            0.5f,  0.5f,  0.5f, 1.0f, 1.0f,
            0.5f,  0.5f, -0.5f, 1.0f, 0.0f,
            -0.5f,  0.5f, -0.5f, 0.0f, 0.0f,
            // Bottom face
            -0.5f, -0.5f,  0.5f, 0.0f, 0.0f,
            0.5f, -0.5f,  0.5f, 1.0f, 0.0f,
            0.5f, -0.5f, -0.5f, 1.0f, 1.0f,
            -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
    };

    public static final int[] CUBE_INDICES = {
            0,1,2, 2,3,0,
            4,5,6, 6,7,4,
            8,9,10,10,11,8,
            12,13,14,14,15,12,
            16,17,18,18,19,16,
            20,21,22,22,23,20
    };

    // _______________________________________

    public Mesh(float[] vertices, int[] indices) {
        vertexCount = indices.length;

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);
        vertexBuffer.put(vertices).flip();
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        IntBuffer indexBuffer = BufferUtils.createIntBuffer(indices.length);
        indexBuffer.put(indices).flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);

        int stride = 5 * Float.BYTES;
        glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, 0); // aPos
        glVertexAttribPointer(1, 2, GL_FLOAT, false, stride, 3 * Float.BYTES); // aTexCoord

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glBindVertexArray(0);
    }

    // _______________________________________

    public void render() {
        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    // _______________________________________

    public void cleanup() {
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
        glDeleteVertexArrays(vao);
    }

} // Class end