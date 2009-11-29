package gui;

import java.awt.Color;
import java.util.LinkedList;

public class Constants
{
    // Stores size of the board, but must be a square number for sudoku puzzle.
    // Is also used as range of possible values that can be put in cells.
    //
    public final static int BOARD_SIZE = 9;
    
    public final static int GEN_ATTEMPTS =5;
    
    public final static int NUM_THREADS = 5;
    
    // Size of the frame, the total frame size will be 9*CELL_SIZE by 9*CELL_SIZE plus BUTTON_SIZE.
    //
    public final static int CELL_SIZE = 50;
    public final static int BUTTON_SIZE = 100;
    
    // Value used for empty cell.
    //
    public final static int EMPTY_CELL = 0;
    
    // Different Color's used by the GUI.
    //
    public final static Color VALID_CELL = Color.GREEN;
    public final static Color INVALID_CELL = Color.RED;
    public final static Color BLANK_CELL = Color.WHITE;
    public final static Color LOCKED_CELL = Color.GRAY.brighter();
    public final static Color CLICKED_CELL = Color.GRAY.darker();
    
    public final static Color BOARD_BACKGROUND = Color.GRAY.brighter();
    public final static Color FRAME_BACKGROUND = VALID_CELL.darker();
    public final static Color FRAME_BUTTONS = VALID_CELL.brighter();
    
}