
package com.MuhammadRizkyFadhlurrahman.frontend.objects.patterns;

import com.MuhammadRizkyFadhlurrahman.frontend.systems.BulletManager;

public interface ShootingPattern {
    void execute(float originX, float originY,
                 BulletManager bulletManager, boolean isPlayer);
}
