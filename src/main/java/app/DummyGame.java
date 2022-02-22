package app;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.opengl.GL11.glViewport;
import engine.ApplicationInterface;
import engine.STLObject;
import engine.Window;
import engine.graph.Mesh;

public class DummyGame implements ApplicationInterface {

	private int direction = 0;

	private float color = 0.0f;

	private final Renderer renderer;
	private STLObject mesh;

	public DummyGame() {
		renderer = new Renderer();
	}

	@Override
	public void init(Window window) throws Exception {
		renderer.init(window);
		mesh = new STLObject(new Mesh(
				new float[]{
						-0.5f,  0.5f, -1.0f,
						-0.5f, -0.5f, -1.0f,
						0.5f, -0.5f, -1.0f,
						0.5f,  0.5f, -1.0f,
				}, new float[]{
						0.5f, 0.0f, 0.0f,
						0.0f, 0.5f, 0.0f,
						0.0f, 0.0f, 0.5f,
						0.0f, 0.5f, 0.5f,
				}, new int[]{
						0, 1, 3, 3, 1, 2,
				}));
		mesh.setRotation(25.0f, 25.0f, 25.0f);
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
		color += direction * 0.01f;
		if (color > 1) {
			color = 1.0f;
		} else if ( color < 0 ) {
			color = 0.0f;
		}
	}

	@Override
	public void render(Window window) {
		if (window.isResized()) {
			glViewport(0, 0, window.getWidth(), window.getHeight());
			window.setResized(false);
		}

		window.setClearColor(color, color, color, 0.0f);
		renderer.clear();

		renderer.render(window, new STLObject[]{mesh});
	}

	@Override
	public void cleanup() {

	}
}
