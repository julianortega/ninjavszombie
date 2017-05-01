package com.myproject.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.myproject.game.MainGame;
import com.myproject.game.Screens.PlayScreen;

/**
 * Created by usuario on 01/02/2017.
 */

public class Zombie extends Enemy {

    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private TextureRegion standAnimation;
    private Array<TextureRegion> frames;
    private boolean destroyed;

    public Zombie(PlayScreen screen, float x, float y){
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for(int i = 0; i < 2; i++){
            frames.add(new TextureRegion(screen.getAtlas()[3].findRegion("zombie_walk"), i*430,0,430,519));
        }
        walkAnimation = new Animation(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 140 / MainGame.PPM, 140 / MainGame.PPM);
        destroyed = false;
    }

    public void update(float dt){
        stateTime += dt;
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(walkAnimation.getKeyFrame(stateTime, true));
    }


    @Override
    protected void defineEnemy() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(30 / MainGame.PPM, 60 / MainGame.PPM);
        fixtureDef.filter.categoryBits = MainGame.ENEMY_BIT;
        fixtureDef.filter.maskBits = MainGame.GROUND_BIT |
                MainGame.PLAYER_BIT |
                MainGame.BRICK_BIT |
                MainGame.BULLET_BIT |
                MainGame.ENEMY_BIT |
                MainGame.OBJECT_BIT;

        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(this);
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

}
