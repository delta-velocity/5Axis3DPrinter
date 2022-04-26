package engine;

import app.Renderer;
import com.fasterxml.jackson.databind.JsonNode;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImInt;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.io.FileWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Contained UI drawing using a dynamically generated parameter list
 */
public class UI {
	public static UI ui = new UI();
	private Window window;

	private int sidebarWidth = 400;
	private Renderer render;
	private STL[] objects;
	private Vector3f previousRot;
	private double prevA = 0;
	private double prevX = 0;
	private double prevY = 0;
	private Quaternionf prevQ;
	private Vector3f v; // temp vector for math
	private Parameters config;

	public UI() {
	}

	public void draw() {
		// model view
		ImGui.setNextWindowPos(0, 0);
		ImGui.setNextWindowSize(window.getWidth() - sidebarWidth, window.getHeight());
		ImGui.begin("model view", ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize
				| ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoBackground);

		ImGui.text("Mouse Drag Delta X: " + ImGui.getMouseDragDeltaX());
		ImGui.text("Mouse Drag Delta Y: " + ImGui.getMouseDragDeltaY());

		// left click drag model view
		if (ImGui.isMouseClicked(0)) {
			v = objects[0].getRotation();
			prevQ = new Quaternionf();
			prevX = 0;
			prevY = 0;
		}

		if (ImGui.isMouseDragging(0)) {
			double x = (ImGui.getMouseDragDeltaX()) / window.getWidth() * Math.PI;
			double y = (ImGui.getMouseDragDeltaY()) / window.getHeight() * Math.PI;

			Vector3f newRot = new Vector3f((float) x, (float) y, 0).normalize();

			Quaternionf q = new Quaternionf();
			q.rotateAxis((float) x, 1, 0, 0);
			q.rotateAxis((float) y, 0, 1, 0);

			objects[0].getRotation().rotate(prevQ.invert());
			objects[0].getRotation().rotate(q);

			prevQ = q;
		}

		if (ImGui.isMouseReleased(0)) {
		}
		// right click drag model view
		if (ImGui.isMouseDragging(1)) {
		}
		// middle click drag model view
		if (ImGui.isMouseDragging(2)) {

		}
		ImGui.end();

		// parameters and finish print (save)
		ImGui.setNextWindowPos(window.getWidth() - sidebarWidth, 0);
		ImGui.setNextWindowSize(sidebarWidth, window.getHeight());
		ImGui.setNextWindowBgAlpha(1);
		ImGui.begin("print settings", ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize
				| ImGuiWindowFlags.NoCollapse);

		ImGui.pushItemWidth(sidebarWidth / 2f);

		for (Iterator<JsonNode> i1 = config.config.elements(); i1.hasNext(); ) {
			JsonNode jn1 = i1.next();
			if (ImGui.collapsingHeader(jn1.get("display").asText(), ImGuiTreeNodeFlags.DefaultOpen)) {
				for (Iterator<JsonNode> i2 = jn1.elements(); i2.hasNext(); ) {
					JsonNode jn2 = i2.next();
					if (jn2.has("display")) {
						String path = jn1.get("display").asText() + "." + jn2.get("display").asText();
						if (jn2.get("tags").has("int")) {
							ImGui.inputInt(jn2.get("display").asText(), (ImInt) config.get(path), 0);
						}

						if (jn2.get("tags").has("list")) {
							// all lists in the JSON should be lists of strings at this point
							ArrayList<String> a = (ArrayList<String>) config.get(path);
							ImGui.combo(jn2.get("display").asText(), (ImInt) config.get(path + ".selection"),
									a.toArray(new String[0]));
						}

					}

				}

			}

		}

		// save button
		// STL FILE PATH NOT CURRENTLY SAVED
		if (ImGui.button("Save")) {
			try {
				String savePath = "resources/save.json";
				String s = config.save(savePath);
				FileWriter w = new FileWriter(Path.of(savePath).toFile());
				w.write(s);
				w.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		ImGui.end();

		config.validate();
	}

	public void setWindow(Window w) {
		this.window = w;
	}

	public void setRenderer(Renderer renderer) {
		this.render = renderer;
	}

	public void setModels(STL[] objects) {
		this.objects = objects;
	}

	/**
	 * Used to read in the default parameters after being read from a file
	 * @param p config parameters to use, the default config is located at resources/config.json
	 */
	public void useConfig(Parameters p) {
		config = p;
	}

}
