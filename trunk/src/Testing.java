import gui.Constants;
import solver.Helper;
import solver.SudokuPuzzle;
import solver.parallel.ParallelSudokuPuzzle;
import solver.recurssive.RecurssiveSudokuPuzzle;

// TODO: Auto-generated Javadoc
/**
 * The Class Testing.
 */
public class Testing {

	/**
	 * The main method.
	 * 
	 * @param args the args
	 * 
	 * @throws InterruptedException the interrupted exception
	 */
	public static void main(String[] args) throws InterruptedException {
		SudokuPuzzle puzzle;
		SudokuPuzzle parallelPuzzle;
		long totalRec = 0;
		long totalPar = 0;
		for (int i = 0; i < 10; i++) {
			puzzle = new RecurssiveSudokuPuzzle();
			parallelPuzzle = new ParallelSudokuPuzzle(Helper.copyCells(puzzle
					.getBoard()));

			printBoard(puzzle);
			long time1 = System.currentTimeMillis();

			puzzle.solve();

			long time2 = System.currentTimeMillis();
			printBoard(puzzle);
			System.out.println("Puzzle solved in : "
					+ (totalRec += time2 - time1));
			printBoard(parallelPuzzle);
			long time3 = System.currentTimeMillis();

			parallelPuzzle.solve();

			long time4 = System.currentTimeMillis();
			printBoard(parallelPuzzle);
			System.out.println("Parallel Puzzle solved in : "
					+ (totalPar += time4 - time3));
		}

		System.out.println("Recursive Time: " + totalRec);
		System.out.println("Parallel Time: " + totalPar);
	}

	/**
	 * Prints the board.
	 * 
	 * @param puzzle the puzzle
	 */
	private static void printBoard(SudokuPuzzle puzzle) {
		for (int i = 0; i < Constants.BOARD_SIZE; i++) {
			for (int j = 0; j < Constants.BOARD_SIZE; j++) {
				System.out.print(puzzle.getBoard()[i][j] + ",");
			}
			System.out.println("");
		}
		System.out.println("");
	}

}
