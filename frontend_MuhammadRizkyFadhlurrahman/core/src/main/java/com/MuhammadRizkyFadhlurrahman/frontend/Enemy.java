package com.MuhammadRizkyFadhlurrahman.frontend;

<<<<<<< HEAD
public class Enemy {
    String name;
    int hp;
    int maxHp;

    public Enemy(String name, int hp) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
    }

    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp < 0) {
            this.hp = 0;
        }
        System.out.println(name + " took " + damage + " damage! HP: " + this.hp + "/" + this.maxHp);
        if (this.hp == 0) {
            System.out.println(name + " was defeated!");
        }
=======
import com.badlogic.gdx.graphics.Color;

public class Enemy extends GameObject {
    private String name;
    private int hp;
    private int maxHp;
    private long scoreValue;

    public Enemy(String name, int hp) {
        super(200, 380, 24, 24, 0, Color.PINK);
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.scoreValue = 100L;
    }

    public Enemy(float x, float y, float width, float height, Color color, String name, int hp, long scoreValue) {
        super(x, y, width, height, 0, color);
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.scoreValue = scoreValue;
    }

    public boolean takeDamage(int damage) {
        if (this.hp <= 0) {
            return false;
        }

        this.hp -= damage;
        if (this.hp <= 0) {
            this.hp = 0;
            System.out.println(name + " took " + damage + " damage! HP: " + this.hp + "/" + this.maxHp);
            System.out.println(name + " was defeated!");
            return true;
        }

        System.out.println(name + " took " + damage + " damage! HP: " + this.hp + "/" + this.maxHp);
        return false;
>>>>>>> 1fb6d60 (CS2 - Complete module 2)
    }

    public void attack(Player player, int damage) {
        System.out.println(name + " unleashes bullet barrage on " + player.name + "!");
        player.takeDamage(damage);
    }

    public boolean isAlive() {
        return this.hp > 0;
    }
}
