package com.myproject.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.myproject.game.MainGame;
import com.myproject.game.Scenes.Hud;
import com.myproject.game.Screens.PlayScreen;

/**
 * Created by Brutal on 26/01/2017.
 */

public class Coin extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 28;
    private boolean isActive;
    public Coin(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        tileSet = map.getTileSets().getTileSet("tileset_gutter");
        isActive = true;
        fixture.setUserData(this);
        setCategoryFilter(MainGame.COIN_BIT);
    }

    @Override
    public void onHeadHit() {
        if(isActive) {
            Gdx.app.log("Coin", "Collision");
            Hud.addCoin();
            getCell().setTile(tileSet.getTile(BLANK_COIN));
        }
        isActive = false;
    }
}
