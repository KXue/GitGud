package com.gud.git.gitgud.Engine;

import android.graphics.Point;
import android.media.AudioAttributes;
import android.util.Log;

import com.gud.git.gitgud.App;
import com.gud.git.gitgud.GameObjects.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;


/**
 * Created by KevinXue on 5/24/2017.
 */

public class SpatialHash {
    private int mCellSize;
    private HashMap<Point, HashSet<GameObject>> mHashMap;
    private int mHashScreenWidth;
    private int mHashScreenHeight;
    public SpatialHash(int cellSize){
        mCellSize = cellSize;
        mHashMap = new HashMap<>();
        mHashScreenHeight = App.getScreenHeight()/cellSize;
        mHashScreenWidth = App.getScreenWidth()/cellSize;
    }
    public void insertObject(GameObject obj){
        Circle oCircle = obj.getHitbox();
        Point min = hash(new Point(oCircle.getMinX(), oCircle.getMinY()));
        Point max = hash(new Point(oCircle.getMaxX(), oCircle.getMaxY()));
        int minX, maxX, minY, maxY;
        minX = Math.max(0, min.x);
        minY = Math.max(0, min.y);
        maxX = Math.min(mHashScreenWidth, max.x);
        maxY = Math.min(mHashScreenHeight, max.y);
        Point currentPoint = new Point();
        for(int i = minX; i <= maxX; i++){
            for(int j = minY; j <= maxY; j++){
                currentPoint.x = i;
                currentPoint.y = j;
                HashSet<GameObject> bucket = mHashMap.get(currentPoint);
                if(bucket == null){
                    bucket = new HashSet<>();
                    mHashMap.put(currentPoint, bucket);
                }
                bucket.add(obj);
            }
        }
    }
    public void clear(){
        Collection<HashSet<GameObject>> buckets = mHashMap.values();
        for (HashSet<GameObject> s : buckets) {
            s.clear();
        }
    }
    public HashSet<GameObject> getPotentialColliders(GameObject obj){
        HashSet<GameObject> retBucket = new HashSet<>();
        Circle oCircle = obj.getHitbox();
        Point min = hash(new Point(oCircle.getMinX(), oCircle.getMinY()));
        Point max = hash(new Point(oCircle.getMaxX(), oCircle.getMaxY()));
        int minX, maxX, minY, maxY;
        minX = Math.max(0, min.x);
        minY = Math.max(0, min.y);
        maxX = Math.min(mHashScreenWidth, max.x);
        maxY = Math.min(mHashScreenHeight, max.y);
        Point currentPoint = new Point();
        int buckets = 0;
        for(int i = minX; i <= maxX; i++){
            for(int j = minY; j <= maxY; j++){
                currentPoint.x = i;
                currentPoint.y = j;
                HashSet<GameObject> bucket = mHashMap.get(currentPoint);
                if(bucket != null){
                    retBucket.addAll(bucket);
                    buckets++;
                }
            }
        }
        retBucket.remove(obj);
        return retBucket;
    }
    private Point hash(Point position){
        return new Point(position.x/mCellSize, position.y/mCellSize);
    }

}
