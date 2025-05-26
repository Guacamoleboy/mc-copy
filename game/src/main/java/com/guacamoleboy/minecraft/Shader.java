package com.guacamoleboy.minecraft;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import static org.lwjgl.opengl.GL20.*;

public class Shader {

    // Attributes
    private final int id;

    // ____________________________________________________________

    public Shader(String vertexPath, String fragmentPath) {
        String vertexSrc = readFile(vertexPath);
        String fragmentSrc = readFile(fragmentPath);

        int vertex = compileShader(GL_VERTEX_SHADER, vertexSrc);
        int fragment = compileShader(GL_FRAGMENT_SHADER, fragmentSrc);

        id = glCreateProgram();
        glAttachShader(id, vertex);
        glAttachShader(id, fragment);
        glLinkProgram(id);

        // Check link status
        if (glGetProgrami(id, GL_LINK_STATUS) == GL_FALSE) {
            throw new RuntimeException("Shader linking failed:\n" + glGetProgramInfoLog(id));
        }

        // Delete compiled
        glDeleteShader(vertex);
        glDeleteShader(fragment);
    }

    // ____________________________________________________________

    private int compileShader(int type, String source) {
        int shader = glCreateShader(type);
        glShaderSource(shader, source);
        glCompileShader(shader);

        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException("Shader compile failed:\n" + glGetShaderInfoLog(shader));
        }

        return shader;
    }

    // ____________________________________________________________

    private String readFile(String path) {
        try {
            return new String(Files.readAllBytes(Paths.get("src/main/resources/" + path)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load shader: " + path, e);
        }
    }

    // ____________________________________________________________

    public void use() {
        glUseProgram(id);
    }

    // ____________________________________________________________

    public void delete() {
        glDeleteProgram(id);
    }

    // ____________________________________________________________

    public void setUniform(String name, Matrix4f matrix) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = stack.mallocFloat(16);
            matrix.get(fb);
            int location = glGetUniformLocation(id, name);
            glUniformMatrix4fv(location, false, fb);
        }
    }

    // ____________________________________________________________

    public void setUniform(String name, int value) {
        int location = glGetUniformLocation(id, name);
        if (location == -1) {
            System.err.println("Warning: uniform '" + name + "' not found!");
        }
        glUniform1i(location, value);
    }

} // Class end
