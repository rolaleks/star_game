package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.StringBuilder;
import com.star.app.screen.GameOverScreen;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;
import com.star.app.screen.utils.OptionsUtils;

import java.util.ArrayList;

public class Hero extends GameObject implements Damageable {

    private static final int MAIN_WEAPON_INDEX = 0;

    private GameController gc;
    private final float frontSpeed = 750f;
    private final float rearSpeed = 375f;

    private TextureRegion starTexture;
    private TextureRegion texture;
    private KeysControl keysControl;
    private float fireTimer;
    private int score;
    private int scoreView;
    private int hpMax;
    private int hp;
    private ArrayList<Weapon> weapons;
    private StringBuilder strBuilder;
    private Skill[] skills;
    private int money;
    private Shop shop;
    private Vector2 tmpVector;


    public int getScoreView() {
        return scoreView;
    }

    public void addScore(int amount) {
        score += amount;
    }

    public int getScore() {
        return score;
    }

    public int getHp() {
        return hp;
    }

    public Weapon getCurrentWeapon() {

        return weapons.get(weapons.size() - 1);
    }

    public Skill[] getSkills() {
        return skills;
    }

    public Shop getShop() {
        return shop;
    }

    public void setCurrentWeapon(Weapon currentWeapon) {
        gc.setMsg(currentWeapon.getTitle(), 2.0f);
        weapons.add(currentWeapon);
    }

    public void replaceWeapon(Weapon currentWeapon, int index) {
        if (index < weapons.size())
            weapons.set(index, currentWeapon);
    }

    public void removeCurrentWeapon() {
        weapons.remove(weapons.size() - 1);
    }

    public boolean isMoneyEnough(int amount) {
        return money >= amount;
    }

    public void decreaseMoney(int amount) {
        money -= amount;
    }

