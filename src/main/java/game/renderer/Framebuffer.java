package game.renderer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glReadBuffer;
import static org.lwjgl.opengl.GL11.glReadPixels;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_DEPTH_COMPONENT32F;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30.GL_RENDERBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glBindRenderbuffer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glCheckFramebufferStatus;
import static org.lwjgl.opengl.GL30.glFramebufferRenderbuffer;
import static org.lwjgl.opengl.GL30.glFramebufferTexture2D;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;
import static org.lwjgl.opengl.GL30.glGenRenderbuffers;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL30.glRenderbufferStorage;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Framebuffer {
	private static final int GL_BLEND = 0;
	private int fboID;
	private Texture texture;
	private int quadVAO, quadVBO;
	private Shader screenShader; // This needs to be implemented

	public Framebuffer(int width, int height, Shader shader) {
		// Initialize FBO and texture as before
		fboID = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, fboID);
		this.texture = new Texture(width, height);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, this.texture.getTexID(), 0);
		int rboID = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, rboID);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32F, width, height);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboID);
		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
			throw new RuntimeException("Framebuffer is not complete");
		}
		glBindFramebuffer(GL_FRAMEBUFFER, 0);

		// Setup full-screen quad
		setupFullscreenQuad();

		// Initialize the screen shader
		this.screenShader = shader;
	}

	private void setupFullscreenQuad() {
		float[] quadVertices = {
				// positions // texCoords
				-1.0f, 1.0f, 0.0f, 1.0f,
				-1.0f, -1.0f, 0.0f, 0.0f,
				1.0f, -1.0f, 1.0f, 0.0f,
				-1.0f, 1.0f, 0.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f,
				1.0f, -1.0f, 1.0f, 0.0f
		};

		quadVAO = glGenVertexArrays();
		glBindVertexArray(quadVAO);

		quadVBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, quadVBO);
		FloatBuffer quadBuffer = BufferUtils.createFloatBuffer(quadVertices.length);
		quadBuffer.put(quadVertices).flip();
		glBufferData(GL_ARRAY_BUFFER, quadBuffer, GL_STATIC_DRAW);

		glEnableVertexAttribArray(0); // Position attribute
		glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * Float.BYTES, 0L);

		glEnableVertexAttribArray(1); // Texture coordinate attribute
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}

	public void renderToScreen() {
		// glUseProgram(screenShader.getProgramID());
		screenShader.use();
		glBindVertexArray(quadVAO);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glBindTexture(GL_TEXTURE_2D, texture.getTexID());
		glDrawArrays(GL_TRIANGLES, 0, 6);
		glBindVertexArray(0);
		glUseProgram(0);
	}

	public float[] readPixel(int x, int y) {
		System.out.println("read from: " + fboID);
		glBindFramebuffer(GL_FRAMEBUFFER, fboID);
		glReadBuffer(GL_COLOR_ATTACHMENT0);

		float[] pixel = new float[3];
		glReadPixels(x, y, 1, 1, GL_RGB, GL_FLOAT, pixel);

		return pixel;
	}

	// public void renderToScreen() {
	// Shader vhsShader = AssetPool.getShader("/assets/shader/vhsShader.glsl");
	// vhsShader.use(); // Activate the shader program
	// texture.bind();
	//
	// // glDrawArrays(GL_TRIANGLES, 0, 6); // Draw the quad
	// vhsShader.detach(); // Unbind the shader program
	// }

	public void bind() {
		glBindFramebuffer(GL_FRAMEBUFFER, fboID);
	}

	public void unbind() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	public int getFboID() {
		return fboID;
	}

	public Texture getTexture() {
		return texture;
	}

	public int getTextureID() {
		return texture.getTexID();
	}

}
