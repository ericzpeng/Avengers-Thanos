package byow.Core;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

public class Player {
    private TETile[][] world;
    Point p;
    private TETile character;
    List<TETile> infinityStones;
    int numStones;
    String name;
    HashSet<Point> hitWalls;
    boolean fellOff;
    boolean hasRealityStone;
    boolean hasTimeStone;
    boolean hasPowerStone;
    boolean hasSpaceStone;
    boolean hasMindStone;
    boolean changedAvatar = false;

    /** constructor initializes variables and places character at given Point on the world */
    public Player(TETile[][] world, TETile character, Point p) {
        this.world = world;
        this.p = p;
        this.character = character;
        this.numStones = 0;
        this.infinityStones = new ArrayList<>();
        this.hitWalls = new HashSet<>();
        this.name = "Thanos";
        this.fellOff = false;
        world[p.getX()][p.getY()] = character;
        addInfinityStones();
    }

    public void movePlayer(String direction) {
        Point neighbor = getNeighbor(direction);
        if (isWalkable(neighbor)) {
            swapTiles(p, neighbor);
            p = neighbor;
        }
        if (hasSpaceStone) {

            world[p.getX()][p.getY()] = Constants.FLOOR;
            p = CreateMap.floorTiles.get(500);
            world[p.getX()][p.getY()] = Tileset.AVATAR;
            hasSpaceStone = false;

        }

        if (hasMindStone) {
            world[p.getX()][p.getY()] = Tileset.AVATAR_MIND;

        }

    }

    /** changes player's name */
    public void changeName(String newName) {
        this.name = newName;
    }

    public void changeAvatar(TETile tile) {
        world[p.getX()][p.getY()] = tile;
    }

    /** gets Point of neighbor given the direction */
    private Point getNeighbor(String direction) {
        if (direction.equals(Constants.EAST)) {
            return new Point(p.getX() + 1, p.getY());
        } else if (direction.equals(Constants.WEST)) {
            return new Point(p.getX() - 1, p.getY());
        } else if (direction.equals(Constants.NORTH)) {
            return new Point(p.getX(), p.getY() + 1);
        } else if (direction.equals(Constants.SOUTH)) {
            return new Point(p.getX(), p.getY() - 1);
        }
        return p;
    }

    /** checks to see if point given is walkable */
    private boolean isWalkable(Point position) {

        if (world[position.getX()][position.getY()].equals(Constants.WALL)) {
            //System.out.println("wall");
            hitWalls.add(position);
            world[position.getX()][position.getY()] = Constants.WALL_HIT;
        } else if (world[position.getX()][position.getY()].equals(Constants.WALL_HIT) && !hasRealityStone) {
            world[position.getX()][position.getY()] = Constants.NOTHING;
            fellOff = true;
        } else if(world[position.getX()][position.getY()].equals(Constants.REALITY)) {
            hasRealityStone = true;
        } else if(world[position.getX()][position.getY()].equals(Constants.TIME)) {
            hasTimeStone = true;
        } else if (world[position.getX()][position.getY()].equals(Constants.SOUL)) {
            numStones++;
            world[position.getX()][position.getY()] = Tileset.AVATAR_SOUL;
        } else if (world[position.getX()][position.getY()].equals(Constants.POWER)) {
            hasPowerStone = true;
        } else if (world[position.getX()][position.getY()].equals(Constants.SPACE)) {
            hasSpaceStone = true;
        } else if (world[position.getX()][position.getY()].equals(Constants.MIND)) {
            hasMindStone = true;
        }


        if (hasRealityStone) {

            if (world[position.getX()][position.getY()].equals(Constants.NOTHING) && hasMindStone) {
                changeAvatar(Tileset.AVATAR_MIND_BLACK);
            } else if (world[position.getX()][position.getY()].equals(Constants.NOTHING) && !hasMindStone) {
                changeAvatar(Tileset.AVATAR_BLACK);
            } else {
                changeAvatar(Tileset.AVATAR);

            }
            return !world[position.getX()][position.getY()].equals(Constants.WALL); //reality stone
        } else {
            return !world[position.getX()][position.getY()].equals(Constants.WALL)
                    && !world[position.getX()][position.getY()].equals(Constants.WALL_HIT)
                    && !world[position.getX()][position.getY()].equals(Constants.NOTHING)
                    && !world[position.getX()][position.getY()].equals(Tileset.SAND);
        }
    }

    /** swaps tiles given user point and tile point */
    private void swapTiles(Point user, Point tile) {
        if (infinityStones.contains(world[tile.getX()][tile.getY()])) {
            TETile temp = world[user.getX()][user.getY()];
            world[user.getX()][user.getY()] = Constants.FLOOR;
            world[tile.getX()][tile.getY()] = temp;
            numStones += 1;
        } else {
            TETile temp = world[user.getX()][user.getY()];
            world[user.getX()][user.getY()] = world[tile.getX()][tile.getY()];
            world[tile.getX()][tile.getY()] = temp;
        }
    }

    /** adds all infinity stones into ArrayList */
    private void addInfinityStones() {
        infinityStones.add(Constants.SOUL);
        infinityStones.add(Constants.TIME);
        infinityStones.add(Constants.SPACE);
        infinityStones.add(Constants.MIND);
        infinityStones.add(Constants.REALITY);
        infinityStones.add(Constants.POWER);
    }




}
