package com.loel.mariobros.Sprites.TiledObjects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.loel.mariobros.MarioBros;
import com.loel.mariobros.Scenes.Hud;
import com.loel.mariobros.Screens.PlayScreen;
import com.loel.mariobros.Sprites.Mario;

public class Brick extends InteractiveTileObject {
    private AssetManager manager;

    public Brick(PlayScreen screen, Rectangle bounds){
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(MarioBros.BRICK_BIT);
        manager = screen.getMarioBros().getManager();
    }

    @Override
    public void onHeadHit() {
        setCategoryFilter(MarioBros.DESTROYED_BIT);
        getCell().setTile(null);
        Hud.addScore(200);
        manager.get("audio/sounds/breakblock.wav", Sound.class).play();

    }
}