package restaurant;
import java.awt.Color;
import restaurant.gui.RestaurantGui;
import restaurant.layoutGUI.*;
import agent.Agent;
import java.util.Timer;
import java.util.TimerTask;
import astar.*;
import java.util.*;

/** Restaurant Waiter Agent.
 * Sits customers at assigned tables and takes their orders.
 * Takes the orders to the cook and then returns them 
 * when the food is done.  Cleans up the tables after the customers leave.
 * Interacts with customers, host, and cook */
public class WaiterAgent extends Agent {
    private RestaurantGui gui; //Reference to the gui so the customer can send it messages.

   //State variables for Waiter
    private boolean working = true;
    private boolean onBreak = false;
    private boolean startedBreak = false;

    //State constants for Customers

    public enum CustomerState 
	{NEED_SEATED,READY_TO_ORDER,ORDER_PENDING,ORDER_READY,IS_DONE,NO_ACTION};

    Timer timer = new Timer();

    /** Private class to hold information for each customer.
     * Contains a reference to the customer, his choice, 
     * table number, and state */
    private class MyCustomer {
	public CustomerState state;
	public CustomerAgent cmr;
	public String choice;
	public int tableNum;
	public Food food; //gui thing

	/** Constructor for MyCustomer class.
	 * @param cmr reference to customer
	 * @param num assigned table number */
	public MyCustomer(CustomerAgent cmr, int num){
	    this.cmr = cmr;
	    tableNum = num;
	    state = CustomerState.NO_ACTION;
	}
    }

    //Name of waiter
    private String name;

    //All the customers that this waiter is serving
    private List<MyCustomer> customers = new ArrayList<MyCustomer>();

    private HostAgent host;
    private CookAgent cook;
    AStarTraversal aStar;
    Restaurant restaurant; //the gui layout
    Waiter waiter; //the gui object
    Position currentPosition;
    Position originalPosition;
    Table[] tables; //the gui tables
    

    /** Constructor for WaiterAgent class
     * @param name name of waiter
     * @param gui reference to the gui */
    public WaiterAgent(String name, RestaurantGui gui, AStarTraversal aStar,
		       Restaurant restaurant, Table[] tables) {
	super();
	this.gui = gui;//main gui
	this.name = name;
	this.aStar = aStar;
	this.restaurant = restaurant;//the layout for astar
	waiter = new Waiter(name.substring(0,2), new Color(255, 0, 0), restaurant);
	//currentPosition = new Position(3,13);
	currentPosition = new Position(waiter.getX(), waiter.getY());
	currentPosition.moveInto(aStar.getGrid());
	originalPosition = currentPosition;//save this for moving into
	this.tables = tables;
    } 

    // *** MESSAGES ***

    /** Host sends this to give the waiter a new customer.
     * @param customer customer who needs seated.
     * @param tableNum identification number for table */
    public void msgSitCustomerAtTable(CustomerAgent customer, int tableNum){
	MyCustomer c = new MyCustomer(customer, tableNum);
	c.state = CustomerState.NEED_SEATED;
	customers.add(c);
	stateChanged();
    }

    /** Customer sends this when they are ready.
     * @param customer customer who is ready to order.
     */
    public void msgImReadyToOrder(CustomerAgent customer){
	//print("received msgImReadyToOrder from:"+customer);
	for(int i=0; i < customers.size(); i++){
	    //if(customers.get(i).cmr.equals(customer)){
	    if (customers.get(i).cmr == customer){
		customers.get(i).state = CustomerState.READY_TO_ORDER;
		stateChanged();
		return;
	    }
	}
	System.out.println("msgImReadyToOrder in WaiterAgent, didn't find him?");
    }

    /** Customer sends this when they have decided what they wanted to eat 
     * @param customer customer who has decided their choice
     * @param choice the food item that the customer chose */
    public void msgHereIsMyChoice(CustomerAgent customer, String choice){
	for(MyCustomer c:customers){
	    if(c.cmr.equals(customer)){
		c.choice = choice;
		c.state = CustomerState.ORDER_PENDING;
		tables[c.tableNum].takeOrder(choice.substring(0,2)+"?");
		stateChanged();
		return;
	    }
	}
    }

    /** Cook sends this when the order is ready.
     * @param tableNum identification number of table whose food is ready */
    public void msgOrderIsReady(int tableNum, Food f){
	for(MyCustomer c:customers){
	    if(c.tableNum == tableNum){
		c.state = CustomerState.ORDER_READY;
		c.food = f;
		stateChanged();
		return;
	    }
	}
    }

    /** Customer sends this when they are done eating.
     * @param customer customer who is leaving the restaurant. */
    public void msgDoneEatingAndLeaving(CustomerAgent customer){
	for(MyCustomer c:customers){
	    if(c.cmr.equals(customer)){
		c.state = CustomerState.IS_DONE;
		c.food.remove();
		stateChanged();
		return;
	    }
	}
    }

