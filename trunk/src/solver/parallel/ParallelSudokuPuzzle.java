package solver.parallel;

import gui.Constants;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import solver.SudokuBoard;
import solver.SudokuPuzzle;
import solver.parallel.solvers.ParallelBruteForceSolver;
import solver.parallel.solvers.ParallelStrategySolver;
import solver.parallel.solvers.strategies.semaphore.CountingSemaphore;

// TODO: Auto-generated Javadoc
/**
 * The Class ParallelSudokuPuzzle.
 */
public class ParallelSudokuPuzzle extends SudokuPuzzle {

	/**
	 * Instantiates a new parallel sudoku puzzle.
	 * 
	 * @throws InterruptedException the interrupted exception
	 */
	public ParallelSudokuPuzzle() throws InterruptedException {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new parallel sudoku puzzle.
	 * 
	 * @param board the board
	 * 
	 * @throws InterruptedException the interrupted exception
	 */
	public ParallelSudokuPuzzle(int[][] board) throws InterruptedException {
		super(board);
	}

	/**
	 * Shutdown.
	 * 
	 * @param threadPool the thread pool
	 * 
	 * @throws InterruptedException the interrupted exception
	 */
	private void shutdown(ThreadPoolExecutor threadPool)
			throws InterruptedException {
		//System.out.println("Shutting down threadPool");
		threadPool.shutdown();
		threadPool.awaitTermination(30, TimeUnit.SECONDS);
		threadPool.shutdownNow();
	}

	/**
	 * Solve.
	 * 
	 * @param strategy the strategy
	 * 
	 * @return true, if successful
	 * 
	 * @throws InterruptedException the interrupted exception
	 */
	public boolean solve() throws InterruptedException {
		// Create the thread pool.
		ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors
				.newFixedThreadPool(Constants.NUM_THREADS);

		/*ParallelStrategySolver solver = new ParallelStrategySolver(board);
		solver.solve(threadPool);*/

		if (board.isSolved())
		{
			shutdown(threadPool);
			return true;
		}
		
		/*RecurssiveSudokuPuzzle bruteForce = new RecurssiveSudokuPuzzle(board);
		bruteForce.solve();*/
		
		return startBruteForceSolver(threadPool);
	}
	
	private boolean startBruteForceSolver(ThreadPoolExecutor threadPool) throws InterruptedException {
		CountingSemaphore counter = new CountingSemaphore();
		ParallelBruteForceSolver brute = new ParallelBruteForceSolver(board, threadPool, counter, 0, 0);
		brute.clearSolved();
		brute.run();
		
		//System.out.println("waiting for threads");
		counter.waitForThreadsToFinish();

		//brute.run();
		shutdown(threadPool);
		
		//System.out.println("FINAL COUNTER VALUE: " + counter.getValue());
		
		SudokuBoard tempBoard = brute.getSolvedBoard();
		if (tempBoard != null) {
			board = tempBoard;
			return true;
		} else {
			//System.out.println("NOT SOLVED");
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see solver.SudokuPuzzle#solveBackwards()
	 */
	@Override
	public boolean solveBackwards() throws InterruptedException {

		return solve();
	}

	/* (non-Javadoc)
	 * @see solver.SudokuPuzzle#solveRandom()
	 */
	@Override
	public boolean solveRandom() throws InterruptedException {

		return solve();
	}

}
