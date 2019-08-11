package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CreateMap {
    long SEED;
    static Random RANDOM;
    private Player user;
    private TETile[][] world;
    private TERenderer ter;
    private String typed;
    static boolean gameEnd;
    boolean load;
    boolean replay;
    boolean isAlive;
    static List<Point> floorTiles;
    List<Point> theVanished;
    LocalDateTime current;

    //private InputSource inputSource;

    public CreateMap(TERenderer ter, long seed,
                     InputSource inputSource, boolean load, boolean replay) {
        this.SEED = seed;
        this.RANDOM = new Random(SEED);
        this.load = load;
        this.replay = replay;
        floorTiles = new ArrayList<>();
        theVanished = new ArrayList<>();
        current = LocalDateTime.now();
        this.ter = ter;
        ter.initialize(Constants.MAPWIDTH, Constants.MAPHEIGHT + 3);
        world = new TETile[Constants.MAPWIDTH][Constants.MAPHEIGHT];
        fillWithEmptyTiles();
        typed = "";
        gameEnd = false;
        isAlive = true;

        //this.inputSource = inputSource;

        startWorld(inputSource);
        ter.renderFrame(world);
    }

    /** starts world by randomly drawing first room
    private void startWorld() {
        int width = getRandomWidth();
        int height = getRandomHeight();
        Room start = new Room(getRandomPoint(), width, height);
        start.drawRoom(world);
        fillHoles(start);
    }
     */


    /** get world that was created */
    public TETile[][] getWorld() {
        return world;
    }

    /** fills world with emptyTiles */
    private void fillWithEmptyTiles() {
        int height = world[0].length;
        int width = world.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    /** connects Room's hole to hallway */
    private void fillHoles(Room room) {
        for (Hole h: room.holes) {
            //System.out.println(h.getX() + " " + h.getY());
            connectHoleToHallway(h);
        }


    }

    /** connects Hallway's hole to either hallway or room */
    private void fillHoles(Hallway hallway) {
        for (Hole h : hallway.holes) {
            int tileNum = RANDOM.nextInt(4);
            switch (tileNum) {
                case 0: connectHallwayToHallway(h);
                        break;
                case 1: connectHoleToRoom(h);
                        break;
                case 2: connectHoleToRoom(h);
                        break;
                default: connectHoleToRoom(h);

            }

        }
    }


    private void changetoFloor(Hole hole, Point p) {
        world[hole.getX()][hole.getY()] = Constants.FLOOR;
        world[p.getX()][p.getY()] = Constants.FLOOR;

        floorTiles.add(p);
        floorTiles.add(new Point(hole.getX(), hole.getY()));

    }


    private void connectHoleToRoom(Hole h) {
        if (h.getOrientation().equals("top")) {
            int width = getRandomWidth();
            int height = getRandomHeight();

            int offset = RandomUtils.uniform(RANDOM, h.getX() - (width - 2), h.getX());

            Point start = new Point(offset, h.getY() + 1);
            Room room = new Room(start, width, height);
            room.drawRoom(world, floorTiles);
            if (room.drawn) {
                Point p = new Point(h.getX(), h.getY() + 1);
                changetoFloor(h, p);
                fillHoles(room);
            }
        } else if (h.getOrientation().equals("bot")) {
            int width = getRandomWidth();
            int height = getRandomHeight();

            int offset = RandomUtils.uniform(RANDOM, h.getX() - (width - 2), h.getX());

            Point start = new Point(offset, h.getY() - height);
            Room room = new Room(start, width, height);
            room.drawRoom(world, floorTiles);
            if (room.drawn) {
                Point p = new Point(h.getX(), h.getY() - 1);
                changetoFloor(h, p);
                fillHoles(room);
            }
        } else if (h.getOrientation().equals("left")) {
            int width = getRandomWidth();
            int height = getRandomHeight();

            int offset = RandomUtils.uniform(RANDOM, h.getY() - (height - 2), h.getY());
            Point start = new Point(h.getX() - width, offset);

            Room room = new Room(start, width, height);
            room.drawRoom(world, floorTiles);

            if (room.drawn) {
                Point p = new Point(h.getX() - 1, h.getY());
                changetoFloor(h, p);
                fillHoles(room);
            }
        } else if (h.getOrientation().equals("right")) {
            int width = getRandomWidth();
            int height = getRandomHeight();

            int offset = RandomUtils.uniform(RANDOM, h.getY() - (height - 2), h.getY());
            Point start = new Point(h.getX() + 1, offset);

            Room room = new Room(start, width, height);
            room.drawRoom(world, floorTiles);

            if (room.drawn) {
                Point p = new Point(h.getX() + 1, h.getY());
                changetoFloor(h, p);
                fillHoles(room);
            }

        }
    }





    private void connectHoleToHallway(Hole h) {
        if (h.getOrientation().equals("top")) {
            int width = 3;
            int height = getRandomDimension();
            Point start = new Point(h.getX() - 1, h.getY() + 1);
            Hallway hallway = new Hallway(start, width, height);
            hallway.drawRoom(world, floorTiles);
            if (hallway.drawn) {

                Point p = new Point(h.getX(), h.getY() + 1);
                changetoFloor(h, p);

                fillHoles(hallway);
            }

        } else if (h.getOrientation().equals("bot")) {
            int width = 3;
            int height = getRandomDimension();
            Point start = new Point(h.getX() - 1, h.getY() - height);
            Hallway hallway = new Hallway(start, width, height);
            hallway.drawRoom(world, floorTiles);
            if (hallway.drawn) {
                Point p = new Point(h.getX(), h.getY() - 1);
                changetoFloor(h, p);
                fillHoles(hallway);
            }

        } else if (h.getOrientation().equals("left")) {
            int width = getRandomDimension();
            int height = 3;
            Point start = new Point(h.getX() - width, h.getY() - 1);

            Hallway hallway = new Hallway(start, width, height);
            hallway.drawRoom(world, floorTiles);
            if (hallway.drawn) {
                Point p = new Point(h.getX() - 1, h.getY());
                changetoFloor(h, p);
                fillHoles(hallway);
            }

        } else if (h.getOrientation().equals("right")) {
            int width = getRandomDimension();
            int height = 3;
            Point start = new Point(h.getX() + 1, h.getY() - 1);

            Hallway hallway = new Hallway(start, width, height);
            hallway.drawRoom(world, floorTiles);
            if (hallway.drawn) {
                Point p = new Point(h.getX() + 1, h.getY());
                changetoFloor(h, p);
                fillHoles(hallway);
            }
        }
    }

    private void connectHallwayToHallway(Hole h) {
        if (h.getOrientation().equals("top")) {
            int width = getRandomDimension();
            int height = 3;

            int offset = RandomUtils.uniform(RANDOM, h.getX() - (width - 2), h.getX());

            Point start = new Point(offset, h.getY() + 1);
            Room room = new Room(start, width, height);
            room.drawRoom(world, floorTiles);
            if (room.drawn) {
                Point p = new Point(h.getX(), h.getY() + 1);
                changetoFloor(h, p);
                fillHoles(room);
            }
        } else if (h.getOrientation().equals("bot")) {
            int width = getRandomDimension();
            int height = 3;

            int offset = RandomUtils.uniform(RANDOM, h.getX() - (width - 2), h.getX());

            Point start = new Point(offset, h.getY() - height);
            Room room = new Room(start, width, height);
            room.drawRoom(world, floorTiles);
            if (room.drawn) {
                Point p = new Point(h.getX(), h.getY() - 1);
                changetoFloor(h, p);

                fillHoles(room);
            }
        } else if (h.getOrientation().equals("left")) {
            int width = 3;
            int height = getRandomDimension();

            int offset = RandomUtils.uniform(RANDOM, h.getY() - (height - 2), h.getY());
            Point start = new Point(h.getX() - width, offset);

            Room room = new Room(start, width, height);
            room.drawRoom(world, floorTiles);

            if (room.drawn) {
                Point p = new Point(h.getX() - 1, h.getY());
                changetoFloor(h, p);
                fillHoles(room);
            }
        } else if (h.getOrientation().equals("right")) {
            int width = 3;
            int height = getRandomDimension();

            int offset = RandomUtils.uniform(RANDOM, h.getY() - (height - 2), h.getY());
            Point start = new Point(h.getX() + 1, offset);

            Room room = new Room(start, width, height);
            room.drawRoom(world, floorTiles);

            if (room.drawn) {
                Point p = new Point(h.getX() + 1, h.getY());
                changetoFloor(h, p);

                fillHoles(room);
            }

        }
    }

    private int getRandomWidth() {
        int randomWidth = RandomUtils.uniform(RANDOM, 4, Constants.MAXROOMWIDTH + 1);
        //System.out.println(randomWidth);
        return randomWidth;
    }

    private int getRandomHeight() {
        int randomHeight = RandomUtils.uniform(RANDOM, 4, Constants.MAXROOMHEIGHT + 1);
        //System.out.println(randomHeight);
        return randomHeight;
    }

    private int getRandomDimension() {
        int randomDimension = RandomUtils.uniform(RANDOM, 4, Constants.MAXHALLWAYDIMENSION + 1);
        return randomDimension;
    }

    private Point getRandomPoint() {
        int x = RandomUtils.uniform(RANDOM, 0, Constants.MAPWIDTH - Constants.MAXROOMWIDTH);
        int y = RandomUtils.uniform(RANDOM, 0, Constants.MAPHEIGHT - Constants.MAXROOMHEIGHT);
        return new Point(x, y);
    }

    /** ERIC's STUFF **/

    private Point getRandomRoomPoint(Room room) {
        int floorWidth = room.getWidth() - 2;
        int floorHeight = room.getHeight() - 2;
        Point start = room.getStart();
        Point floorStart = new Point(start.getX() + 1, start.getY() + 1);
        int x = RandomUtils.uniform(RANDOM, floorStart.getX(), floorStart.getX() + floorWidth);
        int y = RandomUtils.uniform(RANDOM, floorStart.getY(), floorStart.getY() + floorHeight);
        return new Point(x, y);
    }

    /**
    public void startGame() {
        boolean gameEnd = false;
        while (!gameEnd) {
            if (StdDraw.hasNextKeyTyped()) {
                String key = Character.toString(StdDraw.nextKeyTyped());
                if (key.equals(Constants.WEST)) {
                    user.movePlayer(Constants.WEST);
                    ter.renderFrame(world);
                } else if (key.equals(Constants.EAST)) {
                    user.movePlayer(Constants.EAST);
                    ter.renderFrame(world);
                } else if (key.equals(Constants.NORTH)) {
                    user.movePlayer(Constants.NORTH);
                    ter.renderFrame(world);
                } else if (key.equals(Constants.SOUTH)) {
                    user.movePlayer(Constants.SOUTH);
                    ter.renderFrame(world);
                } else if (key.equals("q")) {
                    System.out.println("The End Game");
                    gameEnd = true;
                }
            }
        }
    }
**/
    public void startGameAllInputs(InputSource inputSource) {

        if (inputSource.getClass().equals(KeyboardInputSource.class)) {
            ter.renderFrame(world);
        }

        String previousKey = "";

        double lastX = Constants.MAPWIDTH * 0.5;
        double lastY = Constants.MAPHEIGHT * 0.5;

        while (!gameEnd) {


            if (inputSource.possibleNextInput()) {
                String key = Character.toString(inputSource.getNextKey());
                boolean shouldRender =
                        inputSource.getClass().equals(KeyboardInputSource.class) || replay;
                if (shouldRender && (StdDraw.mouseX() != lastX || StdDraw.mouseY() != lastY))  {
                    renderChoices(replay, shouldRender);
                    lastX = StdDraw.mouseX();
                    lastY = StdDraw.mouseY();
                }
                if (key.equals(Constants.WEST)) {
                    user.movePlayer(Constants.WEST);
                    renderChoices(replay, shouldRender);
                    typed = typed + Constants.WEST;
                } else if (key.equals(Constants.EAST)) {
                    user.movePlayer(Constants.EAST);
                    renderChoices(replay, shouldRender);
                    typed = typed + Constants.EAST;
                } else if (key.equals(Constants.NORTH)) {
                    user.movePlayer(Constants.NORTH);
                    renderChoices(replay, shouldRender);
                    typed = typed + Constants.NORTH;
                } else if (key.equals(Constants.SOUTH)) {
                    user.movePlayer(Constants.SOUTH);
                    renderChoices(replay, shouldRender);
                    typed = typed + Constants.SOUTH;
                } else if (previousKey.equals(":") && key.equals("Q")) {
                    //ends and saves game for interact with Keyboard
                    saveGame(typed);
                    gameEnd = true;
                    System.exit(0);
                } else if (key.equals("T") && user.numStones == 6) {
                    deleteHalf(shouldRender);
                    renderChoices(replay, shouldRender);
                    typed = typed + "T";
                } else if (user.fellOff) {

                    fellOff();
                } else if (user.hasPowerStone && key.equals("P")) {
                    world[user.p.getX()-1][user.p.getY()] = Tileset.SAND;
                    renderChoices(replay, shouldRender);
                    typed = typed + "P";
                }

                if (!key.equals("+")) {
                    previousKey = key;
                }

            } else if (load) { //hands off control to keyboard if this game was loaded

                ter.renderFrame(world);


                startGameAllInputs(new KeyboardInputSource());

            } else {
                gameEnd = true;
            }

        }


    }

    public void fellOff() {

        user.changeAvatar(Constants.FLOOR);
        ter.renderFrame(world);

        gameEnd = true;
        drawEndHUD();
    }

    public void deleteHalf(boolean shouldRender) {
        for (Point p: floorTiles) {
            int zeroOrOne = RandomUtils.uniform(RANDOM, 0, 2);

            if (zeroOrOne == 0) {

                if (p.getX() == user.p.getX() && p.getY() == user.p.getY()) { //p.equals(user.p)
                    System.out.println("YOU HAVE VANISHED");
                    gameEnd = true;
                    isAlive = false;
                }

                world[p.getX()][p.getY()] = Tileset.SAND;
                if (shouldRender) {
                    ter.renderFrame(world);
                    StdDraw.pause(5);
                }


                theVanished.add(p);
            }

        }

        StdDraw.pause(100);

        for (Point v: theVanished) {
            world[v.getX()][v.getY()] = Tileset.NOTHING;
        }

        if (shouldRender) {
            ter.renderFrame(world);
        }
        if (!gameEnd) {
            System.out.println("You survived good job. You win");
            gameEnd = true;
        }

        drawEndHUD();

    }


    /** renders each movement pausing if is a replay*/
    private void renderChoices(boolean replayPause, boolean shouldRender) {
        if (replayPause) {
            StdDraw.pause(100);
        }
        if (shouldRender) {
            ter.renderFrame(world);
            drawCurrentHUD();
        }
    }

    /** draws current HUD */
    private void drawCurrentHUD() {
        String canSnap = "No";
        String tile = "";
        current = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String fdt = current.format(formatter);
        if (user.numStones == 6) {
            canSnap = "Yes, press T for Thanos";
        }
        int mouseX = (int) Math.round(StdDraw.mouseX());
        int mouseY = (int) Math.round(StdDraw.mouseY());
        if (checkMouseInBounds(mouseX, mouseY)) {
            TETile mouseTile = world[mouseX][mouseY];
            tile = getTileString(mouseTile);
        }
        StdDraw.setPenColor(Color.white);
        StdDraw.line(0, Constants.MAPHEIGHT + 1, Constants.MAPWIDTH, Constants.MAPHEIGHT + 1);
        StdDraw.textLeft(1, Constants.MAPHEIGHT + 2, "Tile: " + tile);
        StdDraw.textLeft(Constants.MAPWIDTH * 0.25, Constants.MAPHEIGHT + 2,
                "Number of Stones: " + user.numStones);
        StdDraw.textLeft(Constants.MAPWIDTH * 0.5, Constants.MAPHEIGHT + 2,
                "Can Snap: " + canSnap);
        if (!user.hasTimeStone) {
            StdDraw.textLeft(Constants.MAPWIDTH * 0.75, Constants.MAPHEIGHT + 2,
                    "Date & Time: " + fdt);
        } else {
            StdDraw.textLeft(Constants.MAPWIDTH * 0.75, Constants.MAPHEIGHT + 2,
                    "Date & Time: I CONTROL TIME");

        }
        StdDraw.show();
    }

    /** draw end result HUD */
    private void drawEndHUD() {
        StdDraw.setPenColor(Color.white);
        StdDraw.line(0, Constants.MAPHEIGHT + 1, Constants.MAPWIDTH, Constants.MAPHEIGHT + 1);

        if (user.fellOff) {
            StdDraw.textLeft(1, Constants.MAPHEIGHT + 2,
                    "You fell into the nothing. You lose.");
        }
        else if (isAlive) {
            StdDraw.textLeft(1, Constants.MAPHEIGHT + 2,
                    "You have survived the snap! Good job you win!");
        } else {
            StdDraw.textLeft(1, Constants.MAPHEIGHT + 2, "You have vanished to dust! You lost...");
        }
        StdDraw.show();
        StdDraw.pause(10000);
    }

    /** checks if mouse input is in bounds of world */
    private boolean checkMouseInBounds(int x, int y) {
        return !(x >= Constants.MAPWIDTH || y >= Constants.MAPHEIGHT || x < 0 || y < 0);
    }

    /** gets tile string for HUD */
    private String getTileString(TETile tile) {
        if (tile.equals(Constants.WALL)) {
            return "Wall";
        }
        if (tile.equals(Constants.FLOOR)) {
            return "Floor";
        }
        if (tile.equals(Constants.NOTHING)) {
            return "Nothing";
        }
        if (user.infinityStones.contains(tile)) {
            return tile.description();
        }
        if (tile.equals(Constants.USER)) {
            return user.name;
        }
        return "";
    }

    /** starts world by randomly drawing first room */
    private void startWorld(InputSource inputSource) {
        int width = getRandomWidth();
        int height = getRandomHeight();
        Room start = new Room(getRandomPoint(), width, height);
        start.drawRoom(world, floorTiles);
        user = new Player(world, Tileset.AVATAR, getRandomRoomPoint(start));
        fillHoles(start);
        placeInfinityStones();
        startGameAllInputs(inputSource);
    }

    /** places infinty stones randomly throughout the floors */
    private void placeInfinityStones() {
        for (int i = 0; i < 6; i++) {
            int rand = RANDOM.nextInt(floorTiles.size());
            Point p = floorTiles.get(rand);
            world[p.getX()][p.getY()] = user.infinityStones.get(i);
        }
    }

    /** saves game */
    private void saveGame(String typedString) {
        String text = "N" + SEED + typedString;
        try {
            saveString(text);
        } catch (IOException e) {
            System.out.println("Can't find the file.");
        }

    }






    /**

    static String readSaveFile() throws IOException {
        try (BufferedReader br =
                     new BufferedReader(new FileReader("byow/Core/save.txt"))) {
            return br.readLine();

        }
    }
**/

    static void saveString(String text) throws IOException {
        //System.out.println(text);
        //System.out.println("ahh");

        try (PrintWriter out = new PrintWriter("save.txt")) {
            out.println(text);

        }

    }

    public static void main(String[] args) {
        //CreateMap map = new CreateMap(20339232);
        //map.startGame();
        /**

        String text = "N534234123S";

        try {
            saveString(text);
        } catch (IOException e) {
            System.out.println("Can't find the file.");
        }


        try {
            System.out.println(readSaveFile());
        } catch (Exception e) {
            System.out.println("IDK");
        }

        **/






    }


}
