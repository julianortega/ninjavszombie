package com.myproject.game.Screens;

/**
 * Created by usuario on 2/21/17.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SplashScreen implements Screen {
    private SpriteBatch batch;
    private TextureRegion[] textures;
    public Animation<TextureRegion> loadingAnimation;
    public TextureRegion splashBackground;
    float stateTime;


    public SplashScreen() {
        super();
        batch = new SpriteBatch();
        textures = new TextureRegion[8];
        for(int i = 0; i < textures.length; i++) {
            textures[i] = new TextureRegion(new Texture("loading/load-" + i + "-0.png"));
        }
        loadingAnimation = new Animation<TextureRegion>(0.1f, textures);
        splashBackground = new TextureRegion(new Texture("ninja_splash.png"));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.85f, 0.85f, 0.85f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

        // Get current frame of animation for the current stateTime
        TextureRegion currentFrame = loadingAnimation.getKeyFrame(stateTime, true);
        float size = Gdx.graphics.getHeight()/10;
        batch.begin();
        batch.draw(splashBackground,0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Draw the current frame at the center-bottom of the screen
        batch.draw(
                currentFrame,
                Gdx.graphics.getWidth()/2-size/2,
                Gdx.graphics.getHeight()/2-size/2,
                size,size
        );
        batch.end();
    }

    @Override
    public void hide() { }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void show() { }

    @Override
    public void resize(int width, int height) { }

    @Override
    public void dispose() {
        batch.dispose();
    }
}