package game2048logic;

import game2048rendering.Side;
import static game2048logic.MatrixUtils.rotateLeft;
import static game2048logic.MatrixUtils.rotateRight;

/**
 * @author  Josh Hug
 */
public class GameLogic {
    /** Moves the given tile up as far as possible, subject to the minR constraint.
     *
     * @param board the current state of the board
     * @param r     the row number of the tile to move up
     * @param c -   the column number of the tile to move up
     * @param minR  the minimum row number that the tile can land in, e.g.
     *              if minR is 2, the moving tile should move no higher than row 2.
     * @return      if there is a merge, returns the 1 + the row number where the merge occurred.
     *              if no merge occurs, then return 0.
     */
    public static int moveTileUpAsFarAsPossible(int[][] board, int r, int c, int minR) {
        if (r == 0 || r == minR || board[r][c] == 0) {
            return 0;
        } else if (board[r - 1][c] == 0) {
            // empty spot
            board[r - 1][c] = board[r][c];
            board[r][c] = 0;
        } else if (board[r - 1][c] == board[r][c]) {
            // merge logic
            board[r - 1][c] *= 2;
            board[r][c] = 0;
            return r;
        }
        return moveTileUpAsFarAsPossible(board, r - 1, c, minR);
    }

    /**
     * Modifies the board to simulate the process of tilting column c
     * upwards.
     *
     * @param board     the current state of the board
     * @param c         the column to tilt up.
     */
    public static void tiltColumn(int[][] board, int c) {
        int minR = 0;
        for (int r = 0; r < board.length; r++) {
            if (board[r][c] != 0) {
                minR = moveTileUpAsFarAsPossible(board, r, c, minR);
            }
        }
    }

    /**
     * Modifies the board to simulate tilting all columns upwards.
     *
     * @param board     the current state of the board.
     */
    public static void tiltUp(int[][] board) {
        for (int c = 0; c < board.length; c++) {
            tiltColumn(board, c);
        }
    }

    /**
     * Modifies the board to simulate tilting the entire board to
     * the given side.
     *
     * @param board the current state of the board
     * @param side  the direction to tilt
     */
    public static void tilt(int[][] board, Side side) {
        if (side == Side.EAST) {
            // right
            rotateLeft(board);
            tiltUp(board);
            rotateRight(board);
        } else if (side == Side.WEST) {
            // left
            rotateRight(board);
            tiltUp(board);
            rotateLeft(board);
        } else if (side == Side.SOUTH) {
            // down
            rotateRight(board);
            rotateRight(board);
            tiltUp(board);
            rotateLeft(board);
            rotateLeft(board);
        } else {
            tiltUp(board);
        }
    }
}
