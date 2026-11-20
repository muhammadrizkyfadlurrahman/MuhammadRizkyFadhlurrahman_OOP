
package com.MuhammadRizkyFadhlurrahman.frontend.states;

import com.MuhammadRizkyFadhlurrahman.frontend.objects.enemies.Boss;
import com.MuhammadRizkyFadhlurrahman.frontend.systems.LevelWaveManager;

public class StageClearState implements WaveState {

    @Override
    public void onEnter(LevelWaveManager manager) {
        System.out.println("STAGE CLEAR!");

        Boss boss = manager.getBoss();

        if (boss != null && !boss.isDestroyed()) {
            boss.destroy();
        }
    }

    @Override
    public void update(LevelWaveManager manager, float delta) {
    }
}
