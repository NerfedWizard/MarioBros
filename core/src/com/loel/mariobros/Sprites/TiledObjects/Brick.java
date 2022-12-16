package com.loel.mariobros.Sprites.TiledObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;
import com.loel.mariobros.MarioBros;
import com.loel.mariobros.Scenes.Hud;
import com.loel.mariobros.Screens.PlayScreen;
import com.loel.mariobros.Sprites.Mario;

public class Brick extends InteractiveTileObject {
  private AssetManager manager;

  public Brick(PlayScreen screen, MapObject object) {
    super(screen, object);
    fixture.setUserData(this);
    manager = screen.getMarioBros().getManager();
    setCategoryFilter(MarioBros.BRICK_BIT);
  }

  @Override
  public void onHeadHit(Mario mario) {
    setCategoryFilter(MarioBros.DESTROYED_BIT);
    getCell().setTile(null);
    Hud.addScore(200);
    manager.get("audio/sounds/breakblock.wav", Sound.class).play();
  }
}