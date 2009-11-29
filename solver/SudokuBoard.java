package solver;

import java.util.Arrays;

import gui.Constants;
import solver.parallel.solvers.strategies.Strategy;

public class SudokuBoard {
	private int[][] board;
	
	public SudokuBoard(int size) {
		initializeBoard(size);
	}

	/**
	 * Initializes a solved sudoku board that is random.
	 * 
	 * @throws InterruptedException 
	 */
	private void initializeBoard(int size) {
		// Initiate an empty board.
		//
		board = new int[size][size];

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				board[i][j] = Constants.EMPTY_CELL;
			}
		}
	}

	public SudokuBoard(int[][] board2) {
		board = board2;
	}

	public int[][] getBoard() {
		return board;
	}
	
	/**
	 * Returns true if the board is solved.
	 * 
	 * @return true, if checks if is solved
	 */
	public boolean isSolved() {
		for (int i = 0; i < Constants.BOARD_SIZE; i++) {
			for (int j = 0; j < Constants.BOARD_SIZE; j++) {
				if (board[i][j] == Constants.EMPTY_CELL
						|| !legalMove(i, j, board[i][j])) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Returns whether or not a move is legal.
	 * 
	 * @param row Horizontal cell location.
	 * @param col Vertical cell location.
	 * @param val Value to be entered in cell.
	 * 
	 * @return boolean
	 */
	public boolean legalMove(int row, int col, int val) {
		// Move must be legal in row, col and square.
		
		boolean outcome = checkRowLegalMove(row, col, val);
		outcome &= checkColLegalMove(row, col, val);
		outcome &= checkSquareLegalMove(row, col, val);

		return outcome;
	}

	/**
	 * @param row
	 * @param col
	 * @param val
	 */
	private boolean checkRowLegalMove(int row, int col, int val) {
		// Check row to see if move cannot be made.
		//
		for (int k = 0; k < Constants.BOARD_SIZE; k++) {
			if (val == board[k][col] && row != k) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * @param row
	 * @param col
	 * @param val
	 */
	private boolean checkColLegalMove(int row, int col, int val) {
		// Check column to see if move cannot be made.
		//
		for (int k = 0; k < Constants.BOARD_SIZE; k++) {
			if (val == board[row][k] && col != k) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * @param row
	 * @param col
	 * @param val
	 */
	private boolean checkSquareLegalMove(int row, int col, int val) {
		// Set beginning of square this cell is in.
		//
		int rowOffset = Strategy.getOffset(row);
		int colOffset = Strategy.getOffset(col);

		// Check square for repeated number.
		//
		for (int k = 0; k < Math.sqrt(Constants.BOARD_SIZE); k++) {
			for (int m = 0; m < Math.sqrt(Constants.BOARD_SIZE); m++) {
				if (val == board[k + rowOffset][m + colOffset] && (k + rowOffset != row) && (m + colOffset != col)){
					return false;
				}
			}
		}
		
		return true;
	}

	public static boolean equalBoards(SudokuBoard board1, SudokuBoard board2) {
		for (int i = 0; i < Constants.BOARD_SIZE; i++) {
			if (!Arrays.equals(board1.getBoard()[i],board2.getBoard()[i])) {
				return false;
			}
		}
		return true;
	}

	public void updateBoard(int x, int y, int value) {
		board[x][y]=value;
	}

	public int getValueAt(int x, int y) {
		// TODO Auto-generated method stub
		return board[x][y];
	}

	public void setValueAt(int x, int y, int val) {
		board[x][y] = val;
	}

	public SudokuBoard newChangedValue(int startRow, int startCol, int val) {
		SudokuBoard newBoard = new SudokuBoard(Helper.copyCells(board));
		newBoard.setValueAt(startRow, startCol, val);
		return newBoard;
	}
}
