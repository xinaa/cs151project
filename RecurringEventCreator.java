import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class RecurringEventCreator {

	private static final int[] MONTHS = new int[] { Calendar.JANUARY, Calendar.FEBRUARY, Calendar.MARCH, Calendar.APRIL,
			Calendar.MAY, Calendar.JUNE, Calendar.JULY, Calendar.AUGUST, Calendar.SEPTEMBER, Calendar.OCTOBER,
			Calendar.NOVEMBER, Calendar.DECEMBER };

	private ArrayList<String[]> eventInputs;

	public RecurringEventCreator(ArrayList<String[]> eventInputs) {
		this.eventInputs = eventInputs;
	}

	/**
	 * Method that creates recurring events 
	 * Order of Strings in inputs: eventName; year; startMonth; endMonth; sequence; startHour; endHour
	 * 
	 * @return
	 */
	public ArrayList<Event> createRecurringEvents() {
		
		String weekSequence = "SMTWHFA";	//Used to check if event recurs on specific days
		SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy"); // Formats date for creating event
		SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
		
		ArrayList<Event> events = new ArrayList<Event>();	//List of events

		for (String[] inputs : eventInputs) { // For every recurring event inputed

			if (inputs.length != 7) // Check if valid number of inputs were given
				continue; //Abandons everything event events already created :l

			//Get event details from the String in the Array
			String eventName = inputs[0];
			int year = Integer.parseInt(inputs[1]);
			int startMonth = Integer.parseInt(inputs[2]) - 1;
			int endMonth = Integer.parseInt(inputs[3]) - 1;
			String eventSequence = inputs[4];
			int startHour = Integer.parseInt(inputs[5]);
			int endHour = Integer.parseInt(inputs[6]);

			// Create starting date & time for first event
			Calendar eventStartDate = Calendar.getInstance();
			eventStartDate.set(Calendar.YEAR, year);
			eventStartDate.set(Calendar.MONTH, MONTHS[startMonth]);
			eventStartDate.set(Calendar.DAY_OF_MONTH, 1); //? 
			eventStartDate.set(Calendar.HOUR_OF_DAY, startHour);
			eventStartDate.set(Calendar.MINUTE, 0);

			// Create ending date & time for first event
			Calendar eventEndDate = (Calendar) eventStartDate.clone(); // All data is primitive, so no need for implementing deep clone
																		
			eventEndDate.set(Calendar.HOUR_OF_DAY, endHour);
			
			Calendar afterEndMonth = (Calendar) eventEndDate.clone();
			afterEndMonth.set(Calendar.MONTH, MONTHS[endMonth]);
			afterEndMonth.add(Calendar.MONTH, 1);
			int aEM = afterEndMonth.get(Calendar.MONTH);

			boolean[] hasEvent = new boolean[7]; // Checks for recurring event on day of week
			
			for (int i = 0; i < weekSequence.length(); i++) { // For each day of the week
				if (eventSequence.contains(String.valueOf(weekSequence.charAt(i))))
					hasEvent[i] = true;
			}

			for (; eventEndDate.get(Calendar.MONTH) != aEM; //For each day between starting and ending month
					eventStartDate.add(Calendar.DAY_OF_YEAR, 1), eventEndDate.add(Calendar.DAY_OF_YEAR, 1)) {
				if (hasEvent[eventStartDate.get(Calendar.DAY_OF_WEEK) - 1]) {
					events.add(new Event(eventName, dateFormatter.format(eventStartDate.getTime()),	
							timeFormatter.format(eventStartDate.getTime()), timeFormatter.format(eventEndDate.getTime())));
				}
			}
		}
		return events;
	}
}
