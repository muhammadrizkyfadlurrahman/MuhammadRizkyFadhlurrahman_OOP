
package com.MuhammadRizkyFadhlurrahman.frontend.systems;


public void resolveCollisions(Player player, List<GameObject> entities, BulletManager bulletManager) {
    if (player == null || entities == null || bulletManager == null) return;

    List<GameObject> newDrops = new ArrayList<>();
