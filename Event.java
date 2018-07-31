
/**
 * Models a scheduled event - an event has a name, date, start time, and 
 * (optional) end time
 * 
 * @author Christina Andrade
 * @version 07.28.2018 HW 3
 */
public class Event implements Comparable<Event>{

	private String 		name; 
	private String 		date; //Format should be MM/DD/YYYY
	private String 		startTime; //Format should be 24 hour clock HH:MM
	private String 		endTime;  //Format should be 24 hour clock HH:MM 

	public static final double MINUTES_PER_HOUR = 60; 
	public static final String END_OF_DAY = "23:59"; 


	/**
	 * Constructs an event with all four components
	 * 
	 * @param name the name of the event
	 * @param date the date the event is to take place
	 * @param startTime the start time of the event
	 * @param endTime the end time of the event
	 */
	public Event(String name, String date, String startTime, String endTime)
	{
		this.name = name; 
		this.date = date; 
		this.startTime = translateTimeTo24Hour(startTime); 
		this.endTime = translateTimeTo24Hour(endTime); 
	}


	/**
	 * Overload constructor for events with no logical end time
	 * 
	 * @param name the name of the event
	 * @param date the date the event is to take place
	 * @param startTime the start time of the event 
	 */
	public Event(String name, String date, String startTime)
	{
		this.name = name; 
		this.date = date; 
		this.startTime = startTime; 
		this.endTime = END_OF_DAY; 
	}


	/**
	 * Get the name of the event
	 * @return name the name of the event
	 */
	public String getName()
	{
		return name; 
	}


	/**
	 * Gets the date of the event
	 * @return date the date of the event
	 */
	public String getDate()
	{
		return date; 
	}


	/**
	 * Gets the start time of the event
	 * @return startTime the start time of the event
	 */
	public String getStartTime()
	{
		return startTime; 
	}


	/**
	 * Gets the end time of the event
	 * @return endTime the end time of the event 
	 */
	public String getEndTime()
	{
		return endTime; 
	}


	/**
	 * Compares events on their date then start time 
	 * @return returns 0 if two events are equal, -1 if this event is earlier than that event, 1 if this event is later than that event
	 */
	@Override
	public int compareTo(Event that) 
	{
		//Compare dates
		int eventComp =  this.date.compareTo(that.date); 
		if (eventComp != 0)
			return eventComp;

		//Dates were equal, compares start times
		return this.startTime.compareTo(that.startTime); 
	}


	/**
	 * Checks if two events are equal
	 * For purposes of this class, equality is based on event date and start time only 
	 * @return true if two events have the same date and start time 
	 */
	public boolean equals(Object x) //Okay?? - For purposes of this program 
	{
		//Cast
		Event that = (Event) x; 

		//Checks equality using compareTo method
		return this.compareTo(that) == 0; 
	}

	/**
	 * Provides a hashcode for an event 
	 * @return hashcode for the event
	 */
	public int hashcode()
	{
		return this.date.hashCode() + this.startTime.hashCode(); 
	}


	/**
	 * Checks if two events overlap. 
	 * @param e the event to check for overlap with this event
	 * @return returns true if the events overlap, false if they don't
	 */
	public boolean overlapsEvent(Event e)
	{

		//Translate String start time to double for overlap checking
		//Minutes part of start time are divided by the MINUTES_PER_HOUR (60) and represented in decimal format 
		double thisEventStartTime = Double.parseDouble(this.startTime.substring(0,2)) + 
				(Double.parseDouble(this.startTime.substring(3))/MINUTES_PER_HOUR);

		double thatEventStartTime = Double.parseDouble(e.startTime.substring(0,2)) + 
				(Double.parseDouble(e.startTime.substring(3))/MINUTES_PER_HOUR);


		//Translate string end time to double for overlap checking
		//Minutes part of end time are divided by the MINUTES_PER_HOUR (60) and represented in decimal format 
		double thisEventEndTime = Double.parseDouble(this.endTime.substring(0,2)) + 
				(Double.parseDouble(this.endTime.substring(3))/MINUTES_PER_HOUR);

		double thatEventEndTime  = Double.parseDouble(e.endTime.substring(0,2)) + 
				(Double.parseDouble(e.endTime.substring(3))/MINUTES_PER_HOUR);

		//Checks for overlap 
		if( ((thisEventStartTime <= thatEventStartTime) && (thisEventEndTime >= thatEventEndTime)) || 

				((thatEventStartTime <= thisEventStartTime) && (thatEventEndTime >= thisEventEndTime)) ||

				((thatEventStartTime <= thisEventStartTime) && 
						((thatEventEndTime < thisEventEndTime) && (thatEventEndTime > thisEventStartTime))) || 

				(thisEventStartTime <= thatEventStartTime) &&
				((thisEventEndTime < thatEventEndTime) && (thisEventEndTime > thatEventStartTime))) 
			return true; 

		//No overlap found
		return false; 
	}

	/**
	 * Translate a event object to a String representation 
	 * @return a String representation of the event's variables
	 */
	public String toString()
	{
		return name + "###" +  date + "###" + startTime + "###" + endTime;
	}

	/**
	 * Helper method that translate non 24-hour times into 24-hour format 
	 * @param time the initial time entered for an event (Can be 24 hours format such as HH:MM or 12Hour format such as 
	 * 12:00pm, or 3:00am or 03:00am); 
	 * @return time the event translated to 24hour time (assumed user won't enter a time past 11:59pm or 23:59)
	 */
	public static String translateTimeTo24Hour(String time)
	{
		
		//ENTERED IN FORMAT HH:MMam or HH:MMpm
		if (time.length() == 7)
		{
			//Get the AM/PM indicator
			String timeIndicator = time.substring(5).toUpperCase(); 

			//IF AM or 12PM indicator simply dropped 
			if (timeIndicator.equals("AM") || (time.substring(0,2).equals("12") && timeIndicator.equals("PM")))
			{
				time = time.substring(0,5); 
			}
			//12 is added to the hours to achieve the correct 24 hour time format
			else
			{
				time = time.substring(0,5);
				time = Integer.toString(Integer.parseInt(time.substring(0,2)) + 12) + time.substring(2);
			}

		}
		//Entered in format H:MMam or H:MMpm
		else if (time.length() == 6) 
		{
			//gets am or pm indicator 
			String timeIndicator = time.substring(4).toUpperCase(); 

			//IF AM , indicator dropped and 0 added. 
			if (timeIndicator.equals("AM"))
			{
				time = time.substring(0,4); 
				time = "0" + time; 
			}
			
			//12 is added to the hours to achieve the correct 24 hour time format
			else
			{
				time = time.substring(0,4);
				time = Integer.toString(Integer.parseInt(time.substring(0,1)) + 12) + time.substring(1);
			}
		}

		//Time entered in 24 hour format HH:MM 
		else if (time.length() == 5)
		{
			return time; 
		}

		//Time wasn't in correct format. 
		else
		{
			throw new IllegalArgumentException("Entered time format is incorrect. Please try again using one of the"
					+ "following formats: HH:MM (24 hour time), HH:MMam (12 hour time), HH:MMpm (12 hour time),"
					+ "H:MMam (12 hour time), or H:MMpm (12 Hour time"); 
		}

		return time; 
	}

}
