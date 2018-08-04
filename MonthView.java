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

public class MonthView implements ViewStrategy{

	// Declaring and defining an array of days of a given week followed by an array of months
	private static String[] partialDayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
	static final String[] arrayOfMonths = {"January", "Feb","Mar","Apr","May","Jun",
	"Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
	
	// Declaring and defining a font so that events can appear on the month on the appropriate date
	Font eventFont = new Font(new JLabel().getFont().toString(), Font.BOLD, 8);
	
	public void drawHeader(GregorianCalendar c, Graphics2D g2, Component container)
	{
		Font defaultFont = new JLabel().getFont(); 
        Font dayNumberFont = new Font(defaultFont.toString(), Font.BOLD, 28); 
		
		GregorianCalendar temp = (GregorianCalendar) c.clone(); 
		
		g2.setFont(dayNumberFont);
		String monthAtWeekStart = arrayOfMonths[temp.get(Calendar.MONTH)];
		
		g2.drawString(monthAtWeekStart + " " + Integer.toString(temp.get(Calendar.YEAR)), 450, 35);
	
	}
	
	
	
	public void drawEventGrid(GregorianCalendar c, DataModel d, Graphics2D g2, Component container)
	{
		Map<String, TreeSet<Event>> events = d.getEvents(); 
		
//		int currDate = c.get(Calendar.DAY_OF_MONTH);
		c.set(Calendar.DAY_OF_MONTH, 1);	
		
		int start = c.get(Calendar.DAY_OF_WEEK);
		c.add(Calendar.MONTH, 1);
		c.add(Calendar.DATE, -1);
		
		int numDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		c.set(Calendar.DAY_OF_MONTH, 1);
		
		int maxWeeknumber = 5;
		
		if(start == 6 && numDays > 30) 
		{
			maxWeeknumber = 6;
		}
		else if(start == 7 && numDays >= 30) {
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
		
		for (int i = 0; i<rows-1; i ++)
		{	
			//Draw the Lines indicating where the week begins
			g2.setColor(Color.LIGHT_GRAY);
			Line2D.Double weekLine = new Line2D.Double(0, i*vOffset, container.getWidth(), i*vOffset); 
			g2.draw(weekLine); 
		}
		
		int backTrack = 1 - c.get(Calendar.DAY_OF_WEEK); 

		GregorianCalendar temp = ((GregorianCalendar) c.clone()); 
		temp.add(Calendar.DAY_OF_MONTH, backTrack);
		
		// Declaring and defining a Map that is responsible for collecting the events of current month
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
				
			int weekDayIndex; 
			
			// Defining keyValue data set to organize events of this month
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
				double x = (((index) %7) * ((container.getWidth()-offset)/7.0));
				double y = ((index)/7)*vOffset;
//				double width = ((container.getWidth()-offset)/7.0);
//				double h = ((vOffset));
				
//				Rectangle2D.Double eventBox = new Rectangle2D.Double( x, y, width, h); 
				
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
 	 * This function determines whether a scrollbar is necessary or not
 	 */
	public Dimension getGridDimension() {
		return new Dimension (1000, 840);
	}
}
