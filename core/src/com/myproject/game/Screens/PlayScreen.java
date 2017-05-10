package com.myproject.game.Screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.myproject.game.Scenes.Hud;
import com.myproject.game.MainGame;
import com.myproject.game.Sprites.Bullet;
import com.myproject.game.Sprites.Zombie;
import com.myproject.game.Sprites.Player;
import com.myproject.game.Tools.B2WorldCreator;
import com.myproject.game.Tools.Controller;
import com.myproject.game.Tools.Parallax.ParallaxBackground;
import com.myproject.game.Tools.Parallax.ParallaxLayer;
import com.myproject.game.Tools.WorldContactListener;

import java.util.Random;

/**
 * Created by usuario on 26/01/2017.
 */

public class PlayScreen implements Screen {
    private MainGame game;
    private TextureAtlas[] atlas;
    private WorldContactListener wcl;

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
    private int numZombies;
    private float[] zombie_spawns;
    private Array<Bullet> bullets;

    // Musica
    private Music music;


    private boolean paused;
    private Label pause_label;
    private int distance;

    public static final long FIRE_RATE = 3000000L;
    public long lastShot;


    public PlayScreen(MainGame game) {
        this.game = game;
        atlas = game.atlas;

        gamecam = new OrthographicCamera();

        gamePort = new FitViewport(MainGame.V_WIDTH / MainGame.PPM, MainGame.V_HEIGHT / MainGame.PPM, gamecam);
        wcl = new WorldContactListener();
        hud = new Hud(game.batch);

        Random r = new Random();
        int map_rand = r.nextInt(2)+1;
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("mapa"+map_rand+".tmx");
        //renderer = new OrthogonalTiledMapRenderer(map, 1/MainGame.PPM);
        renderer = new OrthogonalTiledMapRenderer(getMap(), 1/ MainGame.PPM);
        //gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        controller = new Controller(game.batch);

        world = new World(new Vector2(0, -9.8f), true);
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(this);

        player = new Player(this);

        numZombies = 10; // number of zombies that spawn

        // define zombie spawns
        zombie_spawns = new float[numZombies];
        zombie_spawns[0] = 24.5f;
        zombie_spawns[1] = 30;
        zombie_spawns[2] = 43;
        zombie_spawns[3] = 40.6f;
        zombie_spawns[4] = 60.7f;
        zombie_spawns[5] = 67;
        zombie_spawns[6] = 74;
        zombie_spawns[7] = 82;
        zombie_spawns[8] = 84;
        zombie_spawns[9] = 92;

        zombies = new Zombie[numZombies];
        for(int i = 0; i < numZombies; i++)
            zombies[i] = new Zombie(this, zombie_spawns[i], 12+i);

        bullets = new Array<Bullet>();

        world.setContactListener(wcl);
        // MUSIC
        music = MainGame.manager.get("audio/music/music.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(0.3f);
        music.play();

        rbg = new ParallaxBackground(new ParallaxLayer[]{
                new ParallaxLayer(atlas[2].findRegion("bg"),new Vector2(),new Vector2(0, 0)),
        }, 1000, 750,new Vector2(0,0));

        pause_label = new Label("PAUSE", new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/consolas.fnt"), false), Color.WHITE));
        distance = (int)player.body.getPosition().x;
    }

    public TextureAtlas[] getAtlas(){
        return atlas;
    }

    @Override
    public void show() {

    }

    private void handleInput(float dt){
        // keyboard input
        if(!paused) {
            if(Gdx.input.isKeyPressed(Input.Keys.SPACE))
                player.jump();
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.body.getLinearVelocity().x <= 7)
                player.body.applyLinearImpulse(new Vector2(0.3f, 0), player.body.getWorldCenter(), true);
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.body.getLinearVelocity().x >= -7)
                player.body.applyLinearImpulse(new Vector2(-0.3f, 0), player.body.getWorldCenter(), true);
            if(Gdx.input.isKeyPressed(Input.Keys.Z)) {
                if(System.nanoTime() - lastShot >= FIRE_RATE) {
                    bullets.add(new Bullet(this, player.body.getPosition().x, player.body.getPosition().y));
                    lastShot = System.nanoTime();
                }
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
            paused ^= true;


        // android input
        if (controller.isUpPressed())
            player.jump();
        if (controller.isRightPressed() && player.body.getLinearVelocity().x <= 7)
            player.body.applyLinearImpulse(new Vector2(0.3f, 0), player.body.getWorldCenter(), true);
        if (controller.isLeftPressed() && player.body.getLinearVelocity().x >= -7)
            player.body.applyLinearImpulse(new Vector2(-0.3f, 0), player.body.getWorldCenter(), true);
    }

    private void update(float dt){
        // update world 60 times per second
        world.step(dt, 6, 2);

        // remove bodies
        Array<Body> bodiesToRemove = wcl.getBodiesToRemove();
        for(Body b : bodiesToRemove){
            Array<Body> bd = new Array<Body>();
            world.getBodies(bd);

            if (bd.contains(b, true)) {
                b.setActive(false);
                world.destroyBody(b);
            }
        }
        bodiesToRemove.clear();

        player.update(dt);

        if(player.isDead()) {
            game.setScreen(new GameOverScreen(game));
        }

        for(Zombie z: zombies) {
            z.update(dt);
            if (player.body.getPosition().x - z.getX() < 6 && player.body.getPosition().x - z.getX() > -6) {
                if (player.body.getPosition().x - z.body.getPosition().x > 0) {
                    z.flip(false, false);
                    if (z.body.getLinearVelocity().x < 4) {
                        z.body.applyLinearImpulse(0.34f, 0, 0, 0, true);

                    }
                } else {
                    z.flip(true, false);
                    if (z.body.getLinearVelocity().x > -4) {
                        z.body.applyLinearImpulse(-0.3f, 0, 0, 0, true);
                    }
                }
            }
        }
        for(Bullet b: bullets){
            if(b.isDestroyed())
                bullets.removeValue(b,true);
            else
                b.update(dt);
        }

        gamecam.position.x = player.body.getPosition().x;
        gamecam.position.y = 13;

        gamecam.update();
        renderer.setView(gamecam);
    }


    @Override
    public void render(float delta) {
        // button input
        handleInput(delta);

        // it deletes pause label when game is resumed
        if(hud.stage.getActors().contains(pause_label, true))
            hud.stage.getActors().pop();

        if(!paused) {
            update(delta);
            game.batch.setProjectionMatrix(gamecam.combined);
            // dibuja la camara del hud
            hud.setFps(Gdx.graphics.getFramesPerSecond());
            hud.setHp(player.getHp());
            if(player.body.getPosition().x>distance) {
                distance = (int)player.body.getPosition().x;
                hud.setDistance(distance-10);
            }
            hud.update(delta);
        }else{
            hud.stage.getActors().add(pause_label);
        }
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
        for (Zombie z : zombies) {
            if (!z.isDestroyed())
                z.draw(game.batch);
        }
        for(Bullet b: bullets){
            if (!b.isDestroyed())
                b.draw(game.batch);
        }
        game.batch.end();

        // solo se muestran controles en pantalla si se ejecuta en android
        if (Gdx.app.getType() == Application.ApplicationType.Android)
            controller.draw();

        /*hud.setFps(Gdx.graphics.getFramesPerSecond());
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);*/
        hud.stage.draw();

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

    public Player getPlayer() {
        return player;
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
