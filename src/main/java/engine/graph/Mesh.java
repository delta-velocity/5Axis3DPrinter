package engine.graph;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Mesh {

	private int vaoId;

	private int posVboId;

	private int colourVboId;

	private int idxVboId;

	private int vertexCount;

	public Mesh(float[] positions, float[] colors, int[] indices) {
		init(positions, colors, indices);
	}

	public Mesh(List<Float> positions, List<Float> colors, List<Integer> indices) {
		float[] pos = new float[positions.size()];
		float[] col = new float[colors.size()];
		int[] ind = new int[indices.size()];
		for (int i = 0; i < pos.length; i++) {
			pos[i] = positions.get(i);
		}
		for (int i = 0; i < col.length; i++) {
			col[i] = colors.get(i);
		}
		for (int i = 0; i < ind.length; i++) {
			ind[i] = indices.get(i);
		}
		init(pos, col, ind);
	}

	public void init(float[] positions, float[] colors, int[] indices) {
		FloatBuffer posBuffer = null;
		FloatBuffer colorBuffer = null;
		IntBuffer indicesBuffer = null;
		try {
			vertexCount = indices.length;

			vaoId = glGenVertexArrays();
			glBindVertexArray(vaoId);

			// Position VBO
			posVboId = glGenBuffers();
			posBuffer = MemoryUtil.memAllocFloat(positions.length);
			posBuffer.put(positions).flip();
			glBindBuffer(GL_ARRAY_BUFFER, posVboId);
			glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_DYNAMIC_DRAW);
			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

			// Colour VBO
			colourVboId = glGenBuffers();
			colorBuffer = MemoryUtil.memAllocFloat(colors.length);
			colorBuffer.put(colors).flip();
			glBindBuffer(GL_ARRAY_BUFFER, colourVboId);
			glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_DYNAMIC_DRAW);
			glEnableVertexAttribArray(1);
			glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

			// Index VBO
			idxVboId = glGenBuffers();
			indicesBuffer = MemoryUtil.memAllocInt(indices.length);
			indicesBuffer.put(indices).flip();
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_DYNAMIC_DRAW);

			glBindBuffer(GL_ARRAY_BUFFER, 0);
			glBindVertexArray(0);
		} finally {
			if (posBuffer != null) {
				MemoryUtil.memFree(posBuffer);
			}
			if (colorBuffer != null) {
				MemoryUtil.memFree(colorBuffer);
			}
			if (indicesBuffer != null) {
				MemoryUtil.memFree(indicesBuffer);
			}
		}
	}

	public int getVaoId() {
		return vaoId;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public void render() {
		// Draw the mesh
		glBindVertexArray(getVaoId());

		glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

		// Restore state
		glBindVertexArray(0);
	}

	public void cleanUp() {
		glDisableVertexAttribArray(0);

		// Delete the VBOs
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glDeleteBuffers(posVboId);
		glDeleteBuffers(colourVboId);
		glDeleteBuffers(idxVboId);

		// Delete the VAO
		glBindVertexArray(0);
		glDeleteVertexArrays(vaoId);
	}
}