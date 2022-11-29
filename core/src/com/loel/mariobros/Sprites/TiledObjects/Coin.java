package com.loel.mariobros.Sprites.TiledObjects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.loel.mariobros.MarioBros;
import com.loel.mariobros.Scenes.Hud;
import com.loel.mariobros.Screens.PlayScreen;
import com.loel.mariobros.Sprites.Items.ItemDef;
import com.loel.mariobros.Sprites.Items.Mushroom;
import com.loel.mariobros.Sprites.Mario;

public class Coin extends InteractiveTileObject {
  private static TiledMapTileSet tileSet;
  private final int BLANK_COIN = 28;
  private AssetManager manager;
  private PlayScreen screen;

  public Coin(PlayScreen screen, MapObject object) {
    super(screen, object);
    tileSet = map.getTileSets().getTileSet("tileset_gutter");
    fixture.setUserData(this);
    setCategoryFilter(MarioBros.COIN_BIT);
    manager = screen.getMarioBros().getManager();
    this.screen = screen;
  }

  @Override
  public void onHeadHit() {
    if (getCell().getTile().getId() == BLANK_COIN)
      manager.get("audio/sounds/bump.wav", Sound.class).play();
    else {
      if (object.getProperties().containsKey("mushroom")) {
        screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 / MarioBros.PPM),
            Mushroom.class));
        manager.get("audio/sounds/powerup_spawn.wav", Sound.class).play();
      } else
        manager.get("audio/sounds/coin.wav", Sound.class).play();
      getCell().setTile(tileSet.getTile(BLANK_COIN));
      Hud.addScore(100);
    }
  }
}
