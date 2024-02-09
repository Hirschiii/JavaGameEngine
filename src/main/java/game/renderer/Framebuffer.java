package game.renderer;

import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_DEPTH_COMPONENT32F;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30.GL_RENDERBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glBindRenderbuffer;
import static org.lwjgl.opengl.GL30.glCheckFramebufferStatus;
import static org.lwjgl.opengl.GL30.glFramebufferRenderbuffer;
import static org.lwjgl.opengl.GL30.glFramebufferTexture2D;
import static org.lwjgl.opengl.GL30.glFramebufferTextureLayer;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;
import static org.lwjgl.opengl.GL30.glGenRenderbuffers;
import static org.lwjgl.opengl.GL30.glRenderbufferStorage;

public class Framebuffer {
	private int fboID = 0;
	private Texture texture = null;

	public Framebuffer(int width, int height) {
		// Same as VBO

		fboID = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, fboID);

		// Create Texture, render data to, attach to fb
		this.texture = new Texture(width, height);
		// Change COLOR_ATTACHMENT0 for heightmap etc (More infos)
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, this.texture.getTexID(), 0);

		// Create Render Buffer with Depth info
		int rboID = glGenRenderbuffers();

		glBindRenderbuffer(GL_RENDERBUFFER, rboID);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32F, width, height);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboID);

		if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
			assert false : "Error: FramBuffer not complete";
		}

		glBindFramebuffer(GL_FRAMEBUFFER, 0);

	}

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
