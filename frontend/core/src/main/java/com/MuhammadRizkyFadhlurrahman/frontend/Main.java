package com.MuhammadRizkyFadhlurrahman.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import java.util.Iterator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.GameObject;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.Player;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.enemies.Boss;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.enemies.Fairy;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.items.Item;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.items.ItemType;
import com.MuhammadRizkyFadhlurrahman.frontend.systems.AssetManager;
import com.MuhammadRizkyFadhlurrahman.frontend.systems.EntityFactory;

import java.util.ArrayList;
import java.util.List;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;

    private Player player;
    private List<Fairy> fairy;
    private Boss boss;
    private Item powerItem;
    private Item pointItem;
    private List<GameObject> entities;

    @Override
    public void create() {
        batch = new SpriteBatch();
        entities = new ArrayList<>();
        fairy = new ArrayList<>();
        AssetManager.getInstance().init();


        player = EntityFactory.createPlayer(280, 40, "Reimu Hakurei", 100, 15, 3);

        Fairy redFairy= EntityFactory.createPlayer(150, 380, "Red Fairy", 20);
        Fairy bluefairy= EntityFactory.createFairy( 250,380, "Blue Fairy", 20, "fairy_idle_blue");
        fairy.add(redfairy);
        fairy.add(blueFairy);

        boss = EntityFactory.createBoss(380, 400, "Rumia", 150);

        powerItem = EntityFactory.createItem(200, 450, 16, 16, 80f, ItemType.POWER);
        pointItem = EntityFactory.createItem(320, 480, 12, 12, 120f, ItemType.POINT);

        entities.add(player);
        entities.addAll(fairy);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);
    }

    public <T extends GameObject> void updateAndClean(
        List<T> list, float delta, float screenWidth, float screenHeight) {

        Iterator<T> iterator = list.iterator();

        while (iterator.hasNext()) {
            T entity = iterator.next();

            entity.update(delta);

            if (entity.isOffScreen(screenWidth, screenHeight)) {
                iterator.remove();
            }
        }
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        for (GameObject entity : entities) {
            entity.update(delta);
        }

        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                GameObject a = entities.get(i);
                GameObject b = entities.get(j);

                if (a.getCoreHitbox().overlaps(b.getCoreHitbox())) {
                    a.onCollision(b);
                    b.onCollision(a);
                }
            }
        }

        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        batch.begin(ShapeRenderer.ShapeType.Filled);

        for (GameObject entity : entities) {
            if(!entity.isDestroyed()) {
                entity.render(batch);
            }
        }

        batch.end();
    }

    @Override
    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
    }
}
