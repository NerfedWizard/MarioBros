package com.loel.mariobros.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.loel.mariobros.MarioBros;
import com.loel.mariobros.Scenes.Hud;
import com.loel.mariobros.Sprites.Enemies.Enemy;
import com.loel.mariobros.Sprites.Enemies.Goomba;
import com.loel.mariobros.Sprites.Items.Item;
import com.loel.mariobros.Sprites.Items.ItemDef;
import com.loel.mariobros.Sprites.Mario;
import com.loel.mariobros.Tools.B2WorldCreator;
import com.loel.mariobros.Tools.WorldContactListener;

import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Main class that handles the game and screen for mario
 */
public class PlayScreen implements Screen {
  //Reference to our Game, used to set Screens
  private MarioBros game;
  private TextureAtlas atlas;
  public static boolean alreadyDestroyed = false;
  private AssetManager manager;

  //basic playscreen variables
  private OrthographicCamera gamecam;
  private Viewport gamePort;
  private Hud hud;

  //Tiled map variables
  private TmxMapLoader maploader;
  private TiledMap map;
  private OrthogonalTiledMapRenderer renderer;

  //Box2d variables
  private World world;
  private Box2DDebugRenderer b2dr;

  //sprites
  private Mario player;
  private Goomba goomba;

  private Music music;
  private LinkedBlockingQueue<ItemDef> itemsToSpawn;


  public PlayScreen(MarioBros game) {
    manager = game.getManager();
    manager.get("audio/music/mario_music.ogg", Music.class).play();
    atlas = new TextureAtlas("Mario_and_Enemies.pack");

    this.game = game;
    //create cam used to follow mario through cam world
    gamecam = new OrthographicCamera();

    //create a FitViewport to maintain virtual aspect ratio despite screen size
    gamePort = new FitViewport(MarioBros.V_WIDTH / MarioBros.PPM, MarioBros.V_HEIGHT / MarioBros.PPM, gamecam);

    //create our game HUD for scores/timers/level info
    hud = new Hud(game.batch);

    //Load our map and setup our map renderer
    maploader = new TmxMapLoader();
    map = maploader.load("level1.tmx");
    renderer = new OrthogonalTiledMapRenderer(map, 1 / MarioBros.PPM);

    //initially set our gamcam to be centered correctly at the start of of map
    gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

    //create our Box2D world, setting no gravity in X, -10 gravity in Y, and allow bodies to sleep
    world = new World(new Vector2(0, -10), true);
    //allows for debug lines of our box2d world.
    b2dr = new Box2DDebugRenderer();

    new B2WorldCreator(this);

    //create mario in our game world
    player = new Mario(this);

    world.setContactListener(new WorldContactListener());

    music = manager.get("audio/music/mario_music.ogg", Music.class);
    music.setLooping(true);
    music.play();

    goomba = new Goomba(this, .32f, .32f);

  }

  public TextureAtlas getAtlas() {
    return atlas;
  }

  @Override
  public void show() {


  }

  public MarioBros getMarioBros() {
    return game;
  }

  public void handleInput(float dt) {
    //control our player using immediate impulses
    if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
      player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)
      player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
    if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
      player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);

  }

  public void update(float dt) {
    //handle user input first
    handleInput(dt);

    //takes 1 step in the physics simulation(60 times per second)
    world.step(1 / 60f, 6, 2);

    player.update(dt);
    goomba.update(dt);
    hud.update(dt);

    //attach our gamecam to our players.x coordinate
    gamecam.position.x = player.b2body.getPosition().x;

    //update our gamecam with correct coordinates after changes
    gamecam.update();
    //tell our renderer to draw only what our camera can see in our game world.
    renderer.setView(gamecam);

  }

  @Override
  public void render(float delta) {
    //separate our update logic from render
    update(delta);

    //Clear the game screen with Black
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    //render our game map
    renderer.render();

    //renderer our Box2DDebugLines
    b2dr.render(world, gamecam.combined);

    game.batch.setProjectionMatrix(gamecam.combined);
    game.batch.begin();
    player.draw(game.batch);
    goomba.draw(game.batch);
    game.batch.end();

    //Set our batch to now draw what the Hud camera sees.
    game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
    hud.stage.draw();

  }

  @Override
  public void resize(int width, int height) {
    //updated our game viewport
    gamePort.update(width, height);

  }

//  public void spawnItem(ItemDef idef) {
//    itemsToSpawn.add(idef);
//  }

  public TiledMap getMap() {
    return map;
  }

  public World getWorld() {
    return world;
  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void hide() {

  }

  @Override
  public void dispose() {
    //dispose of all our opened resources
    map.dispose();
    renderer.dispose();
    world.dispose();
    b2dr.dispose();
    hud.dispose();
  }

}
