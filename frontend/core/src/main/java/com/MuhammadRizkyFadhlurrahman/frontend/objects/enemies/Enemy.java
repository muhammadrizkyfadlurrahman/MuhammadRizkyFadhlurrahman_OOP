
package com.MuhammadRizkyFadhlurrahman.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

import com.MuhammadRizkyFadhlurrahman.frontend.objects.EntityShooter;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.Player;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.items.ItemType;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.patterns.ShootingPattern;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.patterns.entityStrategy.EntityMovementPattern;

public class Enemy extends EntityShooter {

    protected String name;
    protected int hp;
    protected int maxHp;
    protected long scoreValue;

    protected ItemType itemDrop = null;
    protected boolean damageable = true;
    protected boolean collidable = true;
    protected float shotHitboxWidth = 0f;
    protected float shotHitboxHeight = 0f;

    public Enemy(String name, int hp) {
        super(200, 380, 24, 24, 0, Color.PINK);
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.scoreValue = 100;
    }

    public Enemy(float x, float y, float width, float height, Color color,
                 String name, int hp, long scoreValue) {
        super(x, y, width, height, 0, color);
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.scoreValue = scoreValue;
    }

    // Constructor Injection - Problem 3
    public Enemy(float x, float y, float width, float height, Color color,
                 String name, int hp, long scoreValue,
                 ShootingPattern shootingPattern,
                 EntityMovementPattern movementPattern) {

        super(x, y, width, height, 0, color, shootingPattern, movementPattern);

        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.scoreValue = scoreValue;
    }

    public boolean takeDamage(int damage) {
        if (!damageable) {
            return false;
        }

        boolean wasAlive = isAlive();
        setHp(getHp() - damage);

        System.out.println(getName() + " took " + damage + " damage! HP: "
            + getHp() + "/" + getMaxHp());

        if (wasAlive && getHp() == 0) {
            System.out.println(getName() + " was defeated!");
            destroy();
            return true;
        }

        return false;
    }

    public void attack(Player player, int damage) {
        System.out.println(name + " unleashes bullet barrage on "
            + player.getName() + "!");
        player.takeDamage(damage);
    }

    public ItemType getItemDrop() {
        return itemDrop;
    }

    public void setItemDrop(ItemType itemDrop) {
        this.itemDrop = itemDrop;
    }

    public boolean isDamageable() {
        return damageable;
    }

    public void setDamageable(boolean damageable) {
        this.damageable = damageable;
    }

    public boolean isCollidable() {
        return collidable;
    }

    public void setCollidable(boolean collidable) {
        this.collidable = collidable;
    }

    public Rectangle getShotHitbox() {
        if (shotHitboxWidth <= 0f || shotHitboxHeight <= 0f) {
            return getCoreHitbox();
        }

        return new Rectangle(
            x + (width - shotHitboxWidth) / 2f,
            y + (height - shotHitboxHeight) / 2f,
            shotHitboxWidth,
            shotHitboxHeight
        );
    }

    public void setShotHitboxSize(float width, float height) {
        this.shotHitboxWidth = width;
        this.shotHitboxHeight = height;
    }

    public boolean isAlive() {
        return this.hp > 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, hp);
    }

    public int getMaxHp() {
        return maxHp;
    }

    public long getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(long scoreValue) {
        this.scoreValue = scoreValue;
    }
}
