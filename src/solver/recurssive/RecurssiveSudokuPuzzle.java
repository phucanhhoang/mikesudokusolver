package solver.recurssive;

import gui.Constants;
import solver.SudokuBoard;
import solver.SudokuPuzzle;
import solver.recurssive.arrayGeneration.ArrayGeneratorStrategy;
import solver.recurssive.arrayGeneration.BackwardArray;
import solver.recurssive.arrayGeneration.ForwardArray;
import solver.recurssive.arrayGeneration.RandomArray;

public class RecurssiveSudokuPuzzle extends SudokuPuzzle {

	public RecurssiveSudokuPuzzle() throws InterruptedException {
		super();
		// TODO Auto-generated constructor stub
	}

	public RecurssiveSudokuPuzzle(SudokuBoard board) {
		super(board);
	}

	@Override
	public boolean solve() {

		return solve(0, 0, new ForwardArray());
	}

	/**
	 * Solve the single spot on the board.
	 * 
	 * @param i
	 *            the row
	 * @param j
	 *            the colum
	 * @param strategy
	 *            the strategy
	 * 
	 * @return true, if solve
	 */
	public boolean solve(int i, int j, ArrayGeneratorStrategy strategy) {
		// Check if the solver has run out of squares to figure out.
		//
		if (i == Constants.BOARD_SIZE) {
			i = 0;

			if (++j == Constants.BOARD_SIZE) {
				return true;
			}
		}

		// Skip cells with values already in it.
		//
		if (board.getValueAt(i, j) != Constants.EMPTY_CELL) {
			return solve(i + 1, j, strategy);
		}

		// Check all possible values in cell.
		//
		for (int val : strategy.generateArray(Constants.BOARD_SIZE)) {
			if (board.legalMove(i, j, val)) {
				board.setValueAt(i, j, val);
				if (solve(i + 1, j, strategy)) {
					return true;
				}
			}
		}

		// Reset board and return failure.
		//
		board.setValueAt(i, j, Constants.EMPTY_CELL);
		return false;
	}

	@Override
	public boolean solveBackwards() {

		return solve(0, 0, new BackwardArray());
	}

	@Override
	public boolean solveRandom() {

		return solve(0, 0, new RandomArray());
	}
}
