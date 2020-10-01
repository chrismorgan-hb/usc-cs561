package restaurant.layoutGUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Restaurant extends JFrame
{   
    private long animDelay;
    private JButton[][] buttons;
    private int grilX, grilY, grilSize, cntrX, cntrY, cntrSize, waitX, waitY, waitSize, waiterX, waiterY;
    private String defaultText;
    private boolean grilFull, cntrFull, waitFull;
    private int xPos, yPos;
    
    public Restaurant(String caption, int x, int y, boolean gridPresent)
    {
        this.setTitle(caption);
        this.getContentPane().setLayout(new GridLayout(x, y, 0, 0));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        defaultText =   "$$$$$$";
        xPos        =   x;
        yPos        =   y;
        buttons     =   new JButton[x][y];
        for (int i  = 0; i < x; i++)
        {
            for (int j = 0; j < y; j++)
            {
                buttons[i][j]  =   new JButton(defaultText);
                buttons[i][j].setBackground(new Color(255, 255, 255));
                buttons[i][j].setForeground(new Color(255, 255, 255));
                buttons[i][j].setBorderPainted(gridPresent);
                this.getContentPane().add(buttons[i][j]);
            }
        }
        animDelay   =   1000;
    }
    
    public void setAnimDelay(long animDelay)
    {
        this.animDelay  =   animDelay;
    }
    
    public void addTable(String tableNum, int x, int y, int size)
    {
        for (int i = (x - 1); i < (x - 1 + size); i++)
        {
            for (int j = (y - 1); j < (y - 1 + size); j++)
            {
                buttons[i][j].setForeground(new Color(0, 0, 0));
                buttons[i][j].setBackground(new Color(0, 0, 0));
            }
        }
        buttons[x-1][y-1].setForeground(new Color(255, 255, 255));
        buttons[x-1][y-1].setText(tableNum);
    }
    
    public void addWaitArea(int x, int y, int size)
    {
        waitX       =   x;
        waitY       =   y;
        waitSize    =   size;
        for (int j  = (y - 1); j < (y - 1 + size); j++)
        {
            buttons[x-1][j].setForeground(new Color(0, 0, 0));
            buttons[x-1][j].setBackground(new Color(0, 0, 0));
        }
    }
        
    public void addCounter(int x, int y, int size)
    {
        cntrX       =   x;
        cntrY       =   y;
        cntrSize    =   size;
        for (int j  = (y - 1); j < (y - 1 + size); j++)
        {
            buttons[x-1][j].setForeground(new Color(0, 0, 0));
            buttons[x-1][j].setBackground(new Color(0, 0, 0));
        }
    }
        
    public void addGrill(int x, int y, int size)
    {
        grilX       =   x;
        grilY       =   y;
        grilSize    =   size;
        for (int j  = (y - 1); j < (y - 1 + size); j++)
        {
            buttons[x-1][j].setForeground(new Color(0, 0, 0));
            buttons[x-1][j].setBackground(new Color(0, 0, 0));
        }
    }
    
    public void displayRestaurant()
    {
        this.pack();
        this.setVisible(true);
    }
    
    protected int getGrilX()
    {
        if (!grilFull)
        {
            return grilX;
        }
        else
        {
            for(int i = 1; i <= xPos; i++)
            {
                for(int j = 1; j <= yPos; j++)
                {
                    if(buttons[i-1][j-1].getText().equals(defaultText) &&
                       buttons[i-1][j-1].getBackground().getRed() == 255 && 
                       buttons[i-1][j-1].getBackground().getGreen() == 255 && 
                       buttons[i-1][j-1].getBackground().getBlue() == 255)
                    {
                        return i;
                    }
                }
            }
        }
        return -1;
    }
    
    protected int getGrilY()
    {
        grilFull    =   false;
        for(int i = grilY; i < grilY + grilSize; i++)
        {
            if (buttons[grilX-1][i-1].getText().equals(defaultText))
            {
                return i;
            }
        }
        grilFull    =   true;
        for(int i = 1; i <= xPos; i++)
        {
            for(int j = 1; j <= yPos; j++)
            {
                if(buttons[i-1][j-1].getText().equals(defaultText) &&
                   buttons[i-1][j-1].getBackground().getRed() == 255 && 
                   buttons[i-1][j-1].getBackground().getGreen() == 255 && 
                   buttons[i-1][j-1].getBackground().getBlue() == 255)
                {
                    return j;
                }
            }
        }
        return -1;
    }
    
    protected int getCntrX()
    {
        if (!cntrFull)
        {
            return cntrX;
        }
        else
        {
            for(int i = 1; i <= xPos; i++)
            {
                for(int j = 1; j <= yPos; j++)
                {
                    if(buttons[i-1][j-1].getText().equals(defaultText) &&
                       buttons[i-1][j-1].getBackground().getRed() == 255 && 
                       buttons[i-1][j-1].getBackground().getGreen() == 255 && 
                       buttons[i-1][j-1].getBackground().getBlue() == 255)
                    {
                        return i;
                    }
                }
            }
        }
        return -1;
    }
    
    protected int getCntrY()
    {
        cntrFull    =   false;
        for(int i = cntrY; i < cntrY + cntrSize; i++)
        {
            if (buttons[cntrX-1][i-1].getText().equals(defaultText))
            {
                return i;
            }
        }
        cntrFull    =   true;
        for(int i = 1; i <= xPos; i++)
        {
            for(int j = 1; j <= yPos; j++)
            {
                if(buttons[i-1][j-1].getText().equals(defaultText) &&
                   buttons[i-1][j-1].getBackground().getRed() == 255 && 
                   buttons[i-1][j-1].getBackground().getGreen() == 255 && 
                   buttons[i-1][j-1].getBackground().getBlue() == 255)
                {
                    return j;
                }
            }
        }
        return -1;
    }

    protected int getWaitX()
    {
        if (!waitFull)
        {
            return waitX;
        }
        else
        {
            for(int i = 1; i <= xPos; i++)
            {
                for(int j = 1; j <= yPos; j++)
                {
                    if(buttons[i-1][j-1].getText().equals(defaultText) &&
                       buttons[i-1][j-1].getBackground().getRed() == 255 && 
                       buttons[i-1][j-1].getBackground().getGreen() == 255 && 
                       buttons[i-1][j-1].getBackground().getBlue() == 255)
                    {
                        return i;
                    }
                }
            }
        }
        return -1;
    }
    
    protected int getWaitY()
    {
        waitFull    =   false;
        for(int i = waitY; i < waitY + waitSize; i++)
        {
            if (buttons[waitX-1][i-1].getText().equals(defaultText))
            {
                return i;
            }
        }
        waitFull    =   true;
        for(int i = 1; i <= xPos; i++)
        {
            for(int j = 1; j <= yPos; j++)
            {
                if(buttons[i-1][j-1].getText().equals(defaultText) &&
                   buttons[i-1][j-1].getBackground().getRed() == 255 && 
                   buttons[i-1][j-1].getBackground().getGreen() == 255 && 
                   buttons[i-1][j-1].getBackground().getBlue() == 255)
                {
                    return j;
                }
            }
        }
        return -1;
    }
    
    protected int getWaiterX()
    {
         for(int j = 1; j <= yPos; j++)
         {
	     for(int i = 10; i <= xPos; i++)
            {
                if(buttons[i-1][j-1].getText().equals(defaultText) &&
                   buttons[i-1][j-1].getBackground().getRed() == 255 && 
                   buttons[i-1][j-1].getBackground().getGreen() == 255 && 
                   buttons[i-1][j-1].getBackground().getBlue() == 255)
                {
                    waiterX =   i;
                    waiterY =   j;
                    return waiterX;
                }
            }
        }
        return waiterX;
    }
    
    protected int getWaiterY()
    {
        return waiterY;
    }
    
    protected void placeWaiter(int x, int y, Color color, String name)
    {
        buttons[x-1][y-1].setForeground(color);
        buttons[x-1][y-1].setText(name);
    }
    
    protected void moveWaiter(int oldx, int oldy, int newx, int newy, Color color, String name)
    {
        buttons[oldx-1][oldy-1].setForeground(new Color(255, 255, 255));
        buttons[oldx-1][oldy-1].setText(defaultText);
        buttons[newx-1][newy-1].setForeground(color);
        buttons[newx-1][newy-1].setText(name);
        try
        {
            Thread.sleep(animDelay);
        }
        catch(Exception e) {}
    }
  
    protected void moveWaiterCustomer(int oldx, int oldy, int newx, int newy, Color color, String waiterName, String customerName)
    {
        buttons[oldx-1][oldy-1].setForeground(new Color(255, 255, 255));
        buttons[oldx-1][oldy-1].setText(defaultText);
        buttons[newx-1][newy-1].setForeground(color);
        buttons[newx-1][newy-1].setText(waiterName + customerName);
        try
        {
            Thread.sleep(animDelay);
        }
        catch(Exception e) {}
    }
    
    protected void moveWaiterFood(int oldx, int oldy, int newx, int newy, Color color, String waiterName, String foodName)
    {
        buttons[oldx-1][oldy-1].setForeground(new Color(255, 255, 255));
        buttons[oldx-1][oldy-1].setText(defaultText);
        buttons[newx-1][newy-1].setForeground(color);
        buttons[newx-1][newy-1].setText(waiterName + foodName);
        try
        {
            Thread.sleep(animDelay);
        }
        catch(Exception e) {}
    }

    protected void placeCustomer(int x, int y, Color color, String name)
    {
        buttons[x-1][y-1].setForeground(color);
        buttons[x-1][y-1].setText(name);
        try
        {
            Thread.sleep(animDelay);
        }
        catch(Exception e) {}
    }
    
    protected void removeCustomer(int x, int y)
    {
        buttons[x-1][y-1].setForeground(new Color(0, 0, 0));
        buttons[x-1][y-1].setText(defaultText);
        try
        {
            Thread.sleep(animDelay);
        }
        catch(Exception e) {}
    }
    
    protected void placeFood(int x, int y, Color color, String name)
    {
        buttons[x-1][y-1].setForeground(color);
        buttons[x-1][y-1].setText(name);
        try
        {
            Thread.sleep(animDelay);
        }
        catch(Exception e) {}
    }
    
    protected void removeFood(int x, int y)
    {
        buttons[x-1][y-1].setForeground(buttons[x-1][y-1].getBackground());
        buttons[x-1][y-1].setText(defaultText);
        try
        {
            Thread.sleep(animDelay);
        }
        catch(Exception e) {}
    }   
}
