import java.lang.Math;
import java.util.Arrays;
import java.util.ArrayList;
//6/15/23
//trying to think through or solve the prisoner chessboard puzzle that 3blue1brown talks about
//https://youtu.be/wTJI_WuZSwE
//I watched far enough into the video to get the idea of enocding the key's location in the array of heads and tails
//I didn't solve it, but I have some conclusions at the end of this program

class Main {
  public static void main(String[] args) {
    /**/
    // this board is a random collection of 1s and 0s to represent heads and tails
    int[][] board = { { 0, 1, 0, 1, 0, 1, 0, 0 },
        { 1, 0, 0, 0, 1, 1, 1, 0 },
        { 0, 1, 1, 0, 1, 0, 0, 0 },
        { 0, 1, 1, 1, 0, 1, 0, 1 },
        { 0, 0, 0, 1, 0, 1, 1, 1 },
        { 1, 0, 1, 1, 0, 1, 1, 0 },
        { 0, 0, 0, 0, 1, 0, 0, 1 },
        { 0, 1, 0, 1, 1, 1, 0, 1 } };
    /**/

    /*
     * //this board is symmetric, which demonstrates the weakness of my strategy:
     * int[][] board = { { 0, 1, 0, 1, 0, 1, 0, 1 },
     * { 1, 0, 1, 0, 1, 0, 1, 0 },
     * { 0, 1, 0, 1, 0, 1, 0, 1 },
     * { 1, 0, 1, 0, 1, 0, 1, 0 },
     * { 0, 1, 0, 1, 0, 1, 0, 1 },
     * { 1, 0, 1, 0, 1, 0, 1, 0 },
     * { 0, 1, 0, 1, 0, 1, 0, 1 },
     * { 1, 0, 1, 0, 1, 0, 1, 0 } };
     */
    /*
     * // this board is all 1's
     * int[][] board = { { 1, 1, 1, 1, 1, 1, 1, 1 },
     * { 1, 1, 1, 1, 1, 1, 1, 1 },
     * { 1, 1, 1, 1, 1, 1, 1, 1 },
     * { 1, 1, 1, 1, 1, 1, 1, 1 },
     * { 1, 1, 1, 1, 1, 1, 1, 1 },
     * { 1, 1, 1, 1, 1, 1, 1, 1 },
     * { 1, 1, 1, 1, 1, 1, 1, 1 },
     * { 1, 1, 1, 1, 1, 1, 1, 1 } };
     */
    /*
     * // this board is all 0's
     * int[][] board = { {0, 0, 0, 0, 0, 0, 0, 0},
     * {0, 0, 0, 0, 0, 0, 0, 0},
     * {0, 0, 0, 0, 0, 0, 0, 0},
     * {0, 0, 0, 0, 0, 0, 0, 0},
     * {0, 0, 0, 0, 0, 0, 0, 0},
     * {0, 0, 0, 0, 0, 0, 0, 0},
     * {0, 0, 0, 0, 0, 0, 0, 0},
     * {0, 0, 0, 0, 0, 0, 0, 0}};
     */
    int code = calcDiff(board);
    System.out.println("The untouched board encodes: " + code);
    // the following flips one of the coins, calculates the new code, stores that in
    // diffs, and flips that coin back over. It tests each possible move prisoner 1
    // can make and stores the result of that move in the corresponding position in
    // diffs.
    int[][] diffs = new int[8][8];
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        board[i][j] = toggle(board[i][j]);
        diffs[i][j] = calcDiff(board);
        board[i][j] = toggle(board[i][j]);
      }
    }
    System.out.println("code resulting from each possible flip: ");
    for (int[] row : diffs) {
      System.out.println(Arrays.toString(row));
    }
    System.out.println("Here are all the codes that can't be reached: ");
    ArrayList<Integer> missingNums = check(diffs);
    System.out.println(missingNums.toString());
    double odds = (64 - missingNums.size()) / 64.0;
    System.out.println("The odds of winning for this specific board: " + odds);

  }

  // this function is called calcDiff but what it really does is encode the board.
  static int calcDiff(int[][] board) {
    int diff = 0;
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if (board[i][j] == 1) {
          diff += (8 * (i)) + j;
        }
      }
    }
    return Math.floorMod(diff, 64);
  }

  // toggle takes a coin and flips it over.
  static int toggle(int i) {
    if (i == 0)
      return 1;
    if (i == 1)
      return 0;
    return i;
  }

  // check will check a 2d array of all possible moves and see if every value from
  // 0 to 63 is present. a list of values that don't appear is returned.
  static ArrayList<Integer> check(int[][] board) {
    ArrayList<Integer> list = new ArrayList();
    for (int i = 0; i < 64; i++) {
      list.add(i);
    }
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if (list.contains(board[i][j])) {
          list.remove(Integer.valueOf(board[i][j]));
        }
      }
    }
    return list;
  }
}
/*
 * Conclusions:
 * the challenge is to
 * a) find a way to encode the sequence of 64 coins into a single number between
 * 0 and 63, so that prisoner 2 can look at the board and calculate the location
 * of the key
 * b) find a way of encoding that makes it possible for prisoner 1 to encode the
 * correct value by only flipping over one coin, no matter the original board or
 * location of the key.
 * my first encoding strategy:
 * 1. turn each row and each column into an 8-bit binary number
 * 2. sum the 8 row numbers to create the row total
 * 3. sum the 8 column numbers to create the column total
 * 4. take the difference of the row total and the column total
 * 5. mod that number by 64
 * The problem with this method is that prisoner 1 couldn't encode for certain
 * values between 0 and 63. if the original, untouched board's code was 5, and
 * the key was in the 10th space, prisoner 1 would be unable to modify the board
 * to encode to 10, because there is no coin that can be flipped that will
 * modify the code by 5.
 * the reason it doesn't work is because it is symmetrical. for example,
 * flipping any of the 8 coins along the diagonal would make the code stay the
 * same, and that means there are 7 less codes that can be reached, just along
 * the diagonal.
 * Here is my second strategy:
 * 1. if there is a 1 in a cell, add that cell's order to the total. so if there
 * was a 1 in the fifth cell, a 5 gets added to the total.
 * 2. mod by 64.
 * this method works better. For my randomized board, if the key was placed
 * randomly, there'd be an 80 percent chance that the board could be encoded
 * correctly with only the one coin flip.
 * However, the main problem can be seen in the randomized board in location
 * [1][0] and [7][0].
 * the original board encodes to 61, so if the key was in cell 5, you would want
 * to either increase the total by 8 by switching index 8 from a 0 to a 1, or
 * decrease the total by 56 by switching index 56 from a 1 to a 0. However,
 * index 5 already has a 1, and index 56 already has a 0. so there is no way to
 * create a board that will create the code 5. Correspondingly, if the key was
 * in the 53rd cell, the first prisoner has two ways to convey that: flipping 8
 * or 56.
 *******************************************************
 * 
 * 
 * 
 * Here are my less useful ramblings as I worked through this problem:
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
 * but i'm going to publish to github before I change this. (omg I totally
 * forget how I did that before! but replit has version control in and of
 * itself.)
 * shifting the columns by 1 bit just moves the diagonal with all the
 * repetitions. let me try doing i instead of 7 - i
 * missing: 2-6, 10, 17, 20, 21, 23-26, 28, 32, 33-42, 44, 48, 49, 51-53, 55-63
 * I tried summing 8(i) + j, but the issue there is you are constrained by only
 * flipping one coin. if you would want to increase the code by 19, you would
 * want to flip the 19th coin, but if it was a 1 to begin with, then the total
 * would decrease by 19. you could instead flip the 45th coin, because with mod
 * 64 they would come out the same, but only if the 45th coin was flipped where
 * you wanted it.
 * I may be stumped. I might look at the video.
 */