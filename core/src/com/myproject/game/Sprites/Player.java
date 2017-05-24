package com.myproject.game.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.myproject.game.MainGame;
import com.myproject.game.Screens.PlayScreen;
import com.myproject.game.Tools.AnimationCreator;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

/**
 * Created by Brutal on 26/01/2017.
 */

public class Player extends Sprite {

    public enum State { FALLING, JUMPING, STANDING, RUNNING, ATTACKING };
    public State currentState;
    private State previousState;

    private World world;
    public Body body;
    private TextureRegion playerStand;
    private Animation<TextureRegion> playerRun, playerJump, playerAttack;
    private float stateTimer;
    private boolean runningRight, shooting;
    private int hp;
    private Sound jumpSound;
    private Array<Bullet> bullets;
    private PlayScreen screen;

    public Player(PlayScreen screen) {
        AnimationCreator fc = new AnimationCreator(screen);
        this.screen = screen;
        world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;
        playerRun = fc.createPlayerRunAnimation();
        playerJump = fc.createPlayerJumpAnimation();
        playerAttack = fc.createPlayerAttackAnimation();
        playerStand = fc.createPlayerStandTexture();
        hp = 3;
        bullets = new Array<Bullet>();
        shooting = false;

        definePlayer();
        setBounds(0, 0, 140 / MainGame.PPM, 140 / MainGame.PPM);
        setRegion(playerStand);

        // SOUNDS
        jumpSound = MainGame.manager.get("audio/sounds/jump1.ogg", Sound.class);
    }

    public void update(float dt){
        setPosition(body.getPosition().x - getWidth() /2, body.getPosition().y - getHeight() /2);
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();

        TextureRegion region;

        switch (currentState){
            case JUMPING:
            case FALLING:
                region = playerJump.getKeyFrame(stateTimer, false);
                break;
            case RUNNING:
                region = playerRun.getKeyFrame(stateTimer, true);
                break;
            case ATTACKING:
                region = playerAttack.getKeyFrame(stateTimer, true);
                break;

            case STANDING:
            default:
                region = playerStand;
                break;
        }

        if((body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }
        else if((body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;

        return region;
    }

    public State getState(){
        if(body.getLinearVelocity().y > 0 || (body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if(body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if(body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else {
            if(!shooting && bullets.size==0)
                return State.STANDING;
            else
                return State.ATTACKING;
        }
    }

    public void definePlayer() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(1024 / MainGame.PPM, 1024 / MainGame.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.3f, 0.6f);

        fixtureDef.filter.categoryBits = MainGame.PLAYER_BIT;
        fixtureDef.filter.maskBits = MainGame.GROUND_BIT |
                MainGame.ENEMY_BIT;

        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(this);
    }

    public void jump(){
        if ( currentState != State.JUMPING ) {
            body.applyLinearImpulse(new Vector2(0, 6), body.getWorldCenter(), true);
            jumpSound.play();
        }
    }

    public void shoot(){
        bullets.add(new Bullet(screen, body.getPosition().x, body.getPosition().y));
        shooting = true;
    }

    public void stopShoot(){
        shooting = false;
    }

    public void hit(){
        hp--;
        System.out.println("Tocado! Te quedan "+hp+" vidas!");
    }

    public boolean isDead(){
        return hp<=0 || body.getPosition().y<6;
    }

    public int getHp(){
        return hp;
    }
    public Array<Bullet> getBullets(){
        return bullets;
    }

}
