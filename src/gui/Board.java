package gui;

import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Board extends Panel {
	private Cell cells[][];
	private ActionListener listener;

	public Board(int[][] initialArray, ActionListener listener) {
		// Create grid of cells and initialize.
		//
		cells = new Cell[initialArray.length][initialArray.length];
		setBackground(Constants.BOARD_BACKGROUND);
		setLayout(new GridLayout(initialArray.length, initialArray.length, 5,
				5));
		this.setSize(Constants.CELL_SIZE * initialArray.length,
				Constants.CELL_SIZE * initialArray.length);
		this.listener = listener;

		for (int i = 0; i < initialArray.length; i++) {
			for (int j = 0; j < initialArray.length; j++) {
				cells[i][j] = new Cell(initialArray[i][j], listener);
				this.add(cells[i][j]);
			}
		}
	}

	// Returns where action was caused.
	//
	public boolean cellClickedAt(int i, int j, ActionEvent e) {
		return e.getSource().equals(cells[i][j]);
	}

	public void cellValidAt(int i, int j) {
		cells[i][j].valid();
	}

	public int[][] getArray() {
		// Create new array instance and load values.
		//
		int[][] newCells = new int[cells.length][cells.length];

		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells.length; j++) {
				newCells[i][j] = cells[i][j].getValue();
			}
		}

		return newCells;
	}

	public void lockCellAt(int i, int j) {
		cells[i][j].nonLock();
	}

	public void setArray(int[][] newCells) {
		// Loads values.
		//
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells.length; j++) {
				cells[i][j].setValue(newCells[i][j]);
			}
		}
	}

	public void setCellClickedAt(int i, int j) {
		cells[i][j].clicked();
	}

	public boolean setValueAt(int i, int j, int val, boolean legalMove) {
		// Set the color before setting the value.
		//
		if (legalMove) {
			cells[i][j].valid();
		} else {
			cells[i][j].invalid();
		}

		return cells[i][j].setValue(val);
	}

	public void unSetCellClickedAt(int i, int j) {
		cells[i][j].unclicked();
	}
}