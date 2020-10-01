package restaurant;

import restaurant.gui.RestaurantGui;
import restaurant.layoutGUI.*;
import agent.Agent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;
import java.awt.Color;

import restaurant.PowerloomHelper;
import edu.isi.powerloom.*;
import edu.isi.powerloom.logic.*;
import edu.isi.stella.Module;
import edu.isi.stella.javalib.*;
import edu.isi.stella.Stella_Object;

/** Restaurant customer agent. 
 * Comes to the restaurant when hungry.
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
   // ** Agent variables for Powerloom **
    String loomInstance; //for the Customer
    private String state;
    //These string states come from the AgentState enum below
    private static String NEED_SEATED = "NEED_SEATED";
    private static String NEED_DECIDE = "NEED_DECIDE";
    private static String NEED_ORDER = "NEED_ORDER";
    private static String NEED_EAT = "NEED_EAT";
    private static String NEED_LEAVE = "NEED_LEAVE";
    private static String NO_ACTION = "NO_ACTION";

   // ** Agent state **
    //private boolean isHungry = false;
    //public enum AgentState 
	// {NEED_SEATED,NEED_DECIDE,NEED_ORDER,NEED_EAT,NEED_LEAVE,NO_ACTION};
    //private AgentState state = AgentState.NO_ACTION;
    
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
	//powerloom code...
	//When this constructor is calledd by the gui, two things can happen
	//1) The name of the customer is one we already know from
	//restaurant.plm  In that case, we simply set loomInstance to it.
	//2) If we don't find the customer in restaurant.plm, then we
	//create new instance for him, assert he has a home in our
	//restaurant's neighborhood.
	//We don't know anything about his home,
	//so the go to restaurant rule will fire and he'll be seated.
	
	//First, do we know about this customer? Here's a typical query:
	// (?person)(and (person ?person)(hasname ?person "Joe"))
	//Notice how in the code below, we had to "escape" in the quote
	//marks using the \ character.
	String query =
	   "1 (?person)(and (person ?person)(hasname ?person \"" + name+"\"))";
	loomInstance = PowerloomHelper.retrieve1(query);
	if (loomInstance!=null){
	    PowerloomHelper.getloomMap().put(loomInstance, this);			
	    System.out.println("Found person in knowledge base: " + loomInstance);
	}
	else {
	System.out.println("No customer found. Instantiating...");
	loomInstance = PowerloomHelper.instantiate("person",this);//puts it in map
	//instantiate a home in same neighborhoom as our restaurant,
	//then associate name and home with this instance.
	/*Here is a typical assertion:
	  (and (haslocation HOME-014 northbeach)
	       (lives PERSON-013 HOME-014)
	       (hasname PERSON-013 "c2"))
	*/
	String h = PowerloomHelper.instantiate("home",null);
	query = "(and (haslocation " +h+" "+gui.getneighborhoodInstance()
		+") (lives "+ loomInstance+" "+h+")(hasname "
		+ loomInstance+" \""+name+"\"))";
	print("asserting facts about new instance:"+query);
	PLI.sAssertProposition(query, PowerloomHelper.getWorkingModule(), null);		
	}
	//Set initial State for agent
	PLI.sAssertProposition("(hasCustomerState "+loomInstance+" NO_ACTION)",
				PowerloomHelper.getWorkingModule(), null);
}	
    // *** MESSAGES ***

    /** Sent from GUI to set the customer as hungry */
    //Consider this a message from your stomach
    public void setHungry() {
	PLI.sAssertProposition("(hungry " + loomInstance + ")",
			       PowerloomHelper.getWorkingModule(), null);
	//The above assert replace these two commented out lines
	//isHungry = true;
	//state = AgentState.NEED_SEATED;
	print("I'm hungry");
	stateChanged();
    }

    /** Waiter sends this message so the customer knows to sit down 
     * @param waiter the waiter that sent the message
     * @param menu a reference to a menu */
    public void msgFollowMeToTable(WaiterAgent waiter, Menu menu) {
	this.menu = menu;//should do a deep copy
	this.waiter = waiter;
	print("Received msgFollowMeToTable. Asserting state=NEED_DECIDE");
	//state = AgentState.NEED_DECIDE;
	PLI.sAssertProposition("(hasCustomerState "+loomInstance+" NEED_DECIDE)",
			       PowerloomHelper.getWorkingModule(), null);
	stateChanged();
    }

    /** Waiter sends this message to take the customer's order */
    public void msgWhatWouldYouLike(){
	print("Received msgWhatWouldYouLike. Asserting state=NEED_ORDER");
//	state = AgentState.NEED_ORDER;
	PLI.sAssertProposition("(hasCustomerState "+loomInstance+" NEED_ORDER)",
			       PowerloomHelper.getWorkingModule(), null);
	stateChanged();
    }

    /** Waiter sends this when the food is ready 
     * @param choice the food that is done cooking for the customer to eat */
    public void msgHereIsYourFood(String choice) {
	print("Received msgHereIsYourFood. Asserting state=NEED_EAT");
//	state = AgentState.NEED_EAT;
	PLI.sAssertProposition("(hasCustomerState "+loomInstance+" NEED_EAT)",
			       PowerloomHelper.getWorkingModule(), null);
	stateChanged();
    }

    /** Scheduler.  Determine what action is called for, and do it. */
    protected boolean pickAndExecuteAnAction() {
	//for simplicity, I'm going to divide my rules into two parts
	//(a)In the restaurant
	//(b)Not in the restaurant
	String query = "(isAt "+loomInstance+" "+gui.getrestaurantInstance()+")";
	if (!PowerloomHelper.ask(query)) {//not at the restaurant
	    print("Not at a restaurant. Find something to do.");

	    //Rule 1
	    //If we're hungry, is there a restaurant nearby (with a
	    //host we can talk to)? 
	    /*  Here's what a typical query looks like:
	     (?host)(and (hungry PERSON-011)
			 (exists (?r) (and (restaurantNearby PERSON-011 ?r)
					     (hasemployee ?r ?host)
					     (host ?host)))
	     */
	    query = "1 (?host)(and (hungry "+ loomInstance+ ")"+
		    "(exists (?r) (and (restaurantNearby "
		    + loomInstance+ " ?r)(hasemployee ?r ?host)(host ?host))))";
	    String hostInstance = PowerloomHelper.retrieve1(query);

	    print("hostInstance retrieved:"+hostInstance);
	    HostAgent h = (HostAgent)PowerloomHelper.getloomMap().get(hostInstance);
	    if (h!=null) {
		print("found a restaurant via loom");
		goToRestaurant(h);//Found an action. Add the h parameter.
		return true;
	    }

	    //Rule 2
	    //should we eat at home?
	    //if hungry with food at home then eatAtHome()
	    //CS561 students. Write a rule here and add an action.
	   	
	    query = "(eatAtHome " + loomInstance + ")";
	    if (PowerloomHelper.ask(query)) {
		print("There is food at my house... I'm eating there.");
		eatAtHome();
		return true;
	    }

	    return false;//didn't find anything to do or anywhere to eat. 
	}
	//we're at the restaurant. What state are we in.
	String state = PowerloomHelper.retrieve1(
	    ("1 (?state) (hasCustomerState "+loomInstance+" ?state)"));
	//print("in state="+state);
	
	//Simple linear state machine for the customer once in restaurant

	//this state of need_seated is not needed anymore. It's handled
	//by the code at the beginning of the scheduler.
	/*
	if (state == AgentState.NEED_SEATED) {
	    goToRestaurant(host);
	    return true;
	}
	*/
	
//	if (state == AgentState.NEED_DECIDE) {
	if (state.equals(NEED_DECIDE)) {
	    decideMenuChoice();
	    return true;
	}
//	if (state == AgentState.NEED_ORDER) {
	if (state.equals(NEED_ORDER)) {
	    orderFood();
	    return true;
	}
//	if (state == AgentState.NEED_EAT) {
	if (state.equals(NEED_EAT)) {
	    startEating();
	    return true;
	}
//	if (state == AgentState.NEED_LEAVE) {
	if (state.equals(NEED_LEAVE)) {
	    leaveRestaurant();
	    return true;
	}
	//We are not in a restaurant and have found nothing to do.
	return false;
    }
    
    // *** ACTIONS ***
    
    /** Goes to the nearby restaurant when the customer becomes hungry */
    private void goToRestaurant(HostAgent host) {
	print("Going to restaurant");
	guiCustomer.appearInWaitingQueue();
	host.msgIWantToEat(this);//send him our instance, so he can respond to us
	PLI.sAssertProposition(
		"(isAt " +loomInstance+" "+gui.getrestaurantInstance() +")",
		PowerloomHelper.getWorkingModule(), null);
//	state = AgentState.NO_ACTION; //means wait
	PLI.sAssertProposition("(hasCustomerState "+loomInstance+" NO_ACTION)",
			       PowerloomHelper.getWorkingModule(), null);
	stateChanged();
    }
    
    /** Starts a timer to simulate the customer thinking about the menu */
    private void decideMenuChoice(){
	print("Deciding menu choice...(3000 milliseconds)");
//	state = AgentState.NO_ACTION;
	PLI.sAssertProposition("(hasCustomerState "+loomInstance+" NO_ACTION)",
			       PowerloomHelper.getWorkingModule(), null);
	timer.schedule(new TimerTask() {
	    public void run() {  
		decided();	    
	    }},
	    3000);//how long to wait before executing the run method
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
//	state = AgentState.NO_ACTION;
	PLI.sAssertProposition("(hasCustomerState "+loomInstance+" NO_ACTION)",
			       PowerloomHelper.getWorkingModule(), null);
	waiter.msgHereIsMyChoice(this, choice);
	stateChanged();
    }

    /** Starts a timer to simulate eating */
    private void startEating() {
	print("Eating for " + hungerLevel*1000 + " milliseconds.");
//	state = AgentState.NO_ACTION;
	PLI.sAssertProposition("(hasCustomerState "+loomInstance+" NO_ACTION)",
			       PowerloomHelper.getWorkingModule(), null);
	timer.schedule(new TimerTask() {
	    public void run() {  
		print("Done eating");
//		state = AgentState.NEED_LEAVE;
		PLI.sAssertProposition(
			"(hasCustomerState "+loomInstance+" NEED_LEAVE)",
			PowerloomHelper.getWorkingModule(), null);
		stateChanged();		    
	    }},
	    getHungerLevel() * 1000);//how long to wait before running task
	stateChanged();
    }    

    /** When the customer is done eating, he leaves the restaurant */
    private void leaveRestaurant() {
	print("Leaving the restaurant");
	guiCustomer.leave();
	PLI.sRetractProposition(
		"(isAt " +loomInstance+" "+gui.getrestaurantInstance() +")",
		PowerloomHelper.getWorkingModule(), null);
	PLI.sRetractProposition("(hungry " + loomInstance + ")",
			       PowerloomHelper.getWorkingModule(), null);
	//isHungry = false;
//	state = AgentState.NO_ACTION;
	PLI.sAssertProposition("(hasCustomerState "+loomInstance+" NO_ACTION)",
			       PowerloomHelper.getWorkingModule(), null);
	waiter.msgDoneEatingAndLeaving(this);
	stateChanged();
	gui.setCustomerEnabled(this); //Message to gui to enable hunger button

	//hack to keep customer getting hungry. Only for non-gui customers
	if (gui==null) becomeHungryInAWhile();//set a timer to make us hungry.
    }    

    /** If there is food at home, eat it */
    private void eatAtHome() {
	// retract food in refrigerator
	PLI.sRetractProposition("(hungry " + loomInstance + ")", 
				PowerloomHelper.getWorkingModule(), null);
	String refrigerator = PowerloomHelper.retrieve1(
	    ("1 (?refrig) (hasRefrigerator " + loomInstance + " ?refrig)"));
	String foodEaten = PowerloomHelper.retrieve1(
	    ("1 (?f) (hasFood " + refrigerator + " ?f)"));

	startEatingAtHome();
	print("I ate " + foodEaten + ", and it was great. Not hungry anymore.");
	PLI.sRetractProposition("(hasFood " + refrigerator + " " 
				+ foodEaten + ")", PowerloomHelper.getWorkingModule(),
				null);
    }

    /** Starts a timer to simulate eating at home */
    private void startEatingAtHome() {
	print("Eating at home for " + hungerLevel*1000 + " milliseconds.");
	PLI.sAssertProposition("(hasCustomerState "+loomInstance+" NO_ACTION)",
			       PowerloomHelper.getWorkingModule(), null);
	timer.schedule(new TimerTask() {
	    public void run() { print("Done eating at home."); } },
	    getHungerLevel() * 1000);//how long to wait before running task
	stateChanged();
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
    //this method is not really need in the Loom version, since we find
    //him using Powerloom
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
	return PowerloomHelper.ask("(Hungry "+loomInstance+")");
//	return isHungry;
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
