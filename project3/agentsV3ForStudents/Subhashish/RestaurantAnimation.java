import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.math.*;

/*************** CODES IN THE GRID ***************

    0 - Empty
    1 - Waiting Area
    2 - Table
    3 - Counter
    4 - Grill
    5 - Waiter
    6 - Waiter with Food
    7 - Waiter with Customer
    8 - Customer
    9 - Food

*************************************************/

class TableData
{
    int pos_x;
    int pos_y;
    int size;
}

class LocationData
{
    int pos_x;
    int pos_y;
}

class GenericData
{
    int pos_x;
    int pos_y;
    int rows;
    int cols;
}

class RestaurantAnimation extends JFrame
{
    static final int animationDelay = 500;

    static final Color COLOR_EMPTY                = Color.CYAN;
    static final Color COLOR_WAITING_AREA         = Color.LIGHT_GRAY;
    static final Color COLOR_TABLE                = Color.GREEN;
    static final Color COLOR_COUNTER              = new Color(219, 170, 247);
    static final Color COLOR_GRILL                = Color.PINK;
    static final Color COLOR_WAITER               = Color.ORANGE;
    static final Color COLOR_WAITER_WITH_FOOD     = Color.BLUE;
    static final Color COLOR_WAITER_WITH_CUSTOMER = Color.RED;
    static final Color COLOR_CUSTOMER             = Color.GRAY;
    static final Color COLOR_FOOD                 = Color.MAGENTA;

    static final int CODE_EMPTY                = 0;
    static final int CODE_WAITING_AREA         = 1;
    static final int CODE_TABLE                = 2;
    static final int CODE_COUNTER              = 3;
    static final int CODE_GRILL                = 4;
    static final int CODE_WAITER               = 5;
    static final int CODE_WAITER_WITH_FOOD     = 6;
    static final int CODE_WAITER_WITH_CUSTOMER = 7;
    static final int CODE_CUSTOMER             = 8;
    static final int CODE_FOOD                 = 9;

    int numGridRows;
    int numGridCols;
    int [][] gridData;

    GridLayout animationLayout;
    JLabel [][] gridLabels;

    Map<String, TableData> tableDetails;
    Map<String, LocationData> waiterDetails;
    Map<String, LocationData> customerDetails;
    Map<String, LocationData> foodDetails;

    GenericData counterDetails;
    GenericData grillDetails;
    GenericData waitingAreaDetails;

    public RestaurantAnimation(int rows, int cols)
    {
        numGridRows = rows;
        numGridCols = cols;

        tableDetails    = new HashMap<String, TableData>();
        waiterDetails   = new HashMap<String, LocationData>();
        customerDetails = new HashMap<String, LocationData>();
        foodDetails     = new HashMap<String, LocationData>();

        counterDetails     = new GenericData();
        grillDetails       = new GenericData();
        waitingAreaDetails = new GenericData();

        gridData = new int[numGridRows][numGridCols];

        for (int row_pos=0; row_pos < numGridRows; row_pos++) 
        {
            for (int col_pos=0; col_pos < numGridCols; col_pos++)
            {
                gridData[row_pos][col_pos] = CODE_EMPTY;
            }
        }

        animationLayout = new GridLayout(numGridRows, numGridCols, 1, 1);
        getContentPane().setLayout(animationLayout);

        gridLabels = new JLabel[numGridRows][numGridCols];

        for (int row_pos=0; row_pos < numGridRows; row_pos++) 
        {
            for (int col_pos=0; col_pos < numGridCols; col_pos++)
            {
                gridLabels[row_pos][col_pos] = new JLabel("     ");

                gridLabels[row_pos][col_pos].setOpaque(true);
                gridLabels[row_pos][col_pos].setBackground(COLOR_EMPTY);

                getContentPane().add(gridLabels[row_pos][col_pos]);
            }
        }

        addWindowListener(new WindowAdapter() 
	{
            public void windowClosing(WindowEvent e) 
            {
                System.exit(0);
            }
        });
    }


    public void Table(int tableID, int size, int table_x, int table_y)
    {
        TableData temp_data = new TableData();

        temp_data.pos_x = table_x;
        temp_data.pos_y = table_y;
        temp_data.size  = size;

        String str_table_ID = Integer.toString(tableID);
        tableDetails.put(str_table_ID, temp_data);

        for (int pos_x = table_x; pos_x < table_x+size; pos_x++)
        {
            for (int pos_y = table_y; pos_y < table_y+size; pos_y++)
            {
                gridLabels[pos_x][pos_y].setBackground(COLOR_TABLE);
                gridData[pos_x][pos_y] = CODE_TABLE;
            }
        }
    }

