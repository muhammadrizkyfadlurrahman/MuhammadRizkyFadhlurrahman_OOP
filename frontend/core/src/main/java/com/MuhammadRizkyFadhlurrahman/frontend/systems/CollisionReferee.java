package com.MuhammadRizkyFadhlurrahman.frontend.systems;

import com.MuhammadRizkyFadhlurrahman.frontend.objects.Player;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.GameObject;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.bullets.Bullet;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.enemies.Enemy;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.items.Item;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.items.ItemType;

import java.util.List;
import java.util.ArrayList;

public class CollisionReferee {

    public void resolveCollisions(Player player, List<GameObject> entities, BulletManager bulletManager) {

        if (player == null || entities == null || bulletManager == null) return;

        List<GameObject> newDrops = new ArrayList<>();

        // 1. Evaluate Player Bullets vs Enemies
        for (Bullet bullet : bulletManager.getActivePlayerBullets()) {

            if (bullet.isDestroyed()) continue;

            for (GameObject entity : entities) {

                if (entity instanceof Enemy enemy && !enemy.isDestroyed()) {

                    if (bullet.getCoreHitbox().overlaps(enemy.getCoreHitbox())) {

                        boolean defeated = enemy.takeDamage(bullet.getDamage());

                        if (defeated) {
                            player.addScore(enemy.getScoreValue());

                            Item drop = EntityFactory.createItem(
                                enemy.getX(),
                                enemy.getY(),
                                ItemType.POWER
                            );

                            newDrops.add(drop);
                        }

                        bullet.destroy();
                        break;
                    }
                }
            }
        }

        // 2. Evaluate Enemy Bullets vs Player
        for (Bullet bullet : bulletManager.getActiveEnemyBullets()) {

            if (bullet.isDestroyed()) continue;

            if (!player.isDestroyed()) {

                // PRIORITY 1: Core Hit
                if (bullet.getCoreHitbox().overlaps(player.getCoreHitbox())) {

                    player.takeDamage(bullet.getDamage());
                    bullet.destroy();

                }

                // PRIORITY 2: Graze Hit
                else if (!bullet.hasBeenGrazed()
                    && bullet.getCoreHitbox().overlaps(player.getGrazeHitbox())) {

                    bullet.setGrazed(true);
                    player.addScore(50);
                }
            }
        }

        // 3. Evaluate Player vs Items and Physical Enemy Contact
        for (GameObject entity : entities) {

            if (entity.isDestroyed()) continue;

            // CASE A: Item Collection
            if (entity instanceof Item item) {

                if (item.getCoreHitbox().overlaps(player.getCoreHitbox())
                    || item.getCoreHitbox().overlaps(player.getGrazeHitbox())) {

                    player.collectItem(item);
                }
            }

            // CASE B: Physical Contact with Enemy
            else if (entity instanceof Enemy enemy) {

                if (player.getCoreHitbox().overlaps(enemy.getCoreHitbox())) {
                    player.takeDamage(10);
                }
            }
        }

        entities.addAll(newDrops);
    }
}
