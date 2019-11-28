package com.star.app.game;


public class GameController {
    private Background background;
    private AsteroidController asteroidController;
    private BulletController bulletController;
    private ParticleController particleController;
    private Hero hero;
    private CollisionManager collisionManager;

    public AsteroidController getAsteroidController() {
        return asteroidController;
    }

    public BulletController getBulletController() {
        return bulletController;
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
        this.particleController = new ParticleController();
    }

    public void update(float dt) {
        background.update(dt);
        hero.update(dt);
        bulletController.update(dt);
        asteroidController.update(dt);
        particleController.update(dt);
        checkCollisions();
    }


    public void checkCollisions() {
        this.collisionManager.check(bulletController.getActiveList(), asteroidController.getActiveList());
        this.collisionManager.check(asteroidController.getActiveList(), this.hero);
    }
}