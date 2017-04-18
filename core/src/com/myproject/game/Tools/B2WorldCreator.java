package com.myproject.game.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.myproject.game.MainGame;
import com.myproject.game.Screens.PlayScreen;
import com.myproject.game.Sprites.Brick;
import com.myproject.game.Sprites.Coin;
import com.myproject.game.Sprites.Water;

/**
 * Created by Brutal on 26/01/2017.
 */

public class B2WorldCreator {

    public B2WorldCreator(PlayScreen screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        // suelo
        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth()/2) / MainGame.PPM, (rect.getY() + rect.getHeight()/2) / MainGame.PPM) ;

            body = world.createBody(bodyDef);

            shape.setAsBox((rect.getWidth()/2) / MainGame.PPM, (rect.getHeight()/2) / MainGame.PPM);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
        }


        // tuberias
      /*  for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth()/2) / MainGame.PPM, (rect.getY() + rect.getHeight()/2) / MainGame.PPM) ;

            body = world.createBody(bodyDef);

            shape.setAsBox((rect.getWidth()/2) / MainGame.PPM, (rect.getHeight()/2) / MainGame.PPM);
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = MainGame.OBJECT_BIT;
            body.createFixture(fixtureDef);
        }

        // monedas
        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Coin(screen, rect);
        }

        // ladrillos
        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Brick(screen, rect);
        } */

    }
}
