package com.star.app.game;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject implements Collidable {

    protected Vector2 position;
    protected Vector2 velocity;
    protected Circle hitArea;
    protected float angle;

    public float getAngle() {
        return angle;
    }

    public GameObject() {
        angle = 0.0f;
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.hitArea = new Circle(0, 0, 1);
    }

    public boolean getIsCollided(Circle circle) {

        return this.getHitArea().overlaps(circle);
    }

    @Override
    public Circle getHitArea() {
        return hitArea;
    }

    public void update() {
        this.hitArea.setPosition(position);
    }


    public Vector2 getPosition() {
        return this.position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void checkSpaceBorders() {
        if (position.x < hitArea.radius) {
            position.x += GameController.SPACE_WIDTH;
        }
        if (position.x > GameController.SPACE_WIDTH - hitArea.radius) {
            position.x -= GameController.SPACE_WIDTH;
        }
        if (position.y < hitArea.radius) {
            position.y += GameController.SPACE_HEIGHT;
        }
        if (position.y > GameController.SPACE_HEIGHT - hitArea.radius) {
            position.y -= GameController.SPACE_HEIGHT;
        }
    }
}
