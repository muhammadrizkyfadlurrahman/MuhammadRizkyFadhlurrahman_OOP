
package com.MuhammadRizkyFadhlurrahman.frontend.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.MuhammadRizkyFadhlurrahman.frontend.objects.BulletType;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.GameObject;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.bullets.Bullet;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.items.Item;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.items.ItemType;

import com.MuhammadRizkyFadhlurrahman.frontend.objects.patterns.bulletStrategy.HomingBulletMovement;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.patterns.bulletStrategy.FantasySealMovement;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.patterns.shootingStrategy.RingShot;

import java.util.Queue;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class BulletManager {

    private Queue<Bullet> playerBulletPool;
    private Queue<Bullet> enemyBulletPool;

    private List<Bullet> activePlayerBullets;
    private List<Bullet> activeEnemyBullets;

    private List<GameObject> entities;

    public BulletManager() {
        playerBulletPool = new LinkedList<>();
        enemyBulletPool = new LinkedList<>();

        activePlayerBullets = new ArrayList<>();
        activeEnemyBullets = new ArrayList<>();
    }

    public Bullet spawnPlayerBullet(
        float x, float y, float vx, float vy,
        int damage, String spriteKey
    ) {
        String key = (spriteKey != null) ? spriteKey : "bullet_amulet";
        Bullet bullet;

        if (!playerBulletPool.isEmpty()) {
            bullet = playerBulletPool.poll();
            bullet.init(x, y, vx, vy, BulletType.AMULET, damage, true);
            bullet.setSprite(AssetManager.getInstance().getTextureRegion(key));
        } else {
            bullet = EntityFactory.createPlayerBullet(x, y, damage, key);
            bullet.init(x, y, vx, vy, BulletType.AMULET, damage, true);
        }

        activePlayerBullets.add(bullet);
        return bullet;
    }

    public Bullet spawnPlayerBullet(
        float x, float y, float vx, float vy, int damage
    ) {
        return spawnPlayerBullet(x, y, vx, vy, damage, "bullet_amulet");
    }

    public Bullet spawnEnemyBullet(
        float x, float y, float vx, float vy,
        int damage, String spriteKey
    ) {
        String key = (spriteKey != null) ? spriteKey : "bullet_danmaku";
        Bullet bullet;

        if (!enemyBulletPool.isEmpty()) {
            bullet = enemyBulletPool.poll();
            bullet.init(x, y, vx, vy, BulletType.DANMAKU, damage, false);
            bullet.setSprite(AssetManager.getInstance().getTextureRegion(key));
        } else {
            bullet = EntityFactory.createEnemyBullet(x, y, damage, key);
            bullet.init(x, y, vx, vy, BulletType.DANMAKU, damage, false);
        }

        activeEnemyBullets.add(bullet);
        return bullet;
    }

    public Bullet spawnEnemyBullet(
        float x, float y, float vx, float vy, int damage
    ) {
        return spawnEnemyBullet(x, y, vx, vy, damage, "bullet_danmaku");
    }

    public void update(float delta, float screenWidth, float screenHeight) {
        Iterator<Bullet> pIter = activePlayerBullets.iterator();

        while (pIter.hasNext()) {
            Bullet bullet = pIter.next();

            if (entities != null &&
                bullet.getMovementPattern() instanceof HomingBulletMovement homing &&
                homing.getEntities() == null) {

                homing.setEntities(entities);
            }

            bullet.update(delta);

            if (bullet.isDestroyed() ||
                bullet.isOffScreen(screenWidth, screenHeight)) {

                bullet.destroy();
                pIter.remove();
                playerBulletPool.offer(bullet);
            }
        }

        Iterator<Bullet> enemyIterator = activeEnemyBullets.iterator();

        while (enemyIterator.hasNext()) {
            Bullet bullet = enemyIterator.next();

            bullet.update(delta);

            if (bullet.isDestroyed() ||
                bullet.isOffScreen(screenWidth, screenHeight)) {

                bullet.destroy();
                enemyIterator.remove();
                enemyBulletPool.offer(bullet);
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (Bullet bullet : activePlayerBullets) {
            if (!bullet.isDestroyed()) {
                bullet.render(batch);
            }
        }

        for (Bullet bullet : activeEnemyBullets) {
            if (!bullet.isDestroyed()) {
                bullet.render(batch);
            }
        }
    }

    public void clearEnemyBullets(List<GameObject> entities) {
        Iterator<Bullet> iterator = activeEnemyBullets.iterator();

        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();

            if (entities != null) {
                Item item = EntityFactory.createItem(
                    bullet.getX(),
                    bullet.getY(),
                    ItemType.POINT
                );

                entities.add(item);
            }

            bullet.destroy();
            iterator.remove();
            enemyBulletPool.offer(bullet);
        }
    }

    public void clearEnemyBullets() {
        clearEnemyBullets(null);
    }

    public void clearPlayerBullets() {
        Iterator<Bullet> pIter = activePlayerBullets.iterator();

        while (pIter.hasNext()) {
            Bullet bullet = pIter.next();

            bullet.destroy();
            pIter.remove();
            playerBulletPool.offer(bullet);
        }
    }

    // Fantasy Seal: RingShot + FantasySealMovement
    public void spawnBombOrbs(
        float originX,
        float originY,
        GameObject targetEnemy,
        List<GameObject> entities
    ) {
        int orbCount = 8;
        int orbDamage = 35;

        List<GameObject> targetEntities =
            (entities != null) ? entities : this.entities;

        RingShot bombPattern = new RingShot(
            150f,
            orbCount,
            orbDamage,
            new FantasySealMovement(targetEnemy, targetEntities)
        );

        bombPattern.execute(originX, originY, this, true);

        System.out.println(
            "[BulletManager] Spawned 8 Bomb Spirit Orbs via RingShot composed with FantasySealMovement!"
        );
    }

    public void spawnBombOrbs(
        float originX,
        float originY,
        GameObject targetEnemy
    ) {
        spawnBombOrbs(originX, originY, targetEnemy, null);
    }

    public List<Bullet> getActivePlayerBullets() {
        return activePlayerBullets;
    }

    public List<Bullet> getActiveEnemyBullets() {
        return activeEnemyBullets;
    }

    public int getPlayerPoolSize() {
        return playerBulletPool.size();
    }

    public int getEnemyPoolSize() {
        return enemyBulletPool.size();
    }

    public List<GameObject> getEntities() {
        return entities;
    }

    public void setEntities(List<GameObject> entities) {
        this.entities = entities;
    }
}
