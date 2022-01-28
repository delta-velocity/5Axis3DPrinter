import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_EXTENSIONS;
import static org.lwjgl.opengl.GL11.GL_MAX_TEXTURE_SIZE;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_RENDERER;
import static org.lwjgl.opengl.GL11.GL_VENDOR;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

public class AntiLatheApp {
	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;

	private long window;

	private float sp = 0.0f;
	private boolean swapcolor = false;

	private void init() {
		glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

		int WIDTH = 300;
		int HEIGHT = 300;

		window = glfwCreateWindow(WIDTH, HEIGHT, "Hello LWJGL3", NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
					glfwSetWindowShouldClose(window, true);
			}
		});

		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (vidmode.width() - WIDTH) / 2, (vidmode.height() - HEIGHT) / 2);

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);

		glfwShowWindow(window);
	}

	private void update() {
		sp = sp + 0.001f;
		if (sp > 1.0f) {
			sp = 0.0f;
			swapcolor = !swapcolor;
		}
	}

	private void render() {
		drawQuad();
	}

	private void drawQuad() {
		if (!swapcolor) {
			glColor3f(0.0f, 1.0f, 0.0f);
		} else {
			glColor3f(0.0f, 0.0f, 1.0f);
		}

		glBegin(GL_QUADS);
		{

			glVertex3f(-sp, -sp, 0.0f);
			glVertex3f(sp, -sp, 0.0f);
			glVertex3f(sp, sp, 0.0f);
			glVertex3f(-sp, sp, 0.0f);
		}
		glEnd();

	}

	private void loop() {
		GL.createCapabilities();
		System.out.println("----------------------------");
		System.out.println("OpenGL Version : " + glGetString(GL_VERSION));
		System.out.println("OpenGL Max Texture Size : " + glGetInteger(GL_MAX_TEXTURE_SIZE));
		System.out.println("OpenGL Vendor : " + glGetString(GL_VENDOR));
		System.out.println("OpenGL Renderer : " + glGetString(GL_RENDERER));
		System.out.println("OpenGL Extensions supported by your card : ");
		String extensions = glGetString(GL_EXTENSIONS);
		String[] extArr = extensions.split("\\ ");
		for (int i = 0; i < extArr.length; i++) {
			System.out.println(extArr[i]);
		}
		System.out.println("----------------------------");

		while (!glfwWindowShouldClose(window)) {
			if (!swapcolor) {
				glClearColor(0.0f, 0.0f, 1.0f, 0.0f);
			} else {
				glClearColor(0.0f, 1.0f, 0.0f, 0.0f);
			}

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			update();
			render();
			glfwSwapBuffers(window);

			glfwPollEvents();
		}
	}
	public void run() {
		System.out.println("Hello LWJGL3 " + Version.getVersion() + "!");

		try {
			init();
			loop();
			glfwDestroyWindow(window);
		} finally {
			glfwTerminate();
		}
	}

	public static void main(String[] args) {
		new AntiLatheApp().run();
	}
}
