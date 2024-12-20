/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
//Hala jabban
//2121221350
//hala.jabban@stu.fsm.edu.tr
package mazegame;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
/**
 *
 * 
 */
 
public class MazaGame {
        
    public static int playerX = 0;
    public static int playerY = 0;
    public static int playerMovesCount = 0;
    public static boolean playerCanThroughWall = false;
    public static boolean playerCanPassMine = false;
    public static boolean playerCanTeleport = false;
    public static boolean playerCanMoveCount = false;

    public Scanner scanner = new Scanner(System.in);
    public static int rounds = 0;

    public static char MOVE_COUNT_TREASURE = 'H';
    public static char MINE_PASSING_TREASURE = 'F';
    public static char TELEPORTATION_TREASURE = 'T';
    public static char WALL_PASSING_TREASURE = 'R';
    public static char MINE = '!';

    public static final String UP = "UP";
    public static final String DOWN = "DOWN";
    public static final String LEFT = "LEFT";
    public static final String RIGHT = "RIGHT";

    public static char[][] maze = {
            {'#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#', '.', '.', '.'},
            {'.', '.', '#', '.', '.', '.', '#', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '.', '.', '#', '#', '.'},
            {'.', '.', '#', '.', '.', '#', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '#', '.', '#', '.', '#', '.', '.', '#', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'#', '#', '#', '#', '.', '.', '#', '.', '.', '.', '.', '.', '.', '.', 'E'},
            {'.', '.', '#', '.', '.', '.', '#', '#', '.', '#', '#', '#', '#', '.', '.'},
            {'.', '#', '.', '.', '.', '.', '.', '.', '#', '.', '.', '.', '#', '.', '.'},
            {'.', '.', '.', '.', '#', '#', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '#', '.', '.', '.', '#', '.', '#', '.', '#', '.', '.', '.'},
            {'B', '.', '#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#', '.', '.', '#', '.'},
            {'.', '.', '.', '#', '.', '.', '#', '.', '#', '.', '#', '.', '.', '#', '#'}};


    public static void main(String[] args) {

        MazaGame mazeGame = new MazaGame();
        int[] startPosition = mazeGame.findStartPositionX();
        int[] endPosition = mazeGame.findEndPosition();

        playerX = startPosition[0];
        playerY = startPosition[1];
        

        mazeGame.distributeTreasures(MINE, 10);
        mazeGame.distributeTreasures(TELEPORTATION_TREASURE, 5);
        mazeGame.distributeTreasures(WALL_PASSING_TREASURE, 5);
        mazeGame.distributeTreasures(MOVE_COUNT_TREASURE, 5);
        mazeGame.distributeTreasures(MINE_PASSING_TREASURE, 5);

        mazeGame.scanner = new Scanner(System.in);
        mazeGame.printGameInfo();
        gameloop:
        while (true) {
            
            if (mazeGame.isReachedEnd(endPosition[0], endPosition[1])) {
                System.out.println("🥳🥳🥳You won!🥳🥳🥳");
                mazeGame.printPlayerInfo();
                break;
            }
            if (rounds == 5) {
                rounds = 0;
                mazeGame.rearrangeMazeTreasure(MINE);
                mazeGame.rearrangeMazeTreasure(TELEPORTATION_TREASURE);
                mazeGame.rearrangeMazeTreasure(WALL_PASSING_TREASURE);
                mazeGame.rearrangeMazeTreasure(MOVE_COUNT_TREASURE);
                mazeGame.rearrangeMazeTreasure(MINE_PASSING_TREASURE);
            }
            mazeGame.printMaze();
            System.out.print("Input: ");
            String move = mazeGame.scanner.nextLine();
            switch (move.toUpperCase()) {
                case "W":
                    mazeGame.move(UP);
                    break;
                case "S":
                    mazeGame.move(DOWN);
                    break;
                case "A":
                    mazeGame.move(LEFT);
                    break;
                case "D":
                    mazeGame.move(RIGHT);
                    break;
                case "T":
                    mazeGame.teleport();
                    break;
                case "M":
                    mazeGame.useMoveCountTreasure();
                    System.out.printf("Your moves count is now %d\n", playerMovesCount);
                    break;
                case "+":
                    System.out.println("Goodbye!");
                    break gameloop;
                default:
                    System.out.println("Invalid input!");
            }
            rounds++;
        }
    }


