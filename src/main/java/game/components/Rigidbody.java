package game.components;

import org.joml.Vector3f;
import org.joml.Vector4f;


public class Rigidbody extends Component{
	private boolean active = false;
	private int collider_type = 1;
	private float friction = 0.3f;
	public Vector3f velocity = new Vector3f();

	
}
