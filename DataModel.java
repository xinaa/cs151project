
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar; 
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * Models a calendar that manages events. Calendar can load events from a file, save events to a file, 
 * create events (add data). The Model also informs listeners of relevant changes to the data. 
 * @author Christina Andrade
 * @version Draft - Last Update June 22nd 2018
 */
public class DataModel {

	private static Map<String, TreeSet<Event>> events; 
	private static ArrayList<ChangeListener> listeners; 


	/**
	 * Constructs the Tree Map that will hold dates that point to a collection of events on that date 
	 */
	public DataModel()
	{	
		events = new TreeMap<>(); 
		listeners = new ArrayList<ChangeListener>(); 
	}

	
	/**
	 * Adds listeners to the data model so they can be updated about changes to the data 
	 * @param l ChangeListener to be added 
	 */
	public void attach(ChangeListener l)
	{
		listeners.add(l); 
	}
	
	
	//EHHHHH????
	/**
	 * Takes a date in MM/DD/YYYY format, formats it, then gets the events on that date from the TreeMap 
	 * @param month 
	 * @param day
	 * @param year
	 * @return TreeSet of Events on the specified day 
	 */
	public TreeSet<Event> getEvents(int month, int day, int year)
	{
		String date = translateDateToString(month, day, year);

		//If Map has events mapped to the date given
		if (hasEventOnDate(date))
			return events.get(formatDate(date)); 
		
		//No events mapped to the date given returns empty TreeSet
		return new TreeSet<Event>(); 
	}
	
	public Map<String, TreeSet<Event>> getEvents()
	{
		return events; 
	}
	
	
	/**
	 * Helper method that determines if any events exist on a given date
	 * @param date the date being checked for already scheduled events. Date should be in MM/DD/YYYY format
	 * @return boolean false if no events are on the date, true if otherwise 
	 */
	public static boolean hasEventOnDate(String date)
	{
		if (events.get(formatDate(date)) == null)
			return false; 
		return true; 
	}
	
	
	/**
	 * Formats the date so it will be properly sorted in order by year, month, then day in the TreeMap
	 * @param date the date in MM/DD/YYYY 
	 * @return formattedDate the date in the proper format (YYYY/MM/DD)
	 */
	public static String formatDate(String date)
	{
		String formattedDate = "";
		
		formattedDate += date.substring(6) + "/" + date.substring(0,2) + "/" + date.substring(3,5); 
		
		return formattedDate; 
		
	}

	
	/**
	 * Helper method that translate ints representing month, day, and year to a String date in MM/DD/YYYY format 
	 * @param month the month of the date to translate
	 * @param day the day of the date to translate
	 * @param year the year of the date to translate
	 * @return searchableDate a date in String format 
	 */
	public static String translateDateToString(int month, int day, int year)
	{
		String searchableDate; 

		//Translate Month to MM format
		if (month < 10)
			searchableDate = "0" + Integer.toString(month) + "/"; 

		else
			searchableDate = Integer.toString(month) + "/"; 

		//Translate day to DD format
		if (day < 10)

			searchableDate = searchableDate + "0" + Integer.toString(day) + "/"; 

		else
			searchableDate = searchableDate + Integer.toString(day) + "/"; 

		//Translate year
		searchableDate = searchableDate + Integer.toString(year);

		//Return string version of date
		return searchableDate; 
	}
	
	
	/**
	 * Adds a new event to the map storing events. If an event has a time conflict with another 
	 * event in the Map it will not be added. 
	 * @param e the event to add 
	 * @postcondition event added, or user informed of event overlap
	 */
	public void addEvent(Event e)
	{
		String newEventDate = formatDate(e.getDate()); 
		boolean canBeAdded = true; 

		//Retrieves events scheduled on the new event's date
		TreeSet<Event> previouslyScheduledEvents = events.get(newEventDate); 

		//If no events have been scheduled on the date before, event is added to a TreeSet that is put to the map
		if (previouslyScheduledEvents == null)
		{
			TreeSet<Event> t = new TreeSet<Event>(); 
			t.add(e);
			events.put(newEventDate, t);
			
			for (ChangeListener l : listeners)
			{
				l.stateChanged(new ChangeEvent(this));
			}
		}

		//There exists previously scheduled events on this date
		else 
		{
			//Check all previously scheduled events for overlap with new event
			for ( Event ev : previouslyScheduledEvents)
			{
				if (ev.overlapsEvent(e) == true) 
					canBeAdded = false; 	
			}

			//Overlap found, event cannot be added, user notified
			if (!canBeAdded)
			{
				
				String issue = "The event " + e.getName() + " on " + e.getDate() + " overlaps with a previously scheduled event and will not be added.";
				javax.swing.JOptionPane.showMessageDialog(null, issue);
			}

			//No overlap found, event is added and updated list of scheduled events is put to map 
			else 
			{
				previouslyScheduledEvents.add(e);
				events.put(newEventDate, previouslyScheduledEvents);
				
				for (ChangeListener l : listeners)
				{
					l.stateChanged(new ChangeEvent(this));
				}

			}
		}
	}
	
	
	/**
	 * Reads events from events.txt file to populate the map storing this calendar's
	 * events
	 * @precondition events.txt file exists
	 * @postcondition all event objects defined in the file (with no overlap) are added to the calendar 
	 */
	public void loadEvents() throws IOException
	{
		try
		{
			//Construct Readers
			FileReader fr = new FileReader("events.txt");
			BufferedReader br = new BufferedReader(fr); 
			
			boolean done = false; 
			while (!done)
			{
				//Get event from file
				String eventInFile = br.readLine(); 

				//EOF
				if (eventInFile == null)
					done = true; 
				else
				{
					//Gather event details into Array
					String[] eventDetails = eventInFile.split("###");

					//Construct event 
					Event eventToPopulate = new Event(eventDetails[0],eventDetails[1],eventDetails[2],eventDetails[3]);
					addEvent(eventToPopulate); 
				}
			}

			//Close Readers 
			br.close();
			fr.close(); 

		}

		catch (FileNotFoundException x) 
		{
			String err = "This calendar does not have any events yet."; 
			javax.swing.JOptionPane.showMessageDialog(null, err);
		}	
	}

	
	/**
	 * Rewrites (updates) the events.txt file to save events for future run of the program  
	 * Creates event.txt file if none yet exists 
	 * @throws IOException
	 * @postcondition event.txt file exists and is populated with all events objects in the calendar 
	 */
	public void saveEventsToFile()
	{
		FileWriter fw;
		try 
		{
			//Create Writers
			fw = new FileWriter("events.txt");
			PrintWriter pw = new PrintWriter(fw); 
			
			Collection<TreeSet<Event>> values; 

			values = events.values();

			for (TreeSet<Event> t : values)
			{
				for (Event e : t)
				{
					pw.println(e.toString()); 
				}
			}

			//Close Writers
			pw.close(); 
			fw.close();
		} 
		
		catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
