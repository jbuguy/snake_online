package snake.engine;

import org.joml.Vector2f;

public class Transform {
    public Vector2f position;
    public Vector2f scale;
    private float cosAngle = 1;
    private float sinAngle = 0;

    public Transform() {
        init(new Vector2f(), new Vector2f());
    }

    public Transform(Vector2f position) {
        init(position, new Vector2f());
    }

    public Transform(Vector2f position, Vector2f scale) {
        init(position, scale);
    }

    public float getCosAngle() {
        return cosAngle;
    }

    public float getSinAngle() {
        return sinAngle;
    }

    public void init(Vector2f position, Vector2f scale) {
        this.position = position;
        this.scale = scale;
    }

    public Transform copy() {
        return new Transform(new Vector2f(this.position), new Vector2f(this.scale));
    }

    public void copy(Transform to) {
        to.position.set(this.position);
        to.scale.set(this.scale);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((position == null) ? 0 : position.hashCode());
        result = prime * result + ((scale == null) ? 0 : scale.hashCode());
        return result;
    }

    public void setAngle(float angle) {
        this.cosAngle = (float) Math.cos(Math.toRadians((float) angle));
        this.sinAngle = ((float) Math.sin((Math.toRadians((float) angle))));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Transform other = (Transform) obj;
        return position.equals(other.position) && scale.equals(other.scale) && sinAngle == other.sinAngle
                && cosAngle == other.cosAngle;
    }

}
