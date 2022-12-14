package com.loel.mariobros.Sprites.Items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.loel.mariobros.MarioBros;
import com.loel.mariobros.Screens.PlayScreen;
import com.loel.mariobros.Sprites.Mario;

public class Mushroom extends Item {
    public Mushroom(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        setRegion(screen.getAtlas().findRegion("mushroom"), 0, 0, 16, 16);
        velocity = new Vector2(1, -2);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        body.setLinearVelocity(velocity);
    }

    @Override
    public void use(Mario mario) {
        destroy();

    }

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);
//        fdef.filter.categoryBits = MarioBros.ENEMY_BIT; //ITEM BIT
//        fdef.filter.maskBits = MarioBros.GROUND_BIT |
//                MarioBros.COIN_BIT |
//                MarioBros.BRICK_BIT |
//                MarioBros.ENEMY_BIT |
//                MarioBros.OBJECT_BIT |
//                MarioBros.MARIO_BIT;

        fdef.shape = shape;
        fdef.density = 1;
        body.createFixture(fdef).setUserData(this);
    }
}
