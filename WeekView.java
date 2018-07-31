import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TreeSet;

import javax.swing.JLabel;

public class WeekView implements ViewStrategy{

	private static String[] partialDayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
	static final String[] arrayOfAbbreviatedMonths = {"Jan", "Feb","Mar","Apr","May","Jun",
			"Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

	public void drawHeader(GregorianCalendar c, Graphics2D g2, Component container)
	{
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
					g2.drawString(text, (int) (5 + (80 + (i * ((container.getWidth()-60)/7.0)))), 15);

					g2.setFont(dayNumberFont);

					g2.drawString(Integer.toString(temp.get(Calendar.DAY_OF_MONTH)), (int) (5 + (80 + (i * ((container.getWidth()-60)/7.0)))), 45);

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


	public void drawEventGrid(GregorianCalendar c, DataModel d, Graphics2D g2, Component container)
	{
				 Map<String, TreeSet<Event>> events = d.getEvents(); 

				//Draw line dividing hours and days

				g2.setColor(Color.LIGHT_GRAY);
				Line2D.Double vertLine = new Line2D.Double(60, 0, 60, container.getHeight()); 
				g2.draw(vertLine);

				for (int i = 1; i<8; i++)
				{
					Line2D.Double vertLine2 = new Line2D.Double(60 + (i * ((container.getWidth()-60)/7.0)), 0, 60 + (i * ((container.getWidth()-60)/7.0)), container.getHeight());
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
					Line2D.Double hourLine = new Line2D.Double(60, i*60, container.getWidth(), i*60); 
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
						Rectangle2D.Double eventBox = new Rectangle2D.Double(((60 + (weekDayIndex - 1)  * ((container.getWidth()-60)/7.0))) , ((startHour * 60) + startMin), 
								((container.getWidth()-65)/7.0), (((endHour -startHour) * 60) + (endMin - startMin)) ); 
						g2.fill(eventBox);

						//Draw the event details onto the rectangle
						FontMetrics fm = g2.getFontMetrics(container.getFont()); 


						g2.setColor(Color.WHITE);
						g2.drawString( " " + e.getName(), (60 + (weekDayIndex - 1)  * ((container.getWidth()-60)/7)), ((startHour * 60) + startMin + fm.getHeight())); 
						g2.drawString(" " + e.getStartTime() + " - " + e.getEndTime(), (60 + (weekDayIndex - 1)  * ((container.getWidth()-60)/7)), ((startHour * 60) + startMin + fm.getHeight()*2));
					}
				}
		
	}
}

