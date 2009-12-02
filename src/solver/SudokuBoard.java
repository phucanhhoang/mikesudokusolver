package solver;

import gui.Constants;

import java.util.Arrays;

import solver.parallel.solvers.strategies.Strategy;

public class SudokuBoard {
	public static boolean equalBoards(SudokuBoard board1, SudokuBoard board2) {
		for (int i = 0; i < board1.getSize(); i++) {
			if (!Arrays.equals(board1.getBoard()[i], board2.getBoard()[i])) {
				return false;
			}
		}
		return true;
	}

	private int[][] board;

	public SudokuBoard(int size) {
		initializeBoard(size);
	}

	public SudokuBoard(int[][] board2) {
		board = board2;
	}
	
	public int getSize() {
		return board.length;
	}

	/**
	 * @param row
	 * @param col
	 * @param val
	 */
	private boolean checkColLegalMove(int row, int col, int val) {
		// Check column to see if move cannot be made.
		//
		for (int k = 0; k < getSize(); k++) {
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
	private boolean checkRowLegalMove(int row, int col, int val) {
		// Check row to see if move cannot be made.
		//
		for (int k = 0; k < getSize(); k++) {
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
	private boolean checkSquareLegalMove(int row, int col, int val) {
		// Set beginning of square this cell is in.
		//
		int rowOffset = Strategy.getOffset(row, getSize());
		int colOffset = Strategy.getOffset(col, getSize());

		// Check square for repeated number.
		//
		for (int k = 0; k < Math.sqrt(getSize()); k++) {
			for (int m = 0; m < Math.sqrt(getSize()); m++) {
				if (val == board[k + rowOffset][m + colOffset]
						&& k + rowOffset != row && m + colOffset != col) {
					return false;
				}
			}
		}

		return true;
	}

	public int[][] getBoard() {
		return board;
	}

	public int getValueAt(int x, int y) {
		// TODO Auto-generated method stub
		return board[x][y];
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

	/**
	 * Returns true if the board is solved.
	 * 
	 * @return true, if checks if is solved
	 */
	public boolean isSolved() {
		for (int i = 0; i < getSize(); i++) {
			for (int j = 0; j < getSize(); j++) {
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
	 * @param row
	 *            Horizontal cell location.
	 * @param col
	 *            Vertical cell location.
	 * @param val
	 *            Value to be entered in cell.
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

	public SudokuBoard newChangedValue(int startRow, int startCol, int val) {
		SudokuBoard newBoard = new SudokuBoard(Helper.copyCells(board));
		newBoard.setValueAt(startRow, startCol, val);
		return newBoard;
	}

	public void setValueAt(int x, int y, int val) {
		board[x][y] = val;
	}

	public void updateBoard(int x, int y, int value) {
		board[x][y] = value;
	}
}
