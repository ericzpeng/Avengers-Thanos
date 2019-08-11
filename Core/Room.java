package byow.Core;
import java.util.List;
import java.util.ArrayList;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

class Room {
    private Point start;
    private int width;
    private int height;
    List<Hole> holes;


    boolean drawn;

    Room(Point start, int width, int height) {
        this.start = start;
        this.width = width;
        this.height = height;
        holes = new ArrayList<>();
        getRoomHoles();
        this.drawn = false;
    }

    void drawRoom(TETile[][] world, List<Point> floorTiles) {
        if (canAddRoom(world)) {
            drawWall(world);
            drawFloor(world, floorTiles);

            drawn = true;
        }
    }

    private void drawFloor(TETile[][] world, List<Point> floorTiles) {
        int floorWidth = width - 2;
        int floorHeight = height - 2;
        Point floorStart = new Point(start.getX() + 1, start.getY() + 1);
        for (int x = floorStart.getX(); x < floorStart.getX() + floorWidth; x++) {
            for (int y = floorStart.getY(); y < floorStart.getY() + floorHeight; y++) {
                world[x][y] = Constants.FLOOR;

                Point p = new Point(x, y);
                floorTiles.add(p);
            }
        }
    }



    private void drawWall(TETile[][] world) {
        drawTopWall(world);
        drawBotWall(world);
        drawLeftWall(world);
        drawRightWall(world);
    }

    private void drawRightWall(TETile[][] world) {
        int x = start.getX() + width - 1;
        for (int i = start.getY(); i < start.getY() + height; i++) {
            world[x][i] = Constants.WALL;
        }
    }

    private void drawLeftWall(TETile[][] world) {
        int x = start.getX();
        for (int i = start.getY(); i < start.getY() + height; i++) {
            world[x][i] = Constants.WALL;
        }
    }

    private void drawBotWall(TETile[][] world) {
        int y = start.getY();
        for (int i = start.getX(); i < start.getX() + width; i++) {
            world[i][y] = Constants.WALL;
        }
    }

    private void drawTopWall(TETile[][] world) {
        int y = start.getY() + height - 1;
        for (int i = start.getX(); i < start.getX() + width; i++) {
            world[i][y] = Constants.WALL;
        }
    }

    private boolean canAddRoom(TETile[][] world) {
        return !checkOutOfBounds(world) && !checkOverlap(world);

    }

    private boolean checkOverlap(TETile[][] world) {
        for (int x = start.getX(); x < start.getX() + width; x++) {
            for (int y = start.getY(); y < start.getY() + height; y++) {
                if (!world[x][y].equals(Tileset.NOTHING)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkOutOfBounds(TETile[][] world) {


        return (start.getX() < 0) || (start.getX() + width > Constants.MAPWIDTH)
                || (start.getY() + height > Constants.MAPHEIGHT) || (start.getY() < 0);


    }

    private void getRoomHoles() {
        holes.add(botHole());
        holes.add(topHole());
        holes.add(leftHole());
        holes.add(rightHole());

    }

    Hole botHole() {
        int randomX = RandomUtils.uniform(CreateMap.RANDOM, start.getX() + 1,
                start.getX() + width - 1);
        return new Hole(randomX, start.getY(), "bot");
    }

    Hole topHole() {
        int randomX = RandomUtils.uniform(CreateMap.RANDOM, start.getX() + 1,
                start.getX() + width - 1);
        return new Hole(randomX, start.getY() + height - 1, "top");
    }

    Hole leftHole() {
        int randomY = RandomUtils.uniform(CreateMap.RANDOM, start.getY() + 1,
                start.getY() + height - 1);
        return new Hole(start.getX(), randomY, "left");
    }

    Hole rightHole() {
        int randomY = RandomUtils.uniform(CreateMap.RANDOM, start.getY() + 1,
                start.getY() + height - 1);
        return new Hole(start.getX() + width - 1, randomY, "right");
    }


    /** Erics stuff */
    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }

    Point getStart() {
        return start;
    }







}
