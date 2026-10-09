package com.MuhammadRizkyFadhlurrahman.frontend.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

public class AssetManager {

    // 1. Single instance for the Singleton pattern
    private static AssetManager instance;

    // 2. Caches for the Flyweight pattern
    private Map<String, TextureRegion> textureRegionMap;
    private Map<String, Animation<TextureRegion>> animationMap;
    private Map<String, Texture> textureMap;

    // Find out: why is the AssetManager constructor private?
    private AssetManager() {

        // TODO: Initialize the three Maps above as empty HashMaps
        textureRegionMap = new HashMap<>();
        animationMap = new HashMap<>();
        textureMap = new HashMap<>();
    }

    // Global access point for the single instance
    public static AssetManager getInstance() {

        // TODO: If instance is still null, create a new instance (Lazy Initialization)
        if (instance == null) {
            instance = new AssetManager();
        }

        // Return the instance reference
        return instance;
    }

    public Texture loadTexture(String filename) {

        if (!textureMap.containsKey(filename)) {

            if (Gdx.files != null && Gdx.files.internal(filename).exists()) {
                Texture texture = new Texture(Gdx.files.internal(filename));
                textureMap.put(filename, texture);
            } else {
                return null;
            }
        }

        return textureMap.get(filename);
    }

    // ========================================================================
    // Register the Region Textures
    // ========================================================================

    // Register a single region
    public void registerRegion(String key, TextureRegion region) {
        textureRegionMap.put(key, region);
    }

    // Extract a specific cell from the sprite sheet at [row][col]
    public void registerRegionFromSheet(String key, String filename, int tileWidth,
                                        int tileHeight, int row, int col) {

        Texture tex = loadTexture(filename);

        if (tex != null) {
            TextureRegion[][] grid = TextureRegion.split(tex, tileWidth, tileHeight);
            textureRegionMap.put(key, grid[row][col]);
        }
    }

    // Convenience overload: register an animation starting at column 0 with PlayMode.LOOP
    public void registerAnimationFromSheet(String key, String filename, int tileWidth,
                                           int tileHeight, int row, int numFrames,
                                           float frameDuration) {

        registerAnimationFromSheet(
            key,
            filename,
            tileWidth,
            tileHeight,
            row,
            0,
            numFrames,
            frameDuration,
            Animation.PlayMode.LOOP
        );
    }

    // Register a sequence of horizontal frames as an Animation object
    public void registerAnimationFromSheet(String key, String filename, int tileWidth,
                                           int tileHeight, int row, int startCol,
                                           int numFrames, float frameDuration,
                                           Animation.PlayMode playMode) {

        Texture tex = loadTexture(filename);

        if (tex != null) {
            TextureRegion[][] grid = TextureRegion.split(tex, tileWidth, tileHeight);

            TextureRegion[] frames = new TextureRegion[numFrames];

            for (int i = 0; i < numFrames; i++) {
                frames[i] = grid[row][startCol + i];
            }

            Animation<TextureRegion> anim = new Animation<>(frameDuration, frames);
            anim.setPlayMode(playMode);

            animationMap.put(key, anim);
            textureRegionMap.put(key, frames[0]);
        }
    }

    // Overload for flipped animations
    public void registerFlippedAnimationFromSheet(String key, String filename,
                                                  int tileWidth, int tileHeight,
                                                  int row, int numFrames,
                                                  float frameDuration,
                                                  boolean flipX, boolean flipY) {

        registerFlippedAnimationFromSheet(
            key,
            filename,
            tileWidth,
            tileHeight,
            row,
            0,
            numFrames,
            frameDuration,
            Animation.PlayMode.LOOP,
            flipX,
            flipY
        );
    }

    public void registerFlippedAnimationFromSheet(String key, String filename,
                                                  int tileWidth, int tileHeight,
                                                  int row, int startCol,
                                                  int numFrames, float frameDuration,
                                                  Animation.PlayMode playMode,
                                                  boolean flipX, boolean flipY) {

        Texture tex = loadTexture(filename);

        if (tex != null) {
            TextureRegion[][] grid = TextureRegion.split(tex, tileWidth, tileHeight);

            TextureRegion[] frames = new TextureRegion[numFrames];

            for (int i = 0; i < numFrames; i++) {
                TextureRegion frame =
                    new TextureRegion(grid[row][startCol + i]);

                frame.flip(flipX, flipY);
                frames[i] = frame;
            }

            Animation<TextureRegion> anim =
                new Animation<>(frameDuration, frames);

            anim.setPlayMode(playMode);

            animationMap.put(key, anim);
            textureRegionMap.put(key, frames[0]);
        }
    }

    // ========================================================================
    // Getting the Region Textures
    // ========================================================================

    // Retrieve a region by key
    public TextureRegion getTextureRegion(String key) {
        return textureRegionMap.get(key);
    }

    public TextureRegion getRegion(String key) {
        return getTextureRegion(key);
    }

    // Retrieve an animation by key
    public Animation<TextureRegion> getAnimation(String key) {
        return animationMap.get(key);
    }

    // ========================================================================
    // Initialize Assets
    // ========================================================================

    public void init() {

        // Register Reimu Hakurei's idle animation
        registerAnimationFromSheet(
            "player_idle",
            "player.png",
            32,
            48,
            0,
            8,
            0.1f
        );

        // Register the Boss's idle animation
        registerAnimationFromSheet(
            "boss_idle",
            "rumia.png",
            64,
            64,
            0,
            8,
            0.1f
        );

        // Register the Fairy animation
        registerAnimationFromSheet(
            "fairy_idle",
            "fairy.png",
            32,
            32,
            0,
            8,
            0.1f
        );

        // Register the enemy bullet
        registerRegionFromSheet(
            "bullet_danmaku",
            "bullets_small.png",
            16,
            16,
            2,
            3
        );

        // Register the 4 Item variants
        registerRegionFromSheet(
            "item_power",
            "items.png",
            16,
            16,
            0,
            0
        );

        registerRegionFromSheet(
            "item_point",
            "items.png",
            16,
            16,
            0,
            1
        );

        registerRegionFromSheet(
            "item_bomb",
            "items.png",
            16,
            16,
            0,
            2
        );

        registerRegionFromSheet(
            "item_life",
            "items.png",
            16,
            16,
            0,
            3
        );
    }

    public void dispose() {

        for (Texture texture : textureMap.values()) {
            texture.dispose();
        }

        textureMap.clear();
        textureRegionMap.clear();
        animationMap.clear();
    }
}
