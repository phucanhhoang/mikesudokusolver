package solver.parallel.solvers;

import gui.Constants;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import solver.Helper;
import solver.SudokuBoard;
import solver.parallel.solvers.strategies.semaphore.CountingSemaphore;

public class ParallelBruteForceSolver implements Runnable{
	
	private SudokuBoard board;
	private static SudokuBoard solvedBoard;
	private static boolean boardSolved = false;
	private int startRow;
	private int startCol;
	private CountingSemaphore counter;
	ThreadPoolExecutor threadPool;
	
	public ParallelBruteForceSolver() {
		super();
		counter = new CountingSemaphore();
	}
	
	public ParallelBruteForceSolver(SudokuBoard board2, ThreadPoolExecutor threadPool, CountingSemaphore counter, int startRow, int startCol) {
		this.board = board2;
		this.threadPool = threadPool;
		this.counter = counter;
		this.startRow = startRow;
		this.startCol = startCol;
		counter.incrementCounter();
	}

	@Override
	public void run() {
		
		if (ParallelBruteForceSolver.boardSolved) {
			return;
		}
		else if (board.isSolved()) {
			setSolvedBoard(board);
			return;
		}
		
		ParallelStrategySolver solver = new ParallelStrategySolver(board);
		solver.solve(null);
		
		if (board.isSolved() || boardSolved) {
			setSolvedBoard(board);
		} else if (!findNextEmptyCell()) {
			return;
		}
		
		startBruteForceSolver(threadPool, solver);
		counter.decrementCounter();
	}
	

	private boolean findNextEmptyCell() {
		int row = startRow;
		int col = startCol;
		
		while (board.getValueAt(row,col) != Constants.EMPTY_CELL) {
			if (row == Constants.BOARD_SIZE - 1) {
				row = 0;
				
				if (++col == Constants.BOARD_SIZE) 
					return false;
			} else {
				row++;
			}
		}
		startRow = row;
		startCol = col;
		return true;
	}

	private synchronized void setSolvedBoard(SudokuBoard board2) {
		if (boardSolved) {
			return;
		}
		boardSolved = true;
		solvedBoard = board2;
	}

	/**
	 * @param threadPool
	 * @param solver
	 */
	private void startBruteForceSolver(ThreadPoolExecutor threadPool,
			ParallelStrategySolver solver) {
		
		for (int j:solver.getPossibleValuesCell(startRow, startCol)) {
			if (j == Constants.EMPTY_CELL) {
				break;
			}
			
			SudokuBoard tempBoard = board.newChangedValue(startRow,startCol,j);

			if (!startThread(tempBoard)) {
				SudokuBoard boardSave = board;
				this.board = tempBoard;
				run();
				board = boardSave;
			}
		}
	}

	private synchronized boolean startThread(SudokuBoard tempBoard) {
		
		if (counter.getValue() < (Constants.NUM_THREADS)) {
			ParallelBruteForceSolver bruteSolver;
			bruteSolver = new ParallelBruteForceSolver(tempBoard, threadPool, counter, startRow, startCol);
			//new Thread(bruteSolver).start();
			//threadPool.execute(bruteSolver);
			bruteSolver.run();
			return true;
		}
		return false;
		
	}

	public SudokuBoard getSolvedBoard() {
		if (boardSolved)
			return solvedBoard;
		else
			return null;
	}
}
