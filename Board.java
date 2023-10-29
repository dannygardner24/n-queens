package method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Stack;

/** The Board class represents a chess board, which can be made to have varying dimensions as long as they are >= 1. 
 * At it's core is a 2 dimensional array of the Square enumerated type, which indicate whether a position on the chess
 *  board is empty, has a queen on it, or is within the attack scope of a queen on the board. A Stack is used to store 
 * references of Pair objects containing the integer coordinates of all the queens on the chess board. A Map is used to
 * store the references of coordinates synonymous to those on the stack as keys, and map them to a Set of all the
 * coordinates of squares they attack, except the ones already attacked by a queen that was placed before it. A board
 * also stores information about it's size and the number of queens it has.
 * 
 * @author Daniel Gardner (2023)
 */

public class Board {

	// Contains the status of each square on the board, represented through a Square enum
	private Square[][] board;

	// Stores the dimension of the board, which will always be square.
	private final int size;

	// Stores the number of queens currently on the board.
	private int numQueens;

	// A stack storing the coordinates of queens in last-in first-out order.
	private Stack<Pair<Integer, Integer>> queenCoordinates;

	// A map that maps the coordinates of a queen to all the squares it attacks except squares that were attacked by any
	// queens placed before it.
	private Map<Pair<Integer, Integer>, Set<Pair<Integer, Integer>> > scopeSquares;


	/** A constructor which initializes an empty, classic 8x8 chess board.
	 */
	public Board() {
		this(8);
	}

