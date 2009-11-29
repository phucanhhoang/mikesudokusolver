package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import solver.SudokuPuzzle;
import solver.parallel.ParallelSudokuPuzzle;
import solver.recurssive.RecurssiveSudokuPuzzle;


public class Controller implements ActionListener
{
    private SudokuGui gui;
    private int lastClickedi;
    private int lastClickedj;
    private SudokuPuzzle handler;
    private final int[][] hardBoard = {{1,0,0,0,0,0,0,0,2},
   									   {0,9,0,4,0,0,0,5,0},
   									   {0,0,6,0,0,0,7,0,0},
   									   {0,5,0,9,0,3,0,0,0},
   									   {0,0,0,0,7,0,0,0,0},
   									   {0,0,0,8,5,0,0,4,0},
   									   {7,0,0,0,0,0,6,0,0},
   									   {0,3,0,0,0,9,0,8,0},
   									   {0,0,2,0,0,0,0,0,1}};
    
    /**
     * Start a new gui and sudoku puzzle.
     */
    public Controller() throws InterruptedException
    {
        //handler = new RecurssiveSudokuPuzzle();
		//handler = new ParallelSudokuPuzzle(handler.getBoard());
		handler = new ParallelSudokuPuzzle(hardBoard);
        gui = new SudokuGui(handler.getBoard(), this);
        
        // Used to store dark gray cells.
        //
        lastClickedi = Constants.BOARD_SIZE;
        lastClickedj = Constants.BOARD_SIZE;
    }
    
    /**
     * Handle the actions thrown by the gui.
     */
    public void actionPerformed(ActionEvent e)
    {
        // QUIT clicked.
        //
        if (gui.quitClicked(e))
            System.exit(0);
            
        // SOLVE clicked.
        //
        else if (gui.solveClicked(e))
        {
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
        else if (gui.newGameClicked(e))
        {
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
        else if (gui.enterClicked(e))
        {
            String str = gui.getValue();
            try
            {
                int num = Integer.parseInt(str);
                if (num > 0 && num < Constants.BOARD_SIZE + 1)
                {
                    if (gui.setValueAt(lastClickedi, lastClickedj, num, handler.isLegalMove(lastClickedi, lastClickedj, num)))
                    {
                    	handler.setBoardSpot(lastClickedi, lastClickedj, num);
                        gui.setValue("");
                        gui.cellClicked(false);
                        lastClickedi = Constants.BOARD_SIZE;
                        lastClickedj = Constants.BOARD_SIZE;
                    }
                }
            }
            
            catch (Exception ex)
            {
                gui.setValue("");
            }
        }
        
        // Look through all cells for action event.
        //
        for (int i = 0; i < Constants.BOARD_SIZE; i++)
            for (int j = 0; j < Constants.BOARD_SIZE; j++)
                if (gui.cellClickedAt(i, j, e))
                {
                    if (lastClickedi < Constants.BOARD_SIZE && lastClickedj < Constants.BOARD_SIZE)
                    {
                        gui.unSetCellClickedAt(lastClickedi, lastClickedj);
                    }
                    
                    if (lastClickedi != i || lastClickedj != j)
                    {
                        lastClickedi = i;
                        lastClickedj = j;
                        gui.setCellClickedAt(i, j);
                        gui.cellClicked(true);
                    }
                    else
                    {
                        lastClickedi = Constants.BOARD_SIZE;
                        lastClickedj = Constants.BOARD_SIZE;
                        gui.unSetCellClickedAt(i, j);
                        gui.cellClicked(false);
                    }
                }
    }
}