package byow.Core;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.*;

public class Constants {
    public static final int MAPWIDTH = 80;
    public static final int MAPHEIGHT = 45;

    public static final int MAXROOMWIDTH = 15;
    public static final int MAXROOMHEIGHT = 15;

    public static final int MAXHALLWAYDIMENSION = 5;




    public static final boolean HORIZONTAL = true;
    public static final boolean VERTICAL = false;

    public static final String NORTH = "W";
    public static final String EAST = "D";
    public static final String SOUTH = "S";
    public static final String WEST = "A";

    //public static final TETile FLOOR = Tileset.FLOOR;
    public static final TETile FLOOR = new TETile('▲', Color.orange, Color.black, "Floor", "./images/ground1.png");
    public static final TETile WALL = new TETile('▲', Color.orange, Color.black, "Wall", "./images/crate_14.png");
    public static final TETile WALL_HIT = new TETile('▲', Color.orange, Color.black, "Damaged Wall", "./images/crate_04.png");
    //public static final TETile WALL = Tileset.WALL;
    public static final TETile NOTHING = Tileset.NOTHING;
    public static final TETile SOUL = new TETile('▲', Color.orange, Color.black, "Soul Stone", "./images/soul.png");
    public static final TETile TIME = new TETile('▲', Color.green, Color.black, "Time Stone", "./images/time.png");
    public static final TETile SPACE = new TETile('▲', Color.cyan, Color.black, "Space Stone", "./images/space.png");
    public static final TETile MIND = new TETile('▲', Color.yellow, Color.black, "Mind Stone", "./images/mind.png");
    public static final TETile REALITY = new TETile('▲', Color.red, Color.black, "Reality Stone", "./images/reality.png");
    public static final TETile POWER = new TETile('▲', Color.magenta, Color.black, "Power Stone", "./images/power.png");
    public static final TETile USER = Tileset.AVATAR;

}