    /** Sent from GUI to control breaks 
     * @param state true when the waiter should go on break and 
     *              false when the watier should go off break */
    public void setBreakStatus(boolean state){
	onBreak = state;
	stateChanged();
    }



    /** Scheduler.  Determine what action is called for, and do it. */
    protected boolean pickAndExecuteAnAction() {
	//print("in waiter scheduler");
	//Starts the break cycle if onBreak == true
	if(onBreak && working ) {
	    initiateBreak();
	    return true;
	}

	//Once all the customers have been served
	//then the waiter can actually go on break
	if(!working && customers.isEmpty() && !startedBreak) {
	    goOnBreak();
	    return true;
	}

	if(!onBreak && !working){
	    endBreak();
	}

	
	//Runs through the customers for each rule, so 
	//the waiter doesn't serve only one customer at a time
	if(!customers.isEmpty()){
	    //System.out.println("in scheduler, customers not empty:");
	    //Gives food to customer if the order is ready
	    for(MyCustomer c:customers){
		if(c.state == CustomerState.ORDER_READY) {
		    giveFoodToCustomer(c);
		    return true;
		}
	    }
	    //Clears the table if the customer has left
	    for(MyCustomer c:customers){
		if(c.state == CustomerState.IS_DONE) {
		    clearTable(c);
		    return true;
		}
	    }

	    //Seats the customer if they need it
	    for(MyCustomer c:customers){
		if(c.state == CustomerState.NEED_SEATED){
		    seatCustomer(c);
		    return true;
		}
	    }

	    //Gives all pending orders to the cook
	    for(MyCustomer c:customers){
		if(c.state == CustomerState.ORDER_PENDING){
		    giveOrderToCook(c);
		    return true;
		}
	    }

	    //Takes new orders for customers that are ready
	    for(MyCustomer c:customers){
		//print("testing for ready to order"+c.state);
		if(c.state == CustomerState.READY_TO_ORDER) {
		    takeOrder(c);
		    return true;
		}
	    }	    
	}
	if (!currentPosition.equals(originalPosition)) {
	    moveToOriginalPosition();
	    return true;
	}

	//we have tried all our rules and found nothing to do. 
	// So return false to main loop of abstract agent and wait.
	//print("in scheduler, no rules matched:");
	return false;
    }

    // *** ACTIONS ***
    
    /** Starts the break so that the host won't give
     * any new customers to this waiter */
    private void initiateBreak(){
	Do("Starting to go on break");
	host.msgChangeWorkStatus(this, false);
	working = false;
	stateChanged();
    }

    /** Goes on break once all customers are gone.
     * Starts a break timer. */
    private void goOnBreak(){
	Do("Going on break");
	startedBreak = true;
	stateChanged();
    }

    /** Brings the waiter back from break */
    private void endBreak(){
	Do(name + " is back from break!");
	host.msgChangeWorkStatus(this, true);
	working = true;
	onBreak = false;
	startedBreak = false;

	//Commented out for gui
	//startBreakTimer(15000);
    }

    /** Seats the customer at a specific table 
     * @param customer customer that needs seated */
    private void seatCustomer(MyCustomer customer) {
	//Notice how we print "customer" directly. It's toString method will do it.
	Do("Seating " + customer.cmr + " at table " + (customer.tableNum+1));
	//move to customer first.
	Customer guiCustomer = customer.cmr.getGuiCustomer();
	guiMoveFromCurrentPostionTo(new Position(guiCustomer.getX()+1,guiCustomer.getY()));
	waiter.pickUpCustomer(guiCustomer);
	Position tablePos = new Position(tables[customer.tableNum].getX()-1,
					 tables[customer.tableNum].getY()+1);
	guiMoveFromCurrentPostionTo(tablePos);
	waiter.seatCustomer(tables[customer.tableNum]);
	customer.state = CustomerState.NO_ACTION;
	customer.cmr.msgFollowMeToTable(this, new Menu());
	stateChanged();
    }
    //this is just a subroutine for waiter moves. It's not an "Action"
    //itself, it is called by Actions.
    void guiMoveFromCurrentPostionTo(Position to){

	Position nextPos;
	System.out.println("Calculating path from " + currentPosition + " to " + to);
	
	Node solution = null;
	solution = aStar.generalSearch(currentPosition, to);
	if (solution != null) {

		System.out.println(name + ": Found a path.");
		System.out.print(name + ": Path = ");
		solution.printNode();
		System.out.println();

		AStarNode aStarSolution = (AStarNode)solution;

		List<Position> path = aStarSolution.getPath();
		ListIterator iter = path.listIterator();
		nextPos = (Position)iter.next();

		while(!currentPosition.equals(to)) {
		
			nextPos = (Position)iter.next();
			System.out.println(name + ": Attempting to move into " + nextPos);
			if(nextPos.moveInto(aStar.getGrid())) {
			
				waiter.move(nextPos.getX(), nextPos.getY());
				currentPosition.release(aStar.getGrid());	
				currentPosition = nextPos;
			}
			else {

				System.out.println(name + ": " + nextPos + " is locked.  Recalculating path...");
				solution = aStar.generalSearch(currentPosition, to);
				if (solution != null) {

					System.out.println(name + ": Found a path.");
					System.out.print(name + ": Path = ");
					solution.printNode();
					System.out.println();

					aStarSolution = (AStarNode)solution;
					path = aStarSolution.getPath();
					iter = path.listIterator();
					nextPos = (Position)iter.next();
				}	
				else {

					System.out.println("No path from " + currentPosition + " to " + to);
				}
					
			}
		}
	}
	else {
		System.out.println("No path from " + currentPosition + " to " + to);
	}	
    }
    
