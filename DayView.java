
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Custom JPanel Class. Holds a calendar, instance of a data model, and Grid depicting times of day. 
 * According to the data passed from the dataModel and calendar, DayView depicts a day and any events scheduled.
 * throughout the day. 
 * 
 * @author Christina Andrade
 *
 */
public class DayView extends JPanel implements ViewStrategy {

	private static String[] fullNameDays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
	private GregorianCalendar c; 
	private TreeSet<Event> events; 

	private JLabel dayHeader; 
	private JPanel eventGrid; 
	private DataModel d; 

	/**
	 * Constructs the JPanel with a calendar and a dataModel - needs reference to data model to fetch and draw events
	 * @param cal the calendar (date) to be represented 
	 * @param data the scheduled events that should appear on the day representation 
	 */
	public DayView(GregorianCalendar cal, DataModel data)
	{
		c = (GregorianCalendar) cal.clone();
		d = data;

		//Gets String version of date
		int month = (c.get(Calendar.MONTH) + 1);
		int day =  c.get(Calendar.DAY_OF_MONTH); 
		int year = c.get(Calendar.YEAR); 

		//Fetches Collection of events on the date from Model
		events = d.getEvents(month,day,year); 

		this.setLayout(new BorderLayout());

		//Draw the day's hour grid
		eventGrid = new eventGrid(); 
		eventGrid.setPreferredSize(new Dimension(1000,1440));
		eventGrid.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		eventGrid.setBackground(Color.WHITE);

		//Set up a scroll panel
		int horizontalPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED;
		int verticalPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
		JScrollPane scroll = new JScrollPane(eventGrid, verticalPolicy, horizontalPolicy); 
		scroll.setMinimumSize(new Dimension(1000,840));
		scroll.setPreferredSize(new Dimension(1000,840));

		//Header fetches date to be displayed from the calendar 
		dayHeader = new JLabel(fullNameDays[c.get(Calendar.DAY_OF_WEEK) - 1 ] + " " +
				(c.get(Calendar.MONTH) + 1) + "/"+ c.get(Calendar.DAY_OF_MONTH), SwingConstants.CENTER); 

		//Compile components together into the Jpanel
		JPanel header = new JPanel(); 
		header.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		header.add(dayHeader); 
		this.add(header, BorderLayout.NORTH);
		this.add(scroll, BorderLayout.CENTER); 

	}

	/**
	 * Details how the JPanel should be painted. Any components that change with changes to the calendar or data
	 * are specified here. 
	 */
	public void paintComponent(Graphics g)
	{

		super.paintComponent(g);

		dayHeader.setText(fullNameDays[c.get(Calendar.DAY_OF_WEEK) - 1 ] + " " +
				(c.get(Calendar.MONTH) + 1) + "/"+ c.get(Calendar.DAY_OF_MONTH)); 
		dayHeader.setHorizontalAlignment(SwingConstants.CENTER); 
		dayHeader.repaint(); 

		eventGrid.repaint(); 
	}

	@Override
	/**
	 * Listens for changes from the DataModel
	 * @param the change event from the DataModel
	 */
	public void stateChanged(ChangeEvent e) {

		events = d.getEvents(c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.YEAR)); 
		this.repaint(); 
	}

	@Override
	/**
	 * Updates the calendar so the JPanel will represent a different day
	 */
	public void updateCalendar(GregorianCalendar cal) {
		
		c = (GregorianCalendar) cal.clone(); 
		this.repaint(); 
	}

	/**
	 * Gets the current calendar (date) represented by the panel
	 */
	public GregorianCalendar getCalendar()
	{
		return c; 
	}


	/**
	 * Embedded class - Custom JPanel that is the hour grid and draws Rectangles representing events onto the
	 * grid to visually show their hourly scheduling 
	 * @author Christina
	 *
	 */
	public class eventGrid extends JPanel
	{
		
		/**
		 * Creates and draws necessary components including events. Visual representations of 
		 * events will change as the dataModel changes. 
		 */
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g; 

			//Draw line dividing hours and events
			Line2D.Double vertLine = new Line2D.Double(60, 0, 60, this.getHeight()); 
			g2.setColor(Color.LIGHT_GRAY);
			g2.draw(vertLine);

			//Draw the time text
			int hour = 1;  
			String halfOfDay = "am"; 

			for (int i = 0; i<24; i ++)
			{	
				if (i < 23)
				{
					g2.setColor(Color.BLACK);
					if (i == 11)
					{
						halfOfDay = "pm";
					}
					if (i == 12)
					{
						hour = 1; 
					}

					String time = Integer.toString(hour) + halfOfDay; 
					hour++; 
					g2.drawString(time, 20, ( i + 1 )*60);
				}


				//Draw the Lines indicating where hours begin 
				g2.setColor(Color.LIGHT_GRAY);
				Line2D.Double hourLine = new Line2D.Double(60, i*60, this.getWidth(), i*60); 
				g2.draw(hourLine); 

			}

			//Get events from the datModel 
			TreeSet<Event> events = d.getEvents(c.get(Calendar.MONTH) + 1 , c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.YEAR));
			if (events.size() != 0)
			{
				for (Event e : events)
				{
					g2.setColor(Color.BLUE);
					String eventStart = e.getStartTime(); 
					String eventEnd = e.getEndTime(); 
					int startHour = Integer.parseInt(eventStart.substring(0,2)); 
					int startMin = Integer.parseInt(eventStart.substring(3)); 
					int endHour = Integer.parseInt(eventEnd.substring(0,2)); 
					int endMin = Integer.parseInt(eventEnd.substring(3)); 

					//Draw the Rectangle representing the scheduled event 
					Rectangle2D.Double eventBox = new Rectangle2D.Double(60, ((startHour * 60) + startMin), 
							this.getWidth(), ((endHour * 60) - (startHour * 60) + (endMin - startMin)) ); 
					g2.fill(eventBox);
					
					//Draw the event details onto the rectangle
					FontMetrics fm = g2.getFontMetrics(this.getFont()); 
					g2.setColor(Color.WHITE);
					g2.drawString( " " + e.getName() + " " + e.getStartTime() + " - " + e.getEndTime(), 60, ((startHour * 60) + startMin + fm.getHeight())); 
				}
			}

		}
	}
}
