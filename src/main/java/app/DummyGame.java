package app;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.opengl.GL11.glViewport;
import engine.ApplicationInterface;
import engine.STL;
import engine.Window;
import engine.graph.Mesh;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.Collections;

public class DummyGame implements ApplicationInterface {

	private int direction = 0;

	private float color = 0.0f;

	private final Renderer renderer;
	private STL mesh1, mesh2;

	public DummyGame() {
		renderer = new Renderer();
	}

	@Override
	public void init(Window window) throws Exception {
		renderer.init(window);
		mesh1 = new STL(new Mesh(
				Arrays.asList(-0.5f, 0.5f, -1.0f,
						-0.5f, -0.5f, -1.0f,
						0.5f, -0.5f, -1.0f,
						0.5f, 0.5f, -1.0f), Arrays.asList(0.5f, 0.0f, 0.0f,
				0.0f, 1.0f, 1.0f,
				0.0f, 0.0f, 0.5f,
				0.0f, 0.5f, 0.5f), Arrays.asList(0, 1, 3, 3, 1, 2)));
		mesh1.setRotation(25.0f, 25.0f, 25.0f);

		mesh2 = new STL("C:\\Users\\Delta\\Documents\\5Axis3DPrinter\\GoPro_-_Gerade.stl");
		mesh2.translate(100, 50, 0);
		mesh2.setPosition(0, 0, -300);
		mesh2.setRotation(-45, 0, 45);
	}

	@Override
	public void input(Window window) {
		if ( window.isKeyPressed(GLFW_KEY_UP) ) {
			direction = 1;
		} else if ( window.isKeyPressed(GLFW_KEY_DOWN) ) {
			direction = -1;
		} else {
			direction = 0;
		}
	}

	@Override
	public void update(float interval) {
	}

	@Override
	public void render(Window window) {
		if (window.isResized()) {
			glViewport(0, 0, window.getWidth(), window.getHeight());
			window.setResized(false);
		}

		window.setClearColor(color, color, color, 0.0f);
		renderer.clear();
		Vector3f v = mesh2.getRotation();
		v.x = v.x + 0.5f;
		mesh2.setRotation(v.x, v.y, v.z);

		renderer.render(window, new STL[]{mesh2});
	}

	@Override
	public void cleanup() {

	}
}
