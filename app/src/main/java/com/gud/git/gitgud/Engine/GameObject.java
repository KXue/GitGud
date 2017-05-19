package com.gud.git.gitgud.Engine;


import com.gud.git.gitgud.GameObjects.Enemy;
import com.gud.git.gitgud.R;

/**
 * Created by KevinXue on 5/15/2017.
 */

public abstract class GameObject implements Updateable, Renderable {

    public abstract Circle getHitbox();
    public abstract boolean checkCollision(GameObject other);
}
