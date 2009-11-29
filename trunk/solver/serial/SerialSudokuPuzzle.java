package solver.serial;

import gui.Constants;
import solver.SudokuPuzzle;
import solver.arrayGeneration.ArrayGeneratorStrategy;
import solver.arrayGeneration.BackwardArray;
import solver.arrayGeneration.ForwardArray;
import solver.arrayGeneration.RandomArray;

public class SerialSudokuPuzzle extends SudokuPuzzle {

	boolean boardValues[][][];
	
	public SerialSudokuPuzzle(int[][] board) {
		super(board);
		boardValues= new boolean [Constants.BOARD_SIZE][Constants.BOARD_SIZE][Constants.BOARD_SIZE];
	}

	@Override
	public boolean solve() throws InterruptedException {
		return solve(new ForwardArray());
	}

	@Override
	public boolean solveBackwards() throws InterruptedException {
		// TODO Auto-generated method stub
		return solve(new BackwardArray());
	}

	@Override
	public boolean solveRandom() throws InterruptedException {
		return solve(new RandomArray());
	}

	private boolean solve(ArrayGeneratorStrategy forwardArray) {
		boolean reloop;
		
		getEmtpySpaces();
		
		do
		{
			for (int row = 0; row< Constants.BOARD_SIZE; row++)
	        {
	        	for (int col = 0; col < Constants.BOARD_SIZE; col++)
	        	{
	        		searchThroughRow(row,col);
	        		
	        		searchThroughCol(row,col);
					
	        	}
	        }
		} while(reloop);
		
		return true;
	}

	private void searchThroughCol(int row, int col) {
		// Search column for matching values.
        //
        for (int k = 0; k < Constants.BOARD_SIZE; k++)
        {
            if ( board[row][k] != Constants.EMPTY_CELL && boardValues[row][col][board[row][k]-1])
            {
            	boardValues[row][col][board[row][k]-1] = false;
            }
        }
	}

	/**
	 * Search for and remove similar values for the 
	 * @param row
	 * @param col
	 */
	private void searchThroughRow(int row, int col) {
		// Search row for matching values.
        //
        for (int k = 0; k < Constants.BOARD_SIZE; k++)
        {
            if ( board[k][col] != Constants.EMPTY_CELL && boardValues[row][col][board[k][col]-1])
            {
            	boardValues[row][col][board[k][col]-1] = false;
            }
        }
	}

	/**
	 * Setup the possible moves for each square in the board.
	 */
	private void getEmtpySpaces() {
		for (int m = 0; m < Constants.BOARD_SIZE; m++)
        {
        	for (int n = 0; n < Constants.BOARD_SIZE; n++)
        	{
        		for (int p = 0; p < Constants.BOARD_SIZE; n++)
        		{
	        		if (board[m][n] == Constants.EMPTY_CELL || board[m][n]==(p+1))
	        		{
	        			boardValues[m][n][p]=false;
	        		}
	        		else
	        		{
	        			boardValues[m][n][p]=true;
	        		}
        		}
        	}
        }
	}
}