    public Hero(GameController gc, String keysControlPrefix) {
        super();
        this.gc = gc;
        this.starTexture = Assets.getInstance().getAtlas().findRegion("star16");
        this.texture = Assets.getInstance().getAtlas().findRegion("ship");
        this.position = new Vector2(640, 360);
        this.angle = 0.0f;
        this.hitArea.radius = 30;
        this.hpMax = 50;
        this.hp = hpMax;
        this.strBuilder = new StringBuilder();
        this.keysControl = new KeysControl(OptionsUtils.loadProperties(), keysControlPrefix);
        this.createSkillsTable();
        this.shop = new Shop(this);
        this.tmpVector = new Vector2(0, 0);
        this.weapons = new ArrayList<>();
        this.setCurrentWeapon(new Weapon(
                gc, this, "Laser", 0.2f, 1, 600.0f, 500,
                new Vector3[]{
                        new Vector3(28, 0, 0),
                        new Vector3(28, 90, 90),
                        new Vector3(28, -90, -90)
                },
                1
        ));
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32, 64, 64, 1, 1, angle);
    }

    public void renderGUI(SpriteBatch batch, BitmapFont font) {
        strBuilder.clear();
        strBuilder.append("SCORE: ").append(scoreView).append("\n");
        strBuilder.append("MONEY: ").append(money).append("\n");
        strBuilder.append("HP: ").append(hp).append(" / ").append(hpMax).append("\n");
        strBuilder.append("BULLETS: ").append(getCurrentWeapon().getCurBullets()).append(" / ").append(getCurrentWeapon().getMaxBullets()).append("\n");
        font.draw(batch, strBuilder, 20, 1060);

        int mapX = 1700;
        int mapY = 800;
        batch.setColor(Color.GREEN);
        batch.draw(starTexture, mapX - 24, mapY - 24, 48, 48);
        batch.setColor(Color.RED);
        for (int i = 0; i < gc.getAsteroidController().getActiveList().size(); i++) {
            Asteroid a = gc.getAsteroidController().getActiveList().get(i);
            float dst = position.dst(a.getPosition());
            if (dst < 5000.0f) {
                tmpVector.set(a.getPosition()).sub(this.position);
                tmpVector.scl(160.0f / 5000.0f);
                batch.draw(starTexture, mapX + tmpVector.x - 16, mapY + tmpVector.y - 16, 32, 32);
            }
        }
        for (int i = 0; i < gc.getSpaceItemController().getObjectPools().size(); i++) {
            for (int j = 0; j < gc.getSpaceItemController().getObjectPools().get(i).getActiveList().size(); j++) {
                SpaceItem si = gc.getSpaceItemController().getObjectPools().get(i).getActiveList().get(j);
                float dst = position.dst(si.getPosition());
                if (dst < 5000.0f) {
                    tmpVector.set(si.getPosition()).sub(this.position);
                    tmpVector.scl(160.0f / 5000.0f);
                    batch.setColor(Color.YELLOW);
                    batch.draw(starTexture, mapX + tmpVector.x - 16, mapY + tmpVector.y - 16, 32, 32);
                }
            }
        }

        batch.setColor(Color.WHITE);
        for (int i = 0; i < 120; i++) {
            batch.draw(starTexture, mapX + 160.0f * MathUtils.cosDeg(360.0f / 120.0f * i) - 8, mapY + 160.0f * MathUtils.sinDeg(360.0f / 120.0f * i) - 8);
        }
    }

    public void update(float dt) {
        if (velocity.len() > 1000.0f) {
            velocity.nor().scl(1000.0f);
        }
        fireTimer += dt;
        updateScore(dt);

        if (Gdx.input.isKeyPressed(keysControl.fire)) {
            tryToFire();
        }
        if (Gdx.input.isKeyPressed(keysControl.left)) {
            angle += 180.0f * dt;
        }
        if (Gdx.input.isKeyPressed(keysControl.right)) {
            angle -= 180.0f * dt;
        }
        if (Gdx.input.isKeyPressed(keysControl.forward)) {
            velocity.x += (float) Math.cos(Math.toRadians(angle)) * frontSpeed * dt;
            velocity.y += (float) Math.sin(Math.toRadians(angle)) * frontSpeed * dt;
        } else if (Gdx.input.isKeyPressed(keysControl.backward)) {
            velocity.x -= (float) Math.cos(Math.toRadians(angle)) * rearSpeed * dt;
            velocity.y -= (float) Math.sin(Math.toRadians(angle)) * rearSpeed * dt;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
            shop.setVisible(true);
        }
        position.mulAdd(velocity, dt);
        float stopKoef = 1.0f - 2.0f * dt;
        if (stopKoef < 0.0f) {
            stopKoef = 0.0f;
        }
        velocity.scl(stopKoef);

        if (velocity.len() > 50.0f) {
            float bx, by;
            bx = position.x - 28.0f * (float) Math.cos(Math.toRadians(angle));
            by = position.y - 28.0f * (float) Math.sin(Math.toRadians(angle));
            for (int i = 0; i < 5; i++) {
                gc.getParticleController().setup(
                        bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                        velocity.x * -0.3f + MathUtils.random(-20, 20), velocity.y * -0.3f + MathUtils.random(-20, 20),
                        0.5f,
                        1.2f, 0.2f,
                        1.0f, 0.5f, 0.0f, 1.0f,
                        1.0f, 1.0f, 1.0f, 0.0f
                );
            }
        }
        checkSpaceBorders();

        super.update();
    }

    @Override
    public boolean takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            GameOverScreen.setScore(this.score);
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAMEOVER);
        }
        return false;
    }


    public void tryToFire() {
        if (fireTimer > getCurrentWeapon().getFirePeriod()) {
            fireTimer = 0.0f;
            if (getCurrentWeapon().isExpired() && getCurrentWeapon().getCurBullets() == 0) {
                removeCurrentWeapon();
            } else {
                getCurrentWeapon().fire();
            }
        }
    }

    public void updateScore(float dt) {
        if (scoreView != score) {
            float scoreSpeed = (score - scoreView) / 2.0f;
            if (Math.abs(scoreSpeed) < 2000.0f) {
                scoreSpeed = Math.signum(scoreSpeed) * 2000.0f;
            }
            scoreView += scoreSpeed * dt;
            if (Math.abs(scoreView - score) < Math.abs(scoreSpeed * dt)) {
                scoreView = score;
            }
        }
    }

    @Override
    public void collide(Collidable collidable) {
        if (collidable instanceof Asteroid) {
            collideAsteroid((Asteroid) collidable);
        }
    }

    private void collideAsteroid(Asteroid asteroid) {
        this.takeDamage((int) (asteroid.getScale() * 10));
    }

    public void addHp(int amount) {
        hp += amount;
        if (hp > hpMax) {
            hp = hpMax;
        }
    }

    public void upgrade(int index) {
        int level = this.skills[index].level;
        this.skills[index].effects[level - 1].run();
        this.skills[index].level++;
    }

    public void createSkillsTable() {
        this.skills = new Skill[2];
        skills[0] = new Skill("HP",
                new Runnable[]{
                        () -> hpMax += 10,
                        () -> hpMax += 20,
                        () -> hpMax += 30,
                        () -> hpMax += 40,
                        () -> hpMax += 50,
                        () -> hpMax += 50
                },
                new int[]{
                        10,
                        20,
                        30,
                        50,
                        100,
                        500
                }
        );

        skills[1] = new Skill("WX-I",
                new Runnable[]{
                        () -> {
                            this.replaceWeapon(
                                    new Weapon(
                                            gc, this, "Laser", 0.3f, 1, 600.0f, 320,
                                            new Vector3[]{
                                                    new Vector3(24, 90, 10),
                                                    new Vector3(24, 0, 0),
                                                    new Vector3(24, -90, -10)
                                            },
                                            1
                                    ), MAIN_WEAPON_INDEX);
                        },
                        () -> {
                            this.replaceWeapon(new Weapon(
                                    gc, this, "Laser", 0.3f, 1, 600.0f, 320,
                                    new Vector3[]{
                                            new Vector3(24, 90, 20),
                                            new Vector3(24, 20, 0),
                                            new Vector3(24, -20, 0),
                                            new Vector3(24, -90, -20)
                                    },
                                    1
                            ), MAIN_WEAPON_INDEX);
                        },
                        () -> {
                            this.replaceWeapon(new Weapon(
                                    gc, this, "Laser", 0.05f, 2, 600.0f, 32000,
                                    new Vector3[]{
                                            new Vector3(24, 90, 20),
                                            new Vector3(24, 20, 0),
                                            new Vector3(24, 0, 0),
                                            new Vector3(24, -20, 0),
                                            new Vector3(24, -90, -20)
                                    },
                                    1
                            ), MAIN_WEAPON_INDEX);
                        }
                },
                new int[]{
                        100,
                        200,
                        300
                }
        );
    }

    public class Skill {
        private int level;
        private int maxLevel;
        private String title;
        private Runnable[] effects;
        private int[] cost;

        public int getLevel() {
            return level;
        }

        public String getTitle() {
            return title;
        }

        public int getMaxLevel() {
            return maxLevel;
        }

        public int getCurrentLevelCost() {
            return cost[level - 1];
        }

        public Skill(String title, Runnable[] effects, int[] cost) {
            this.level = 1;
            this.title = title;
            this.effects = effects;
            this.cost = cost;
            this.maxLevel = effects.length;
            if (effects.length != cost.length) {
                throw new RuntimeException("Unable to create skill tree");
            }
        }

        public boolean isUpgradable() {
            return level < effects.length + 1;
        }

        public void upgrade() {
            effects[level - 1].run();
            level++;
        }
    }
}
