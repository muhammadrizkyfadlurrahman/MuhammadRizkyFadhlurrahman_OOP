
package com.MuhammadRizkyFadhlurrahman.frontend.objects.enemies;

import com.MuhammadRizkyFadhlurrahman.frontend.objects.patterns.entityStrategy.FixedMovement;
import com.badlogic.gdx.graphics.Color;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.Collidable;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.Player;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.patterns.ShootingPattern;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.MuhammadRizkyFadhlurrahman.frontend.systems.BulletManager;

    public class Boss extends Enemy {
       public Boss(String name, int hp) {
           super(380, 400, 64, 64, Color.BLUE, name, hp, 5000L);
           setShootingPattern((new SpreadShot(200f, 3, 30f));
           setMovementPattern(new FixedMovement());

        public Boss(String name, int hp) {
            super(380, 400, 64, 64, Color.BLUE, name, hp, 5000L);
            setShootingPattern(new SpreadShot(200f, 3, 30f));
            setMovementPattern(new FixedMovement());

        }
           @Override
           public void onCollision(Collidable other) {
               if (other instanceof Player) {
                   System.out.println("Player touches boss");
               }
           }


