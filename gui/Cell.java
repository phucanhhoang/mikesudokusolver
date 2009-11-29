package gui;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;


public class Cell extends Button
{
    private int value;
    private boolean locked = false;
    private Color col;
    private Color prevColor = Constants.BLANK_CELL;
    private ActionListener listener;
    
    
    /**
     * Constructor that specifies value.
     *
     */
    public Cell(int value, ActionListener listener)
    {
        super();
        this.value = value;
        this.listener = listener;
        
        this.setSize(Constants.CELL_SIZE, Constants.CELL_SIZE);
        this.setFont(new Font("SANS_SERIF", Font.BOLD, 18));
        this.addActionListener(listener);
        this.setVisible(true);
        
        if (this.value != Constants.EMPTY_CELL)
            locked();
        else
            blank();
    }
    
    
    // Causes cell to be blank color.
    //
    public void blank()
    {
        if (!locked)
        {
            col = Constants.BLANK_CELL;
            upd();
        }
    }
    
    
    // Causes cell to be locked.
    //
    public void locked()
    {
        locked = true;
        col = Constants.LOCKED_CELL;
        upd();
    }
    
    // Locks the cell without changing colour.
    //
    public void nonLock()
    {
        locked = true;
    }
    
    
    public void clicked()
    {
        if (!locked)
        {
            prevColor = col;
            col = Constants.CLICKED_CELL;
            upd();
        }
    }
    
    
    public void unclicked()
    {
        if (!locked)
        {
            col = prevColor;
            upd();
        }
    }
    
    
    /**
     * Returns number stored in cell.
     *
     * @returns int
     */
    public int getValue()
    {
        return value;
    }
    
    
    /**
     * Sets background color to valid Color.
     *
     */
    public void valid()
    {
        if (!locked)
        {
            col = Constants.VALID_CELL;
            prevColor = col;
            upd();
        }
    }
    
    
    /**
     * Sets background color to invalid Color.
     *
     */
    public void invalid()
    {
        if (!locked)
        {
            col = Constants.INVALID_CELL;
            prevColor = col;
            upd();
        }
    }
    
    
    /**
     * Sets number stored in cell.
     *
     * @param value Number to be stored in the cell.
     */
    public boolean setValue(int value)
    {
        if (!locked)
        {
            this.value = value;
            upd();
            return true;
        }
        
        return false;
    }
    
    
    /**
     * Updates cell with background color and value.
     *
     */
    private void upd()
    {
        if (value != Constants.EMPTY_CELL)
            this.setLabel(value+"");
        else
            this.setLabel("");
            
        this.setBackground(col);
    }
}