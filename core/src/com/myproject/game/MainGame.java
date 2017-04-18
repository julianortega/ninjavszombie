package com.myproject.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Timer;
import com.myproject.game.Screens.MenuScreen;
import com.myproject.game.Screens.PlayScreen;
import com.myproject.game.Screens.SplashScreen;

import java.util.Random;

public class MainGame extends Game {
	public static final int V_WIDTH = 1920;
	public static final int V_HEIGHT = 1080;
	public static final float PPM = 100;

	public static final short GROUND_BIT = 1;
	public static final short PLAYER_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short COIN_BIT = 8;
	public static final short DESTROYED_BIT = 16;
	public static final short OBJECT_BIT = 32;
	public static final short ENEMY_BIT = 64;
	public static final short ENEMY_HEAD_BIT = 128;

	private static long SPLASH_MINIMUM_MILLIS = 1500;

	public static AssetManager manager;
	public static MenuScreen screen;

	public SpriteBatch batch;
	public TextureAtlas[] atlas;
	public TextureAtlas buttonAtlas;

	@Override
	public void create () {
		setScreen(new SplashScreen());

		final long splash_start_time = System.currentTimeMillis();
		batch = new SpriteBatch();
		atlas = new TextureAtlas[4];


		// BUTTON ATLAS
		buttonAtlas = new TextureAtlas(Gdx.files.internal("buttons/button.txt"));

		// ATLAS 1 - MALE PLAYER
		atlas[0] = new TextureAtlas();
		atlas[0].addRegion("ninja_idle", new Texture("ninja/ninja_idle.png"), 0, 0, 362, 569);
		atlas[0].addRegion("ninja_jump", new Texture("ninja/ninja_jump.png"), 0, 0, 1810, 966);
		atlas[0].addRegion("ninja_run", new Texture("ninja/ninja_run.png"), 0, 0, 1815, 916);
		atlas[0].addRegion("ninja_attack", new Texture("ninja/ninja_attack.png"), 0, 0, 1608, 1980);

		// ATLAS 2 - FEMALE PLAYER TODO


		// ATLAS 3 - BACKGROUND
		atlas[2] = new TextureAtlas();
		atlas[2].addRegion("bg", new Texture("bg/bg"+new Random().nextInt(5)+".png"), 0, 0, 1000, 750);

		//ATLAS 4 - ENEMIES
		atlas[3] = new TextureAtlas();
		atlas[3].addRegion("zombie_walk", new Texture("zombie/zombie_walk.png"),0,0,1720,1557);


		// MUSIC MANAGER
		manager = new AssetManager();
		manager.load("audio/music/music1.ogg", Music.class);
		manager.load("audio/sounds/jump1.ogg", Sound.class);
		//manager.load("audio/sounds/jump2.ogg", Sound.class);
		//manager.load("audio/sounds/coin.wav", Sound.class);
		//manager.load("audio/sounds/break.wav", Sound.class);
		manager.finishLoading();

		//screen = new PlayScreen(this);
		screen = new MenuScreen(this);

		new Thread(new Runnable() {
			@Override
			public void run() {
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						long splash_elapsed_time = System.currentTimeMillis() - splash_start_time;
						System.out.println("Loading time: " + splash_elapsed_time);
						if (splash_elapsed_time < MainGame.SPLASH_MINIMUM_MILLIS) {
							Timer.schedule(
									new Timer.Task() {
										@Override
										public void run() {
											MainGame.this.setScreen(screen);
										}
									}, (float)(MainGame.SPLASH_MINIMUM_MILLIS - splash_elapsed_time) / 1000f);
						} else {
							MainGame.this.setScreen(screen);
						}
					}
				});
			}
		}).start();

	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose(){
		super.dispose();
		batch.dispose();
		manager.dispose();
	}
	

}
