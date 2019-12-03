package com.star.app.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.star.app.game.controllers.AsteroidController;
import com.star.app.game.controllers.BulletController;
import com.star.app.game.controllers.ParticleController;
import com.star.app.game.controllers.SpaceItemController;
import com.star.app.screen.ScreenManager;

public class GameController {
    private int level;
    private Background background;
    private AsteroidController asteroidController;
    private BulletController bulletController;
    private ParticleController particleController;
    private Hero hero;
    private CollisionManager collisionManager;
    private SpaceItemController spaceItemController;
    private Stage stage;

    public Stage getStage() {
        return stage;
    }

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

    public GameController(SpriteBatch batch) {
        this.hero = new Hero(this, "PLAYER1");
        this.background = new Background(this);
        this.bulletController = new BulletController(this);
        this.asteroidController = new AsteroidController(this, 1);
        this.collisionManager = new CollisionManager();
        this.spaceItemController = new SpaceItemController(this);
        this.particleController = new ParticleController();
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.stage.addActor(hero.getShop());
        this.level = 0;
        Gdx.input.setInputProcessor(stage);
    }

    public void update(float dt) {
        background.update(dt);
        hero.update(dt);
        bulletController.update(dt);
        asteroidController.update(dt);
        spaceItemController.update(dt);
        particleController.update(dt);
        checkCollisions();
        stage.act(dt);
    }


    public void checkCollisions() {
        this.collisionManager.check(bulletController.getActiveList(), asteroidController.getActiveList());
        this.collisionManager.check(asteroidController.getActiveList(), this.hero);
        this.collisionManager.check(spaceItemController.getBulletPool().getActiveList(), this.hero);
        this.collisionManager.check(spaceItemController.getMoneyPool().getActiveList(), this.hero);
        this.collisionManager.check(spaceItemController.getHpPool().getActiveList(), this.hero);
    }


    public void dispose() {
        background.dispose();
    }

    public int nextLevel() {

        return ++level;
    }

    public int getLevel() {

        return level;
    }
}