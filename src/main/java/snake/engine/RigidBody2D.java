package snake.engine;

import org.joml.Vector2f;

import snake.components.Component;

public class RigidBody2D extends Component{
	private Transform transform=new Transform();

	private Vector2f linearVelocity=new Vector2f();
	private float angularVelocity=0.0f;
	private float linearDamping=0.0f;
	private float angularDamping=0.0f;

	private boolean fixedRotation =false;
	public Vector2f getPosition(){
		return this.transform.position;
	}
	public void setTransform(Vector2f position,float rotation){
		this.transform.position.set(position);
		this.transform.setAngle(rotation);
	}
	public void setTransform(Vector2f position){
		this.transform.position.set(position);
	}
	public float getRotation(){
		return this.transform.angle;
	}
	
}
