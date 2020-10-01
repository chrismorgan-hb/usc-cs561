package restaurant;

import restaurant.gui.RestaurantGui;
import restaurant.layoutGUI.*;
import agent.Agent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;
import java.awt.Color;

/** Restaurant customer agent. 
 * Comes to the restaurant when he/she becomes hungry.
 * Randomly chooses a menu item and simulates eating 
 * when the food arrives. 
 * Interacts with a waiter only */
public class CustomerAgent extends Agent {
    private String name;
    private int hungerLevel = 5;  // Determines length of meal
    private RestaurantGui gui;
    
    
    // ** Agent connections **
    private HostAgent host;
    private WaiterAgent waiter;
    Restaurant restaurant;
    private Menu menu;
    Timer timer = new Timer();
    Customer guiCustomer;
   // ** Agent state **
    private boolean isHungry = false;
    public enum AgentState 
	{NEED_SEATED,NEED_DECIDE,NEED_ORDER,NEED_EAT,NEED_LEAVE,NO_ACTION};
    private AgentState state = AgentState.NO_ACTION;
    
    /** Constructor for CustomerAgent class 
     * @param name name of the customer
     * @param gui reference to the gui so the customer can send it messages
     */
    public CustomerAgent(String name, RestaurantGui gui, Restaurant restaurant) {
	super();
	this.gui = gui;
	this.name = name;
	this.restaurant = restaurant;
	guiCustomer = new Customer(name.substring(0,2), new Color(0,255,0), restaurant);
    }
    public CustomerAgent(String name, Restaurant restaurant) {
	super();
	this.gui = null;
	this.name = name;
	this.restaurant = restaurant;
	guiCustomer = new Customer(name.substring(0,1), new Color(0,255,0), restaurant);
    }


    // *** MESSAGES ***

    /** Waiter sends this message so the customer knows to sit down 
     * @param waiter the waiter that sent the message
     * @param menu a reference to a menu */
    public void msgFollowMeToTable(WaiterAgent waiter, Menu menu) {
	this.menu = menu;
	this.waiter = waiter;
	print("Received msgFollowMeToTable");
	state = AgentState.NEED_DECIDE;
	stateChanged();
    }

    /** Waiter sends this message to take the customer's order */
    public void msgWhatWouldYouLike(){
	state = AgentState.NEED_ORDER;
	stateChanged();
    }

    /** Waiter sends this when the food is ready 
     * @param choice the food that is done cooking for the customer to eat */
    public void msgHereIsYourFood(String choice) {
	state = AgentState.NEED_EAT;
	stateChanged();
    }

    /** Sent from GUI to set the customer as hungery */
    public void setHungry() {
	isHungry = true;
	state = AgentState.NEED_SEATED;
	print("I'm hungry");
	stateChanged();
    }

    /** Scheduler.  Determine what action is called for, and do it. */
    protected boolean pickAndExecuteAnAction() {

	//Simple linear state machine
	if (state == AgentState.NEED_SEATED) {
	    goToRestaurant();
	    return true;
	}
	if (state == AgentState.NEED_DECIDE) {
	    decideMenuChoice();
	    return true;
	}
	if (state == AgentState.NEED_ORDER) {
	    orderFood();
	    return true;
	}
	if (state == AgentState.NEED_EAT) {
	    startEating();
	    return true;
	}
	if (state == AgentState.NEED_LEAVE) {
	    leaveRestaurant();
	    return true;
	}

	return false;
    }
    
    // *** ACTIONS ***
    
    /** Goes to the restaurant when the customer becomes hungry */
    private void goToRestaurant() {
	print("Going to restaurant");
	guiCustomer.appearInWaitingQueue();
	host.msgIWantToEat(this);//send him our instance, so he can respond to us
	state = AgentState.NO_ACTION;
	stateChanged();
    }
    
    /** Starts a timer to simulate the customer thinking about the menu */
    private void decideMenuChoice(){
	print("Deciding menu choice...(3000 milliseconds)");
	state = AgentState.NO_ACTION;
	timer.schedule(new TimerTask() {
	    public void run() {  
		decided();		    
	    }},
	    3000);//how long to wait before running task
	stateChanged();
    }

    /** Function that is called when the decideMenu timer has expired
     *	Sends a message to the waiter that the customer is ready to order */
    private void decided(){
	print("I decided!");	
	waiter.msgImReadyToOrder(this);
	stateChanged();
    }

    /** Picks a random choice from the menu and sends it to the waiter */
    private void orderFood(){
	String choice = menu.choices[(int)(Math.random()*4)];
	print("Ordering the " + choice);
	waiter.msgHereIsMyChoice(this, choice);
	state = AgentState.NO_ACTION;
	stateChanged();
    }


    /** Starts a timer to simulate eating */
    private void startEating() {
	print("Eating for " + hungerLevel*1000 + " milliseconds.");
	state = AgentState.NO_ACTION;
	timer.schedule(new TimerTask() {
	    public void run() {  
		print("Done eating");
		state = AgentState.NEED_LEAVE;
		stateChanged();		    
	    }},
	    getHungerLevel() * 1000);//how long to wait before running task
	stateChanged();
    }
    

    /** When the customer is done eating, he leaves the restaurant */
    private void leaveRestaurant() {
	print("Leaving the restaurant");
	guiCustomer.leave();
	waiter.msgDoneEatingAndLeaving(this);
	isHungry = false;
	state = AgentState.NO_ACTION;
	stateChanged();
	gui.setCustomerEnabled(this); //Message to gui to enable hunger button

	//hack to keep customer getting hungry. Only for non-gui customers
	if (gui==null) becomeHungryInAWhile();//set a timer to make us hungry.
    }
    

    /** This starts a timer so the customer will become hungry again.
     * This is a hack that is used when the GUI is not being used */
    private void becomeHungryInAWhile() {
	timer.schedule(new TimerTask() {
	    public void run() {  
		setHungry();		    
	    }},
	    15000);//how long to wait before running task
    }
    

    // *** EXTRA ***

    /** hack to establish connection to host agent. 
     * @param host reference to the host */
    public void setHost(HostAgent host) {
		this.host = host;
    }
    
    /** Returns the customer's name
     *@return name of customer */
    public String getName() {
	return name;
    }

    /** @return true if the customer is hungery,
     *          false otherwise.*/
    public boolean isHungry() {
	return isHungry;
    }

    /** @return the hungerlevel of the customer */
    public int getHungerLevel() {
	return hungerLevel;
    }
    
    /** Sets the customer's hungerlevel to a new value
     * @param hungerLevel the new hungerlevel for the customer */
    public void setHungerLevel(int hungerLevel) {
	this.hungerLevel = hungerLevel; 
    }
    public Customer getGuiCustomer(){
	return guiCustomer;
    }
    
    /** @return the string representation of the class */
    public String toString() {
	return "customer " + getName();
    }

    
}

