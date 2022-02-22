package app;

import engine.STLObject;
import engine.Utils;
import engine.Window;
import engine.graph.Mesh;
import engine.graph.ShaderProgram;
import engine.graph.Transformation;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Renderer {
	private static final float FOV = (float) Math.toRadians(60.0f);
	private static final float Z_NEAR = 0.01f;
	private static final float Z_FAR = 1000.f;

	private int vboId;
	private int vaoId;
	private int eboId;
	private ShaderProgram shader;
	private Matrix4f projectionMatrix;
	private Window window;
	private Transformation transformation;

	public Renderer() {
		transformation = new Transformation();
	}

	public void init(Window window) throws Exception {
		this.window = window;
		shader = new ShaderProgram();
		shader.createVertexShader();
		shader.createFragmentShader();
		shader.link();

		// Create projection matrix
		float aspectRatio = (float) window.getWidth() / window.getHeight();
		projectionMatrix = new Matrix4f().setPerspective(Renderer.FOV, aspectRatio, Renderer.Z_NEAR, Renderer.Z_FAR);
		shader.createUniform("projectionMatrix");
		shader.createUniform("worldMatrix");

		window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	}

	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public void render(Window window, STLObject[] objects) {
		clear();

		if (window.isResized()) {
			glViewport(0, 0, window.getWidth(), window.getHeight());
			window.setResized(false);
		}

		shader.bind();

		Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
		shader.setUniform("projectionMatrix", projectionMatrix);

		for (STLObject s : objects) {
			// Set world matrix for this item
			Matrix4f worldMatrix =
					transformation.getWorldMatrix(
							s.getPosition(),
							s.getRotation(),
							s.getScale());
			shader.setUniform("worldMatrix", worldMatrix);
			// Render the mes for this game item
			s.getMesh().render();
		}

		shader.unbind();
	}

	public void cleanup() {
		if (shader != null) {
			shader.cleanup();
		}

		glDisableVertexAttribArray(0);

		// Delete the VBO
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glDeleteBuffers(vboId);

		// Delete the VAO
		glBindVertexArray(0);
		glDeleteVertexArrays(vaoId);
	}
}