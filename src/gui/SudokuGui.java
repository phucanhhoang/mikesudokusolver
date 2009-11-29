package gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import solver.SudokuPuzzle;

// Inner class to set background color of SpacerPanel().
//
class SpacerPanel extends Panel {
	public SpacerPanel() {
		super();
		setBackground(Constants.FRAME_BACKGROUND);
	}
}

public class SudokuGui extends Frame {
	private Button quit;
	private Button newGame;
	private Button solve;
	private Button enter;
	private TextField value;

	private Board gameBoard;

	private Panel buttonPanel;

	private ActionListener listener;

	public SudokuGui(int[][] values, ActionListener listener) {
		super("SUDOKU");

		this.listener = listener;

		gameBoard = new Board(values, listener);

		initialize();

		validate();
	}

	public void boardSolved(SudokuPuzzle puzzle) {
		int[][] board = puzzle.getBoard();
		for (int i = 0; i < Constants.BOARD_SIZE; i++) {
			for (int j = 0; j < Constants.BOARD_SIZE; j++) {
				gameBoard.setValueAt(i, j, board[i][j], puzzle.isLegalMove(i,
						j, board[i][j]));
				// gameBoard.lockCellAt(i,j);
			}
		}
		validate();
	}

	// Called when a cell is clicked.
	//
	public void cellClicked(boolean val) {
		value.setVisible(val);
		enter.setVisible(val);
	}

	public boolean cellClickedAt(int i, int j, ActionEvent e) {
		return gameBoard.cellClickedAt(i, j, e);
	}

	public boolean enterClicked(ActionEvent e) {
		return e.getSource().equals(enter);
	}

	public Board getBoard() {
		return gameBoard;
	}

	// Gets text box value.
	//
	public String getValue() {
		return value.getText();
	}

	private void initialize() {
		// Set some settings for this frame.
		//
		setBackground(Constants.FRAME_BACKGROUND);
		this.setSize(Constants.BOARD_SIZE * Constants.CELL_SIZE
				+ Constants.BUTTON_SIZE, Constants.BOARD_SIZE
				* Constants.CELL_SIZE);
		setLayout(new BorderLayout());
		setResizable(false);
		setVisible(true);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		// Initialize Buttons.
		//
		quit = new Button("QUIT");
		newGame = new Button("NEW GAME");
		solve = new Button("SOLVE");
		enter = new Button("ENTER");
		value = new TextField();

		// Set Colors.
		//
		quit.setBackground(Constants.FRAME_BUTTONS);
		newGame.setBackground(Constants.FRAME_BUTTONS);
		solve.setBackground(Constants.FRAME_BUTTONS);
		enter.setBackground(Constants.FRAME_BUTTONS);

		// Set action listeners.
		//
		quit.addActionListener(listener);
		newGame.addActionListener(listener);
		solve.addActionListener(listener);
		enter.addActionListener(listener);

		// Initialize panel that will hold buttons.
		//
		buttonPanel = new Panel();
		buttonPanel.setSize(Constants.BUTTON_SIZE, Constants.BOARD_SIZE
				* Constants.CELL_SIZE);
		buttonPanel.setLayout(new GridLayout(9, 1));

		// Add 2 blank panels for spacing of buttons to bottom of buttonPanel.
		//
		for (int i = 0; i < 2; i++) {
			buttonPanel.add(new SpacerPanel());
		}

		// Add textfield and button.
		//
		buttonPanel.add(value);
		buttonPanel.add(enter);

		// Add 2 blank panels for spacing of buttons to bottom of buttonPanel.
		//
		for (int i = 0; i < 2; i++) {
			buttonPanel.add(new SpacerPanel());
		}

		// Add 3 buttons to bottom of panel and add button panel to frame.
		//
		buttonPanel.add(newGame);
		buttonPanel.add(solve);
		buttonPanel.add(quit);

		newGame.setVisible(true);
		solve.setVisible(true);
		quit.setVisible(true);
		gameBoard.setVisible(true);

		// Hide enter and value components.
		//
		cellClicked(false);

		// Add and show button panel.
		//
		this.add(buttonPanel, BorderLayout.EAST);
		buttonPanel.setVisible(true);

		// Initialize Board.
		//
		this.add(gameBoard, BorderLayout.CENTER);

		gameBoard.setVisible(true);
	}

	public boolean newGameClicked(ActionEvent e) {
		return e.getSource().equals(newGame);
	}

	// Returns what caused event.
	public boolean quitClicked(ActionEvent e) {
		return e.getSource().equals(quit);
	}

	public void setCellClickedAt(int i, int j) {
		gameBoard.setCellClickedAt(i, j);
	}

	// Sets text box value.
	//
	public void setValue(String text) {
		value.setText(text);
	}

	public boolean setValueAt(int i, int j, int val, boolean valid) {
		return gameBoard.setValueAt(i, j, val, valid);
	}

	public boolean solveClicked(ActionEvent e) {
		return e.getSource().equals(solve);
	}

	public void unSetCellClickedAt(int i, int j) {
		gameBoard.unSetCellClickedAt(i, j);
	}
}
