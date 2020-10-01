package restaurant;

import agent.Agent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.*;
import restaurant.layoutGUI.*;
import restaurant.gui.RestaurantGui;
import java.awt.Color;

import restaurant.PowerloomHelper;
import edu.isi.powerloom.*;
import edu.isi.powerloom.logic.*;
import edu.isi.stella.Module;
import edu.isi.stella.javalib.*;
import edu.isi.stella.Stella_Object;

/** Cook agent for restaurant.
 *  Keeps a list of orders for waiters
 *  and simulates cooking them.
 *  Interacts with waiters only.
 */
public class CookAgent extends Agent {

    /** Private class to store order information.
     *  Contains the waiter, table number, food item,
     *  cooktime and state.
     */
    private class Order {
	public WaiterAgent waiter;
	public int tableNum;
	public String choice;
	public double cookTime;
	public boolean pending = false; //True if order is cooking already
	public Food food;

	/** Constructor for Order class 
	 * @param waiter waiter that this order belongs to
	 * @param tableNum identification number for the table
	 * @param choice type of food to be cooked 
	 */
	public Order(WaiterAgent waiter, int tableNum, String choice){
	    this.waiter = waiter;
	    this.choice = choice;
	    this.tableNum = tableNum;
	    cookTime =10;//default
	    if(choice.equalsIgnoreCase("steak")) cookTime = 5;
	    if(choice.equalsIgnoreCase("chicken")) cookTime = 4;
	    if(choice.equalsIgnoreCase("pizza")) cookTime = 3;
	    if(choice.equalsIgnoreCase("salad")) cookTime = 2;
	}

	/** Represents the object as a string */
	public String toString(){
	    return choice + " for " + waiter + "(" + cookTime*1000 + " milliseconds)";
	}
    }

    //List of all the orders
    private ArrayList<Order> orders = new ArrayList<Order>();

    //Name of the cook
    private String name;
    
    //Timer for simulation
    Timer timer = new Timer();
    Restaurant restaurant; //Gui layout
    RestaurantGui gui;
    String loomInstance; //for this cook

    /** Constructor for CookAgent class
     * @param name name of the cook
     */
    public CookAgent(String name, Restaurant restaurant,RestaurantGui gui) {
	super();
	
	this.name = name;
	this.restaurant = restaurant;
	this.gui = gui;
	//Powerloom for cook's constructor
	loomInstance = PowerloomHelper.instantiate("cook",this);
	PLI.sAssertProposition("(hasEmployee " + gui.getrestaurantInstance() +
			       " " + loomInstance+")",
			       PowerloomHelper.getWorkingModule(), null);
    }
    


    // *** MESSAGES ***

    /** Message from a waiter giving the cook a new order.
     * @param waiter waiter that the order belongs to
     * @param tableNum identification number for the table
     * @param choice type of food to be cooked
     */
    public void msgHereIsAnOrder(WaiterAgent waiter, int tableNum, String choice){
	orders.add(new Order(waiter, tableNum, choice));
	stateChanged();
    }


    /** Scheduler.  Determine what action is called for, and do it. */
    protected boolean pickAndExecuteAnAction() {
	
	//Runs through each order and if its not already cooking,
	//then start cooking it. No limit to simultaneous orders.
	for(Order o:orders){
	    if(!o.pending){
		cookOrder(o);
		return true;
	    }
	}
	//we have tried all our rules (in this case only one) and found
	//nothing to do. So return false to main loop of abstract agent
	//and wait.
	return false;
    }
    

    // *** ACTIONS ***
    
    /** Starts a timer for the order that needs to be cooked. 
     * @param order
     */
    private void cookOrder(final Order order){
	Do("Cooking order " + order);
	//put it on the grill. gui stuff
	order.food = new Food(order.choice.substring(0,2),new Color(0,255,255), restaurant);
	order.food.cookFood();
	order.pending = true;
	
	timer.schedule(new TimerTask(){
	    public void run(){		    
		print("Order finished!");
		order.food.placeOnCounter();
		order.waiter.msgOrderIsReady(order.tableNum, order.food);
					 //order.food.getX(),order.food.getY());
		orders.remove(order);
		stateChanged();
	    }
	}, (int)order.cookTime*1000);
    }


    // *** EXTRA ***

    /** Returns the name of the cook */
    public String getName(){
        return name;
    }   
    
}


    
