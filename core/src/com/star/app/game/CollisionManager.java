package com.star.app.game;

import java.util.List;

public class CollisionManager {


    public void check(List<? extends Collidable> srcObj, List<? extends Collidable> dstObj) {
        for (int i = 0; i < srcObj.size(); i++) {
            for (int j = 0; j < dstObj.size(); j++) {
                if (srcObj.get(i).getIsCollided(dstObj.get(j).getHitArea())) {
                    srcObj.get(i).collide(dstObj.get(j));
                    dstObj.get(j).collide(srcObj.get(i));
                }
            }
        }
    }

    public void check(List<? extends Collidable> srcObj, Collidable dstObj) {
        for (int i = 0; i < srcObj.size(); i++) {
            if (srcObj.get(i).getIsCollided(dstObj.getHitArea())) {
                srcObj.get(i).collide(dstObj);
                dstObj.collide(srcObj.get(i));
            }
        }
    }
}
