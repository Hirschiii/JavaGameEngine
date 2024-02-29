package game.components;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import game.components.enums.BodyType;

public class Rigidbody extends Component {
	private Vector2f velocity = new Vector2f();
	private float mass = 0;
	private BodyType bodyType = BodyType.Dynamic;

}
