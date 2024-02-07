package game.renderer;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Line2D {
	private Vector2f from, to;
	private Vector3f color;
	private int lifetime;

	public Line2D(Vector2f from, Vector2f to, Vector3f color, int lifetime) {
		this.from = from;
		this.to = to;
		this.color = color;
		this.lifetime = lifetime;
	}

	public int beginFrame() {
		 this.lifetime--;
		return this.lifetime;
	}

	public Vector2f getFrom() {
		return from;
	}

	public void setFrom(Vector2f from) {
		this.from = from;
	}

	public Vector2f getTo() {
		return to;
	}

	public void setTo(Vector2f to) {
		this.to = to;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

}
