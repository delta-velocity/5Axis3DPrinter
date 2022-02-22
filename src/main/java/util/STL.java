package util;

import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class STL {
	public ArrayList<Solid> solids;

	public STL(String path) {
		try {
			BufferedReader r = new BufferedReader(new FileReader(path));
			Vector3f p1, p2, p3, normal;
			String command;
			solids = new ArrayList<>();

			while ((command = r.readLine()) != null) {
				String[] s = command.trim().split("\\s+");
				if (s[0].equals("solid")) {
					solids.add(new Solid());
					//System.out.println("Solid " + solids.size());
				} else if (s[0].equals("facet")) {
					normal = new Vector3f(Float.parseFloat(s[2]), Float.parseFloat(s[3]), Float.parseFloat(s[4]));
					r.readLine();
					s = r.readLine().split("\\s+");
					p1 = new Vector3f(Float.parseFloat(s[2]), Float.parseFloat(s[3]), Float.parseFloat(s[4]));
					s = r.readLine().split("\\s+");
					p2 = new Vector3f(Float.parseFloat(s[2]), Float.parseFloat(s[3]), Float.parseFloat(s[4]));
					s = r.readLine().split("\\s+");
					p3 = new Vector3f(Float.parseFloat(s[2]), Float.parseFloat(s[3]), Float.parseFloat(s[4]));

					Face f = new Face(p1, p2, p3, normal);
					solids.get(solids.size() - 1).addFace(f);
				}
			}
			r.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String toString() {
		return Integer.toString(solids.size());
	}

	// a single solid part of an util.STL file
	public class Solid {
		public ArrayList<Face> faces;
		public Solid() {
			faces = new ArrayList<>();
		}

		public void addFace(Face f) {
			faces.add(f);
		}
	}

	// a single face from an util.STL file, with a normal vector
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