    public void move(String direction) {
        if (!canMove(direction)) {
            return;
        }
        char nextChar = getNextChar(direction);
        if (!isWall(direction)) {
            setNextChar(direction, '.');
        }

        switch (direction) {
            case UP:
                if (playerY == 0) return;
                playerY--;
                break;
            case DOWN:
                if (playerY == 14) return;
                playerY++;
                break;
            case LEFT:
                if (playerX == 0) return;
                playerX--;
                break;
            case RIGHT:
                if (playerX == 14) return;
                playerX++;
                break;
            default:
        }
       playerMovesCount++;

        System.out.println("your moves count is :"+playerMovesCount);
        System.out.println("You moved " + getDirectionText(direction) + "!");
        System.out.println("Your current position: (" + playerX + ", " + playerY + ")");
    }

    private boolean isReachedEnd(int endPositionX, int endPositionY) {
        return playerX == endPositionX && playerY == endPositionY;
    }
    
    
    
    public void rearrangeMazeTreasure(char treasure) {
        int treasureCount = getCharCount(treasure);
        removeCurrentTreasures(treasure);
        int[][] freePositions = getFreePositions();
        for (int i = 0; i < treasureCount; i++) {
            maze[freePositions[i][1]][freePositions[i][0]] = treasure;
        }
    }

    private void removeCurrentTreasures(char treasure) {
        int mazeY, mazeX;
        for (mazeY = 0; mazeY < maze.length; mazeY++) {
            for (mazeX = 0; mazeX < maze[mazeY].length; mazeX++) {
                if (maze[mazeY][mazeX] == treasure) {
                    maze[mazeY][mazeX] = '.';
                }
            }
        }
    }

    public void distributeTreasures(char treasure, int count) {
        int[][] freePositions = getFreePositions();
        for (int i = 0; i < count; i++) {
            maze[freePositions[i][1]][freePositions[i][0]] = treasure;
        }
    }

    private int[][] getFreePositions() {
        int mazeY, mazeX;
        int freePositions = getCharCount('.');
        int[][] freeIndexes = new int[freePositions][2];
        int index = 0;
        for (mazeY = 0; mazeY < maze.length; mazeY++) {
            for (mazeX = 0; mazeX < maze[mazeY].length; mazeX++) {
                if (maze[mazeY][mazeX] == '.') {
                    freeIndexes[index][0] = mazeX;
                    freeIndexes[index][1] = mazeY;
                    index++;
                }
            }
        }
        randomizePositions(freeIndexes);
        return freeIndexes;
    }

    private void randomizePositions(int[][] positions) {
        for (int i = 0; i < positions.length; i++) {
            int randomIndexToSwap = new Random().nextInt(positions.length);
            int[] temp = positions[randomIndexToSwap];
            positions[randomIndexToSwap] = positions[i];
            positions[i] = temp;
        }
    }

    private int getCharCount(char c) {
        int mazeY, mazeX;
        int count = 0;
        for (mazeY = 0; mazeY < maze.length; mazeY++) {
            for (mazeX = 0; mazeX < maze[mazeY].length; mazeX++) {
                if (maze[mazeY][mazeX] == c) {
                    count++;
                }
            }
        }
        return count;
    }

    public int[] findStartPositionX() {
        int mazeY, firstX = 0;
        for (mazeY = 0; mazeY < maze.length; mazeY++) {
            if (maze[mazeY][firstX] == 'B') {
                return new int[]{firstX, mazeY};
            }
        }
        return null;
    }

    public int[] findEndPosition() {
        int mazeY, lastX = maze[0].length - 1;
        for (mazeY = 0; mazeY < maze.length; mazeY++) {
            if (maze[mazeY][lastX] == 'E') {
                return new int[]{lastX, mazeY};
            }
        }
        return null;
    }


    public void teleport() {
        if (playerCanTeleport) {
            System.out.println("Enter x: ");
            int x = scanner.nextInt();
            System.out.println("Enter y: ");
            int y = scanner.nextInt();
            if (playerCanTeleport(x, y)) {
                playerX =   x;
                playerY = y;
                System.out.println("You teleported to (" + x + ", " + y + ")");
            } else {
                System.out.println("You can't teleport there!");
            }
        }
    }

    private boolean playerCanTeleport(int x, int y) {
        if (playerCanTeleport) {
            if (x < 0 || x > 14 || y < 0 || y > 14) {
                return false;
            }
            return maze[y][x] == '.';
        }
        return false;
    }

    private String getDirectionText(String direction) {
        return switch (direction) {
            case UP -> "up";
            case DOWN -> "down";
            case LEFT -> "left";
            case RIGHT -> "right";
            default -> "";
        };
    }

