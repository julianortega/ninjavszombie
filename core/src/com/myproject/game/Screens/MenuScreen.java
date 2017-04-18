package com.myproject.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.myproject.game.MainGame;

/**
 * Created by usuario on 3/17/17.
 */

public class MenuScreen implements Screen{
    private Stage stage;
    private MainGame game;
    private SpriteBatch batch;
    private TextureRegion background;
    private TextButton playButton;
    private TextButton.TextButtonStyle textButtonStyle;
    private Skin skin;
    private BitmapFont font;
    private Viewport viewport;


    public MenuScreen(final MainGame game){
        super();
        stage = new Stage();
        batch = new SpriteBatch();
        skin = new Skin();
        font = new BitmapFont(Gdx.files.internal("fonts/consolas.fnt"));


        skin.addRegions(game.buttonAtlas);
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("button-up");
        textButtonStyle.down = skin.getDrawable("button-down");

        playButton = new TextButton("PLAY", textButtonStyle);
        playButton.setPosition(Gdx.graphics.getWidth()/2-playButton.getWidth()/2, Gdx.graphics.getHeight()/2-playButton.getHeight()/2);
        stage.addActor(playButton);
        Gdx.input.setInputProcessor(stage);
        playButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PlayScreen(game));
            }
        });

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
