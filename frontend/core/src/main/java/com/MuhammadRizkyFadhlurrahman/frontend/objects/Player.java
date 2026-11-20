
package com.MuhammadRizkyFadhlurrahman.frontend.objects;

import com.MuhammadRizkyFadhlurrahman.frontend.observers.GameObserver;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.items.Item;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.items.ItemType;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.enemies.Enemy;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.patterns.ShootingPattern;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.patterns.entityStrategy.FixedMovement;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.patterns.shootingStrategy.FrontShot;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.patterns.shootingStrategy.SideShot;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.patterns.shootingStrategy.CompositeShootingPattern;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.patterns.bulletStrategy.HomingBulletMovement;
import com.MuhammadRizkyFadhlurrahman.frontend.systems.BulletManager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;
import java.util.ArrayList;

public class Player extends EntityShooter {

    private String name;
    private int hp;
    private int lives = 3;
    private int power;
    private int spellCards;
    private long score;

    private List<GameObserver> observers = new ArrayList<>();

    private int grazeCount = 0;
    private boolean focused = false;
    private int currentDir = 0;

    private float shootCooldown = 5f / 60f;
    private float shootTimer = shootCooldown;

    private float homingCooldown = 30f / 60f;
    private float homingShootTimer = homingCooldown;

    private float invulnerableTimer = 0f;

    private ShootingPattern mainShootingPattern;
    private ShootingPattern sideShootingPattern;

    public Player(String name, int hp, int power, int spellCards) {
        super(280, 40, 32, 48, 240f, Color.RED);

        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;

        setMovementPattern(new FixedMovement());
        updatePowerStrategy();
    }

    public Player(
        float x, float y, String name,
        int hp, int power, int spellCards
    ) {
        super(x, y, 32, 48, 240f, Color.RED);

        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;

        setMovementPattern(new FixedMovement());
        updatePowerStrategy();
    }

    public void registerObserver(GameObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
            observer.onScoreChanged(score);
            observer.onHpChanged(hp);
            observer.onLivesChanged(lives);
            observer.onSpellCardsChanged(spellCards);
            observer.onPowerChanged(power);
            observer.onGrazeChanged(grazeCount);
        }
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    private void notifyScoreChanged() {
        for (GameObserver obs : observers) {
            obs.onScoreChanged(score);
        }
    }

    private void notifyHpChanged() {
        for (GameObserver obs : observers) {
            obs.onHpChanged(hp);
        }
    }

    private void notifyLivesChanged() {
        for (GameObserver obs : observers) {
            obs.onLivesChanged(lives);
        }
    }

    private void notifySpellCardsChanged() {
        for (GameObserver obs : observers) {
            obs.onSpellCardsChanged(spellCards);
        }
    }

    private void notifyPowerChanged() {
        for (GameObserver obs : observers) {
            obs.onPowerChanged(power);
        }
    }

