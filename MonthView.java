import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JLabel;

public class MonthView implements ViewStrategy{

	private static String[] partialDayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
	static final String[] arrayOfAbbreviatedMonths = {"Jan", "Feb","Mar","Apr","May","Jun",
			"Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
	
	Font eventFont = new Font(new JLabel().getFont().toString(), Font.BOLD, 8);

	public void drawHeader(GregorianCalendar c, Graphics2D g2, Component container)
	{
				Font defaultFont = new JLabel().getFont(); 
				Font dayNumberFont = new Font(defaultFont.toString(), Font.BOLD, 36); 

				GregorianCalendar temp = (GregorianCalendar) c.clone(); 
				int backTrack = temp.getMinimalDaysInFirstWeek(); 
				//temp.add(Calendar.DAY_OF_MONTH, -backTrack);

			
				g2.setFont(defaultFont);
				String monthAtWeekStart = arrayOfAbbreviatedMonths[temp.get(Calendar.MONTH)];
		
				g2.drawString(monthAtWeekStart + " " + Integer.toString(temp.get(Calendar.YEAR)), 5, 25);

	}


	public void drawEventGrid(GregorianCalendar c, DataModel d, Graphics2D g2, Component container)
	{
				 Map<String, TreeSet<Event>> events = d.getEvents(); 
				 

				//Draw line dividing hours and days
				int currDate =  c.get(Calendar.DAY_OF_MONTH);
				c.set(Calendar.DAY_OF_MONTH, 1);		
				int start = c.get(Calendar.DAY_OF_WEEK);
				c.add(Calendar.MONTH, 1);
			    c.add(Calendar.DATE, -1);
			    
			    int numDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
			    
			    c.set(Calendar.DAY_OF_MONTH, 1);
			    
			    int maxWeeknumber = 5;
			    
			    if(start == 6 && numDays > 30) {
			    	maxWeeknumber = 6;
			    }else if(start == 7 && numDays >= 30) {
			    	maxWeeknumber = 6;
			    }
				

				g2.setColor(Color.LIGHT_GRAY);
				int offset = 0;
				int vOffset = 120;
				int rows = 7;
				
				if(maxWeeknumber == 6) {
					vOffset = 100;
					rows = 8;
				}	
				
				int height = vOffset*(rows-2);
				Line2D.Double vertLine = new Line2D.Double(offset, 0, offset, height); 
				g2.draw(vertLine);

				for (int i = 1; i<rows; i++)
				{
					Line2D.Double vertLine2 = new Line2D.Double(offset + (i * ((container.getWidth()-offset)/7.0)), 0, offset + (i * ((container.getWidth()-offset)/7.0)), height);
					g2.draw(vertLine2);

				}

				//Draw the time text
				int hour = 1;  
				String halfOfDay = "am"; 

				for (int i = 0; i<rows-1; i ++)
				{	
					//Draw the Lines indicating where hours begin 
					g2.setColor(Color.LIGHT_GRAY);
					Line2D.Double hourLine = new Line2D.Double(0, i*vOffset, container.getWidth(), i*vOffset); 
					g2.draw(hourLine); 
				}
				
				//c.set(Calendar.MONTH, currMonth);
				int backTrack = 1 - c.get(Calendar.DAY_OF_WEEK); 				
				
				//c.set(Calendar.DAY_OF_MONTH, currDate);
				

				GregorianCalendar temp = ((GregorianCalendar) c.clone()); 
				temp.add(Calendar.DAY_OF_MONTH, backTrack);

				//ArrayList<Event> eventsThisMonth = new ArrayList<Event>(); 
				
				Map<String, TreeSet<Event>> eventsThisMonth = new HashMap<>();
			
				if (events.size() !=0); 
				{

					for (int i = 0; i < numDays; i++)
					{
						int month = temp.get(Calendar.MONTH) + 1; 
						int day = temp.get(Calendar.DAY_OF_MONTH); 
						int year = temp.get(Calendar.YEAR); 
						String date = DataModel.formatDate(DataModel.translateDateToString(month, day, year)); 
						if (events.get(date) != null) {
							eventsThisMonth.put(date, events.get(date)); 
						}
						
						temp.add(Calendar.DAY_OF_MONTH, 1);	
					}
					
					Font defaultFont = new JLabel().getFont(); 
					Font dayNumberFont = new Font(defaultFont.toString(), Font.BOLD, 36); 
					
					for (int i = 0; i < 7; i++)
					{
						String text = partialDayNames[i]; 

						g2.setColor(Color.DARK_GRAY);
						g2.setFont(defaultFont);
						g2.drawString(text, (int) (4 + (offset + (i * ((container.getWidth()-offset)/7.0)))), 15);

						g2.setFont(dayNumberFont);
						

						g2.drawString(Integer.toString(temp.get(Calendar.DAY_OF_MONTH)), (int) (4 + (offset + (i * ((container.getWidth()-offset)/7.0)))), 45);

						temp.add(Calendar.DAY_OF_MONTH, 1);
					}
					
					for(int i=1; i<rows-2;i++) {
						for(int j=0; j<7; j++) {
							g2.setFont(dayNumberFont);

							g2.drawString(Integer.toString(temp.get(Calendar.DAY_OF_MONTH)), (int) (4 + (offset + (j * ((container.getWidth()-offset)/7.0)))), vOffset*i + 45);

							temp.add(Calendar.DAY_OF_MONTH, 1);
						}
					}


					//Get events from the datModel 

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

						g2.setColor(new Color(0, 0, 255, 100));	
						int index = startIndex + weekDayIndex - 2;

						//Draw the Rectangle representing the scheduled event 
						double x = (((index) %7)  * ((container.getWidth()-offset)/7.0));
						double y = ((index)/7)*vOffset;
						double width = ((container.getWidth()-offset)/7.0);
						double h = ((vOffset));
						Rectangle2D.Double eventBox = new Rectangle2D.Double( x, y, 
								width,  h); 
						g2.fill(eventBox);
						
						TreeSet<Event> treeSet = eventsThisMonth.get(eventDate);
						
						Iterator<Event> iterator2 = treeSet.iterator();
						String eventStr = "";
						g2.setFont(eventFont);
						g2.setColor(Color.WHITE);
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
	
	public Dimension getGridDimension() {
		return new Dimension(1000,840);
	}
}