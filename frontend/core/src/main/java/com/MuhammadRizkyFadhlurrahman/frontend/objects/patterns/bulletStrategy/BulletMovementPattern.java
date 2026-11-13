
package com.MuhammadRizkyFadhlurrahman.frontend.objects.patterns.bulletStrategy;

import com.MuhammadRizkyFadhlurrahman.frontend.objects.bullets.Bullet;

public interface BulletMovementPattern {

    void move(Bullet bullet, float delta);

}
