package solver.parallel.solvers.strategies;

import solver.SudokuBoard;
import solver.parallel.solvers.strategies.semaphore.CountingSemaphore;
import gui.Constants;

public class MatchingValueChuteStrategy extends OnePossibilityStrategy {

	int[] sortOrder;
	
	public MatchingValueChuteStrategy(CountingSemaphore instanceCounter) {
		super(instanceCounter);
	}
	
	public MatchingValueChuteStrategy(CountingSemaphore instanceCounter, int spot, SudokuBoard board, boolean[][][] possibleValues, boolean trueForRow) {
		super(instanceCounter, spot, board, possibleValues, trueForRow);
	}

	/**
	 * 
	 */
	private void updatePossibleSpots() {
		for (int i = 0; i < Constants.BOARD_SIZE; i++) {
			for (int j = 0; j < Constants.BOARD_SIZE; j++)
			{
				if (i!=j && possibleSpots[i] == possibleSpots[j]) {
				}
			}
		}
	} 
	
	private void sortPossibleSpots() {
		setupSortOrder();
		
		
	}
	
	private void setupSortOrder() {
		sortOrder = new int[Constants.BOARD_SIZE];
		
		for (int i = 0; i < Constants.BOARD_SIZE; i++) {
			sortOrder[i] = i;
		}
	}
}
