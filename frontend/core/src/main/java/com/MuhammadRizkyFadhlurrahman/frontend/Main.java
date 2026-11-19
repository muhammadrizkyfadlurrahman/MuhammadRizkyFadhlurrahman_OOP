
package com.MuhammadRizkyFadhlurrahman.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import com.MuhammadRizkyFadhlurrahman.frontend.commands.Command;
import com.MuhammadRizkyFadhlurrahman.frontend.commands.InputHandler;

import com.MuhammadRizkyFadhlurrahman.frontend.objects.GameObject;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.Player;

import com.MuhammadRizkyFadhlurrahman.frontend.objects.enemies.Boss;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.enemies.Fairy;

import com.MuhammadRizkyFadhlurrahman.frontend.objects.items.Item;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.items.ItemType;

import com.MuhammadRizkyFadhlurrahman.frontend.objects.patterns.entityStrategy.TargetPointMovement;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.patterns.entityStrategy.SCurveMovement;

import com.MuhammadRizkyFadhlurrahman.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.patterns.shootingStrategy.AlternatingRingShot;

import com.MuhammadRizkyFadhlurrahman.frontend.systems.AssetManager;
import com.MuhammadRizkyFadhlurrahman.frontend.systems.EntityFactory;
import com.MuhammadRizkyFadhlurrahman.frontend.systems.BulletManager;
import com.MuhammadRizkyFadhlurrahman.frontend.systems.CollisionReferee;

import com.MuhammadRizkyFadhlurrahman.frontend.ui.GameHUD;

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
    private CollisionReferee collisionReferee;

    private InputHandler inputHandler;
    private GameHUD gameHUD;

    private float waveTimer = 0f;
    private int wave1SpawnIndex = 0;

    private float enemyShootTimer = 0f;
    private float phaseTimer = 0f;

    private int currentPhase = 0;

    @Override
    public void create() {
        batch = new SpriteBatch();

        entities = new ArrayList<>();
        fairy = new ArrayList<>();

        AssetManager.getInstance().init();

        bulletManager = new BulletManager();
        collisionReferee = new CollisionReferee();
        inputHandler = new InputHandler();

        player = EntityFactory.createPlayer(
            200, 50, "Reimu Hakurei", 100, 15, 3
        );

        gameHUD = new GameHUD();
        player.registerObserver(gameHUD);

        inputHandler.setEntities(entities);
        bulletManager.setEntities(entities);

        boss = EntityFactory.createBoss(
            200, 420, "Rumia", 150
        );

        // Initial Strategy Injection for Boss Rumia
        boss.setMovementPattern(
            new TargetPointMovement(200f, 420f, 150f)
        );

        boss.setShootingPattern(
            new SpreadShot(
                220f, 5, 25f,
                10, player, "bullet_danmaku_red"
            )
        );

        powerItem = EntityFactory.createItem(
            180, 450, ItemType.POWER
        );

        pointItem = EntityFactory.createItem(
            220, 480, ItemType.POINT
        );

        entities.add(player);
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
        resetFairyWave();

        boss = EntityFactory.createBoss(
            200, 420, "Rumia", 150
        );

        // Reinject initial Boss strategies
        boss.setMovementPattern(
            new TargetPointMovement(200f, 420f, 150f)
        );

        boss.setShootingPattern(
            new SpreadShot(
                220f, 5, 25f,
                10, player, "bullet_danmaku_red"
            )
        );

        powerItem = EntityFactory.createItem(
            180, 450, ItemType.POWER
        );

        pointItem = EntityFactory.createItem(
            220, 480, ItemType.POINT
        );

        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);

        bulletManager.setEntities(entities);
        inputHandler.setEntities(entities);

        currentPhase = 0;
        phaseTimer = 0f;
        enemyShootTimer = 0f;
    }

    // Stage 1: Wave 1 Fairy Stream
    private void updateFairyWave(float delta) {
        float[] spawnX = {
            60f, 68f, 76f, 84f, 92f
        };

        waveTimer += delta;

        if (wave1SpawnIndex < spawnX.length
            && waveTimer >= 1.0f + wave1SpawnIndex * 0.267f) {

            float x = spawnX[wave1SpawnIndex++];

            Fairy f = EntityFactory.createFairy(
                x, 480f, "Left S-Curve Fairy",
                8, "fairy_idle_blue"
            );

            f.setMovementPattern(
                new SCurveMovement(
                    130f,
                    (float) Math.toRadians(270),
                    false
                )
            );

            f.setShootingPattern(null);

            fairy.add(f);
            entities.add(f);
        }
    }

    private void resetFairyWave() {
        waveTimer = 0f;
        wave1SpawnIndex = 0;
    }

    private void updateEnemyScheduler(float delta) {

        // 1. Update Fairy Wave
        updateFairyWave(delta);

        fairy.removeIf(
            f -> f.isDestroyed() || f.isOffScreen(416, 560)
        );

        // 2. Periodic Boss Rumia Shooting
        enemyShootTimer += delta;

        if (enemyShootTimer >= 1.5f
            && boss != null
            && !boss.isDestroyed()) {

            boss.shootBullet(bulletManager);

            enemyShootTimer = 0f;
        }

        // 3. Dynamic Strategy Swapping
        phaseTimer += delta;

        if (phaseTimer >= 4.0f
            && boss != null
            && !boss.isDestroyed()) {

            phaseTimer = 0f;

            currentPhase = (currentPhase + 1) % 2;

            if (currentPhase == 0) {

                // Phase 1: Upper Left + Aimed SpreadShot
                boss.setMovementPattern(
                    new TargetPointMovement(
                        120f, 420f, 150f
                    )
                );

                boss.setShootingPattern(
                    new SpreadShot(
                        220f, 5, 25f,
                        10, player, "bullet_danmaku_red"
                    )
                );

                System.out.println(
                    "[Boss Strategy] Rumia shifted to Upper Left with Aimed SpreadShot!"
                );

            } else {

                // Phase 2: Upper Right + Alternating RingShot
                boss.setMovementPattern(
                    new TargetPointMovement(
                        280f, 420f, 150f
                    )
                );

                boss.setShootingPattern(
                    new AlternatingRingShot(
                        180f, 16, 10
                    )
                );

                System.out.println(
                    "[Boss Strategy] Rumia shifted to Upper Right with AlternatingRingShot!"
                );
            }
        }
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input != null
            && Gdx.input.isKeyJustPressed(Input.Keys.R)
            && player.isGameOver()) {

            restartGame();
        }

        List<Command> commands =
            inputHandler.handleInput(delta);

        for (Command command : commands) {
            command.execute(player, bulletManager);
        }

        updateEnemyScheduler(delta);

        updateAndClean(
            entities, delta, 416, 560
        );

        bulletManager.update(
            delta, 416, 560
        );

        collisionReferee.resolveCollisions(
            player, entities, bulletManager
        );

        ScreenUtils.clear(
            0.1f, 0.1f, 0.15f, 1f
        );

        batch.begin();

        for (GameObject entity : entities) {
            if (!entity.isDestroyed()) {
                entity.render(batch);
            }
        }

        bulletManager.render(batch);
        gameHUD.render(batch);

        batch.end();

        gameHUD.renderBackground();

        player.renderFocusIndicator(
            gameHUD.getShapeRenderer()
        );
    }

    @Override
    public void dispose() {
        if (batch != null) {
            batch.dispose();
        }

        if (gameHUD != null) {
            gameHUD.dispose();
        }

        AssetManager.getInstance().dispose();
    }
}
