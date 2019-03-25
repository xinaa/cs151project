import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.JLabel;

/**
 * This program defines the month view of the calendar application by implementing the methods defined in the
 * View Strategy interface. It interacts with the data model to collect any events that are scheduled on any specified
 * day so that it can be displayed on the month view.
 * @author Amala Chirayil
 *
 */
public class MonthView implements ViewStrategy{
	
	// Declaring and defining an array of days of a given week followed by an array of months
	private static String[] partialDayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
	static final String[] arrayOfMonths = {"January", "February","March","April","May","June",
	"July", "August", "September", "October", "November", "December" };
	
	// Declaring and defining a font style for events and days
	Font defaultFont = new JLabel().getFont(); 
	Font dayNumberFont = new Font(defaultFont.toString(), Font.BOLD, 36); 
	Font eventFont = new Font(new JLabel().getFont().toString(), Font.BOLD, 8);
	
	/**
 	 * This function draws the header of the month view
 	 * @param c - GregorianCalendar object type
 	 * @param g2 - Graphics2D context object type
 	 * @param container - Component object type (JPanel)
 	 */
	public void drawHeader(GregorianCalendar c, Graphics2D g2, Component container)
	{		
		String monthAtWeekStart = arrayOfMonths[c.get(Calendar.MONTH)];
		
		g2.setFont(defaultFont);
		g2.drawString(monthAtWeekStart + " " + Integer.toString(c.get(Calendar.YEAR)), 450, 35);
	}
	
