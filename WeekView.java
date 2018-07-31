import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;

public class WeekView extends JPanel implements ViewStrategy{

	private static String[] partialDayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
	static final String[] arrayOfAbbreviatedMonths = {"Jan", "Feb","Mar","Apr","May","Jun",
			"Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
	private GregorianCalendar c; 
	private Map<String, TreeSet<Event>> events; 
	private DataModel d;

	private JPanel eventGrid; 
	private JPanel weekHeader; 

	public WeekView(GregorianCalendar cal, DataModel data)
	{
		c = (GregorianCalendar) cal.clone(); 
		d = data; 

		events = d.getEvents(); 

		this.setLayout( new BoxLayout(this, BoxLayout.Y_AXIS));

		//Draw the week's day grid and events 
		eventGrid = new EventGrid(); 
		eventGrid.setPreferredSize(new Dimension(1000,1440));
		eventGrid.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		eventGrid.setBackground(Color.WHITE);

		//Set up a scroll panel
		int horizontalPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED;
		int verticalPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
		JScrollPane scroll = new JScrollPane(eventGrid, verticalPolicy, horizontalPolicy); 
		scroll.setMinimumSize(new Dimension(1000,840));
		scroll.setPreferredSize(new Dimension(1000,840));

		weekHeader = new JPanel(); 
		weekHeader.setLayout(new GridLayout(1,7));

	




		JPanel weekHeader = new JPanel()
		{
			public void paintComponent(Graphics g)
			{

				super.paintComponent(g); 
				Graphics2D g2 = (Graphics2D) g; 

				Font defaultFont = new JLabel().getFont(); 
				Font dayNumberFont = new Font(defaultFont.toString(), Font.BOLD, 36); 
				
				GregorianCalendar temp = (GregorianCalendar) c.clone(); 
				int backTrack = 1 - temp.get(Calendar.DAY_OF_WEEK); 
				temp.add(Calendar.DAY_OF_MONTH, backTrack);
				
				String monthAtWeekStart = arrayOfAbbreviatedMonths[temp.get(Calendar.MONTH)];
				

				
				for (int i = 0; i < 7; i++)
				{


					String text = partialDayNames[i]; 


					g2.setColor(Color.DARK_GRAY);
					g2.setFont(defaultFont);
					g2.drawString(text, (int) (5 + (80 + (i * ((this.getWidth()-60)/7.0)))), 15);
					
					g2.setFont(dayNumberFont);
					
					g2.drawString(Integer.toString(temp.get(Calendar.DAY_OF_MONTH)), (int) (5 + (80 + (i * ((this.getWidth()-60)/7.0)))), 45);
				
					temp.add(Calendar.DAY_OF_MONTH, 1);
				}
				
				temp.add(Calendar.DAY_OF_MONTH, -1);
				g2.setFont(defaultFont);
				String monthAtWeekEnd = arrayOfAbbreviatedMonths[temp.get(Calendar.MONTH)];
				if (monthAtWeekStart.equals(monthAtWeekEnd))
				{
					g2.drawString(monthAtWeekStart, 5, 25);
					g2.drawString(Integer.toString(temp.get(Calendar.YEAR)), 5, 45);
				}
				else
				{
					g2.drawString(monthAtWeekStart + "/" + monthAtWeekEnd, 5, 25);
					g2.drawString(Integer.toString(temp.get(Calendar.YEAR)), 5, 45);
				}
				
			}
		
		}; 
		
		
		weekHeader.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		weekHeader.setBackground(Color.WHITE);


	
	weekHeader.setPreferredSize(new Dimension(this.getWidth(), 50));
	weekHeader.setMinimumSize(new Dimension(this.getWidth(), 50));
	JPanel buffer = new JPanel(); 
	buffer.setMinimumSize(new Dimension(60,60));
	buffer.setPreferredSize(new Dimension(60,60)); 




	this.add(weekHeader); 
	this.add(scroll); 
}

JPanel buffer = new JPanel(); 
//buffer.setMinimum(new Dimension(60,20)); 

public void paintComponent(Graphics g)
{
	super.paintComponent(g);

	weekHeader.repaint(); 
	eventGrid.repaint(); 
}



@Override
public void updateCalendar(GregorianCalendar cal) {
	// TODO Auto-generated method stub

	c = (GregorianCalendar) cal.clone(); 
	this.repaint(); 

}
@Override
public void stateChanged(ChangeEvent e) {

	events = d.getEvents(); 
	this.repaint(); 
}

@Override
public GregorianCalendar getCalendar() {

	return c;
} 

public class EventGrid extends JPanel
{

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g; 

		//Draw line dividing hours and days

		g2.setColor(Color.LIGHT_GRAY);
		Line2D.Double vertLine = new Line2D.Double(60, 0, 60, this.getHeight()); 
		g2.draw(vertLine);

		for (int i = 1; i<8; i++)
		{
			Line2D.Double vertLine2 = new Line2D.Double(60 + (i * ((this.getWidth()-60)/7.0)), 0, 60 + (i * ((this.getWidth()-60)/7.0)), this.getHeight());
			g2.draw(vertLine2);

		}

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

		int backTrack = 1 - c.get(Calendar.DAY_OF_WEEK); 

		GregorianCalendar temp = ((GregorianCalendar) c.clone()); 
		temp.add(Calendar.DAY_OF_MONTH, backTrack);

		ArrayList<Event> eventsThisWeek = new ArrayList<Event>(); 
		if (events.size() !=0); 
		{
			
			for (int i = 0; i < 7; i++)
			{
				int month = temp.get(Calendar.MONTH) + 1; 
				int day = temp.get(Calendar.DAY_OF_MONTH); 
				int year = temp.get(Calendar.YEAR); 
				String date = DataModel.formatDate(DataModel.translateDateToString(month, day, year)); 
				
				if (events.get(date) != null)
					eventsThisWeek.addAll(events.get(date)); 

				temp.add(Calendar.DAY_OF_MONTH, 1);	
			}

			


			//Get events from the datModel 

			int weekDayIndex; 
			for (Event e : eventsThisWeek )
			{
				
				String date = e.getDate(); 
				
				GregorianCalendar cal = new GregorianCalendar(Integer.parseInt(date.substring(6)), Integer.parseInt(date.substring(0,2)) - 1, Integer.parseInt(date.substring(3,5))); 
				weekDayIndex = cal.get(Calendar.DAY_OF_WEEK); 
				

				g2.setColor(Color.BLUE);
				String eventStart = e.getStartTime(); 
				String eventEnd = e.getEndTime(); 
				int startHour = Integer.parseInt(eventStart.substring(0,2)); 
				int startMin = Integer.parseInt(eventStart.substring(3)); 
				int endHour = Integer.parseInt(eventEnd.substring(0,2)); 
				int endMin = Integer.parseInt(eventEnd.substring(3)); 

				//Draw the Rectangle representing the scheduled event 
				Rectangle2D.Double eventBox = new Rectangle2D.Double(((60 + (weekDayIndex - 1)  * ((this.getWidth()-60)/7.0))) , ((startHour * 60) + startMin), 
						((this.getWidth()-65)/7.0), (((endHour -startHour) * 60) + (endMin - startMin)) ); 
				g2.fill(eventBox);

				//Draw the event details onto the rectangle
				FontMetrics fm = g2.getFontMetrics(this.getFont()); 


				g2.setColor(Color.WHITE);
				g2.drawString( " " + e.getName(), (60 + (weekDayIndex - 1)  * ((this.getWidth()-60)/7)), ((startHour * 60) + startMin + fm.getHeight())); 
				g2.drawString(" " + e.getStartTime() + " - " + e.getEndTime(), (60 + (weekDayIndex - 1)  * ((this.getWidth()-60)/7)), ((startHour * 60) + startMin + fm.getHeight()*2));
			}
		}
	}
}



public static void main(String[] args) throws IOException
{
	GregorianCalendar c = new GregorianCalendar(); 
	DataModel d = new DataModel(); 
	d.loadEvents(); 
	JFrame frame = new JFrame(); 
	WeekView week = new WeekView(c, d); 
	frame.add(week); 

	frame.setVisible(true); 

	frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
}


}
