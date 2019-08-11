package byow.Core;

public class Hole extends Point {

    //private int x;
    //private int y;
    private String orientation;

    public Hole(int x, int y, String orientation) {
        super(x, y);
        this.orientation = orientation;
    }

    public String getOrientation() {
        return this.orientation;
    }



}
