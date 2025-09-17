package core;

import tileengine.TERenderer;
import tileengine.TETile;
import edu.princeton.cs.algs4.StdDraw;
import tileengine.Tileset;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 40;
    private static int avatarX;
    private static int avatarY;
    private static World world;
    private static final TERenderer ter = new TERenderer();

    private static boolean LOSEnabled = false;
    private static final int LOS_RADIUS = 5;

    private static int coinsRemaining = 0;
    private static final Random coinRandom = new Random(); // You can seed this for determinism if needed

    public static void main(String[] args) {
        // build your own world!
        displayMainMenu();
        handleMainMenu();
    }

    private static void displayMainMenu() {
        // creates the menu
        StdDraw.setCanvasSize(WIDTH * 10, HEIGHT * 10);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.enableDoubleBuffering();

        // displays the menu
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(WIDTH / 2.0, HEIGHT * 0.75, "CS 61B: BYOW");
        StdDraw.text(WIDTH / 2.0, HEIGHT * 0.5, "(N) New Game");
        StdDraw.text(WIDTH / 2.0, HEIGHT * 0.4, "(L) Load Game");
        StdDraw.text(WIDTH / 2.0, HEIGHT * 0.3, "(Q) Quit");
        StdDraw.show();
    }

    private static void handleMainMenu() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (key == 'n') {
                    startNewGame();
                } else if (key == 'l') {
                    loadGame();
                } else if (key == 'q') {
                    System.exit(0);
                }
            }
        }
    }

    private static void startNewGame() {
        StringBuilder seedBuilder = initializeSeedMenu();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (key == 's' || key == 'S') {
                    long seed = Long.parseLong(seedBuilder.toString());
                    generateWorld(seed);
                    break;
                } else if (Character.isDigit(key)) {
                    seedBuilder.append(key);
                    createSeedMenu(seedBuilder.toString());
                }
            }
        }
    }

    private static StringBuilder initializeSeedMenu() {
        StdDraw.clear(StdDraw.BLACK);
        StringBuilder seedBuilder = new StringBuilder();
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(WIDTH / 2.0, HEIGHT * 0.75, "CS 61B: BYOW");
        StdDraw.text(WIDTH / 2.0, HEIGHT / 2.0, "Enter Seed followed by S: ");
        StdDraw.show();
        return seedBuilder;
    }

    private static void createSeedMenu(String lastSeed) {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(WIDTH / 2.0, HEIGHT * 0.75, "CS 61B: BYOW");
        StdDraw.text(WIDTH / 2.0, HEIGHT / 2.0, "Enter Seed followed by S: " );
        StdDraw.setPenColor(StdDraw.YELLOW);
        StdDraw.text(WIDTH / 2.0, HEIGHT / 3.0, lastSeed);
        StdDraw.show();
    }

    private static void generateWorld(long seed) {
        int numRooms = 8;

        world = new World(WIDTH, HEIGHT, seed);
        world.generateRooms(numRooms);

        TETile[][] grid = world.getWorld();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(grid);

        boolean avatarPlaced = false;
        for (int x = 0; x < grid.length && !avatarPlaced; x++) {
            for (int y = 0; y < grid[0].length && !avatarPlaced; y++) {
                if (grid[x][y].equals(Tileset.FLOOR)) {
                    avatarX = x;
                    avatarY = y;
                    grid[avatarX][avatarY] = Tileset.AVATAR;
                    avatarPlaced = true;
                }
            }
        }
        // secondary ambition feature
        placeCoins();
        ter.renderFrame(getVisibleWorld());
        gameLoop();
    }

    private static boolean colonPressed = false;

    private static void gameLoop() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = Character.toLowerCase(StdDraw.nextKeyTyped());

                if (colonPressed) {
                    if (key == 'q') {
                        saveGame();
                        System.exit(0);
                    } else {
                        colonPressed = false;
                    }
                }
                else {
                    if (key == ':') {
                        colonPressed = true;
                    } else if (key == 'w') {
                        moveAvatar(0, 1);
                    } else if (key == 'a') {
                        moveAvatar(-1, 0);
                    } else if (key == 's') {
                        moveAvatar(0, -1);
                    } else if (key == 'd') {
                        moveAvatar(1, 0);
                    } else if (key == 'l') {
                        LOSEnabled = !LOSEnabled;
                        ter.renderFrame(getVisibleWorld());
                    }
                }
                ter.renderFrame(getVisibleWorld());
            }
            displayHUD();
        }
    }

    private static TETile[][] getVisibleWorld() {
        TETile[][] fullWorld = world.getWorld();
        int width = fullWorld.length;
        int height = fullWorld[0].length;

        TETile[][] visibleWorld = new TETile[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (!LOSEnabled || inLineOfSight(x, y)) {
                    visibleWorld[x][y] = fullWorld[x][y];
                } else {
                    visibleWorld[x][y] = Tileset.NOTHING;
                }
            }
        }
        return visibleWorld;
    }

    private static boolean inLineOfSight(int x, int y) {
        int dx = x - avatarX;
        int dy = y - avatarY;
        double distance = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        return (int) distance <= LOS_RADIUS;
    }

    private static void placeCoins() {
        TETile[][] tiles = world.getWorld();
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[0].length; y++) {
                if (tiles[x][y].equals(Tileset.FLOOR) && coinRandom.nextDouble() < 0.05) { // 5% chance
                    tiles[x][y] = new TETile('$', StdDraw.YELLOW, StdDraw.BLACK, "coin", 13); // coin tile
                    coinsRemaining++;
                }
            }
        }
    }


    private static void saveGame() {
        try {
            FileWriter fw = new FileWriter("save.txt");

            fw.write(world.serialize());
            fw.write("\n");
            fw.write(avatarX + " " + avatarY + "\n"); // avatar position

            fw.close();
        } catch (IOException e) {
            System.out.println("Failed to save game.");
            e.printStackTrace();
        }
    }

    private static void loadGame() {
        try {
            List<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();

            if (lines.size() < 2) {
                throw new IOException("Save file corrupted or incomplete.");
            }

            // Avatar data is LAST line
            String avatarData = lines.remove(lines.size() - 1);

            // Rest are world data
            StringBuilder worldData = new StringBuilder();
            for (String worldLine : lines) {
                worldData.append(worldLine).append("\n");
            }

            world = core.World.deserialize(worldData.toString());

            String[] avatarParts = avatarData.split(" ");
            avatarX = Integer.parseInt(avatarParts[0]);
            avatarY = Integer.parseInt(avatarParts[1]);

            recountCoins();

            ter.initialize(80, 40);
            ter.renderFrame(getVisibleWorld());
            gameLoop();
        } catch (IOException e) {
            System.out.println("Failed to load the game.");
            e.printStackTrace();
        }
    }

    private static void recountCoins() {
        coinsRemaining = 0;
        TETile[][] tiles = world.getWorld();
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[0].length; y++) {
                if (tiles[x][y].description().equals("coin")) {
                    coinsRemaining++;
                }
            }
        }
    }


    private static void displayHUD() {
        double mouseX = StdDraw.mouseX();
        double mouseY = StdDraw.mouseY();

        int x = (int) mouseX;
        int y = (int) mouseY;
        TETile[][] tiles = world.getWorld();

        // Clear an area for the HUD
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledRectangle(WIDTH / 2.0, HEIGHT - 0.5, WIDTH / 2.0, 0.5);

        // HUD Text
        StdDraw.setPenColor(StdDraw.WHITE);
        String tileInfo;
        if (x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length) {
            tileInfo = "Tile: " + tiles[x][y].description() + " (" + x + ", " + y + ")";
        } else {
            tileInfo = "Tile: (out of bounds)";
        }
        StdDraw.textLeft(1, HEIGHT - 0.5, tileInfo);

        // Coins remaining
        StdDraw.textRight(WIDTH - 1, HEIGHT - 0.5, "Coins Left: " + coinsRemaining);

        StdDraw.show();
    }


    private static void moveAvatar(int dx, int dy) {
        int newX = avatarX + dx;
        int newY = avatarY + dy;
        TETile[][] tiles = world.getWorld();

        // Check if new position is inside bounds
        if (newX >= 0 && newX < tiles.length && newY >= 0 && newY < tiles[0].length) {
            String tileDesc = tiles[newX][newY].description();

            if (tileDesc.equals("floor") || tileDesc.equals("coin")) {
                // If it's a coin, collect it
                if (tileDesc.equals("coin")) {
                    coinsRemaining--;
                    if (coinsRemaining == 0) {
                        victoryScreen();
                        return;
                    }
                }
                // Move avatar
                tiles[avatarX][avatarY] = Tileset.FLOOR; // Replace old spot with FLOOR
                avatarX = newX;
                avatarY = newY;
                tiles[avatarX][avatarY] = Tileset.AVATAR;
            }
            // if there's a wall or anything else, don't move
        }
    }

    private static void victoryScreen() {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.YELLOW);
        StdDraw.text(WIDTH / 2.0, HEIGHT / 2.0, "You collected all the coins!");
        StdDraw.text(WIDTH / 2.0, HEIGHT / 2.0 - 2, "Returning to Main Menu...");
        StdDraw.show();
        StdDraw.pause(3000); // Pause for 3 seconds

        displayMainMenu();
        handleMainMenu();
    }
}
