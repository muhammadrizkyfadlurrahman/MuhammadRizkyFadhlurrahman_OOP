package com.MuhammadRizkyFadhlurrahman.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import com.MuhammadRizkyFadhlurrahman.frontend.objects.GameObject;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.Player;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.enemies.Boss;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.enemies.Fairy;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.items.Item;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.items.ItemType;
import com.MuhammadRizkyFadhlurrahman.frontend.systems.AssetManager;
import com.MuhammadRizkyFadhlurrahman.frontend.systems.EntityFactory;
import com.MuhammadRizkyFadhlurrahman.frontend.systems.BulletManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main extends ApplicationAdapter {

    private SpriteBatch batch;

    private Player player;
    private List<Fairy> fairy;
    private Boss boss;
    private Item powerItem;
    private Item pointItem;
    private List<GameObject> entities;

    private BulletManager bulletManager;

    @Override
    public void create() {
        batch = new SpriteBatch();
        entities = new ArrayList<>();
        fairy = new ArrayList<>();

        AssetManager.getInstance().init();

        bulletManager = new BulletManager();

        player = EntityFactory.createPlayer(
            200, 50, "Reimu Hakurei", 100, 15, 3
        );

        fairy.add(EntityFactory.createFairy(
            150, 380, "Red Fairy", 20
        ));

        fairy.add(EntityFactory.createFairy(
            250, 380, "Blue Fairy", 20, "fairy_idle_blue"
        ));

        boss = EntityFactory.createBoss(
            250, 400, "Rumia", 150
        );

        powerItem = EntityFactory.createItem(
            180, 450, ItemType.POWER
        );

        pointItem = EntityFactory.createItem(
            220, 480, ItemType.POINT
        );

        entities.add(player);
        entities.addAll(fairy);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);
    }

    public <T extends GameObject> void updateAndClean(
        List<T> list,
        float delta,
        float screenWidth,
        float screenHeight
    ) {
        Iterator<T> iterator = list.iterator();

        while (iterator.hasNext()) {
            T entity = iterator.next();

            entity.update(delta);

            if (entity.isOffScreen(screenWidth, screenHeight)
                || entity.isDestroyed()) {
                iterator.remove();
            }
        }
    }

    private void restartGame() {
        System.out.println("[Main] Restarting game...");

        entities.clear();

        bulletManager.clearEnemyBullets();
        bulletManager.clearPlayerBullets();

        entities.add(player);
        player.resetGame();

        fairy.clear();

        fairy.add(EntityFactory.createFairy(
            150, 380, "Red Fairy", 20
        ));

        fairy.add(EntityFactory.createFairy(
            250, 380, "Blue Fairy", 20, "fairy_idle_blue"
        ));

        boss = EntityFactory.createBoss(
            250, 400, "Rumia", 150
        );

        powerItem = EntityFactory.createItem(
            180, 450, ItemType.POWER
        );

        pointItem = EntityFactory.createItem(
            220, 480, ItemType.POINT
        );

        entities.addAll(fairy);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        for (GameObject entity : entities) {
            entity.update(delta);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            player.shootBullet(bulletManager);
        }

        bulletManager.update(
            delta,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );

        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        batch.begin();

        for (GameObject entity : entities) {
            if (!entity.isDestroyed()) {
                entity.render(batch);
            }
        }

        bulletManager.render(batch);

        batch.end();
    }

    @Override
    public void dispose() {
        if (batch != null) {
            batch.dispose();
        }

        AssetManager.getInstance().dispose();
    }
}
