package restaurant;

import restaurant.PowerloomHelper;
import edu.isi.powerloom.*;
import edu.isi.powerloom.logic.*;
import edu.isi.stella.Module;
import edu.isi.stella.javalib.*;
import edu.isi.stella.Stella_Object;

import agent.Agent;
import restaurant.gui.RestaurantGui;
import java.util.*;


/** Host agent for restaurant.
 *  Keeps a list of all the waiters and tables.
 *  Assigns new customers to waiters for seating and 
 *  keeps a list of waiting customers.
 *  Interacts with customers and waiters.
 */
public class HostAgent extends Agent {

    /** Private class storing all the information for each table,
     * including table number and state. */
    /*private class Table {
	public int tableNum;
	public boolean occupied;

	public Table(int num){
	    tableNum = num;
	    occupied = false;
	}	
    }
    */

    /** Private class to hold waiter information and state */
/*    private class MyWaiter {
	public WaiterAgent wtr;
	public boolean working = true;

	public MyWaiter(WaiterAgent waiter){
	    wtr = waiter;
	}
    }
*/
    //List of all the customers that need a table
//   private List<CustomerAgent> waitList =
//		Collections.synchronizedList(new ArrayList<CustomerAgent>());

    //List of all waiter that exist.
/*    private List<MyWaiter> waiters =
		Collections.synchronizedList(new ArrayList<MyWaiter>());
*/
    private WaiterAgent nextWaiter = null; //The next waiter that needs a customer
    
    //List of all the tables
    int nTables;
    //private Table tables[];

    //Name of the host
    private String name;
    RestaurantGui gui;
    String loomInstance; //for the Host

    /** Constructor for HostAgent class 
     * @param name name of the host */
    public HostAgent(String name, int ntables, RestaurantGui gui ) {
	super();
	this.nTables = ntables;
	//tables = new Table[nTables];


	for(int i=0; i < nTables; i++){
	    //tables[i] = new Table(i);
	    PLI.sAssertProposition("(emptyTable table" + i + ")", PowerloomHelper.getWorkingModule(), null);
	    //create gui for each
	}
	this.name = name;
	this.gui = gui;

	//The gui initialization creates the host agent.
	//This powerloom code must go in the Host's constructor
	//loomInstance is slot in Agent
	loomInstance = PowerloomHelper.instantiate("host",this);
	String s = "(hasEmployee " + gui.getrestaurantInstance() +
		   " " + loomInstance+")";
	print("Host assertion="+s);
	PLI.sAssertProposition(s, PowerloomHelper.getWorkingModule(), null);
    }

    // *** MESSAGES ***

    /** Customer sends this message to be added to the wait list 
     * @param customer customer that wants to be added */
    public void msgIWantToEat(CustomerAgent customer){
	// assert that the customer is waiting
  	PLI.sAssertProposition("(waitingCustomer " + customer.loomInstance + ")", 
				PowerloomHelper.getWorkingModule(), null); 
	
	//waitList.add(customer);
	stateChanged();
    }

    /** Waiter sends this message after the customer has left the table 
     * @param tableNum table identification number */
    public void msgTableIsFree(int tableNum){
	//tables[tableNum].occupied = false;
	int tableInst = tableNum-1;
	PLI.sAssertProposition("(emptyTable table" + tableInst + ")", PowerloomHelper.getWorkingModule(), null);
	stateChanged();
    }

    /** Waiter sends this when he goes on or off break 
     * @param waiter waiter that is changing his state
     * @param working a boolean state that is true if he is working 
     *                and false if he is not
     */
    public void msgChangeWorkStatus(WaiterAgent waiter, boolean working){
	/*for (MyWaiter w:waiters)
	    if(w.wtr.equals(waiter)){
		w.working = working;
		stateChanged();
		return;
	    }
	*/

    }

