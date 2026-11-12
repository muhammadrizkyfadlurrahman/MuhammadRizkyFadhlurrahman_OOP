
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


        for (Bullet bullet : bulletManager.getActiveEnemyBullets()) {

            if (bullet.isDestroyed()) continue;

            if (!player.isDestroyed()) {


                if (bullet.getCoreHitbox().overlaps(player.getCoreHitbox())) {

                    if (!player.isInvulnerable()) {
                        System.out.println(
                            "[CollisionReferee] CORE HIT! Enemy Bullet hit "
                                + player.getName() + " for " + bullet.getDamage() + " DMG!"
                        );

                        player.takeDamage(100, bulletManager, entities);
                        bullet.destroy();
                        break;
                    }
                }


                else if (!bullet.hasBeenGrazed()
                    && bullet.getCoreHitbox().overlaps(player.getGrazeHitbox())) {

                    bullet.setGrazed(true);
                    player.addGraze();

                    System.out.println(
                        "[CollisionReferee] GRAZE! "
                            + player.getName() + " grazed an enemy bullet (+50 pts)!"
                    );
                }
            }
        }


        for (GameObject entity : entities) {

            if (entity.isDestroyed()) continue;


            if (entity instanceof Item item) {

                if (item.getCoreHitbox().overlaps(player.getCoreHitbox())
                    || item.getCoreHitbox().overlaps(player.getGrazeHitbox())) {

                    player.collectItem(item);
                }
            }


            else if (entity instanceof Enemy enemy) {

                if (!player.isInvulnerable()
                    && player.getCoreHitbox().overlaps(enemy.getCoreHitbox())) {

                    System.out.println(
                        "[CollisionReferee] CORE COLLISION! Player collided with "
                            + enemy.getName() + "!"
                    );

                    player.takeDamage(100, bulletManager, entities);
                }
            }
        }

        entities.addAll(newDrops);
    }
}
