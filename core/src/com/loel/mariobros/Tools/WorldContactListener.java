package com.loel.mariobros.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.loel.mariobros.MarioBros;
import com.loel.mariobros.Sprites.Enemies.Enemy;
import com.loel.mariobros.Sprites.Items.Item;
import com.loel.mariobros.Sprites.Mario;
import com.loel.mariobros.Sprites.Other.FireBall;
import com.loel.mariobros.Sprites.TiledObjects.InteractiveTileObject;


public class WorldContactListener implements ContactListener {
  int cDef;


  /**
   * We are finding out what the object we have and what is marios head
   */
  @Override
  public void beginContact(Contact contact) {
    Fixture fixA = contact.getFixtureA();
    Fixture fixB = contact.getFixtureB();
    Gdx.app.log("Collision", MarioBros.bitToText(fixA.getFilterData().categoryBits) + " collides with " + MarioBros.bitToText(fixB.getFilterData().categoryBits));
    cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
//
    if (fixA.getUserData() == "head" || fixB.getUserData() == "head") {
      Fixture head = fixA.getUserData() == "head" ? fixA : fixB;
      Fixture object = head == fixA ? fixB : fixA;

      if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())) {
        ((InteractiveTileObject) object.getUserData()).onHeadHit();
      }
    }


//      if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())) {
//        ((InteractiveTileObject) object.getUserData()).onHeadHit((Mario) object.getUserData());
//      }
    switch (cDef) {
//      case MarioBros.MARIO_HEAD_BIT | MarioBros.BRICK_BIT:
//      case MarioBros.MARIO_HEAD_BIT | MarioBros.COIN_BIT:
//        if (fixA.getFilterData().categoryBits == MarioBros.MARIO_HEAD_BIT)
//          ((InteractiveTileObject) fixB.getUserData()).onHeadHit();
//        else
//          ((InteractiveTileObject) fixA.getUserData()).onHeadHit();
//        break;
      case MarioBros.ENEMY_HEAD_BIT | MarioBros.MARIO_BIT:
        if (fixA.getFilterData().categoryBits == MarioBros.ENEMY_HEAD_BIT)
          ((Enemy) fixA.getUserData()).hitOnHead();
        else
          ((Enemy) fixB.getUserData()).hitOnHead();
        break;
      case MarioBros.ENEMY_BIT | MarioBros.OBJECT_BIT:
        if (fixA.getFilterData().categoryBits == MarioBros.ENEMY_BIT)
          ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
        else
          ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
        break;
      case MarioBros.MARIO_BIT | MarioBros.ENEMY_BIT:
        if (fixA.getFilterData().categoryBits == MarioBros.MARIO_BIT)
          ((Mario) fixA.getUserData()).hit((Enemy) fixB.getUserData());
        else
          ((Mario) fixB.getUserData()).hit((Enemy) fixA.getUserData());
        break;
      case MarioBros.ENEMY_BIT /**| MarioBros.ENEMY_BIT*/:
        ((Enemy) fixA.getUserData()).hitByEnemy((Enemy) fixB.getUserData());
        ((Enemy) fixB.getUserData()).hitByEnemy((Enemy) fixA.getUserData());
        break;
      case MarioBros.ITEM_BIT | MarioBros.OBJECT_BIT:
        if (fixA.getFilterData().categoryBits == MarioBros.ITEM_BIT)
          ((Item) fixA.getUserData()).reverseVelocity(true, false);
        else
          ((Item) fixB.getUserData()).reverseVelocity(true, false);
        break;
      case MarioBros.ITEM_BIT | MarioBros.MARIO_BIT:
        if (fixA.getFilterData().categoryBits == MarioBros.ITEM_BIT)
          ((Item) fixA.getUserData()).use((Mario) fixB.getUserData());
        else
          ((Item) fixB.getUserData()).use((Mario) fixA.getUserData());
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
}
