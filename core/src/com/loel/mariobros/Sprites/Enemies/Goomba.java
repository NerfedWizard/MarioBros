package com.loel.mariobros.Sprites.Enemies;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.loel.mariobros.MarioBros;
import com.loel.mariobros.Screens.PlayScreen;
import com.loel.mariobros.Sprites.Enemies.Enemy;
import com.loel.mariobros.Sprites.Mario;

public class Goomba extends Enemy {
  private float stateTime;
  private Animation walkAnimation;
  //    private Animation smashed;
  private Array<TextureRegion> frames;
  private Array<TextureRegion> smash;
  private boolean destroyed;
  private boolean setToDestroy;
  private AssetManager manager;

  public Goomba(PlayScreen screen, float x, float y) {
    super(screen, x, y);
    frames = new Array<>();

    for (int i = 0; i < 2; i++) {
      frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i * 16, 0, 16, 16));
    }
    walkAnimation = new Animation(0.4f, frames);
//        smashed = new Animation(0.4f,smash);
    stateTime = 0;
    setBounds(getX(), getY(), 16 / MarioBros.PPM, 16 / MarioBros.PPM);
    destroyed = false;
    setToDestroy = false;
    manager = screen.getMarioBros().getManager();
  }

  @Override
  public void update(float dt) {
    stateTime += dt;
    if (setToDestroy && !destroyed) {
      world.destroyBody(b2body);
      destroyed = true;
      setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 32, 0, 16, 16));
//            setRegion((TextureRegion) smashed.getKeyFrame(stateTime));
      stateTime = 0;
    } else if (!destroyed) {
      b2body.setLinearVelocity(velocity);
      setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
      setRegion((TextureRegion) walkAnimation.getKeyFrame(stateTime, true));
    }
  }

  @Override
  public void hitOnHead() {
    setToDestroy = true;
    manager.get("audio/sounds/stomp.wav", Sound.class).play();
  }

  @Override
  public void hitByEnemy(Enemy enemy) {
    if (enemy != null && ((Turtle) enemy).currentState == Turtle.State.MOVING_SHELL)
      setToDestroy = true;
    else
      reverseVelocity(true, false);
  }

  @Override
  protected void defineEnemy() {
    BodyDef bdef = new BodyDef();
    bdef.position.set(getX(), getY());
    bdef.type = BodyDef.BodyType.DynamicBody;
    b2body = world.createBody(bdef);

    FixtureDef fdef = new FixtureDef();
    CircleShape shape = new CircleShape();
    shape.setRadius(6 / MarioBros.PPM);
    fdef.filter.categoryBits = MarioBros.ENEMY_BIT;
    fdef.filter.maskBits = MarioBros.GROUND_BIT |
        MarioBros.COIN_BIT |
        MarioBros.BRICK_BIT |
        MarioBros.ENEMY_BIT |
        MarioBros.OBJECT_BIT |
        MarioBros.MARIO_BIT;

    fdef.shape = shape;
    b2body.createFixture(fdef).setUserData(this);

    //Create the Head here:
    PolygonShape head = new PolygonShape();
    Vector2[] vertice = new Vector2[4];
    vertice[0] = new Vector2(-5, 8).scl(1 / MarioBros.PPM);
    vertice[1] = new Vector2(5, 8).scl(1 / MarioBros.PPM);
    vertice[2] = new Vector2(-3, 3).scl(1 / MarioBros.PPM);
    vertice[3] = new Vector2(3, 3).scl(1 / MarioBros.PPM);
    head.set(vertice);

    fdef.shape = head;
    fdef.restitution = 0.5f;
    fdef.filter.categoryBits = MarioBros.ENEMY_HEAD_BIT;
    b2body.createFixture(fdef).setUserData(this);
  }

  public void draw(Batch batch) {
    if (!destroyed || stateTime < 1)
      super.draw(batch);
  }

//  @Override
//  public void hitOnHead() {
//    setToDestroy = true;
//  }
}
