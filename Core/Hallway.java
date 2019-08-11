package byow.Core;

import java.util.ArrayList;
import java.util.List;

public class Hallway extends Room {

    List<Hole> holes;
    boolean orientation;


    public Hallway(Point start, int width, int height) {
        super(start, width, height);

        if (width == 3) {
            orientation = Constants.VERTICAL;
        } else if (height == 3) {
            orientation = Constants.HORIZONTAL;
        }
        holes = new ArrayList<>();
        getHallwayHoles();


    }

    public void getHallwayHoles() {
        if (orientation == Constants.HORIZONTAL) {
            holes.add(leftHole());
            holes.add(rightHole());
        } else {
            holes.add(botHole());
            holes.add(topHole());
        }
    }






}
