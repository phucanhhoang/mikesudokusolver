package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import solver.SudokuBoard;
import solver.SudokuPuzzle;
import solver.parallel.ParallelSudokuPuzzle;
import solver.parallelNoStrategies.ParallelNoStrategiesSudokuPuzzle;
import solver.recurssive.RecurssiveSudokuPuzzle;

public class Controller implements ActionListener {
	private SudokuGui gui;
	private int lastClickedi;
	private int lastClickedj;
	private SudokuPuzzle handler;
	private int boardSize;
	private final int[][] hardBoard = { { 1, 0, 0, 0, 0, 0, 0, 0, 2 },
			{ 0, 9, 0, 4, 0, 0, 0, 5, 0 }, { 0, 0, 6, 0, 0, 0, 7, 0, 0 },
			{ 0, 5, 0, 9, 0, 3, 0, 0, 0 }, { 0, 0, 0, 0, 7, 0, 0, 0, 0 },
			{ 0, 0, 0, 8, 5, 0, 0, 4, 0 }, { 7, 0, 0, 0, 0, 0, 6, 0, 0 },
			{ 0, 3, 0, 0, 0, 9, 0, 8, 0 }, { 0, 0, 2, 0, 0, 0, 0, 0, 1 } };

	/**
	 * Start a new gui and sudoku puzzle.
	 */
	public Controller() throws InterruptedException {
		//SudokuBoard hardBoard = new SudokuBoard(this.hardBoard);
		handler = new RecurssiveSudokuPuzzle();
		//handler = new ParallelSudokuPuzzle(handler.getBoard());
		handler = new ParallelNoStrategiesSudokuPuzzle(handler.getBoard());
		//handler = new ParallelSudokuPuzzle(hardBoard);
		gui = new SudokuGui(handler.getBoard(), this);
		
		boardSize = handler.getBoard().length;
		// Used to store dark gray cells.
		//
		lastClickedi = boardSize;
		lastClickedj = boardSize;
	}

	/**
	 * Handle the actions thrown by the gui.
	 */
	public void actionPerformed(ActionEvent e) {
		// QUIT clicked.
		//
		if (gui.quitClicked(e)) {
			System.exit(0);
		} else if (gui.solveClicked(e)) {
			// Reset board to original board to ensure that it can be solved.
			//
			handler.resetBoard();
			try {
				handler.solve();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				System.err.println(e1.getMessage());
				e1.printStackTrace();
			}
			gui.boardSolved(handler);
			gui.cellClicked(false);
		}

		// NEW GAME clicked.
		//
		else if (gui.newGameClicked(e)) {
			gui.dispose();
			try {
				handler.deleteInstance();
			} catch (Throwable e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.err.println("Cannot Delete Puzzle");
			}
			try {
				new Controller();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		// When enter is clicked load value into cell.
		//
		else if (gui.enterClicked(e)) {
			String str = gui.getValue();
			try {
				int num = Integer.parseInt(str);
				if (num > 0 && num < boardSize + 1) {
					if (gui.setValueAt(lastClickedi, lastClickedj, num, handler
							.isLegalMove(lastClickedi, lastClickedj, num))) {
						handler.setBoardSpot(lastClickedi, lastClickedj, num);
						gui.setValue("");
						gui.cellClicked(false);
						lastClickedi = boardSize;
						lastClickedj = boardSize;
					}
				}
			}

			catch (Exception ex) {
				gui.setValue("");
			}
		}

		// Look through all cells for action event.
		//
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (gui.cellClickedAt(i, j, e)) {
					if (lastClickedi < boardSize
							&& lastClickedj < boardSize) {
						gui.unSetCellClickedAt(lastClickedi, lastClickedj);
					}

					if (lastClickedi != i || lastClickedj != j) {
						lastClickedi = i;
						lastClickedj = j;
						gui.setCellClickedAt(i, j);
						gui.cellClicked(true);
					} else {
						lastClickedi = boardSize;
						lastClickedj = boardSize;
						gui.unSetCellClickedAt(i, j);
						gui.cellClicked(false);
					}
				}
			}
		}
	}
}