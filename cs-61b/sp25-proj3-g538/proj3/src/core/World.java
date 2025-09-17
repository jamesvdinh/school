package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TETile;
import tileengine.Tileset;
import utils.RandomUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class World {
    // build your own world!
    private int width;
    private int height;
    private TETile[][] world;
    private Random random;

    public World(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        this.random = new Random(seed);
        world = new TETile[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    private class Position {
        int x, y;
        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private boolean addRoom(int x, int y, int w, int h) {
        // this checks if the room is outside the world
        if (x + w >= width - 1 || y + h >= height - 1) {
            return false;
        }

        // check overlap
        for (int i = x - 1; i < x + w; i++) {
            for (int j = y - 1; j <= y + h; j++) {
                if (i < 0 || i >= width || j < 0 || j >= height) {
                    continue; // skip out-of-bounds
                }
                if (world[i][j] != Tileset.NOTHING) {
                    return false;
                }
            }
        }

        // draw floor
        for (int i = x + 1; i < x + w - 1; i++) {
            for (int j = y + 1; j < y + h - 1; j++) {
                world[i][j] = Tileset.FLOOR;
            }
        }

        // draw walls
        for (int i = x; i < x + w; i++) {
            world[i][y] = Tileset.WALL;
            world[i][y + h - 1] = Tileset.WALL;
        }
        for (int j = y; j < y + h; j++) {
            world[x][j] = Tileset.WALL;
            world[x + w - 1][j] = Tileset.WALL;
        }

        return true;
    }

    public void generateRooms(int numRooms) {
        Position prevRoom = null;
        for (int i = 0; i < numRooms; i++) {
            int w = RandomUtils.uniform(random, 5, 9);
            int h = RandomUtils.uniform(random, 5, 9);
            int x = RandomUtils.uniform(random, 1, width - w - 1); // keeps the x coord from being out of bounds
            int y = RandomUtils.uniform(random, 1, height - h - 1); // keeps the y coord from being out of bounds

            boolean added = addRoom(x, y, w, h);
            if (!added) {
                continue;
            }

            int centerX = x + w / 2;
            int centerY = y + h / 2;
            Position currRoom = new Position(centerX, centerY);

            if (prevRoom != null) {
                connectRooms(prevRoom.x, prevRoom.y, currRoom.x, currRoom.y);
            }

            prevRoom = currRoom;
        }
    }

    private void connectRooms(int x1, int y1, int x2, int y2) {
        boolean horizFirst = random.nextBoolean();
        if (horizFirst) {
            addHallway(x1, y1, x2, y1);
            addHallway(x2, y1, x2, y2);
        } else {
            addHallway(x1, y1, x1, y2);
            addHallway(x1, y2, x2, y2);
        }
    }

    // draw hallway
    private void addHallway(int x1, int y1, int x2, int y2) {
        // vertical shaft
        if (x1 == x2) {
            int lower = Math.min(y1, y2);
            int upper = Math.max(y1, y2);
            for (int y = lower; y <= upper; y++) {
                if (world[x1][y] == Tileset.WALL || world[x1][y] == Tileset.NOTHING) {
                    world[x1][y] = Tileset.FLOOR;
                    addHallwayWalls(x1, y);
                }
            }
        } else if (y1 == y2) { // horizontal shaft
            int lower = Math.min(x1, x2);
            int upper = Math.max(x1, x2);
            for (int x = lower; x <= upper; x++) {
                if (world[x][y1] == Tileset.WALL || world[x][y1] == Tileset.NOTHING) {
                    world[x][y1] = Tileset.FLOOR;
                    addHallwayWalls(x, y1);
                }
            }
        }
    }

    // helper function for hallway walls
    private void addHallwayWalls(int centerX, int centerY) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                int nx = centerX + x;
                int ny = centerY + y;
                if (nx >= 0 && ny >= 0 && nx < width && ny < height) {
                    TETile tile = world[nx][ny];
                    // Only place a wall if it's NOTHING — do not override FLOOR or WALL
                    if (tile == Tileset.NOTHING) {
                        world[nx][ny] = Tileset.WALL;
                    }
                }
            }
        }
    }

    public String serialize() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                sb.append(world[x][y].id());
                sb.append(",");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static World deserialize(String data) {
        String[] rows = data.split("\n");
        int width = rows[0].split(",").length - 1; // trailing comma
        int height = rows.length;

        World newWorld = new World(width, height, 0); // Seed irrelevant

        for (int y = 0; y < height; y++) {
            String[] ids = rows[y].split(",");
            for (int x = 0; x < width; x++) {
                if (!ids[x].isEmpty()) { // in case of extra commas
                    int id = Integer.parseInt(ids[x].trim()); // <--- TRIM HERE
                    newWorld.world[x][y] = idToTile(id);
                }
            }
        }
        return newWorld;
    }

    private static final Map<Integer, TETile> idToTileMap = new HashMap<>();

    static {
        idToTileMap.put(0, Tileset.AVATAR);
        idToTileMap.put(1, Tileset.WALL);
        idToTileMap.put(2, Tileset.FLOOR);
        idToTileMap.put(3, Tileset.NOTHING);
        idToTileMap.put(4, Tileset.GRASS);
        idToTileMap.put(5, Tileset.WATER);
        idToTileMap.put(6, Tileset.FLOWER);
        idToTileMap.put(7, Tileset.LOCKED_DOOR);
        idToTileMap.put(8, Tileset.UNLOCKED_DOOR);
        idToTileMap.put(9, Tileset.SAND);
        idToTileMap.put(10, Tileset.MOUNTAIN);
        idToTileMap.put(11, Tileset.TREE);
        idToTileMap.put(12, Tileset.CELL);
        idToTileMap.put(13, new TETile('$', StdDraw.YELLOW, StdDraw.BLACK, "coin", 13)); // <-- ADD THIS
    }

    private static TETile idToTile(int id) {
        if (!idToTileMap.containsKey(id)) {
            throw new IllegalArgumentException("Invalid tile id: " + id);
        }
        return idToTileMap.get(id);
    }

    // world getter for render purposes
    public TETile[][] getWorld() {
        return world;
    }
}
