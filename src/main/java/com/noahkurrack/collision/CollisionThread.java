/*
 * Copyright (c) 2018 Noah Kurrack. All rights reserved.
 */

package com.noahkurrack.collision;

import com.noahkurrack.collision.out.Output;

public class CollisionThread implements Runnable {

    private int total;
    private int current;

    private int threadId;
    private CollisionFinder collisionFinder;

    CollisionThread(int threadId, int total) {
        this.threadId = threadId;
        this.collisionFinder = new CollisionFinder(this.threadId);

        this.total = total;
        this.current = 0;
    }

    @Override
    public void run() {
        //run collision finder several times (data recorded to json file)
        //System.out.println("run");
        while (current < total) {
            collisionFinder.reset();
            collisionFinder.findCollisions();
            //collisionFinder.reset();
            current++;
            Output.update(threadId, current+1);
        }

        //System.out.println("run method over");
    }
}