    /** Takes down the customers order 
     * @param customer customer that is ready to order */
    private void takeOrder(MyCustomer customer) {
	Do("Taking " + customer.cmr +"'s order.");
	Position tablePos = new Position(tables[customer.tableNum].getX()-1,
					 tables[customer.tableNum].getY()+1);
	guiMoveFromCurrentPostionTo(tablePos);
	customer.state = CustomerState.NO_ACTION;
	customer.cmr.msgWhatWouldYouLike();
	stateChanged();
    }

    /** Gives any pending orders to the cook 
     * @param customer customer that needs food cooked */
    private void giveOrderToCook(MyCustomer customer) {
	Do("Giving " + customer.cmr + "'s choice of " + customer.choice + " to cook");
	customer.state = CustomerState.NO_ACTION;
	cook.msgHereIsAnOrder(this, customer.tableNum, customer.choice);
	stateChanged();
    }

    /** Gives food to the customer 
     * @param customer customer whose food is ready */
    private void giveFoodToCustomer(MyCustomer customer) {
	Do("Giving finished order of " + customer.choice +" to " + customer.cmr);
	Position inFrontOfGrill = new Position(customer.food.getX()-1,customer.food.getY());
	guiMoveFromCurrentPostionTo(inFrontOfGrill);//in front of grill
	waiter.pickUpFood(customer.food);
	Position tablePos = new Position(tables[customer.tableNum].getX()-1,
					 tables[customer.tableNum].getY()+1);
	guiMoveFromCurrentPostionTo(tablePos);
	waiter.serveFood(tables[customer.tableNum]);
	customer.state = CustomerState.NO_ACTION;
	customer.cmr.msgHereIsYourFood(customer.choice);
	stateChanged();
    }
    private void moveToOriginalPosition(){
	Do("Nothing to do. Moving to original position="+originalPosition);
	guiMoveFromCurrentPostionTo(originalPosition);
	stateChanged();
    }


    /** Starts a timer to clear the table 
     * @param customer customer whose table needs cleared */
    private void clearTable(final MyCustomer customer) {
	Do("Clearing table " + (customer.tableNum+1) + " (2500 milliseconds)");
	timer.schedule(new TimerTask(){
	    public void run(){		    
		endCustomer(customer);
	    }
	}, 2500);
	   
	customer.state = CustomerState.NO_ACTION;
	stateChanged();
    }


    /** Function called at the end of the clear table timer
     * to officially remove the customer from the waiter's list.
     * @param customer customer who needs removed from list */
    private void endCustomer(MyCustomer customer){ 
	Do("Table " + customer.tableNum + " is cleared!");
	host.msgTableIsFree(customer.tableNum);
	customers.remove(customer);
	stateChanged();
    }




    /*
    //This timer controls the time in between breaks
    //This is a hack since we don't have a gui to control
    //when the waiter should go on break
    public void startBreakTimer(int time){
	TimerQueue.schedule(Deadline.in(time),
			    new TimerQueue.Callback() {
				public void timerExpired(Object cookie) {
				    timeForBreak();
				    stateChanged();
				}},
			    null);
    }
    */

    // *** EXTRA ***

    /** @return name of waiter */
    public String getName(){
        return name;
    }

    /** @return string representation of waiter */
    public String toString(){
	return "waiter " + getName();
    }
    
    /** Hack to set the cook for the waiter */
    public void setCook(CookAgent cook){
	this.cook = cook;
    }
    
    /** Hack to set the host for the waiter */
    public void setHost(HostAgent host){
	this.host = host;
    }

    /** @return true if the waiter is on break, false otherwise */
    public boolean isOnBreak(){
	return onBreak;
    }

}

