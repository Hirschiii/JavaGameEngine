package game.components;

import org.joml.Vector3f;
import org.joml.Vector4f;

import game.engine.Component;

public class Rigidbody extends Component{
	private int collider_type = 1;
	private float friction = 0.3f;
	public Vector3f velocity = new Vector3f(0.3f, 1.0f, 0.1f);
	public transient Vector4f  tmp = new Vector4f(0, 0, 0, 0);

	
}