    /** Scheduler.  Determine what action is called for, and do it. */
    protected boolean pickAndExecuteAnAction() {
	
	String nextWaitingCustomer = PowerloomHelper.retrieve1(
		("1 (?cust) (waitingCustomer ?cust)"));

	if (nextWaitingCustomer != null) { print("Next waiting customer: " + nextWaitingCustomer); }
	else { print("No waiting customers."); }

	String randomWaiter = PowerloomHelper.retrieve1(
		("1 (?w) (manages " + loomInstance + " ?w)"));

	if((nextWaitingCustomer != null) && (randomWaiter != null)){
		//Finds the next waiter that is working

		int minCustomers = 999; // The most customers ever
		PlIterator wIt = PLI.sRetrieve("all (?w) (manages " + loomInstance + " ?w)",	
			PowerloomHelper.getWorkingModule(), null);
		print("Total waiters: " + wIt.length());
 
		while (wIt.nextP()) {

			// only check working waiters
			if ( !PowerloomHelper.ask("(workingWaiter " + PLI.getNthString(wIt, 0, null, null) + ")") ) { 
				print(PLI.getNthString(wIt, 0, null, null) + " is not working.  Skippping...");
				continue; 
			}

			String query = "1 (cardinality (fillers hasAssignment " + PLI.getNthString(wIt, 0, null, null) + ") ?n)"; 
			PlIterator cIt = PLI.sRetrieve(query, PowerloomHelper.getWorkingModule(), null);
			int numCustomers = 1000;
			if(!cIt.nextP()) { numCustomers = 0; }
			else { 
				numCustomers = PLI.getNthInteger(cIt, 0, null, null);
			}
			print("Waiter " + PLI.getNthString(wIt, 0, null, null) + " has " + numCustomers + " customers.");

			if(numCustomers < minCustomers) {
				nextWaiter = (WaiterAgent)PowerloomHelper.getloomMap().get(PLI.getNthString(wIt, 0, null, null));
				minCustomers = numCustomers;
			}
		}

	    //Then runs through the tables and finds the first unoccupied 
	    //table and tells the waiter to sit the first customer at that table
	    for(int i=0; i < nTables; i++){

		boolean tableEmpty = false;
		tableEmpty = PowerloomHelper.ask("(emptyTable table" + i + ")");

		if(tableEmpty){
			print("table" + (i+1) + " is empty.");
			print("Asserting that " + nextWaiter + " is assigned to " + nextWaitingCustomer);
			PLI.sAssertProposition("(hasAssignment " + nextWaiter.loomInstance + " " + nextWaitingCustomer + ")",
				PowerloomHelper.getWorkingModule(), null); 
		
			print("Picked " + nextWaiter + " as next waiter"); 

			tellWaiterToSitCustomerAtTable(nextWaiter,
			      (CustomerAgent)PowerloomHelper.getloomMap().get(nextWaitingCustomer), i);
			//    waitList.get(0), i);
			// replace waitList.get(0) with the reverse lookup from nextWaitingCustomer
		    	return true;
		}
	    }

	    print("There are currently no empty tables.  Asking " + nextWaitingCustomer + " to wait.");
	}

	//we have tried all our rules (in this case only one) and found
	//nothing to do. So return false to main loop of abstract agent
	//and wait.
	return false;
    }
    
    // *** ACTIONS ***
    
    /** Assigns a customer to a specified waiter and 
     * tells that waiter which table to sit them at.
     * @param waiter
     * @param customer
     * @param tableNum */
    private void tellWaiterToSitCustomerAtTable(WaiterAgent waiter, CustomerAgent customer, int tableNum){
	print("Telling " + waiter + " to sit " + customer +" at table "+(tableNum+1));
	waiter.msgSitCustomerAtTable(customer, tableNum);
	PLI.sRetractProposition("(emptyTable table" + tableNum + ")",
				PowerloomHelper.getWorkingModule(), null);
	PLI.sAssertProposition("(occupiedBy table" + tableNum + " " + customer.loomInstance + ")",
				PowerloomHelper.getWorkingModule(), null);
	PLI.sRetractProposition("(waitingCustomer " + customer.loomInstance + ")",
				PowerloomHelper.getWorkingModule(), null);	
	
	stateChanged();
    }
	
    

    // *** EXTRA ***

    /** Returns the name of the host 
     * @return name of host */
    public String getName(){
        return name;
    }    

    /** Hack to enable the host to know of all possible waiters 
     * @param waiter new waiter to be added to list
     */
    public void setWaiter(WaiterAgent waiter){
	//waiters.add(new MyWaiter(waiter));
	print("Adding " + waiter.loomInstance + " to my list of waiters in loom.");
	PLI.sAssertProposition("(manages " + loomInstance + " " + waiter.loomInstance + ")",
				PowerloomHelper.getWorkingModule(), null); 
	stateChanged();
    }
    
  
}

