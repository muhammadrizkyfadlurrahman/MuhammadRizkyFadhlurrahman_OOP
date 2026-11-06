package com.MuhammadRizkyFadhlurrahman.frontend.commands;

import com.MuhammadRizkyFadhlurrahman.frontend.objects.GameObject;
import com.MuhammadRizkyFadhlurrahman.frontend.objects.Player;
import com.MuhammadRizkyFadhlurrahman.frontend.systems.BulletManager;

import java.util.List;

public class BombCommand implements Command {

    private List<GameObject> entities;

    public BombCommand() {
    }

    public BombCommand(List<GameObject> entities) {
        this.entities = entities;
    }

    public void setEntities(List<GameObject> entities) {
        this.entities = entities;
    }

    @Override
    public void execute(
        Player player,
        BulletManager bulletManager
    ) {
        if (player != null) {
            player.useBomb(bulletManager, entities);
        }
    }
}
