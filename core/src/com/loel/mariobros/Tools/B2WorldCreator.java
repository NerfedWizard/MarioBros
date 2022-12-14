package com.loel.mariobros.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.loel.mariobros.MarioBros;
import com.loel.mariobros.Screens.PlayScreen;
import com.loel.mariobros.Sprites.Enemies.Enemy;
import com.loel.mariobros.Sprites.Enemies.Goomba;
//import com.loel.mariobros.Sprites.Enemies.Turtle;
import com.loel.mariobros.Sprites.TiledObjects.Brick;
import com.loel.mariobros.Sprites.TiledObjects.Coin;

public class B2WorldCreator {
  public B2WorldCreator(PlayScreen screen) {
    World world = screen.getWorld();
    TiledMap map = screen.getMap();
    //create body and fixture variables
    BodyDef bdef = new BodyDef();
    PolygonShape shape = new PolygonShape();
    FixtureDef fdef = new FixtureDef();
    Body body;

    //create ground bodies/fixtures
    for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
      Rectangle rect = ((RectangleMapObject) object).getRectangle();

      bdef.type = BodyDef.BodyType.StaticBody;
      bdef.position.set((rect.getX() + rect.getWidth() / 2) / MarioBros.PPM, (rect.getY() + rect.getHeight() / 2) / MarioBros.PPM);

      body = world.createBody(bdef);

      shape.setAsBox(rect.getWidth() / 2 / MarioBros.PPM, rect.getHeight() / 2 / MarioBros.PPM);
      fdef.shape = shape;
      body.createFixture(fdef);
    }

    //create pipe bodies/fixtures
    for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
      Rectangle rect = ((RectangleMapObject) object).getRectangle();

      bdef.type = BodyDef.BodyType.StaticBody;
      bdef.position.set((rect.getX() + rect.getWidth() / 2) / MarioBros.PPM, (rect.getY() + rect.getHeight() / 2) / MarioBros.PPM);

      body = world.createBody(bdef);

      shape.setAsBox(rect.getWidth() / 2 / MarioBros.PPM, rect.getHeight() / 2 / MarioBros.PPM);
      fdef.shape = shape;
      fdef.filter.categoryBits = MarioBros.OBJECT_BIT;
      body.createFixture(fdef);
    }

    //create brick bodies/fixtures
    for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
      new Brick(screen, object);
    }

    //create coin bodies/fixtures
    for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {

      new Coin(screen, object);
    }

  }
}