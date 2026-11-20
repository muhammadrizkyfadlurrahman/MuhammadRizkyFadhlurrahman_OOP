
package com.MuhammadRizkyFadhlurrahman.frontend.states;

import com.MuhammadRizkyFadhlurrahman.frontend.systems.LevelWaveManager;

public interface WaveState {

    void onEnter(LevelWaveManager manager);

    void update(LevelWaveManager manager, float delta);
}
