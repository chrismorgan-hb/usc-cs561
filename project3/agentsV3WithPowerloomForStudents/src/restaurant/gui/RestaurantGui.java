package restaurant.gui;

import restaurant.CustomerAgent;
import restaurant.WaiterAgent;

import restaurant.PowerloomHelper;
import edu.isi.powerloom.*;
import edu.isi.powerloom.logic.*;
import edu.isi.stella.Module;
import edu.isi.stella.javalib.*;
import edu.isi.stella.Stella_Object;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/** Main GUI class.
 * Contains the main frame and subsequent panels */
public class RestaurantGui extends JFrame implements ActionListener{
   
    private final int WINDOWX = 450;
    private final int WINDOWY = 350;

    private RestaurantPanel restPanel;
    private JPanel infoPanel = new JPanel();
    private JLabel infoLabel = new JLabel(
    "<html><pre><i>(Click on a customer/waiter)</i></pre></html>");
    private JCheckBox stateCB = new JCheckBox();

    private Object currentPerson;
    //Powerloom variables
    String restaurantInstance;
    String neighborhoodInstance;

    public String getrestaurantInstance() {return restaurantInstance;}
    public String getneighborhoodInstance() {return neighborhoodInstance;}
	    
    /** Constructor for RestaurantGui class.
     * Sets up all the gui components. */
    public RestaurantGui(){
	super("Restaurant Application");
	//do loom stuff first
	restaurantInstance = PowerloomHelper.instantiate("restaurant", null);//no one needs handle
	neighborhoodInstance = "northbeach";
	PLI.sAssertProposition("(haslocation "+restaurantInstance+" "+neighborhoodInstance+" )",
			       PowerloomHelper.getWorkingModule(), null); 
	String anotherRestaurantInstance = PowerloomHelper.instantiate("restaurant",this);
	String anotherNeighborhoodInstance = PowerloomHelper.instantiate("neighborhood", null);
	PLI.sAssertProposition("(haslocation " + anotherRestaurantInstance +
			       " " + anotherNeighborhoodInstance+")",
			       PowerloomHelper.getWorkingModule(), null);
	//anything else? restaurant names? not for now

	restPanel = new RestaurantPanel(this);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setBounds(50,50, WINDOWX, WINDOWY);

	getContentPane().setLayout(new BoxLayout((Container)getContentPane(),BoxLayout.Y_AXIS));

	Dimension rest = new Dimension(WINDOWX, (int)(WINDOWY*.6));
	Dimension info = new Dimension(WINDOWX, (int)(WINDOWY*.25));
	restPanel.setPreferredSize(rest);
	restPanel.setMinimumSize(rest);
	restPanel.setMaximumSize(rest);
	infoPanel.setPreferredSize(info);
	infoPanel.setMinimumSize(info);
	infoPanel.setMaximumSize(info);
	infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

	stateCB.setVisible(false);
	stateCB.addActionListener(this);

	infoPanel.setLayout(new GridLayout(1,2, 30,0));
	infoPanel.add(infoLabel);
	infoPanel.add(stateCB);
	getContentPane().add(restPanel);
	getContentPane().add(infoPanel);	
    }


    /** This function takes the given customer or waiter object and 
     * changes the information panel to hold that person's info.
     * @param person customer or waiter object */
    public void updateInfoPanel(Object person){
	stateCB.setVisible(true);
	currentPerson = person;
	
	if(person instanceof CustomerAgent){
	    CustomerAgent customer = (CustomerAgent) person;
	    stateCB.setText("Hungry?");
	    stateCB.setSelected(customer.isHungry());
	    stateCB.setEnabled(!customer.isHungry());
	    infoLabel.setText(
	    "<html><pre>     Name: " + customer.getName() + " </pre></html>");

	}else if(person instanceof WaiterAgent){
	    WaiterAgent waiter = (WaiterAgent) person;
	    stateCB.setText("On Break?");
	    stateCB.setSelected(waiter.isOnBreak());
	    stateCB.setEnabled(true);
	    infoLabel.setText(
	    "<html><pre>     Name: " + waiter.getName() + " </html>");
	}	   

	infoPanel.validate();
    }

    /** Action listener method that reacts to the checkbox being clicked */
    public void actionPerformed(ActionEvent e){

	if(e.getSource() == stateCB){
	    if(currentPerson instanceof CustomerAgent){
		CustomerAgent c = (CustomerAgent) currentPerson;
		c.setHungry();
		stateCB.setEnabled(false);

	    }else if(currentPerson instanceof WaiterAgent){
		WaiterAgent w = (WaiterAgent) currentPerson;
		w.setBreakStatus(stateCB.isSelected());
	    }
	}
	    
    }

    /** Message sent from a customer agent to enable that customer's 
     * "I'm hungery" checkbox.
     * @param c reference to the customer */
    public void setCustomerEnabled(CustomerAgent c){
	if(currentPerson instanceof CustomerAgent){
	    CustomerAgent cust = (CustomerAgent) currentPerson;
	    if(c.equals(cust)){
		stateCB.setEnabled(true);
		stateCB.setSelected(false);
	    }
	}
    }
	
	
    /** Main routine to get gui started */
    public static void main(String[] args){
	// Initialize the basic PowerLoom code.
	// This may need to be augmented if you want to use additional
	// PowerLoom or Stella code.  One common extension is to use
	// the PowerLoom system, which will require the additional
	// initialization, commented out below.
	if (args[0]!=null&&args[0].equals("loom")) {
	    PowerloomHelper.doPowerLoomRestaurantStartup();
	}
	RestaurantGui gui = new RestaurantGui();
	gui.setVisible(true);
	gui.setResizable(false);
    }

}
    
