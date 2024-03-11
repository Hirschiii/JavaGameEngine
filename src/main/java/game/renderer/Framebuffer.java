package game.renderer;

import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glReadBuffer;
import static org.lwjgl.opengl.GL11.glReadPixels;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_DEPTH_COMPONENT32F;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30.GL_READ_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_RENDERBUFFER;
import static org.lwjgl.opengl.GL30.GL_RGBA32F;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glBindRenderbuffer;
import static org.lwjgl.opengl.GL30.glCheckFramebufferStatus;
import static org.lwjgl.opengl.GL30.glFramebufferRenderbuffer;
import static org.lwjgl.opengl.GL30.glFramebufferTexture2D;
import static org.lwjgl.opengl.GL30.glFramebufferTextureLayer;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;
import static org.lwjgl.opengl.GL30.glGenRenderbuffers;
import static org.lwjgl.opengl.GL30.glRenderbufferStorage;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import game.util.*;

public class Framebuffer {
    private int fboID = 0;
    private Texture texture = null;

    public int width, height;

    public Framebuffer(int width, int height) {
        this.width = width;
        this.height = height;
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

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            assert false : "Error: FramBuffer not complete";
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);

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
    // vhsShader.bind(); // Activate the shader program
    // glBindVertexArray(texture.); // Bind the VAO for the quad
    // glEnable(GL_TEXTURE_2D);
    // glBindTexture(GL_TEXTURE_2D, texture.getTexID()); // Bind the framebuffer
    // texture
    // glDrawArrays(GL_TRIANGLES, 0, 6); // Draw the quad
    // glBindVertexArray(0); // Unbind the VAO
    // screenShader.unbind(); // Unbind the shader program
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
