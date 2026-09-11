package com.MuhammadRizkyFadhlurrahman.frontend;

<<<<<<< HEAD
public class Player {
    String name;
    int hp;
    int power;
    int spellCards;

    public Player(String name, int hp, int power, int spellCards) {
=======
import com.badlogic.gdx.graphics.Color;

public class Player extends GameObject {
    private String name;
    private int hp;
    private int power;
    private int spellCards;
    private long score;

    public Player(String name, int hp, int power, int spellCards) {
        super(280, 40, 32, 32, 0, Color.RED);
>>>>>>> 1fb6d60 (CS2 - Complete module 2)
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
<<<<<<< HEAD
    }

=======
        this.score = 0;
    }
    public Player(float x, float y, String name, int hp, int power, int spellCards) {
        super(x, y, 32, 32, 0, Color.RED);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
    }
    public int getHp() {
        return hp;
    }
    public void setHp(int hp){
        this.hp = Math.max(0,hp);
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public int getPower(){
        return power;
    }
    public void setPower(int power) {
        this.power = power;
    }
    }
    public void addScore(long points) {
        if (points > 0) {
            this.score += points;
        }
    }
>>>>>>> 1fb6d60 (CS2 - Complete module 2)
    public void shoot(Enemy target) {
        int damage = 10 + power;
        System.out.println(name + " shoots " + target.name + " dealing " + damage + " DMG!");
        target.takeDamage(damage);
    }
<<<<<<< HEAD

=======
>>>>>>> 1fb6d60 (CS2 - Complete module 2)
    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp < 0) {
            this.hp = 0;
        }
        System.out.println(name + " took " + damage + " damage! Remaining HP: " + this.hp);
        if (this.hp == 0) {
            System.out.println(name + " was defeated (Pichuun~)! ");
        }
    }
<<<<<<< HEAD

=======
>>>>>>> 1fb6d60 (CS2 - Complete module 2)
    public boolean isAlive() {
        return this.hp > 0;
    }
}
