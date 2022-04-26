package app;

import engine.ApplicationInterface;
import engine.Parameters;
import engine.STL;
import engine.Window;
import engine.UI;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.opengl.GL11.glViewport;

public class AntiLatheApp implements ApplicationInterface {
	private static final String configPath = "resources/config.json";
	public static Parameters PPARAMS;

	private int direction = 0;

	private float color = 0.0f;

	private final Renderer renderer;
	private STL testMesh;

	public AntiLatheApp() {
		renderer = new Renderer();
	}

	@Override
	public void init(Window window) throws Exception {
		PPARAMS = new Parameters(configPath);
		UI.ui.useConfig(PPARAMS);

		renderer.init(window);

		testMesh = new STL("C:\\Users\\Delta\\Documents\\5Axis3DPrinter\\GoPro_-_Gerade.stl");
		testMesh.setPosition(0, 0, -300);
		testMesh.recenter(new Vector3f(-100, 100, 100));
		testMesh.setRotation(-45, 0, 45);
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
		Vector3f v = testMesh.getRotation();
		testMesh.setRotation(v.x, v.y, v.z);

		renderer.render(window, new STL[]{testMesh});
	}

	@Override
	public void cleanup() {

	}
}
