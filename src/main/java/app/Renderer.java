package app;

import engine.STL;
import engine.UI;
import engine.Window;
import engine.graph.Mesh;
import engine.graph.ShaderProgram;
import engine.graph.Transformation;
import imgui.ImGui;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_BACK;
import static org.lwjgl.opengl.GL15.GL_CULL_FACE;
import static org.lwjgl.opengl.GL15.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL15.glCullFace;
import static org.lwjgl.opengl.GL15.glEnable;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;

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
	public Transformation transformation;

	public Renderer() {
		transformation = new Transformation();
	}

	public void init(Window window) throws Exception {
		this.window = window;
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glEnable(GL_DEPTH_TEST);
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
		UI.ui.setRenderer(this);
	}

	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public void render(Window window, STL[] objects) {
		clear();

		// ImGui stuff
		UI.ui.setModels(objects);
		window.imGuiGlfw.newFrame();
		ImGui.newFrame();

		UI.ui.draw();

		ImGui.render();

		// not ImGui stuff
		if (window.isResized()) {
			glViewport(0, 0, window.getWidth(), window.getHeight());
			window.setResized(false);
		}

		shader.bind();

		Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
		shader.setUniform("projectionMatrix", projectionMatrix);

		for (STL s : objects) {
			// Set world matrix for this object
			Matrix4f worldMatrix =
					transformation.getWorldMatrix(
							s.getPosition(),
							s.getRotation(),
							s.getScale());
			shader.setUniform("worldMatrix", worldMatrix);
			// Render the object
			for (Mesh m : s.getMeshes()) {
				m.render();
			}
		}

		window.imGuiGl3.renderDrawData(ImGui.getDrawData());
		shader.unbind();

		// ImGui cleanup

//		if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
//			final long backupWindowHandle = org.lwjgl.glfw.GLFW.glfwGetCurrentContext();
//			ImGui.updatePlatformWindows();
//			ImGui.renderPlatformWindowsDefault();
//			GLFW.glfwMakeContextCurrent(backupWindowHandle);
//		}
//
//		GLFW.glfwSwapBuffers(window.windowHandle);
//		GLFW.glfwPollEvents();
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