package com.myproject.game.Screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.myproject.game.Scenes.Hud;
import com.myproject.game.MainGame;
import com.myproject.game.Sprites.Zombie;
import com.myproject.game.Sprites.Player;
import com.myproject.game.Tools.B2WorldCreator;
import com.myproject.game.Tools.Controller;
import com.myproject.game.Tools.Parallax.ParallaxBackground;
import com.myproject.game.Tools.Parallax.ParallaxLayer;
import com.myproject.game.Tools.WorldContactListener;

import java.util.Random;

/**
 * Created by Brutal on 26/01/2017.
 */

public class PlayScreen implements Screen {
    private MainGame game;
    private TextureAtlas[] atlas;

    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;

    // Tiled map
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // Parallax
    private ParallaxBackground rbg;

    // Box2d
    private World world;
    private Box2DDebugRenderer b2dr;

    // Controles
    private Controller controller;

    // Sprites
    private Player player;
    private Zombie[] zombies;

    // Musica
    private Music music;
    private Sound jumpSound;

    private int frames;
    private float time;
    private float fps;

    public PlayScreen(MainGame game) {
        frames = 0;
        time = 0;

        this.game = game;
        atlas = game.atlas;

        gamecam = new OrthographicCamera();

        gamePort = new FitViewport(MainGame.V_WIDTH / MainGame.PPM, MainGame.V_HEIGHT / MainGame.PPM, gamecam);

        hud = new Hud(game.batch);

        Random r = new Random();
        int map_rand = r.nextInt(2)+1;
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("mapa"+map_rand+".tmx");
        //renderer = new OrthogonalTiledMapRenderer(map, 1/MainGame.PPM);
        renderer = new OrthogonalTiledMapRenderer(getMap(), 1/ MainGame.PPM);
        //gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        controller = new Controller(game.batch);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(this);

        player = new Player(this);
        zombies = new Zombie[8];
        zombies[0] = new Zombie(this, player.body.getPosition().x + 6, player.body.getPosition().y);
        zombies[1] = new Zombie(this, player.body.getPosition().x + 20, player.body.getPosition().y + 10);
        zombies[2] = new Zombie(this, player.body.getPosition().x + 30, player.body.getPosition().y + 10);
        zombies[3] = new Zombie(this, player.body.getPosition().x + 40, player.body.getPosition().y + 10);
        zombies[4] = new Zombie(this, player.body.getPosition().x + 45, player.body.getPosition().y + 10);
        zombies[5] = new Zombie(this, player.body.getPosition().x + 50, player.body.getPosition().y + 10);
        zombies[6] = new Zombie(this, player.body.getPosition().x + 55, player.body.getPosition().y + 10);
        zombies[7] = new Zombie(this, player.body.getPosition().x + 60, player.body.getPosition().y + 10);


        world.setContactListener(new WorldContactListener());
        // MUSIC
        music = MainGame.manager.get("audio/music/music1.ogg", Music.class);
        music.setLooping(true);
        music.setVolume(0.2f);
        //music.play();

        // SOUNDS
        jumpSound = MainGame.manager.get("audio/sounds/jump1.ogg", Sound.class);

        rbg = new ParallaxBackground(new ParallaxLayer[]{
                new ParallaxLayer(atlas[2].findRegion("bg"),new Vector2(),new Vector2(0, 0)),
        }, 1000, 750,new Vector2(0,0));

    }

    public TextureAtlas[] getAtlas(){
        return atlas;
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt){
        // controles para teclado
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && player.currentState != Player.State.JUMPING && player.body.getLinearVelocity().y < 2) {
            player.jump();
            jumpSound.play();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.body.getLinearVelocity().x <= 7)
            player.body.applyLinearImpulse(new Vector2(0.3f, 0), player.body.getWorldCenter(), true);
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.body.getLinearVelocity().x >= -7)
            player.body.applyLinearImpulse(new Vector2(-0.3f, 0), player.body.getWorldCenter(), true);



        // controles para movil
        if (controller.isUpPressed() && player.currentState != Player.State.JUMPING ) {
            player.jump();
            jumpSound.play();
        }
        if (controller.isRightPressed() && player.body.getLinearVelocity().x <= 7)
            player.body.applyLinearImpulse(new Vector2(0.3f, 0), player.body.getWorldCenter(), true);
        if (controller.isLeftPressed() && player.body.getLinearVelocity().x >= -7)
            player.body.applyLinearImpulse(new Vector2(-0.3f, 0), player.body.getWorldCenter(), true);
    }

    public void update(float dt){
        // button input
        handleInput(dt);

        // update world 60 times per second
        world.step(dt, 6, 2);

        player.update(dt);

        System.out.println("Player: "+player.body.getPosition());
        //System.out.println("Enemy: " + zombies[i.getX());

        if(player.body.getPosition().y<6) {
            game.setScreen(new GameOverScreen(game));
        }
        for(int i = 0; i < zombies.length; i++) {
            zombies[i].update(dt);
            if (player.body.getPosition().x - zombies[i].getX() < 8 && player.body.getPosition().x - zombies[i].getX() > -8) {
                System.out.println("Detectado!");
                if (player.body.getPosition().x - zombies[i].body.getPosition().x > 0) {
                    zombies[i].flip(false, false);
                    if (zombies[i].body.getLinearVelocity().x < 4) {
                        zombies[i].body.applyLinearImpulse(0.1f, 0, 0, 0, true);

                    }
                } else {
                    zombies[i].flip(true, false);
                    if (zombies[i].body.getLinearVelocity().x > -4) {
                        zombies[i].body.applyLinearImpulse(-0.1f, 0, 0, 0, true);
                    }
                }
            }
        }
        gamecam.position.x =player.body.getPosition().x;
        //gamecam.position.x = BigDecimal.valueOf(player.body.getPosition().x).setScale(3,BigDecimal.ROUND_HALF_UP).floatValue();
        gamecam.position.y = 13;
        //gamecam.position.y = player.body.getPosition().y+1;
       // System.out.println("Posicion camara: " + gamecam.position);
        gamecam.update();
        renderer.setView(gamecam);
    }


    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        rbg.render(delta);
        // render del mapa
        renderer.render();

        // render de Box2DDebugLines
        //b2dr.render(world, gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for(int i = 0; i < zombies.length; i++) {
            zombies[i].draw(game.batch);
        }
        game.batch.end();

        // dibuja la camara del hud
        hud.setFps(Gdx.graphics.getFramesPerSecond());
        hud.update(delta);
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        // solo se muestran controles en pantalla si se ejecuta en android
        if (Gdx.app.getType() == Application.ApplicationType.Android)
            controller.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
        controller.resize(width, height);
    }

    public TiledMap getMap(){
        return map;
    }

    public World getWorld(){
        return world;
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
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
