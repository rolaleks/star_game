package com.star.app.game.controllers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.star.app.game.Asteroid;
import com.star.app.game.Bot;
import com.star.app.game.GameController;
import com.star.app.game.helpers.ObjectPool;

public class BotController extends ObjectPool<Bot> {

    private GameController gc;
    private int initBotCount;
    private int initMaxHp;

    @Override
    protected Bot newObject() {
        return new Bot(gc);
    }

    public BotController(GameController gc, int initBotCount) {
        this.gc = gc;
        this.initBotCount = initBotCount;
        this.initMaxHp = 200;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            Bot bot = activeList.get(i);
            bot.render(batch);
        }
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }

    public void nextLevel() {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).deactivate();
        }


        int level = gc.getLevel();
        //Каждые 10 уровней добавляем по 1 Боту
        int extraBots = (level - level % 10) / 10;
        for (int i = 0; i < initBotCount + extraBots; i++) {

            //с каждым уровне увеличиваем здоровье боту на 5
            launch(initMaxHp + (level - 1) * 5);
        }

        return;
    }

    public void launch(int maxHp) {
        getActiveElement().activate(maxHp);
    }
}
