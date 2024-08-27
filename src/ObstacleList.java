import java.awt.*;
import java.util.*;

public class ObstacleList {

    ArrayList<Obstacle> obstacles;

    ObstacleList() {
        obstacles = new ArrayList<>();

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
        return obstacles.getLast();
    }

}
