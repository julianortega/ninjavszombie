package com.myproject.game.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.myproject.game.MainGame;
import com.myproject.game.Screens.PlayScreen;

/**
 * Created by Brutal on 01/05/2017.
 */

public class Bullet extends Sprite {
    protected World world;
    protected PlayScreen screen;
    public Body body;
    private TextureRegion kunai;
    private boolean isDestroyed;

    public Bullet(PlayScreen screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineBullet();
        kunai = new TextureRegion(screen.getAtlas()[0].findRegion("kunai"), 0, 0, 160, 32);
        setBounds(0, 0, 80 / MainGame.PPM, 16 / MainGame.PPM);
        setRegion(kunai);
        isDestroyed = false;
        if(screen.getPlayer().isFlipX()) {
            body.applyLinearImpulse(new Vector2(-25, 2), new Vector2(0, 0), true);
            setFlip(true,false);
        }else{
            body.applyLinearImpulse(new Vector2(25, 2), new Vector2(0, 0), true);
            setFlip(false,false);
        }
    }

    public void update(float dt){
        setPosition(body.getPosition().x - getWidth() /2, body.getPosition().y - getHeight() /2);

    }

    public void defineBullet() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(2/MainGame.PPM, 2/MainGame.PPM);
        fixtureDef.filter.categoryBits = MainGame.BULLET_BIT;
        fixtureDef.filter.maskBits = MainGame.GROUND_BIT |
               // MainGame.PLAYER_BIT |
                MainGame.BRICK_BIT |
                MainGame.BULLET_BIT |
                MainGame.ENEMY_BIT |
                MainGame.OBJECT_BIT;
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(this);
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public void setDestroyed(boolean destroyed) {
        isDestroyed = destroyed;
    }
}
