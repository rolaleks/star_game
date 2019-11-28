package com.star.app.game;


import com.star.app.game.controllers.AsteroidController;
import com.star.app.game.controllers.BulletController;
import com.star.app.game.controllers.ParticleController;
import com.star.app.game.controllers.SpaceItemController;

public class GameController {
    private Background background;
    private AsteroidController asteroidController;
    private BulletController bulletController;
    private ParticleController particleController;
    private Hero hero;
    private CollisionManager collisionManager;
    private SpaceItemController spaceItemController;

    public AsteroidController getAsteroidController() {
        return asteroidController;
    }

    public BulletController getBulletController() {
        return bulletController;
    }

    public SpaceItemController getSpaceItemController() {
        return spaceItemController;
    }

    public Background getBackground() {
        return background;
    }

    public ParticleController getParticleController() {
        return particleController;
    }

    public Hero getHero() {
        return hero;
    }

    public GameController() {
        this.background = new Background(this);
        this.hero = new Hero(this);
        this.bulletController = new BulletController(this);
        this.asteroidController = new AsteroidController(this,3);
        this.collisionManager = new CollisionManager();
        this.spaceItemController = new SpaceItemController(this);
        this.particleController = new ParticleController();
    }

    public void update(float dt) {
        background.update(dt);
        hero.update(dt);
        bulletController.update(dt);
        asteroidController.update(dt);
        spaceItemController.update(dt);
        particleController.update(dt);
        checkCollisions();
    }


    public void checkCollisions() {
        this.collisionManager.check(bulletController.getActiveList(), asteroidController.getActiveList());
        this.collisionManager.check(asteroidController.getActiveList(), this.hero);
        this.collisionManager.check(spaceItemController.getBulletPool().getActiveList(), this.hero);
        this.collisionManager.check(spaceItemController.getMoneyPool().getActiveList(), this.hero);
        this.collisionManager.check(spaceItemController.getHpPool().getActiveList(), this.hero);
    }
}