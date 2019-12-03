package com.star.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.star.app.screen.GameScreen;
import com.star.app.screen.ScreenManager;

public class StarGame extends Game {
    private SpriteBatch batch;

    // todo: На подумать:
    // - Поддержка русских символов
    // - Сохранение состояния игры в файл
    // - Перерисовать магазин

    // Домашнее задание:
    // 1. Разбор кода
    // 2. Сделать так, чтобы при приближении к power-ups они начинали лететь в сторону игрока
    // 2. При уничтожении всех астероидов уровень игры повышается, появляются новые астероиды,
    // у которых побольше жизни, и которые бьют игрока больнее
    // *. Можно реализовать свою идею вместо п. 1 и 2 (не сильно большую)

    @Override
    public void create() {
        batch = new SpriteBatch();
        ScreenManager.getInstance().init(this, batch);
        ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        float dt = Gdx.graphics.getDeltaTime();
        getScreen().render(dt);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
