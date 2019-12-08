package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.StringBuilder;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;



public class WorldRenderer {
    private GameController gc;
    private SpriteBatch batch;
    private BitmapFont font32;
    private BitmapFont font72;
    private StringBuilder strBuilder;

    private Camera camera;

    private FrameBuffer frameBuffer;
    private TextureRegion frameBufferRegion;
    private ShaderProgram shaderProgram;

    public WorldRenderer(GameController gc, SpriteBatch batch) {
        this.gc = gc;
        this.batch = batch;
        this.font32 = Assets.getInstance().getAssetManager().get("fonts/font32.ttf", BitmapFont.class);
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/font72.ttf", BitmapFont.class);
        this.strBuilder = new StringBuilder();

        this.frameBuffer = new FrameBuffer(Pixmap.Format.RGB888, ScreenManager.SCREEN_WIDTH, ScreenManager.SCREEN_HEIGHT, false);
        this.frameBufferRegion = new TextureRegion(frameBuffer.getColorBufferTexture());
        this.frameBufferRegion.flip(false, true);
        this.shaderProgram = new ShaderProgram(Gdx.files.internal("shaders/vertex.glsl").readString(), Gdx.files.internal("shaders/fragment.glsl").readString());
        if (!shaderProgram.isCompiled()) {
            throw new IllegalArgumentException("Error compiling shader: " + shaderProgram.getLog());
        }
        this.camera = ScreenManager.getInstance().getCamera();
    }

    public void render() {
        frameBuffer.begin();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        gc.getBackground().render(batch);
        batch.end();

        camera.position.set(gc.getHero().getPosition().x, gc.getHero().getPosition().y, 0.0f);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        gc.getHero().render(batch);
        gc.getAsteroidController().render(batch);
        gc.getBotController().render(batch);
        gc.getBulletController().render(batch);
        gc.getSpaceItemController().render(batch);
        gc.getParticleController().render(batch);
        batch.end();
        frameBuffer.end();

        camera.position.set(ScreenManager.HALF_SCREEN_WIDTH, ScreenManager.HALF_SCREEN_HEIGHT, 0.0f);
        camera.update();
        ScreenManager.getInstance().getViewport().apply();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
//        batch.setShader(shaderProgram);
//        shaderProgram.setUniformf("px", gc.getHero().getPosition().x / ScreenManager.SCREEN_WIDTH);
//        shaderProgram.setUniformf("py", gc.getHero().getPosition().y / ScreenManager.SCREEN_HEIGHT);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.draw(frameBufferRegion, 0, 0);
        batch.end();
//        batch.setShader(null);

        batch.begin();
        gc.getHero().renderGUI(batch, font32);
        if (gc.getMsgTimer() > 0.0f) {
            font72.draw(batch, gc.getMsg(), 0, ScreenManager.HALF_SCREEN_HEIGHT, ScreenManager.SCREEN_WIDTH, Align.center, false);
        }
        batch.end();

        gc.getStage().draw();
    }
}
