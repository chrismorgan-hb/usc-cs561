import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.math.*;

class TryAnimation
{
    public static void main(String args[])
    {
        RestaurantAnimation window = new RestaurantAnimation(40,40);
		
        java.awt.Point p = new java.awt.Point();
        p.setLocation(40,40);
        window.setLocation(p);

        window.setTitle("Restaurant Animation");
        window.setResizable(false);
        window.pack();
        window.setVisible(true);

        window.Table(1, 5, 5, 5);
        window.Table(2, 5, 5, 30);
        window.Table(3, 5, 23, 5);
        window.Table(4, 5, 23, 30);
        window.Table(5, 6, 14, 17);

        window.Counter(2, 30, 33, 5);
        window.Grill(3, 34, 37, 3);
        window.WaitingArea(1, 32, 0, 4);

        window.CookFoodOnGrill(1, 38, 12);
        window.CookFoodOnGrill(2, 37, 21);

        window.Customer(1, 0, 8);
        window.Customer(2, 0, 18);

        window.Waiter(1, 9, 11);
        window.Waiter(2, 31, 10);

        window.MoveWaiter(1, 8, 11);
        window.MoveWaiter(1, 7, 11);
        window.MoveWaiter(1, 6, 11);
        window.MoveWaiter(1, 5, 11);
        window.MoveWaiter(1, 4, 11);
        window.MoveWaiter(1, 3, 11);
        window.MoveWaiter(1, 3, 10);
        window.MoveWaiter(1, 3, 9);
        window.MoveWaiter(1, 3, 8);
        window.MoveWaiter(1, 2, 8);
        window.MoveWaiter(1, 1, 8);

        window.PickCustomer(1, 1, 0, 8);

        window.MoveWithCustomer(1, 1, 1, 9);
        window.MoveWithCustomer(1, 1, 1, 10);
        window.MoveWithCustomer(1, 1, 1, 11);
        for (int pos = 2; pos < 25; pos++) 
        {
            window.MoveWithCustomer(1, 1, pos, 11);
        }
        window.MoveWithCustomer(1, 1, 24, 10);

        window.PlaceCustomer(1, 1, 3);

        window.MoveWaiter(1, 24, 11);
        window.MoveWaiter(1, 24, 12);
        window.MoveWaiter(1, 24, 13);

        window.PickAndPlaceFood(1, 33, 10);
        window.PickAndPlaceFood(2, 34, 20);

        window.MoveWaiter(2, 32, 10);

        window.PickFoodFromCounter(2, 1, 33, 10);

        window.MoveWithFood(2, 1, 32, 10); 
        window.MoveWithFood(2, 1, 31, 10); 
        window.MoveWithFood(2, 1, 30, 10); 
        window.MoveWithFood(2, 1, 29, 10); 
        window.MoveWithFood(2, 1, 29, 9); 
        window.MoveWithFood(2, 1, 28, 9); 

        window.PlaceFoodOnTable(2, 1, 3);

        window.MoveWaiter(2, 28, 10);
        window.MoveWaiter(2, 28, 11);
        window.MoveWaiter(2, 28, 12);

        window.Debug();
        //window.LeavingTable(3);
    }
}
