package restaurant;

import edu.isi.powerloom.*;
import edu.isi.powerloom.logic.*;
import edu.isi.stella.Module;
import edu.isi.stella.javalib.*;
import edu.isi.stella.Stella_Object;
import java.io.File;
import java.util.*;

public class PowerloomHelper {
	//Powerloom stuff
    private static String kbfileDefault = "restaurant.plm";
    private static String kbdirDefault = "doc";
    private static String workingModule = "restaurant";
    private static Map<String,Object> loomMap = new HashMap<String,Object>();//see instantiate() below
    private static Module module;

    public static String getWorkingModule(){return workingModule;}
    public static Module getModule() {return module;}
    public static Map<String,Object> getloomMap(){return loomMap;}
    
    public static void doPowerLoomRestaurantStartup() {
	System.out.print("Powerloom initializing...");
	PLI.initialize();
	System.out.println("    done.");
	// Load the knowledge bases.  This will either take the names from
	// the command line or else it will try a plain file default and a
	// plain file default in the default kb directory.
	System.out.println("Loading KBs:");
	if (new File(kbfileDefault).exists()) {
	    loadVerbosely(kbfileDefault);
	} else if (new File(kbdirDefault, kbfileDefault).exists()) {
	    loadVerbosely(new File(kbdirDefault, kbfileDefault).getAbsolutePath());
	} else {
	    System.err.println("Oops.  This needs an ontology file argument!");
	    System.exit(0);
	}
	module = PLI.sChangeModule(workingModule, null);
	//module = PLI.sGet-module(workingModule, null);
    }

    //This routine is a take on Tom Russ's suggestion for creating instances.
    public static String instantiate(String concept, Object obj){
	LogicObject conceptLO = PLI.getConcept(concept, module, null);
	//  Passing null as the name makes PowerLoom generate a new, unique name
	LogicObject instance = PLI.createObject(null, conceptLO, module, null);
	String instanceName = PLI.getNameInModule(instance, module, null);
	loomMap.put(instanceName, obj);//obj may be null
	System.out.println("Instantiate instanceName="+instanceName);
	return instanceName;
    }
    //This routine tries to retrieve one instance. A riff on the sample code
    //and emails with Tom Russ. Assumes the query is only after ONE instance.
    public static String retrieve1(String query){
	PlIterator answer = PLI.sRetrieve(query, workingModule, null);
	// Check for answer.  Necessary to get to the value anyway:
	//printSeparator();
	//System.out.println("Retrieving '" + query + "'");
	if (answer.nextP()) {
	    // Here we take apart the first tuple, held in the "value" field
	    // of the PLIterator instance.  Also, these interface functions
	    // require the actual module object and not just its name.  So
	    // we look that up and use it.
	    //Module mod = PLI.getModule(workingModule, null);
	    LogicObject lo = PLI.getNthLogicObject(answer.value, 0, module, null);
	    return PLI.getNameInModule(lo, module, null);
	}
	else return null;
    }
    public static boolean ask(String query){
	//System.out.println("Asked:" + query);
	TruthValue answer = PLI.sAsk(query, workingModule, null);
	//printSeparator();
	//System.out.println("Asked:" + query + " got:"+answer);
	return PLI.isTrue(answer);
    }
    public static void loadVerbosely (String filename) {
	System.out.print("  Loading " + filename + " ...");
	PLI.load(filename, null);
	System.out.println("  done.");
    }
    static void printSeparator () {
	System.out.println("-----------------------------------------");
    }
}
