package com.myproject.game.Tools;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.myproject.game.MainGame;
import com.myproject.game.Sprites.Enemy;
import com.myproject.game.Sprites.Player;
import com.myproject.game.Sprites.Bullet;
import com.myproject.game.Sprites.Zombie;

/**
 * Created by usuario on 31/01/2017.
 */
public class WorldContactListener implements ContactListener {
    private Array<Body> bodiesToRemove = new Array<Body>();

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
        switch (cDef){
            case MainGame.PLAYER_BIT | MainGame.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == MainGame.PLAYER_BIT)
                    ((Player) fixA.getUserData()).hit();
                else
                    ((Player) fixB.getUserData()).hit();
                break;
            case MainGame.BULLET_BIT | MainGame.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == MainGame.BULLET_BIT) {
                    bodiesToRemove.add(((Bullet) fixA.getUserData()).body);
                    ((Bullet) fixA.getUserData()).setDestroyed(true);
                    bodiesToRemove.add(((Enemy) fixB.getUserData()).body);
                    ((Zombie) fixB.getUserData()).setDestroyed(true);
                }
                else {
                    bodiesToRemove.add(((Bullet) fixB.getUserData()).body);
                    ((Bullet) fixB.getUserData()).setDestroyed(true);
                    bodiesToRemove.add(((Enemy) fixA.getUserData()).body);
                    ((Zombie) fixA.getUserData()).setDestroyed(true);
                }
                break;
            case MainGame.BULLET_BIT | MainGame.GROUND_BIT:
                if(fixA.getFilterData().categoryBits == MainGame.BULLET_BIT) {
                    bodiesToRemove.add(((Bullet) fixA.getUserData()).body);
                    ((Bullet) fixA.getUserData()).setDestroyed(true);
                }
                else{
                    bodiesToRemove.add(((Bullet) fixB.getUserData()).body);
                    ((Bullet) fixB.getUserData()).setDestroyed(true);

                }
                break;

        }

    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public Array<Body> getBodiesToRemove() {
        return bodiesToRemove;
    }
}
