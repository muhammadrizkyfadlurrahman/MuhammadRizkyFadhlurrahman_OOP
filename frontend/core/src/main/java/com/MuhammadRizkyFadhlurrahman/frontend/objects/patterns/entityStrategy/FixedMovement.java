package com.MuhammadRizkyFadhlurrahman.frontend.objects.patterns.entityStrategy;

import com.MuhammadRizkyFadhlurrahman.frontend.objects.GameObject;

public class FixedMovement implements EntityMovementPattern {

    @Override
    public void move(GameObject entity, float delta) {
        // Fixed position: no positional displacement applied to entity
    }
}
