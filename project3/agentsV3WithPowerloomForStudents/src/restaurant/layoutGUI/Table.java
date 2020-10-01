package restaurant.layoutGUI;

import java.awt.*;

public class Table
{
    private int x, y, size;
    private String name;
    private Restaurant restaurant;
    private String order;
    
    public Table(String name, int x, int y, int size, Restaurant restaurant)
    {
        this.name       =   name;
        this.x          =   x;
        this.y          =   y;
        this.size       =   size;
        this.restaurant =   restaurant;
        this.placeTable();
    }
    
    public int getX()
    {
        return x;
    }
    
    public int getY()
    {
        return y;
    }
    
    public int seatX()
    {
        return x;
    }
    
    public int seatY()
    {
        return y+1;
    }
    
    public int foodX()
    {
        return x+1;
    }
    
    public int foodY()
    {
        return y+1;
    }
    
    protected void placeTable()
    {
        restaurant.addTable(name, x, y, size);
    }
    public void takeOrder(String order)
    {
	this.order      =   order;
	restaurant.placeFood(this.foodX(), this.foodY(),
			     new Color(255, 255, 255), order);
    }
    public String getOrder()
    {
	return order;
    }
}
