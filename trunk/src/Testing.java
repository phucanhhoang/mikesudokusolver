import gui.Constants;
import solver.Helper;
import solver.SudokuPuzzle;
import solver.arrayGeneration.ArrayGeneratorStrategy;
import solver.arrayGeneration.BackwardArray;
import solver.arrayGeneration.ForwardArray;
import solver.arrayGeneration.RandomArray;
import solver.parallel.ParallelSudokuPuzzle;
import solver.recurssive.RecurssiveSudokuPuzzle;


public class Testing {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		
		for (int i = 0; i < 10; i++)
			{
			SudokuPuzzle puzzle = new RecurssiveSudokuPuzzle();
			SudokuPuzzle parallelPuzzle = new ParallelSudokuPuzzle(Helper.copyCells(puzzle.getBoard()));
			
			long time1 = System.currentTimeMillis();
			
			puzzle.solve();
			
			long time2 = System.currentTimeMillis();
			
			System.out.println("Puzzle solved in : " + (time2-time1));
			
			long time3 = System.currentTimeMillis();
			
			parallelPuzzle.solve();
			
			long time4 = System.currentTimeMillis();
			
			System.out.println("Parallel Puzzle solved in : " + (time4-time3));
			}
	}

}
