package com.star.app.game;

import com.badlogic.gdx.math.Circle;

public interface Collidable {

    public Circle getHitArea();

    public boolean getIsCollided(Circle circle);

    public void collide(Collidable collidable);
}
