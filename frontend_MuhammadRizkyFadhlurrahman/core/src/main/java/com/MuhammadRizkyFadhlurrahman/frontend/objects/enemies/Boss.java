package com.MuhammadRizkyFadhlurrahman.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.Collidable;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.Player;

public class Boss extends Enemy {

    public Boss(String name, int hp) {
        super(380, 400, 48, 48, Color.BLUE, name, hp, 5000L);
    }

    public Boss(float x, float y, String name, int hp) {
        super(x, y, 48, 48, Color.BLUE, name, hp, 5000L);
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Player) {
            System.out.println("Player touches boss");
        }
    }
}
