package com.myproject.game.Screens;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.myproject.game.MainGame;

import java.util.ArrayList;

public class MapScreen implements Screen {
    private MainGame game;
    private SpriteBatch batch;
    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    protected Skin skin;
    private TextButton.TextButtonStyle textButtonStyle;
    private BitmapFont font;
    private int nmap;
    private TextureRegion maptr;
    private ArrayList<Texture> mapImages;

    public MapScreen(MainGame game) {
        this.game = game;
        skin = new Skin(game.buttonAtlas);
        font = new BitmapFont(Gdx.files.internal("fonts/consolas.fnt"));
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("button-up");
        textButtonStyle.down = skin.getDrawable("button-down");

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(MainGame.V_WIDTH, MainGame.V_HEIGHT, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport, batch);
        nmap = 1;
        mapImages = new ArrayList<Texture>();

        //Stage should controll input:
        Gdx.input.setInputProcessor(stage);
    }


    @Override
    public void show() {
        //Create Table
        Table mainTable = new Table();
        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.center();

        //Create buttons
        TextButton playButton = new TextButton("Play", textButtonStyle);
        TextButton leftButton = new TextButton("<", textButtonStyle);
        TextButton rightButton = new TextButton(">", textButtonStyle);
        TextButton backButton = new TextButton("Back", textButtonStyle);

        // Load map images
        mapImages.add(new Texture("mapa1.png"));
        mapImages.add(new Texture("mapa2.png"));

        //Add listeners to buttons
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PlayScreen ps = new PlayScreen(game);
                ps.setMap(nmap);
                ((Game) Gdx.app.getApplicationListener()).setScreen(ps);
            }
        });
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MenuScreen(game));
            }
        });
        leftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                nmap = nmap == 0 ? mapImages.size()-1 : nmap-1;
            }
        });
        rightButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                nmap = nmap == mapImages.size()-1 ? 0 : nmap+1;
            }
        });


        maptr = new TextureRegion(mapImages.get(nmap));
        //Add buttons to table
        mainTable.bottom();
        mainTable.add(leftButton);
        mainTable.add(playButton);
        mainTable.add(rightButton);
        mainTable.row();
        mainTable.add(backButton);

        //Add table to stage
        stage.addActor(mainTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(mapImages.get(nmap), 0,0,1920, 1080);
        batch.end();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
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
        skin.dispose();
    }

    public int getMap(){
        return nmap;
    }
}