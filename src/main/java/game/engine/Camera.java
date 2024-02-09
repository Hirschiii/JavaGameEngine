package game.engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
	private Matrix4f projectMatrix, viewMatrix, inverseProjection, inverseView;
	public Vector2f position;
	public float zoom;

	private float aspectRation = (float) Window.getWidth() / Window.getHeight();
	private float projectionHeight = 6;
	private Vector2f projectionSize = new Vector2f(projectionHeight * aspectRation, projectionHeight);

	public Camera(Vector2f position) {
		this.position = position;

		this.projectMatrix = new Matrix4f();
		this.viewMatrix = new Matrix4f();
		this.inverseProjection = new Matrix4f();
		this.inverseView = new Matrix4f();

		this.zoom = 1.0f;

		adjustProjection();
	}

	public void adjustProjection() {
		aspectRation = (float) Window.getWidth() / Window.getHeight();
		projectionSize = new Vector2f(projectionHeight * aspectRation, projectionHeight);

		projectMatrix.identity();

		// Left, Right, Bottom, Top, Near Clipping, Far Clipping
		// projectMatrix.ortho(0.0f, projectionSize.x * zoom,
		projectMatrix.ortho(0.0f, projectionSize.x * zoom,
				0.0f, projectionSize.y * zoom, 0.0f, 100.0f);
		projectMatrix.invert(inverseProjection);
	}

	public Matrix4f getViewMatrix() {
		Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);

		Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);

		this.viewMatrix.identity();
		viewMatrix.lookAt(new Vector3f(position.x, position.y, 20.0f),
				cameraFront.add(position.x, position.y, 0.0f),
				cameraUp);

		this.viewMatrix.invert(inverseView);

		return this.viewMatrix;
	}

	public Matrix4f getProjectionMatrix() {
		return this.projectMatrix;
	}

	public Matrix4f getProjectMatrix() {
		return projectMatrix;
	}

	public void setProjectMatrix(Matrix4f projectMatrix) {
		this.projectMatrix = projectMatrix;
	}

	public void setViewMatrix(Matrix4f viewMatrix) {
		this.viewMatrix = viewMatrix;
	}

	public Matrix4f getInverseProjection() {
		return inverseProjection;
	}

	public void setInverseProjection(Matrix4f inverseProjection) {
		this.inverseProjection = inverseProjection;
	}

	public Matrix4f getInverseView() {
		return inverseView;
	}

	public void setInverseView(Matrix4f inverseView) {
		this.inverseView = inverseView;
	}

	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

	// public float getProjectionWidth() {
	// return projectionWidth;
	// }

	// public void setProjectionWidth(float projectionWidth) {
	// this.projectionWidth = projectionWidth;
	// }

	public float getProjectionHeight() {
		return projectionHeight;
	}

	public void setProjectionHeight(float projectionHeight) {
		this.projectionHeight = projectionHeight;
	}

	// public Vector2f getProjectionSize() {
	// return projectionSize;
	// }
	//
	// public void setProjectionSize(Vector2f projectionSize) {
	// this.projectionSize = projectionSize;
	// }

	public float getZoom() {
		return zoom;
	}

	public void setZoom(float zoom) {
		this.zoom = zoom;
	}

	public float getAspectRation() {
		return aspectRation;
	}

	public void setAspectRation(float aspectRation) {
		this.aspectRation = aspectRation;
	}

	public Vector2f getProjectionSize() {
		return projectionSize;
	}

	public void setProjectionSize(Vector2f projectionSize) {
		this.projectionSize = projectionSize;
	}

}
