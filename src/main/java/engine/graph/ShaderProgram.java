package engine.graph;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {
	private final String vertexShaderSource = """
			#version 330
			   
			layout (location=0) in vec3 position;
			layout (location=1) in vec3 inColour;
			   
			out vec3 exColour;
			   
			uniform mat4 worldMatrix;
			uniform mat4 projectionMatrix;
			   
			void main()
			{
			    gl_Position = projectionMatrix * worldMatrix * vec4(position, 1.0);
			    exColour = inColour;
			}\0""";

	private final String fragmentShaderSource = """
			#version 330
			   
			in  vec3 exColour;
			out vec4 fragColor;
			   
			void main()
			{
			    fragColor = vec4(exColour, 1.0);
			}\0""";

	private final int programId;
	private int vertexShaderId;
	private int fragmentShaderId;
	private final Map<String, Integer> uniforms;

	public ShaderProgram() throws Exception {
		programId = glCreateProgram();
		if (programId == 0) {
			throw new Exception("Could not create Shader");
		}
		uniforms = new HashMap<>();
	}

	public void createUniform(String uniformName) throws Exception {
		int uniformLocation = glGetUniformLocation(programId, uniformName);
		if (uniformLocation < 0) {
			throw new Exception("Could not find uniform:" + uniformName);
		}
		uniforms.put(uniformName, uniformLocation);
	}

	public void setUniform(String uniformName, Matrix4f value) {
		// Dump the matrix into a float buffer
		try (MemoryStack stack = MemoryStack.stackPush()) {
			glUniformMatrix4fv(uniforms.get(uniformName), false,
					value.get(stack.mallocFloat(16)));
		}
	}

	public void createVertexShader() throws Exception {
		vertexShaderId = createShader(vertexShaderSource, GL_VERTEX_SHADER);
	}

	public void createFragmentShader() throws Exception {
		fragmentShaderId = createShader(fragmentShaderSource, GL_FRAGMENT_SHADER);
	}

	protected int createShader(String shaderCode, int shaderType) throws Exception {
		int shaderId = glCreateShader(shaderType);
		if (shaderId == 0) {
			throw new Exception("Error creating shader. Type: " + shaderType);
		}

		glShaderSource(shaderId, shaderCode);
		glCompileShader(shaderId);

		if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
			throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
		}

		glAttachShader(programId, shaderId);

		return shaderId;
	}

	public void link() throws Exception {
		glLinkProgram(programId);
		if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
			throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
		}

		if (vertexShaderId != 0) {
			glDetachShader(programId, vertexShaderId);
		}
		if (fragmentShaderId != 0) {
			glDetachShader(programId, fragmentShaderId);
		}

		glValidateProgram(programId);
		if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
			System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
		}

	}

	public void bind() {
		glUseProgram(programId);
	}

	public void unbind() {
		glUseProgram(0);
	}

	public void cleanup() {
		unbind();
		if (programId != 0) {
			glDeleteProgram(programId);
		}
	}
}