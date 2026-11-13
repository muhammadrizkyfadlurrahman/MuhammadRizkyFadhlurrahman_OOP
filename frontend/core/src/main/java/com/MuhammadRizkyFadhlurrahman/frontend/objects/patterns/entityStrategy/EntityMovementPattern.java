package com.MuhammadRizkyFadhlurrahman.frontend.objects.patterns.entityStrategy;

import com.MuhammadRizkyFadhlurrahman.frontend.objects.GameObject;

public interface EntityMovementPattern {
    void move(GameObject entity, float delta);
}
