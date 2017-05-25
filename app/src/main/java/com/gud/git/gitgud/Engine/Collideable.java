package com.gud.git.gitgud.Engine;

/**
 * Created by KevinXue on 5/24/2017.
 */

public interface Collideable {
    boolean checkCollision(Collideable other);
    Circle getHitbox();
}
