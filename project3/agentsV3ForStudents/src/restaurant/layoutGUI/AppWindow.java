package restaurant.layoutGUI;

import java.awt.*;
import restaurant.layoutGUI.Restaurant;
import restaurant.layoutGUI.Table;
import restaurant.layoutGUI.Waiter;
import restaurant.layoutGUI.Customer;
import restaurant.layoutGUI.Food;

public class AppWindow
{   
    public static void main(String args[])
    {
        Restaurant restaurant   =   new Restaurant("Welcome to My Restaurant", 20, 15, true);
        
        Waiter waiter1          =   new Waiter("W1", new Color(255, 0, 0), restaurant);
        Waiter waiter2          =   new Waiter("W2", new Color(255, 0, 0), restaurant);
        Waiter waiter3          =   new Waiter("W3", new Color(255, 0, 0), restaurant);
        
        Customer customer1      =   new Customer("C1", new Color(0, 255, 0), restaurant);
        Customer customer2      =   new Customer("C2", new Color(0, 255, 0), restaurant);
        Customer customer3      =   new Customer("C3", new Color(0, 255, 0), restaurant);

        Food food1              =   new Food("F1", new Color(0, 0, 255), restaurant);
        Food food2              =   new Food("F2", new Color(0, 0, 255), restaurant);
        Food food3              =   new Food("F3", new Color(0, 0, 255), restaurant);

        Table table1            =   new Table("T1", 5, 3, 3, restaurant);
        Table table2            =   new Table("T2", 5, 8, 3, restaurant);
        Table table3            =   new Table("T3", 10, 3, 3, restaurant);
        Table table4            =   new Table("T4", 10, 9, 3, restaurant);
        
        restaurant.setAnimDelay(250);
        restaurant.addWaitArea(2, 2, 5);
        restaurant.addCounter(17, 2, 4);
        restaurant.addGrill(19, 3, 10);
        restaurant.displayRestaurant();

        customer1.appearInWaitingQueue();
        customer2.appearInWaitingQueue();
        customer3.appearInWaitingQueue();

        food1.cookFood();
        food2.cookFood();
        food1.placeOnCounter();
        food3.cookFood();
        
        waiter1.move(2, 1);
        waiter1.move(3, 1);
        waiter1.move(3, 2);
        waiter1.pickUpCustomer(customer1);
        
        waiter1.move(4, 2);
        waiter1.move(5, 2);
        waiter1.seatCustomer(table1);
        
        for (int i = 6; i <= 16; i++)
            waiter1.move(i, 2);
        waiter1.pickUpFood(food1);
        
        for (int i = 16; i >= 7; i--)
            waiter1.move(i, 2);
        waiter1.serveFood(table1);
        
        for (int i = 6; i >= 3; i--)
            waiter1.move(i, 2);
        waiter1.move(3, 3);
        food1.remove();
        customer1.leave();
        food2.placeOnCounter();
    }
}