    public void Waiter(int waiterID, int waiter_x, int waiter_y)
    {
        LocationData temp_data = new LocationData();

        temp_data.pos_x = waiter_x;
        temp_data.pos_y = waiter_y;

        String str_waiter_ID = Integer.toString(waiterID);
        waiterDetails.put(str_waiter_ID, temp_data);

        gridLabels[waiter_x][waiter_y].setBackground(COLOR_WAITER);
        gridData[waiter_x][waiter_y] = CODE_WAITER;
    }

    public void Customer(int customerID, int customer_x, int customer_y)
    {
        LocationData temp_data = new LocationData();

        temp_data.pos_x = customer_x;
        temp_data.pos_y = customer_y;

        String str_customer_ID = Integer.toString(customerID);
        customerDetails.put(str_customer_ID, temp_data);

        gridLabels[customer_x][customer_y].setBackground(COLOR_CUSTOMER);
        gridData[customer_x][customer_y] = CODE_CUSTOMER;
    }

    public void Counter(int rows, int cols, int counter_x, int counter_y)
    {
        counterDetails.pos_x = counter_x;
        counterDetails.pos_y = counter_y;
        counterDetails.rows  = rows;
        counterDetails.cols  = cols;

        for (int pos_x = counter_x; pos_x < counter_x+rows; pos_x++)
        {
            for (int pos_y = counter_y; pos_y < counter_y+cols; pos_y++)
            {
                gridLabels[pos_x][pos_y].setBackground(COLOR_COUNTER);
                gridData[pos_x][pos_y] = CODE_COUNTER;
            }
        }
    }

    public void Grill(int rows, int cols, int grill_x, int grill_y)
    {
        grillDetails.pos_x = grill_x;
        grillDetails.pos_y = grill_y;
        grillDetails.rows  = rows;
        grillDetails.cols  = cols;

        for (int pos_x = grill_x; pos_x < grill_x+rows; pos_x++)
        {
            for (int pos_y = grill_y; pos_y < grill_y+cols; pos_y++)
            {
                gridLabels[pos_x][pos_y].setBackground(COLOR_GRILL);
                gridData[pos_x][pos_y] = CODE_GRILL;
            }
        }
    }

    public void WaitingArea(int rows, int cols, int waiting_x, int waiting_y)
    {
        waitingAreaDetails.pos_x = waiting_x;
        waitingAreaDetails.pos_y = waiting_y;
        waitingAreaDetails.rows  = rows;
        waitingAreaDetails.cols  = cols;

        for (int pos_x = waiting_x; pos_x < waiting_x+rows; pos_x++)
        {
            for (int pos_y = waiting_y; pos_y < waiting_y+cols; pos_y++)
            {
                gridLabels[pos_x][pos_y].setBackground(COLOR_WAITING_AREA);
                gridData[pos_x][pos_y] = CODE_WAITING_AREA;
            }
        }
    }

    public void MoveWaiter(int waiterID, int new_x, int new_y)
    {
        try 
        {
            Thread.sleep(animationDelay);
        } 
        catch (Exception e) 
        {
            System.out.println("Exception occured : " + e);
        }

        String str_waiter_ID = Integer.toString(waiterID);

        LocationData old_position = new LocationData();
        LocationData new_position = new LocationData();

        old_position = waiterDetails.remove(str_waiter_ID);

        new_position.pos_x = new_x;
        new_position.pos_y = new_y;
        waiterDetails.put(str_waiter_ID, new_position);

        gridLabels[old_position.pos_x][old_position.pos_y].setBackground(COLOR_EMPTY);
        gridData[old_position.pos_x][old_position.pos_y] = CODE_EMPTY;

        gridLabels[new_x][new_y].setBackground(COLOR_WAITER);
        gridData[new_x][new_y] = CODE_WAITER;
    }

    public void CookFoodOnGrill(int foodID, int food_x, int food_y)
    {
        LocationData temp_data = new LocationData();

        temp_data.pos_x = food_x;
        temp_data.pos_y = food_y;

        String str_food_ID = Integer.toString(foodID);
        foodDetails.put(str_food_ID, temp_data);

        gridLabels[food_x][food_y].setBackground(COLOR_FOOD);
        gridData[food_x][food_y] = CODE_FOOD;
    }