	/**
	 * This functions draws out the event grid of the month view
	 * @param c - GregorianCalendar object type
	 * @param d - DataModel object type
	 * @param g2 - Graphics2D context object type
	 * @param container - Component object type (JPanel)
	 */
	public void drawEventGrid(GregorianCalendar c, DataModel d, Graphics2D g2, Component container)
	{	
		// Retrieving all the events from the DataModel
		Map<String, TreeSet<Event>> events = d.getEvents(); 

		// Setting the calendar to the current day of month
		c.set(Calendar.DAY_OF_MONTH, 1);	
		
		// Retrieving the start day of the week and number of days in a given month
		int start = c.get(Calendar.DAY_OF_WEEK);
		int numDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		// Setting the maximum number of weeks that exists in a month to 5
		int maxWeeknumber = 5;
		
		// If start day is on Fri/Sat
		if(start == 6 && numDays > 30) 
		{
			maxWeeknumber = 6;
		}
		else if(start == 7 && numDays >= 30) 
		{
			maxWeeknumber = 6;
		}
		
		int offset = 0;
		int vOffset = 120;
		int rows = 7;
		
		g2.setColor(Color.LIGHT_GRAY);
		
		if(maxWeeknumber == 6) 
		{
			vOffset = 100;
			rows = 8;
		}	
		
		// Drawing the grid lines for month view
		int height = vOffset*(rows-2);
		
		// Vertical Lines in the grid
		Line2D.Double vertLine = new Line2D.Double(offset, 0, offset, height); 
		g2.draw(vertLine);
		for (int i = 1; i<rows; i++)
		{
			Line2D.Double vertLine2 = new Line2D.Double(offset + (i * ((container.getWidth()-offset)/7.0)), 0, offset + (i * ((container.getWidth()-offset)/7.0)), height);
			g2.draw(vertLine2);
		} 
		
		// Horizontal Lines in the grid
		for (int i = 0; i<rows-1; i ++)
		{	
			g2.setColor(Color.LIGHT_GRAY);
			Line2D.Double hourLine = new Line2D.Double(0, i*vOffset, container.getWidth(), i*vOffset); 
			g2.draw(hourLine); 
		}
		
		// Keeping track of the prior week
		int backTrack = 1 - c.get(Calendar.DAY_OF_WEEK); 		
		
		GregorianCalendar temp = ((GregorianCalendar) c.clone()); 
		temp.add(Calendar.DAY_OF_MONTH, backTrack);
		
		// Declared and defined a new hashmap in order to retrieve all the events for the provided month
		Map<String, TreeSet<Event>> eventsThisMonth = new HashMap<>();
		
		GregorianCalendar t = ((GregorianCalendar) temp.clone()); 
		
		// Verifying to see if events exist
		if (events.size() !=0); 
		{
			// Retrieving the events in a given month
			for (int i = 0; i < numDays; i++)
			{
				int month = t.get(Calendar.MONTH) + 1; 
				int day = t.get(Calendar.DAY_OF_MONTH); 
				int year = t.get(Calendar.YEAR); 
				
				String date = DataModel.formatDate(DataModel.translateDateToString(month, day, year)); 
				if (events.get(date) != null) 
				{
					eventsThisMonth.put(date, events.get(date)); 
				}
			
				t.add(Calendar.DAY_OF_MONTH, 1);	
			}
			
			// Printing out the names of the days and corresponding dates
			for (int i = 0; i < 7; i++)
			{
				String day = partialDayNames[i]; 
				
				g2.setColor(Color.DARK_GRAY);
				
				g2.setFont(defaultFont);
				g2.drawString(day, (int) (4 + (offset + (i * ((container.getWidth()-offset)/7.0)))), 15);
				
				g2.setFont(dayNumberFont);
				g2.drawString(Integer.toString(temp.get(Calendar.DAY_OF_MONTH)), (int) (4 + (offset + (i * ((container.getWidth()-offset)/7.0)))), 45);
				
				temp.add(Calendar.DAY_OF_MONTH, 1);
			}
			
			// Drawing each day of the month
			for(int i=1; i<rows-2;i++) {
				for(int j=0; j<7; j++) {
					g2.setFont(dayNumberFont);
					g2.drawString(Integer.toString(temp.get(Calendar.DAY_OF_MONTH)), (int) (4 + (offset + (j * ((container.getWidth()-offset)/7.0)))), vOffset*i + 45);
					temp.add(Calendar.DAY_OF_MONTH, 1);
				}
			}
			
			// Retrieving events from Data Model
			int weekDayIndex; 
			
			Set<String> keySet = eventsThisMonth.keySet();
			Iterator<String> iterator = keySet.iterator();
			
			FontMetrics fm = g2.getFontMetrics(eventFont); 
			
			while (iterator.hasNext()) {
				String eventDate = (String) iterator.next();
				Event first = eventsThisMonth.get(eventDate).first();
				String eDate = first.getDate();
				
				GregorianCalendar cal = new GregorianCalendar(Integer.parseInt(eDate.substring(6)), 
				Integer.parseInt(eDate.substring(0,2)) - 1, Integer.parseInt(eDate.substring(3,5))); 
				weekDayIndex = cal.get(Calendar.DAY_OF_MONTH); 
				
				cal.set(Calendar.DAY_OF_MONTH, 1); 
				int startIndex = cal.get(Calendar.DAY_OF_WEEK);
				
				cal.set(Calendar.DAY_OF_MONTH, weekDayIndex);
				int index = startIndex + weekDayIndex - 2;
				
				// Setting the position of events
				double x = (((index) %7) * ((container.getWidth()-offset)/7.0));
				double y = ((index)/7)*vOffset;
				
				// Retrieving all the events of a given eventDate
				TreeSet<Event> treeSet = eventsThisMonth.get(eventDate);
				Iterator<Event> iterator2 = treeSet.iterator();
				String eventStr = "";
				
				g2.setFont(eventFont);
				g2.setColor(Color.BLACK);
				
				int count = 0;
				while(iterator2.hasNext()) {
					Event next = iterator2.next();
					
					eventStr = " " + next.getStartTime() + " - " + next.getEndTime() + ": " + next.getName();
					g2.drawString( eventStr, (int) x + 4, (int)y + vOffset/2 + fm.getHeight()*count + 4); 
					
					count++;
				}
			}
		}
	}
	
	/**
	 * Retrieves the dimension of the month view display
	 * @return dimension
	 */
	public Dimension getGridDimension() {
		return new Dimension (1000, 840);
	}
}