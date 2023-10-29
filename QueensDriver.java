package driver;
import method.*;
import java.util.Scanner;

/** This driver is used to show the functionality of the Board class and the methods it uses to solve the n Queens
 * 	problem when inputted a dimension by the user.
 * 
 * @author Daniel Gardner (2023)
 *
 */

public class QueensDriver {
	
		public static void main(String[] args) {
			Scanner s = new Scanner(System.in);
			System.out.println("Enter the dimensions of the chess board you'd like to solve:");
			int dim = s.nextInt();
			Board board = new Board(dim);
			board.fillWithNQueens();
			System.out.println("Below is the first found solution to place 8 queens on an " + dim + "x" + dim + " chess"
					+ " board:\n" + board + "\n");
			board.clearBoard();
			System.out.println("There are " + board.getNumCombinations() + " possible solutions on a " + dim + "x" +
			dim + " chess board.");
			s.close();
		}
}