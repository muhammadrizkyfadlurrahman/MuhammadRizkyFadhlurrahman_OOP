
package com.MuhammadRizkyFadhlurrahman.frontend.systems;

import com.MuhammadRizkyFadhlurrahman.frontend.objects.BulletType;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.bullets.Bullet;

import java.util.Queue;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class BulletManager {

    private Queue<Bullet> playerBulletPool;
    private Queue<Bullet> enemyBulletPool;

    // 2. Lists of Active Bullets on Screen
    private List<Bullet> activePlayerBullets;
    private List<Bullet> activeEnemyBullets;

    public BulletManager() {

        playerBulletPool = new LinkedList<>();
        enemyBulletPool = new LinkedList<>();


        activePlayerBullets = new ArrayList<>();
        activeEnemyBullets = new ArrayList<>();
    }

    public Bullet spawnPlayerBullet(float x, float y, float vx, float vy, int damage) {
       Bullet bullet;
       if(!playerBulletPool.isEmpty()) {
           bullet = playerBulletPool.poll();
       } else {
           bullet = EntityFactory.createPlayerBullet(x,y, damage);
       }
       bullet.init(x,y,vx,vy, BulletType.AMULET, damage, true);
       activePlayerBullets.add(bullet);
       return bullet;
    }

    public Bullet spawnEnemyBullet(float x, float y, float vx, float vy, int damage) {
        Bullet bullet;
        if (!enemyBulletPool.isEmpty()) {
            bullet = enemyBulletPool.poll();
        } else {
            bullet = EntityFactory.createEnemyBullet(x, y, damage);
        }
        bullet.init(x, y, vx, vy, BulletType.AMULET, damage, false);
        activeEnemyBullets.add(bullet);
        return bullet;
    }
    public void update(float delta, float screenWidth, float screenHeight) {
        Iterator<Bullet> playerIterator = activePlayerBullets.iterator();
        while (playerIterator.hasNext()) {
            Bullet bullet = playerIterator.next();
            bullet.update(delta);

            if (bullet.isDestroyed() ||
                bullet.getX() < 0 ||
                bullet.getX() > screenWidth ||
                bullet.getY() < 0 ||
                bullet.getY() > screenHeight) {

                bullet.destroy();
                playerIterator.remove();
                playerBulletPool.offer(bullet);
            }
        }
        Iterator<Bullet> enemyIterator = activeEnemyBullets.iterator();
        while (enemyIterator.hasNext()) {
            Bullet bullet = enemyIterator.next();
            bullet.update(delta);

            if (bullet.isDestroyed() ||
                bullet.getX() < 0 ||
                bullet.getX() > screenWidth ||
                bullet.getY() < 0 ||
                bullet.getY() > screenHeight) {

                bullet.destroy();
                enemyIterator.remove();
                enemyBulletPool.offer(bullet);
            }
        }
    }


    public void render(SpriteBatch batch) {
        for (Bullet b : activePlayerBullets) {
            if (!b.isDestroyed()) b.render(batch);
        }

        for (Bullet b : activeEnemyBullets) {
            if (!b.isDestroyed()) b.render(batch);
        }
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
}
