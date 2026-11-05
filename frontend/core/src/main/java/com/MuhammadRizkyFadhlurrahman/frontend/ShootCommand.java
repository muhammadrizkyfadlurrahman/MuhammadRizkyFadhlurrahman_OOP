package com.MuhammadRizkyFadhlurrahman.frontend.commands;

import com.MuhammadRizkyFadhlurrahman.frontend.objects.Player;
import com.MuhammadRizkyFadhlurrahman.frontend.systems.BulletManager;

public class ShootCommand implements Command {

    @Override
    public void execute(
        Player player,
        BulletManager bulletManager
    ) {
        if (player != null) {
            player.shootBullet(bulletManager);
        }
    }
}