    private void notifyGrazeChanged() {
        for (GameObserver obs : observers) {
            obs.onGrazeChanged(grazeCount);
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        shootTimer += delta;
        homingShootTimer += delta;

        if (shootTimer > 0.15f) {
            shootTimer = shootCooldown;
            homingShootTimer = homingCooldown;
        }

        if (invulnerableTimer > 0f) {
            invulnerableTimer -= delta;

            if (invulnerableTimer < 0f) {
                invulnerableTimer = 0f;
            }
        }
    }

    public void updateAnimationState(float dx) {
        int newDir = 0;

        if (dx < 0) {
            newDir = -1;
        } else if (dx > 0) {
            newDir = 1;
        }

        if (newDir != currentDir) {
            currentDir = newDir;

            String key;

            if (currentDir < 0) {
                key = "player_left";
            } else if (currentDir > 0) {
                key = "player_right";
            } else {
                key = "player_idle";
            }

            Animation<TextureRegion> anim =
                com.MuhammadRizkyFadhlurrahman.frontend.systems.AssetManager
                    .getInstance().getAnimation(key);

            if (anim != null) {
                setAnimation(anim);
            }
        }
    }

    public void move(float dx, float dy) {
        this.x += dx * speed;
        this.y += dy * speed;

        clampToPlayfield();
    }

    private void clampToPlayfield() {
        float minX = 32;
        float maxX = 416 - width;
        float minY = 16;
        float maxY = 464 - height;

        if (x < minX) {
            x = minX;
        }

        if (x > maxX) {
            x = maxX;
        }

        if (y < minY) {
            y = minY;
        }

        if (y > maxY) {
            y = maxY;
        }
    }

    public void updatePowerStrategy() {
        float spreadAngle = focused ? 4f : 18f;
        float dualSpreadAngle = focused ? 3f : 12f;

        float sideHOffset = focused ? 8f : 24f;
        float sideVOffset = focused ? 16f : -16f;

        float leftAngle = focused ? 100f : 120f;
        float rightAngle = focused ? 80f : 60f;

        float normalSpeed = 720f;
        float homingSpeed = 500f;

        if (power < 8) {
            mainShootingPattern = new FrontShot(normalSpeed, 48);
            sideShootingPattern = null;

            homingCooldown = 30f / 60f;

        } else if (power < 16) {
            mainShootingPattern = new FrontShot(normalSpeed, 48);

            sideShootingPattern = new SideShot(
                homingSpeed, 14,
                sideHOffset, sideVOffset,
                leftAngle, rightAngle,
                true,
                new HomingBulletMovement(360f),
                "bullet_amulet_homing"
            );

            homingCooldown = 30f / 60f;

        } else if (power < 32) {
            mainShootingPattern = new SpreadShot(
                normalSpeed, 2, dualSpreadAngle,
                30, 8f, null, null, null
            );

            sideShootingPattern = new SideShot(
                homingSpeed, 14,
                sideHOffset, sideVOffset,
                leftAngle, rightAngle,
                true,
                new HomingBulletMovement(360f),
                "bullet_amulet_homing"
            );

            homingCooldown = 30f / 60f;

        } else if (power < 48) {
            mainShootingPattern = new SpreadShot(
                normalSpeed, 3, spreadAngle,
                26, 0f, null, null, null
            );

            sideShootingPattern = new SideShot(
                homingSpeed, 14,
                sideHOffset, sideVOffset,
                leftAngle, rightAngle,
                true,
                new HomingBulletMovement(360f),
                "bullet_amulet_homing"
            );

            homingCooldown = 30f / 60f;

        } else if (power < 64) {
            mainShootingPattern = new SpreadShot(
                normalSpeed, 3, spreadAngle,
                26, 0f, null, null, null
            );

            sideShootingPattern = new SideShot(
                homingSpeed, 12,
                sideHOffset, sideVOffset,
                leftAngle, rightAngle,
                true,
                new HomingBulletMovement(360f),
                "bullet_amulet_homing"
            );

            homingCooldown = 15f / 60f;

        } else if (power < 80) {
            mainShootingPattern = new SpreadShot(
                normalSpeed, 3, spreadAngle,
                26, 0f, null, null, null
            );

            sideShootingPattern = new CompositeShootingPattern(
                new SideShot(
                    homingSpeed, 9,
                    sideHOffset, sideVOffset,
                    leftAngle, rightAngle,
                    true,
                    new HomingBulletMovement(360f),
                    "bullet_amulet_homing"
                ),
                new SideShot(
                    homingSpeed, 12,
                    sideHOffset + 4f, sideVOffset,
                    leftAngle + 30f, rightAngle - 30f,
                    true,
                    new HomingBulletMovement(360f),
                    "bullet_amulet_homing"
                )
            );

            homingCooldown = 15f / 60f;

        } else if (power < 128) {
            mainShootingPattern = new SpreadShot(
                normalSpeed, 3, spreadAngle,
                26, 0f, null, null, null
            );

            sideShootingPattern = new CompositeShootingPattern(
                new SideShot(
                    homingSpeed, 8,
                    sideHOffset, sideVOffset,
                    leftAngle - 10f, rightAngle + 10f,
                    true,
                    new HomingBulletMovement(360f),
                    "bullet_amulet_homing"
                ),
                new SideShot(
                    homingSpeed, 8,
                    sideHOffset + 4f, sideVOffset,
                    leftAngle + 10f, rightAngle - 10f,
                    true,
                    new HomingBulletMovement(360f),
                    "bullet_amulet_homing"
                )
            );

            homingCooldown = 15f / 60f;

        } else {
            mainShootingPattern = new SpreadShot(
                normalSpeed, 4, spreadAngle * 1.2f,
                24, 16f, null, null, null
            );

            sideShootingPattern = new CompositeShootingPattern(
                new SideShot(
                    homingSpeed, 8,
                    sideHOffset, sideVOffset,
                    leftAngle - 10f, rightAngle + 10f,
                    true,
                    new HomingBulletMovement(360f),
                    "bullet_amulet_homing"
                ),
                new SideShot(
                    homingSpeed, 8,
                    sideHOffset + 4f, sideVOffset,
                    leftAngle + 10f, rightAngle - 10f,
                    true,
                    new HomingBulletMovement(360f),
                    "bullet_amulet_homing"
                ),
                new SideShot(
                    homingSpeed, 8,
                    sideHOffset + 8f, sideVOffset,
                    leftAngle + 30f, rightAngle - 30f,
                    true,
                    new HomingBulletMovement(360f),
                    "bullet_amulet_homing"
                )
            );

            homingCooldown = 15f / 60f;
        }

        if (sideShootingPattern != null) {
            setShootingPattern(
                new CompositeShootingPattern(
                    mainShootingPattern,
                    sideShootingPattern
                )
            );
        } else {
            setShootingPattern(mainShootingPattern);
        }
    }

    @Override
    public void shootBullet(BulletManager bulletManager) {
        if (bulletManager == null) {
            return;
        }

        if (shootTimer >= shootCooldown) {
            if (mainShootingPattern != null) {
                mainShootingPattern.execute(
                    x + width / 2f - 8f,
                    y + height,
                    bulletManager,
                    true
                );
            } else if (shootingPattern != null) {
                shootingPattern.execute(
                    x + width / 2f - 8f,
                    y + height,
                    bulletManager,
                    true
                );
            } else {
                bulletManager.spawnPlayerBullet(
                    x + width / 2f - 8f,
                    y + height,
                    0,
                    400f,
                    10 + power
                );
            }

            shootTimer = 0f;
        }

        if (sideShootingPattern != null
            && homingShootTimer >= homingCooldown) {

            sideShootingPattern.execute(
                x + width / 2f - 8f,
                y + height,
                bulletManager,
                true
            );

            homingShootTimer = 0f;
        }
    }

    public void useBomb(
        BulletManager bulletManager,
        List<GameObject> entities
    ) {
        if (spellCards > 0) {
            spellCards--;
            invulnerableTimer = 5.0f;
            notifySpellCardsChanged();

            if (bulletManager != null) {
                bulletManager.clearEnemyBullets(entities);

                GameObject target = null;

                if (entities != null) {
                    for (GameObject obj : entities) {
                        if (obj instanceof Enemy enemy
                            && !enemy.isDestroyed()) {

                            target = obj;
                            break;
                        }
                    }
                }

                bulletManager.spawnBombOrbs(
                    x + width / 2f,
                    y + height / 2f,
                    target,
                    entities
                );
            }

            System.out.println(
                name + " unleashes SPELL CARD: Spirit Sign Fantasy Seal (霊符「夢想封印」)! All enemy bullets cleared & 8 homing orbs launched!"
            );

        } else {
            System.out.println("No Spell Cards (Bombs) remaining!");
        }
    }

    public void useBomb(BulletManager bulletManager) {
        useBomb(bulletManager, null);
    }

    public void addGraze() {
        this.grazeCount++;
        addScore(50);
        notifyGrazeChanged();
    }

    @Override
    public Rectangle getCoreHitbox() {
        return new Rectangle(
            x + (width / 2f) - 4f,
            y + (height / 2f) - 4f,
            8f,
            8f
        );
    }

    @Override
    public Rectangle getGrazeHitbox() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public void render(SpriteBatch batch) {
        if (batch != null && active && !isDestroyed()) {

            if (invulnerableTimer > 0f) {
                boolean visible =
                    ((int) (invulnerableTimer * 16)) % 2 == 0;

                if (!visible) {
                    return;
                }
            }

            if (focused) {
                batch.setColor(1f, 1f, 1f, 0.5f);
            }

            super.render(batch);

            if (focused) {
                batch.setColor(1f, 1f, 1f, 1.0f);
            }
        }
    }

    public void renderFocusIndicator(ShapeRenderer shapeRenderer) {
        if (focused && shapeRenderer != null && active) {
            Rectangle core = getCoreHitbox();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            shapeRenderer.setColor(Color.RED);
            shapeRenderer.circle(
                core.x + core.width / 2f,
                core.y + core.height / 2f,
                4f
            );

            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.circle(
                core.x + core.width / 2f,
                core.y + core.height / 2f,
                2f
            );

            shapeRenderer.end();
        }
    }

    public void takeDamage(int damage) {
        takeDamage(damage, null, null);
    }

    public void takeDamage(
        int damage,
        BulletManager bulletManager,
        List<GameObject> entities
    ) {
        if (isInvulnerable() || isDestroyed()) {
            if (isInvulnerable()) {
                System.out.println(
                    name + " is INVULNERABLE! (0 damage taken)"
                );
            }

            return;
        }

        this.hp -= damage;

        if (this.hp < 0) {
            this.hp = 0;
        }

        notifyHpChanged();

        System.out.println(
            name + " took " + damage
                + " damage! Remaining HP: " + this.hp
        );

        if (this.hp == 0) {
            System.out.println(
                name + " was defeated (Pichuun~)! "
            );

            loseLife(bulletManager, entities);
        }
    }

    public void loseLife(
        BulletManager bulletManager,
        List<GameObject> entities
    ) {
        this.lives--;
        notifyLivesChanged();

        if (bulletManager != null) {
            bulletManager.clearEnemyBullets();
        }

        if (this.lives > 0) {
            if (bulletManager != null) {
                System.out.println(
                    "[Player] " + name
                        + " lost a life! Remaining Lives: "
                        + this.lives
                        + ". Respawning at (200, 50)..."
                );
            }

            respawn();
        } else {
            if (bulletManager != null) {
                System.out.println(
                    "[Player] GAME OVER! "
                        + name + " has run out of lives!"
                );
            }

            destroy();
        }
    }

    public void respawn() {
        this.active = true;
        this.x = 200;
        this.y = 50;
        this.hp = 100;
        this.invulnerableTimer = 3.0f;
        this.power = Math.max(10, this.power - 16);
        this.spellCards = 3;

        updatePowerStrategy();

        notifyHpChanged();
        notifyPowerChanged();
        notifySpellCardsChanged();
    }

    public void resetGame() {
        this.lives = 3;
        this.hp = 100;
        this.power = 0;
        this.spellCards = 3;
        this.score = 0;
        this.grazeCount = 0;
        this.active = true;
        this.x = 200;
        this.y = 50;
        this.invulnerableTimer = 3.0f;

        updatePowerStrategy();

        notifyLivesChanged();
        notifyHpChanged();
        notifyPowerChanged();
        notifySpellCardsChanged();
        notifyScoreChanged();
        notifyGrazeChanged();
    }

    public void collectItem(Item item) {
        if (item.isDestroyed()) {
            return;
        }

        ItemType type = item.getItemTypeEnum();

        if (type != null) {
            switch (type) {
                case POWER -> {
                    this.power += type.getPowerBonus();
                    updatePowerStrategy();
                    notifyPowerChanged();
                    addScore(item.getScoreValue());

                    System.out.println(
                        name + " collected POWER item! Power increased to " + power
                    );
                }

                case POINT -> {
                    addScore(item.getScoreValue());

                    System.out.println(
                        name + " collected POINT item!"
                    );
                }

                case BOMB -> {
                    this.spellCards++;
                    addScore(item.getScoreValue());
                    notifySpellCardsChanged();

                    System.out.println(
                        name + " collected BOMB item! SpellCards: " + spellCards
                    );
                }

                case LIFE -> {
                    this.lives++;
                    this.hp = 100;
                    addScore(item.getScoreValue());
                    notifyLivesChanged();
                    notifyHpChanged();

                    System.out.println(
                        name + " collected LIFE item! Remaining Lives: " + lives
                    );
                }
            }
        } else {
            addScore(item.getScoreValue());

            System.out.println(
                name + " collected " + item.getItemType() + "!"
            );
        }

        item.destroy();
    }

    public void addScore(long points) {
        if (points > 0) {
            this.score += points;
            notifyScoreChanged();

            System.out.println(
                name + " gained " + points
                    + " pts! Total Score: " + this.score
            );
        }
    }

    public boolean isAlive() {
        return this.hp > 0 && this.lives > 0 && !isDestroyed();
    }

    public boolean isGameOver() {
        return this.lives <= 0;
    }

    public boolean isInvulnerable() {
        return invulnerableTimer > 0f;
    }

    public float getInvulnerableTimer() {
        return invulnerableTimer;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
        notifyLivesChanged();
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        if (this.focused != focused) {
            this.focused = focused;
            updatePowerStrategy();
        }
    }

    public int getGrazeCount() {
        return grazeCount;
    }

    public void shoot(Enemy target) {
        int damage = 10 + getPower();

        System.out.println(
            getName() + " shoots " + target.getName()
                + " dealing " + damage + " DMG!"
        );

        boolean defeated = target.takeDamage(damage);

        if (defeated) {
            addScore(target.getScoreValue());
        }
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
        notifyHpChanged();
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
        updatePowerStrategy();
        notifyPowerChanged();
    }

    public int getSpellCards() {
        return spellCards;
    }

    public void setSpellCards(int spellCards) {
        this.spellCards = spellCards;
        notifySpellCardsChanged();
    }

    public long getScore() {
        return score;
    }

    public ShootingPattern getMainShootingPattern() {
        return mainShootingPattern;
    }

    public ShootingPattern getSideShootingPattern() {
        return sideShootingPattern;
    }

    public float getShootCooldown() {
        return shootCooldown;
    }

    public float getHomingCooldown() {
        return homingCooldown;
    }
}
