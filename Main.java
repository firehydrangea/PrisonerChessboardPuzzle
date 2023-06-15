import java.lang.Math;
import java.util.Arrays;
//6/15/23
//trying to think through or solve the prisoner chessboard puzzle that 3blue1brown talks about
//I watched far enough into the video to get the idea of enocding the key's location in the array of heads and tails
//I didn't solve it, but I have some conclusions at the end of this program

class Main {
  public static void main(String[] args) {
    int[][] board = { { 0, 1, 0, 1, 0, 1, 0, 0 },
        { 1, 0, 0, 0, 1, 1, 1, 0 },
        { 0, 1, 1, 0, 1, 0, 0, 0 },
        { 0, 1, 1, 1, 0, 1, 0, 1 },
        { 0, 0, 0, 1, 0, 1, 1, 1 },
        { 1, 0, 1, 1, 0, 1, 1, 0 },
        { 0, 0, 0, 0, 1, 0, 0, 1 },
        { 0, 1, 0, 1, 1, 1, 0, 1 } };
    // this board is a random collection of 1s and 0s to represent heads and tails
    int[] rowtotals = new int[9]; // 0-7 will be totals of individual rows; 8 is overall total
    int[] coltotals = new int[9]; // 0-7 will be totals of individual cols; 8 is overall total
    int diff = 0;
    // diff will be the difference between the sum of the rows and the sum of the
    // cols
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if (board[i][j] == 1) {
          rowtotals[i] = rowtotals[i] + (int) Math.pow(2, 7 - j);
          rowtotals[8] = rowtotals[8] + (int) Math.pow(2, 7 - j);
          coltotals[j] = coltotals[j] + (int) Math.pow(2, 7 - i);
          coltotals[8] = coltotals[8] + (int) Math.pow(2, 7 - i);
        }
      }
    }
    System.out.println("rows: " + Arrays.toString(rowtotals));
    System.out.println("Cols: " + Arrays.toString(coltotals));
    diff = rowtotals[8] - coltotals[8];
    System.out.println("Row total minus column total: " + diff);
    System.out.println("Row total minus column total MOD 64: " + Math.floorMod(diff, 64));
    int[][] diffs = new int[8][8];
    // diffs[i][j] will house the diff mod 64 if you toggle board[i][j]
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        board[i][j] = toggle(board[i][j]);
        diffs[i][j] = diff - calcDiff(board);
        // diffs[i][j] = calcDiff(board);
        // diffs[i][j] = Math.floorMod(calcDiff(board), 64);
        // diffs[i][j] = calcDiff(board);
        // diffs[i][j] = (diffs[i][j]/64 + Math.floorMod(diffs[i][j], 64));

        board[i][j] = toggle(board[i][j]);
      }
    }
    System.out.println("total for each possible flip: ");
    for (int[] row : diffs) {
      System.out.println(Arrays.toString(row));
    }

  }

  static int calcDiff(int[][] board) {
    int diff = 0;
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if (board[i][j] == 1) {
          diff += (int) Math.pow(2, 7 - j);
          diff -= (int) Math.pow(2, 7 - i);
          // rowtotals[i]= rowtotals[i] + (int)Math.pow(2, 7-j);
          // rowtotals[8] = rowtotals[8] + (int)Math.pow(2, 7-j);
          // coltotals[j] = coltotals[j] + (int)Math.pow(2, 7-i);
          // coltotals[8] = coltotals[8] + (int)Math.pow(2, 7-i);
        }
      }
    }
    return diff;
  }

  static int toggle(int i) {
    if (i == 0)
      return 1;
    if (i == 1)
      return 0;
    return i;
  }
}
/*
 * Conclusions:
 * if you encode each row as an 8-bit binary number, and each column as an 8-bit
 * binary number, sum the 8 row numbers into a rows total and the 8 column
 * numbers into a columns total, and then do the difference between the row
 * total and the column total, and mod that difference by 64, you have a number
 * that will relate to one cell on the board.
 * that is true, but to find a solution, I would need to find a way that you
 * could take any random board of heads and tails, and flip one coin to encode
 * any integer between 1 and 64 (or 0 and 63). That way, no matter where on the
 * board the key is hidden, you can convey that to the second prisoner.
 * i used diffs to calculate what the difference would be for every coin you
 * could possibly flip.
 * I tried a couple different things (modding by 64, for example) and these
 * options are commented out but still present to help visualize.
 * it's clear this isn't quite the right solution. Whatever the "code" of the
 * original board is, you can modify to a range of values, but not all 64. the
 * currently uncommented line of code shows that you could modofy the original
 * code by 0, or 1, or 2, or 3, or 4, but not 5.
 * Here is a list of numbers you can do:
 * 0, 1, 2, 3, 4, 6, 7, 8, 12, 14, 15, 16, 24, 28, 30, 31, 32, 48, 56, 60, 62,
 * 63, 64
 * also some numbers above 64.
 * so one way we can perceive the problem with this strategy is that some of the
 * numbers go higher than 64, but really, you could make up some sort of
 * encoding for numbers above 64; like 112 could be 112/64 = 1 remainder 48, so
 * 48 plus 1 is 49, or something.
 * The real issue is that there is repetition. Whatever modifications we do,
 * flipping coin [i][j] would be the same as flipping coin [j][i]. In addition,
 * flipping a coin on the diagonal, [i][i], would never change the board. So no
 * matter what the original code is, we have 8 places we can flip that will give
 * us the exact same answer, and for some desired codes we have 2 or more places
 * we can flip to get that code, and for other codes, we might not have any way
 * we can encode for that.
 * Now I want to see what changing the column's powers will do. Let's say I
 * leave the rows as 8-bit numbers where index 7 will add 1, index 6 will add 2,
 * etc until index 0 adds 128.
 * but i'm going to change the columns so index 7 is 2, index 6 is 4, etc, until
 * index 0 is 256.
 * but i'm going to publish to github before I change this.
 * 
 */