package com.myproject.game.Screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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

public class GameOverScreen implements Screen {
    private MainGame game;
    private SpriteBatch batch;
    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    protected Skin skin;
    private TextButton.TextButtonStyle textButtonStyle;
    private BitmapFont font;
    public TextureRegion background;
    private int nmap;

    public GameOverScreen(MainGame game)
    {
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

       /* camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();*/

        stage = new Stage(viewport, batch);
        background = new TextureRegion(new Texture("gameover_background.png"));
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
        TextButton playButton = new TextButton("Play again", textButtonStyle);
        TextButton menuButton = new TextButton("Back to menu", textButtonStyle);

        //Add listeners to buttons
        playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PlayScreen ps = new PlayScreen(game);
                ps.setMap(nmap);
                ((Game)Gdx.app.getApplicationListener()).setScreen(ps);
            }
        });
        menuButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MenuScreen(game));
            }
        });



        //Add buttons to table
        mainTable.bottom().padBottom(5);
        mainTable.add(playButton);
        mainTable.row();
        mainTable.add(menuButton);

        //Add table to stage
        if (Gdx.app.getType() == Application.ApplicationType.Android)
            stage.addActor(mainTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(background,0,0,1920,1080);
        batch.end();
        stage.act();
        stage.draw();
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            PlayScreen ps = new PlayScreen(game);
            ps.setMap(nmap);
            ((Game)Gdx.app.getApplicationListener()).setScreen(ps);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
            ((Game)Gdx.app.getApplicationListener()).setScreen(new MenuScreen(game));
    }

    @Override
    public void resize(int width, int height) {
     /*   viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();*/
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
        batch.dispose();
    }

    public void setMap(int nmap) {
        this.nmap = nmap;
    }
}