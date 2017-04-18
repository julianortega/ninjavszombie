package com.myproject.game.Sprites;

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

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

/**
 * Created by Brutal on 26/01/2017.
 */

public class Player extends Sprite {
    public enum State { FALLING, JUMPING, STANDING, RUNNING, ATTACKING };
    public State currentState;
    public State previousState;

    public World world;
    public Body body;
    private TextureRegion playerStand;
    private Animation<TextureRegion> playerRun, playerJump, playerAttack;
    private float stateTimer;
    private boolean runningRight;


    public Player(PlayScreen screen){
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 5; i++)
            frames.add(new TextureRegion(screen.getAtlas()[0].findRegion("ninja_run"), i*363, 0, 363, 458));
        for(int i = 0; i < 5; i++)
            frames.add(new TextureRegion(screen.getAtlas()[0].findRegion("ninja_run"), i*363, 458, 363, 458));
        playerRun = new Animation(0.1f, frames);
        frames.clear();

        for(int i = 0; i < 5; i++)
            frames.add(new TextureRegion(screen.getAtlas()[0].findRegion("ninja_jump"), i*362, 0, 362, 483));
        for(int i = 0; i < 5; i++)
            frames.add(new TextureRegion(screen.getAtlas()[0].findRegion("ninja_jump"), i*362, 483, 362, 483));
        playerJump = new Animation(0.1f, frames);
        frames.clear();

        for(int i = 0; i < 3; i++)
            frames.add(new TextureRegion(screen.getAtlas()[0].findRegion("ninja_attack"), i*536, 0, 536, 495));
        for(int i = 0; i < 3; i++)
            frames.add(new TextureRegion(screen.getAtlas()[0].findRegion("ninja_attack"), i*536, 495, 536, 495));
        frames.add(new TextureRegion(screen.getAtlas()[0].findRegion("ninja_attack"), 0, 495*2, 536, 495));
        playerAttack = new Animation(0.1f, frames);
        frames.clear();

        playerStand = new TextureRegion(screen.getAtlas()[0].findRegion("ninja_idle"), 0, 50, 360, 470);

        definePlayer();
        setBounds(0, 0, 140 / MainGame.PPM, 140 / MainGame.PPM);
        setRegion(playerStand);
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
        else
            return State.STANDING;
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
                MainGame.BRICK_BIT |
                MainGame.COIN_BIT |
                MainGame.ENEMY_BIT |
                MainGame.OBJECT_BIT |
                MainGame.ENEMY_HEAD_BIT;

        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MainGame.PPM, 6 / MainGame.PPM), new Vector2(2 / MainGame.PPM, 6 / MainGame.PPM));
        fixtureDef.shape = head;
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef).setUserData("head");
    }

    public void jump(){
        if ( currentState != State.JUMPING ) {
            body.applyLinearImpulse(new Vector2(0, 6), body.getWorldCenter(), true);
            currentState = State.JUMPING;
        }
    }

}