    public void PickAndPlaceFood(int foodID, int counter_x, int counter_y)
    {
        try 
        {
            Thread.sleep(animationDelay);
        } 
        catch (Exception e) 
        {
            System.out.println("Exception occured : " + e);
        }

        String str_food_ID = Integer.toString(foodID);

        LocationData old_position = new LocationData();
        LocationData new_position = new LocationData();

        old_position = foodDetails.remove(str_food_ID);

        new_position.pos_x = counter_x;
        new_position.pos_y = counter_y;
        foodDetails.put(str_food_ID, new_position);

        gridLabels[old_position.pos_x][old_position.pos_y].setBackground(COLOR_GRILL);
        gridData[old_position.pos_x][old_position.pos_y] = CODE_GRILL;

        gridLabels[counter_x][counter_y].setBackground(COLOR_FOOD);
        gridData[counter_x][counter_y] = CODE_FOOD;
    }

    public void PickFoodFromCounter(int waiterID, int foodID, int counter_x, int counter_y)
    {
        try 
        {
            Thread.sleep(animationDelay);
        } 
        catch (Exception e) 
        {
            System.out.println("Exception occured : " + e);
        }

        String str_food_ID = Integer.toString(foodID);
        String str_waiter_ID = Integer.toString(waiterID);

        foodDetails.remove(str_food_ID);
        gridLabels[counter_x][counter_y].setBackground(COLOR_COUNTER);
        gridData[counter_x][counter_y] = CODE_COUNTER;

        LocationData waiter_position = new LocationData();
        waiter_position = waiterDetails.get(str_waiter_ID);

        gridLabels[waiter_position.pos_x][waiter_position.pos_y].setBackground(COLOR_WAITER_WITH_FOOD);
        gridData[waiter_position.pos_x][waiter_position.pos_y] = CODE_WAITER_WITH_FOOD;
    }

    public void MoveWithFood(int waiterID, int foodID, int new_x, int new_y)
    {
        try 
        {
            Thread.sleep(animationDelay);
        } 
        catch (Exception e) 
        {
            System.out.println("Exception occured : " + e);
        }

        String str_waiter_ID = Integer.toString(waiterID);

        LocationData old_position = new LocationData();
        LocationData new_position = new LocationData();

        old_position = waiterDetails.remove(str_waiter_ID);

        new_position.pos_x = new_x;
        new_position.pos_y = new_y;
        waiterDetails.put(str_waiter_ID, new_position);

        gridLabels[old_position.pos_x][old_position.pos_y].setBackground(COLOR_EMPTY);
        gridData[old_position.pos_x][old_position.pos_y] = CODE_EMPTY;

        gridLabels[new_x][new_y].setBackground(COLOR_WAITER_WITH_FOOD);
        gridData[new_x][new_y] = CODE_WAITER_WITH_FOOD;
    }

    public void PlaceFoodOnTable(int waiterID, int foodID, int tableID)
    {
        try 
        {
            Thread.sleep(animationDelay);
        } 
        catch (Exception e) 
        {
            System.out.println("Exception occured : " + e);
        }

        String str_waiter_ID = Integer.toString(waiterID);
        String str_table_ID  = Integer.toString(tableID);

        LocationData waiter_position = new LocationData();
        TableData table_position     = new TableData();

        waiter_position = waiterDetails.get(str_waiter_ID);
        table_position  = tableDetails.get(str_table_ID);

        for (int pos_x = table_position.pos_x; pos_x < (table_position.pos_x + table_position.size); pos_x++) 
        {
            for (int pos_y = table_position.pos_y; pos_y < (table_position.pos_y + table_position.size); pos_y++)
            {
                if (gridData[pos_x][pos_y] == CODE_TABLE)
                {
                    gridLabels[pos_x][pos_y].setBackground(COLOR_FOOD);
                    gridData[pos_x][pos_y] = CODE_FOOD;

                    gridLabels[waiter_position.pos_x][waiter_position.pos_y].setBackground(COLOR_WAITER);
                    gridData[waiter_position.pos_x][waiter_position.pos_y] = CODE_WAITER;

                    return;
                }
            }
        }
    }