    private boolean canMove(String direction) {
        if (isWall(direction)) {
            if (playerCanThroughWall) {
                removeWall(direction);
                playerCanThroughWall = false;
                return true;
            }
            System.out.println("You can't move there! There is a wall.");
            return false;
        }

        if (isMine(direction)) {
            if (playerCanPassMine) {
                removeMine(direction);
                playerCanPassMine = false;
                return true;
            }
            playerMovesCount += 5;
            System.out.println("The mine exploded!!");
            System.out.println("Your moves count is now " + playerMovesCount);
            return false;
        }
        return true;
    }

    private void removeWall(String direction) {
        setNextChar(direction, '.');
    }

    private boolean isWall(String direction) {
        return getNextChar(direction) == '#';
    }

    private boolean isMine(String direction) {
        return getNextChar(direction) == '!';
    }

    private char getNextChar(String direction) {
        return switch (direction) {
            case UP -> (playerY == 0) ?
                    maze[playerY][playerX] :
                    maze[playerY - 1][playerX];
            case DOWN -> (playerY == 14) ?
                    maze[playerY][playerX] :
                    maze[playerY + 1][playerX];
            case LEFT -> (playerX == 0) ?
                    maze[playerY][playerX] :
                    maze[playerY][playerX - 1];
            case RIGHT -> (playerX == 14) ?
                    maze[playerY][playerX] :
                    maze[playerY][playerX + 1];
            default -> ' ';
        };
    }

    private void setNextChar(String direction, char c) {
        int nextX, nextY;
        switch (direction) {
            case UP:
                if (playerY == 0) return;
                nextX = playerX;
                nextY = playerY - 1;
                break;
            case DOWN:
                if (playerY == 14) return;
                nextX = playerX;
                nextY = playerY + 1;
                break;
            case LEFT:
                if (playerX == 0) return;
                nextX = playerX - 1;
                nextY = playerY;
                break;
            case RIGHT:
                if (playerX == 14) return;
                nextX = playerX + 1;
                nextY = playerY;
                break;
            default:
                return;
        }
        maze[nextY][nextX] = c;
    }

    private void removeMine(String direction) {
        setNextChar(direction, '.');
    }

    public void printMaze() {
        for (int mazeY = 0; mazeY < maze.length; mazeY++) {
            for (int mazeX = 0; mazeX < maze[mazeY].length; mazeX++) {
                printMazePoint(mazeX, mazeY);
            }
            System.out.println();
        }
    }
// إضافة متغيرات لعدد مرات استخدام البونصات
public static int moveCountTreasureUsage = 0;
public static int minePassingTreasureUsage = 0;
public static int teleportationTreasureUsage = 0;
public static int wallPassingTreasureUsage = 0;
public static int mineUsage = 0;

// في كل مرة يتم استخدام فيها البونص، زيادة العداد المناسب
// مثال لاستخدام بونص MOVE_COUNT_TREASURE
private void useMoveCountTreasure() {
    if (playerCanMoveCount) {
        playerMovesCount -= 2;
        if (playerMovesCount < 0) {
            playerMovesCount = 0;
        }
        playerCanMoveCount = false;
        
        // زيادة عداد استخدام البونص بنقطة
        moveCountTreasureUsage++;
    }
}



    private void printMazePoint(int x, int y) {
        if (isPlayerPosition(x, y)) {
            System.out.print("@\t");
        } else {
            System.out.print(maze[y][x] + "\t");
        }
    }

    private boolean isPlayerPosition(int x, int y) {
        return x == playerX && y == playerY;
    }

    public void printGameInfo() {
        System.out.println("******** Maze Game ********");
        printPlayerInfo();
        System.out.println("------ how to play ------");
        System.out.println("Press W, A, S, D to move, T to teleport, M to use move count treasure, + to exit");
        System.out.println("You have 4 treasures:");
        System.out.println(MOVE_COUNT_TREASURE + ": reduce your moves count by 2");
        System.out.println(MINE_PASSING_TREASURE + ": remove the mine in front of you");
        System.out.println(TELEPORTATION_TREASURE + ": teleport you to a position you choose");
        System.out.println(WALL_PASSING_TREASURE + ": let you pass through walls");
        System.out.println(MINE + ": adds 5 moves to your moves count");
        System.out.println("You can use each treasure once per game");
        System.out.println("********* Good luck! *********");
        
    }

    public void printPlayerInfo() {
        System.out.println("Your current position: (" + playerX + ", " + playerY + ")");
        System.out.println("Your moves count:  " + playerMovesCount);
    }
}