	/** A constructor which initializes an empty chess board with the desired dimensions.
	 * 
	 * @param size The desired dimension of the board.
	 */
	public Board(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException(" \"size\" must be a positive integer");
		}
		this.board = new Square[size][size];
		this.size = size;
		clearBoard();
	}

	/** Gets the size of the chess board.
	 * 
	 * @return The size of the board.
	 */
	public int getSize() {
		return this.size;
	}

	/** Gets the number of queens currently on the chess board.
	 * 
	 * @return The number of queens on the board.
	 */
	public int getNumQueens() {
		return this.numQueens;
	}


	/** Clears all elements of the board except it's size. All squares will become empty and all queens will be removed.
	 */
	public void clearBoard() {
		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				board[i][j]= Square.EMPTY;
			}
		}
		this.queenCoordinates = new Stack<>();
		this.scopeSquares = new HashMap<>();
		this.numQueens = 0;
	}

	/** placeQueen takes in a pair of coordinates attempts to place a queen on the board. If successful, the queen
	 * Will have all the squares which it attacks added to a the set in the scopeSquares map structure, where the key
	 * is a Pair of the Queen's coordinates, and the value is the set of the squares it attacks. If a queen lower on the
	 * stack queenCoordinates already attacks a square(s) that the new queen will attack, those squares will not be
	 * added to the set.
	 * 
	 * @param y The row of the queen attempting to be placed.
	 * @param z The column of the queen attempting to be placed.
	 * @return True if the queen was successfully placed, false if the queen was attempted to be placed on an 
	 * invalid square.
	 * @throws IllegalArgumentException if y or z would try to place a queen on a square that doesn't exist on the
	 * current board.
	 */

	public boolean placeQueen(int y, int z) {

		if (y < 0 || z < 0 || y >= this.size || z >= this.size) {

			throw new IllegalArgumentException("Board position (" + y + "," + z + ") does not exist");

		} else {

			if(board[y][z] == Square.EMPTY) {

				Pair<Integer, Integer> coords = new Pair<>(y, z);
				queenCoordinates.push(coords);
				scopeSquares.put(coords, new HashSet<Pair<Integer, Integer>>() );

				for(int index = 0; index < this.size; index++) {

					// fills horizontal scope
					if (board[y][index] == Square.EMPTY) {
						board[y][index] = Square.SCOPED;
						scopeSquares.get(coords).add(new Pair<Integer, Integer>(y, index));
					}

					//fills vertical scope
					if (board[index][z] == Square.EMPTY) {
						board[index][z] = Square.SCOPED;
						scopeSquares.get(coords).add(new Pair<Integer, Integer>(index, z));
					}

				}

				int row, col;

				//"decreasing" diagonal scope
				for (row = y + 1, col = z + 1; (row < this.size) && (col < this.size); row++, col++) {

					if (board[row][col] == Square.EMPTY) {

						board[row][col] = Square.SCOPED;
						scopeSquares.get(coords).add(new Pair<Integer, Integer>(row, col));

					}
				}
				for (row = y - 1, col = z - 1; (row >= 0) && (col >= 0); row--, col--) {

					if (board[row][col] == Square.EMPTY) {

						board[row][col] = Square.SCOPED;
						scopeSquares.get(coords).add(new Pair<Integer, Integer>(row, col));

					}

				}

				//"increasing" diagonal scope
				for (row = y + 1, col = z - 1; (row < this.size) && (col >= 0); row++, col--) {

					if (board[row][col] == Square.EMPTY) {

						board[row][col] = Square.SCOPED;
						scopeSquares.get(coords).add(new Pair<Integer, Integer>(row, col));

					}

				}
				for (row = y - 1, col = z + 1; (row >= 0) && (col < this.size); row--, col++) {

					if (board[row][col] == Square.EMPTY) {

						board[row][col] = Square.SCOPED;
						scopeSquares.get(coords).add(new Pair<Integer, Integer>(row, col));

					}

				}

				board[y][z] = Square.QUEEN;
				this.numQueens++;
				return true;

			} else {
				return false;
			}

		}
	}

	/** Removes the last queen placed on the board, and frees all the squares it previously attacked to empty squares.
	 * 
	 * @throws EmptyStackException if there are no queens on the board.
	 */

	public void removeQueen() {

		// remove all squares the last queen attacked
		for ( Pair<Integer, Integer> curr : scopeSquares.get(queenCoordinates.peek())) {

			board[curr.getKey()][curr.getValue()] = Square.EMPTY;

		}

		// removes the last queen from the stack, and removes it from the map as well
		scopeSquares.remove(queenCoordinates.pop());
		this.numQueens--;
	}


	/** This method tries to fill the board (in its current position) with n queens using recursive backtracking. If it
	 * is successful, the method will return true and the board will be modified to have the first found solution. If
	 * it is false, the method will return false and the board will be in it's original state when the method was
	 * called. WARNING: Calling this method on Boards with dimension greater than 10 may have a long runtime,
	 * as the time and memory complexity of the backtracking algorithm is O(n^2).
	 * 
	 * @return true if the method succeeded in placing n queens onto the board, false if it was not possible to place
	 * n queens onto the current board.
	 */
	public boolean fillWithNQueens() {

		//creates a set of options
		HashSet<Pair<Integer, Integer>> choices = new HashSet<>();

		//go through each chess square and add it to the set of options if it is empty
		for (int y = 0, z = 0; y < this.size; z++) {

			if (board[y][z] == Square.EMPTY) {
				choices.add(new Pair<>(y, z));
			}

			if (z == this.size - 1) {
				y++;
				z = -1;
			}
		}


		// if there are no possible choices and there are not n queens on the board, return false and try another 
		//choice in the previous recursive method. 
		if (choices.isEmpty() && this.numQueens < size) {

			return false;

			// else, check if the current solution actually has n queens and is a valid solution
		} else if (this.numQueens == size) {

			return true;

			// After checking the first two conditions, it is known that the puzzle isn't solved and we have more options to
			// try. So, we will now try all the options recursively, and return true if we find one that works. However, if
			// none of the options work, the method will backtrack to the previous call to try another option. If this 
			// point is reached in the parent call to the method, the method will return false altogether.
		} else {

			for (Pair<Integer, Integer> next : choices) {

				this.placeQueen(next.getKey(), next.getValue());

				if (fillWithNQueens()) {
					return true;
				}
				this.removeQueen();
			}

			return false;
		}
	}


	public int getNumCombinations() {

		return getNumCombinationsHelper(0);

	}

	private int getNumCombinationsHelper(int row) {

		if (this.numQueens == this.size) {

			return 1;

		}

		int count = 0;

		//creates a set of options
		HashSet<Pair<Integer, Integer>> choices = new HashSet<>();

		//go through each chess square and add it to the set of options if it is empty
		for (int i = 0; i < this.size; i++) {
			if (board[row][i] == Square.EMPTY) {
				choices.add(new Pair<>(row, i));
			}
		}


		for (Pair<Integer, Integer> next : choices) {

			this.placeQueen(next.getKey(), next.getValue());
			count += getNumCombinationsHelper(row + 1);
			this.removeQueen();

		}

		return count;
	}

	/** Returns a String representation of the current Board.
	 */
	@Override
	public String toString() {

		StringBuffer result = new StringBuffer();
		for(Square[] i : board) {
			for(Square j : i) {
				result.append("[" + j.toString() + "]");
			}
			result.append("\n");
		}
		return result.toString();
	}
}
