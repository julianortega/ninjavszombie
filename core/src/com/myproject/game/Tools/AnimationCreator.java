package com.myproject.game.Tools;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.myproject.game.Screens.PlayScreen;

/**
 * Created by leon on 4/26/17.
 */

public class AnimationCreator {
    private final PlayScreen screen;
    private final Array<TextureRegion> frames = new Array<TextureRegion>();

    public AnimationCreator(PlayScreen screen) {
        this.screen = screen;
    }


    public Array<TextureRegion> addFrames(String assetName, int frameCount, int xCoord, int yCoord, int width, int height) {
        for(int i = 0; i < frameCount; i++) {
            TextureAtlas.AtlasRegion atlasRegion = screen.getAtlas()[0].findRegion(assetName);
            TextureRegion textureRegion = new TextureRegion(atlasRegion, i * xCoord, yCoord, width, height);
            frames.add(textureRegion);
        }
        return frames;
    }

    public Animation<TextureRegion> createAnimation(float speed) {
        Animation<TextureRegion> animation = new Animation(speed, frames);
        frames.clear();
        return animation;
    }

    public Animation<TextureRegion> createPlayerRunAnimation() {
        addFrames("ninja_run", 5, 363, 0, 364, 458);
        addFrames("ninja_run", 5, 363, 458, 363, 458);
        return createAnimation(0.1f);
    }

    public Animation<TextureRegion> createPlayerJumpAnimation() {
        addFrames("ninja_jump", 5, 362, 0, 362, 483);
        addFrames("ninja_jump", 5, 362, 483, 362, 483);
        return createAnimation(0.1f);
    }


    public Animation<TextureRegion> createPlayerAttackAnimation() {
        addFrames("ninja_attack", 5, 380, 0, 380, 450);
        addFrames("ninja_attack", 5, 380, 451, 380, 450);
        return createAnimation(0.05f);
    }

    public TextureRegion createPlayerStandTexture() {
        return new TextureRegion(screen.getAtlas()[0].findRegion("ninja_idle"), 0, 50, 360, 470);
    }
}