    public void PickCustomer(int waiterID, int customerID, int customer_x, int customer_y)
    {
        try 
        {
            Thread.sleep(animationDelay);
        } 
        catch (Exception e) 
        {
            System.out.println("Exception occured : " + e);
        }

        String str_customer_ID = Integer.toString(customerID);
        String str_waiter_ID   = Integer.toString(waiterID);

        customerDetails.remove(str_customer_ID);
        gridLabels[customer_x][customer_y].setBackground(COLOR_WAITING_AREA);
        gridData[customer_x][customer_y] = CODE_WAITING_AREA;

        LocationData waiter_position = new LocationData();
        waiter_position = waiterDetails.get(str_waiter_ID);

        gridLabels[waiter_position.pos_x][waiter_position.pos_y].setBackground(COLOR_WAITER_WITH_CUSTOMER);
        gridData[waiter_position.pos_x][waiter_position.pos_y] = CODE_WAITER_WITH_CUSTOMER;
    }

    public void MoveWithCustomer(int waiterID, int customerID, int new_x, int new_y)
    {
        try 
        {
            Thread.sleep(animationDelay);
        } 
        catch (Exception e) 
        {
            System.out.println("Exception occured : " + e);
        }

        String str_waiter_ID = Integer.toString(waiterID);

        LocationData old_position = new LocationData();
        LocationData new_position = new LocationData();

        old_position = waiterDetails.remove(str_waiter_ID);

        new_position.pos_x = new_x;
        new_position.pos_y = new_y;
        waiterDetails.put(str_waiter_ID, new_position);

        gridLabels[old_position.pos_x][old_position.pos_y].setBackground(COLOR_EMPTY);
        gridData[old_position.pos_x][old_position.pos_y] = CODE_EMPTY;

        gridLabels[new_x][new_y].setBackground(COLOR_WAITER_WITH_CUSTOMER);
        gridData[new_x][new_y] = CODE_WAITER_WITH_CUSTOMER;
    }

    public void PlaceCustomer(int waiterID, int customerID, int tableID)
    {
        try 
        {
            Thread.sleep(animationDelay);
        } 
        catch (Exception e) 
        {
            System.out.println("Exception occured : " + e);
        }

        String str_waiter_ID = Integer.toString(waiterID);
        String str_table_ID  = Integer.toString(tableID);

        LocationData waiter_position = new LocationData();
        TableData table_position     = new TableData();

        waiter_position = waiterDetails.get(str_waiter_ID);
        table_position  = tableDetails.get(str_table_ID);

        for (int pos_x = table_position.pos_x; pos_x < (table_position.pos_x + table_position.size); pos_x++) 
        {
            for (int pos_y = table_position.pos_y; pos_y < (table_position.pos_y + table_position.size); pos_y++)
            {
                if (gridData[pos_x][pos_y] == CODE_TABLE)
                {
                    gridLabels[pos_x][pos_y].setBackground(COLOR_CUSTOMER);
                    gridData[pos_x][pos_y] = CODE_CUSTOMER;

                    gridLabels[waiter_position.pos_x][waiter_position.pos_y].setBackground(COLOR_WAITER);
                    gridData[waiter_position.pos_x][waiter_position.pos_y] = CODE_WAITER;

                    return;
                }
            }
        }
    }

    public void LeavingTable(int tableID)
    {
        try 
        {
            Thread.sleep(animationDelay);
        } 
        catch (Exception e) 
        {
            System.out.println("Exception occured : " + e);
        }

        String str_table_ID      = Integer.toString(tableID);
        TableData table_position = new TableData();
        table_position           = tableDetails.get(str_table_ID);

        for (int pos_x = table_position.pos_x; pos_x < (table_position.pos_x + table_position.size); pos_x++) 
        {
            for (int pos_y = table_position.pos_y; pos_y < (table_position.pos_y + table_position.size); pos_y++)
            {
                if (gridData[pos_x][pos_y] != CODE_TABLE)
                {
                    gridLabels[pos_x][pos_y].setBackground(COLOR_TABLE);
                    gridData[pos_x][pos_y] = CODE_TABLE;
                }
            }
        }
    }

    public void Debug()
    {
        for (int row_pos=0; row_pos < numGridRows; row_pos++) 
        {
            for (int col_pos=0; col_pos < numGridCols; col_pos++)
            {
                gridLabels[row_pos][col_pos].setText(Integer.toString(gridData[row_pos][col_pos]));
            }
        }
    }
}

