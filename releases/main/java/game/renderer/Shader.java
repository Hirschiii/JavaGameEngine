package game.renderer;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform1iv;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniformMatrix3fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

public class Shader {
	private int shaderProgramID;
	private String vertexSource, fragmentSource;

	private boolean beingUsed = false;

	private String filepath;

	public Shader(String filepath) {
		this.filepath = filepath;
		try {
			String source = new String(Files.readAllBytes(Paths.get(filepath)));
			String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

			// FInd the first pattern after #type
			int index = source.indexOf("#type") + 6;
			int eol = source.indexOf("\n", index);
			String firstPattern = source.substring(index, eol).trim();

			// FInd the second pattern after #type
			index = source.indexOf("#type", eol) + 6;
			eol = source.indexOf("\n", index);

			String secondPattern = source.substring(index, eol).trim();

			if (firstPattern.equals("vertex")) {
				vertexSource = splitString[1];
			} else if (firstPattern.equals("fragment")) {
				fragmentSource = splitString[1];
			} else {
				throw new IOException("Unexpected Token '" + firstPattern + "'");
			}

			if (secondPattern.equals("vertex")) {
				vertexSource = splitString[2];
			} else if (secondPattern.equals("fragment")) {
				fragmentSource = splitString[2];
			} else {
				throw new IOException("Unexpected Token '" + secondPattern + "'");
			}

		} catch (IOException e) {
			e.printStackTrace();
			assert false : "Error : Could not open file for Shader : '" + filepath + "'";
		}

	}

	public void compile() {
		int vertexID, fragmentID;
		// Compile and link shaders
		// Load and Compile the vertex Shader:
		vertexID = glCreateShader(GL_VERTEX_SHADER);
		// Pass the shader source
		glShaderSource(vertexID, vertexSource);
		glCompileShader(vertexID);

		// Check for Error:
		int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
		if (success == GL_FALSE) {
			int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
			System.out.println("ERROR: '" + filepath + "'\n\tVertex shader compile failed");
			System.out.println(glGetShaderInfoLog(vertexID, len));
			assert false : "";
		}

		// Load and Compile the fragment Shader:
		fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
		// Pass the shader source
		glShaderSource(fragmentID, fragmentSource);
		glCompileShader(fragmentID);

		// Check for Error:
		success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
		if (success == GL_FALSE) {
			int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
			System.out.println("ERROR: '" + filepath + "'\n\tFragment shader compile failed");
			System.out.println(glGetShaderInfoLog(fragmentID, len));
			assert false : "";
		}

		// Link Shaders

		shaderProgramID = glCreateProgram();

		glAttachShader(shaderProgramID, vertexID);
		glAttachShader(shaderProgramID, fragmentID);
		glLinkProgram(shaderProgramID);

		// Check Errors:
		success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);

		if (success == GL_FALSE) {
			int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
			System.out.println("ERROR: 'shaderProgam'");
			System.out.println(glGetShaderInfoLog(shaderProgramID, len));
			assert false : "";

		}

	}

	public void use() {

		if (!beingUsed) {
			// Bind ShaderProgram
			glUseProgram(shaderProgramID);
			beingUsed = true;
		}
	}

	public void detach() {
		glUseProgram(0);
		beingUsed = false;
	}

	public void uploadMat4f(String varName, Matrix4f mat4) {
		// Puts the mat4 Matrix into the Buffer for the GPU
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use(); // Make sure the right shader is in the GPU
		FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
		mat4.get(matBuffer); // Flat out the Matrix to 1D: [1, 1, 1, ...]

		glUniformMatrix4fv(varLocation, false, matBuffer);
	}

	public void uploadMat3f(String varName, Matrix3f mat3) {
		// Puts the mat3 Matrix into the Buffer for the GPU
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use(); // Make sure the right shader is in the GPU
		FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
		mat3.get(matBuffer); // Flat out the Matrix to 1D: [1, 1, 1, ...]

		glUniformMatrix3fv(varLocation, false, matBuffer);
	}

	public void uploadVec4f(String varName, Vector4f vec) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
	}

	public void uploadVec3f(String varName, Vector3f vec) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform3f(varLocation, vec.x, vec.y, vec.z);
	}

	public void uploadVec2f(String varName, Vector2f vec) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform2f(varLocation, vec.x, vec.y);
	}

	public void uploadFloat(String varName, float val) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform1f(varLocation, val);
	}

	public void uploadInt(String varName, int val) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform1i(varLocation, val);
	}

	public void uploadTexture(String varName, int slot) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform1i(varLocation, slot);

	}

	public void uploadIntArray(String varName, int[] array) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform1iv(varLocation, array);
	}
}
