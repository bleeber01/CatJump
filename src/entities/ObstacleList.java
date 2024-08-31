package entities;
import java.util.*;

public class ObstacleList {
    private final ArrayList<Obstacle> obstacles;

    public ObstacleList() {
        obstacles = new ArrayList<>();
    }

    public ArrayList<Obstacle> getObstacleList() {
        return obstacles;
    }

    public void add(Obstacle o){
        obstacles.add(o);
    }
    public int getSize() {
        return obstacles.size();
    }

    public Obstacle getIndex(int i) {
        return obstacles.get(i);
    }

    public Obstacle getLast() {
        int last = obstacles.size() - 1;
        return obstacles.get(last);
    }

}
