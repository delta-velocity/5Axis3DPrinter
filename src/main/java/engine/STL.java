package engine;

import engine.graph.Mesh;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class STL {
	public static final Vector3f LIGHTING_ANGLE = new Vector3f(0, 1, 12);


	private List<Mesh> meshes;
	private Vector3f position;
	private float scale;
	private Vector3f rotation;
	private ArrayList<Solid> solids;

	public STL(Mesh mesh) {
		this.meshes = Collections.singletonList(mesh);
		position = new Vector3f(0, 0, 0);
		scale = 1;
		rotation = new Vector3f(0, 0, 0);
	}

	public STL(Mesh... meshes) {
		this.meshes = Arrays.asList(meshes);
		position = new Vector3f(0, 0, 0);
		scale = 1;
		rotation = new Vector3f(0, 0, 0);
	}

	public STL(String path) {
		this.meshes = new ArrayList<>();
		position = new Vector3f(0, 0, 0);
		scale = 1;
		rotation = new Vector3f(0, 0, 0);
		try {
			BufferedReader r = new BufferedReader(new FileReader(path));
			Vector3f p1, p2, p3, normal;
			String command;
			solids = new ArrayList<>();

			int index = 0;
			float minX = 0, maxX = 0, minY = 0, maxY = 0, minZ = 0, maxZ = 0;
			ArrayList<Float> pos = new ArrayList<>();
			ArrayList<Float> col = new ArrayList<>();
			ArrayList<Integer> ind = new ArrayList<>();

			while ((command = r.readLine()) != null) {
				String[] s = command.trim().split("\\s+");
				if (s[0].equals("solid")) {
					solids.add(new Solid());
					//System.out.println("Solid " + solids.size());
				} else if (s[0].equals("facet")) {
					normal = new Vector3f(-Float.parseFloat(s[2]), -Float.parseFloat(s[3]), -Float.parseFloat(s[4]));
					r.readLine();
					s = r.readLine().split("\\s+");
					p1 = new Vector3f(Float.parseFloat(s[2]), Float.parseFloat(s[3]), Float.parseFloat(s[4]));
					s = r.readLine().split("\\s+");
					p2 = new Vector3f(Float.parseFloat(s[2]), Float.parseFloat(s[3]), Float.parseFloat(s[4]));
					s = r.readLine().split("\\s+");
					p3 = new Vector3f(Float.parseFloat(s[2]), Float.parseFloat(s[3]), Float.parseFloat(s[4]));
					STL.Face f = new STL.Face(p1, p2, p3, normal);
					solids.get(solids.size() - 1).addFace(f);

					pos.add(p1.x); pos.add(p1.y); pos.add(p1.z);
					pos.add(p2.x); pos.add(p2.y); pos.add(p2.z);
					pos.add(p3.x); pos.add(p3.y); pos.add(p3.z);

					float shade = 1 - normal.angleCos(LIGHTING_ANGLE);

					col.add(shade); col.add(shade); col.add(shade);
					col.add(shade); col.add(shade); col.add(shade);
					col.add(shade); col.add(shade); col.add(shade);
					ind.add(index++); ind.add(index++); ind.add(index++);
				}
			}
			r.close();

			meshes.add(new Mesh(pos, col, ind));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void translate(float x, float y, float z) {
		for (Solid s : solids) {
			for (Face f : s.faces) {
				f.p1 = new Vector3f(f.p1.x + x, f.p1.y + y, f.p1.z + z);
				f.p2 = new Vector3f(f.p2.x + x, f.p2.y + y, f.p2.z + z);
				f.p3 = new Vector3f(f.p3.x + x, f.p3.y + y, f.p3.z + z);
			}
		}
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(float x, float y, float z) {
		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(float x, float y, float z) {
		this.rotation.x = x;
		this.rotation.y = y;
		this.rotation.z = z;
	}

	public List<Mesh> getMeshes() {
		return meshes;
	}

	// a single solid part of an STL file
	public class Solid {
		public ArrayList<STL.Face> faces;
		public Solid() {
			faces = new ArrayList<>();
		}

		public void addFace(STL.Face f) {
			faces.add(f);
		}
	}

	// a single face from an STL file, with a normal vector
	public class Face {
		public Vector3f p1, p2, p3, normal;
		public Face(Vector3f p1, Vector3f p2, Vector3f p3, Vector3f normal) {
			this.p1 = p1;
			this.p2 = p2;
			this.p3 = p3;
			this.normal = normal;
		}
	}
}
