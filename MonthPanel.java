
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
 * Custom JPanel Class - Utilizes custom JButton Class to present a calendar. Calendar changes if prev/next buttons 
 * are pressed. Current day is show in red. Has method for attaching listeners for the day buttons so the implementing program 
 * can dictate their function.  
 * @author Christina Andrade
 */
public class MonthPanel extends JPanel{

	private static final String[] arrayOfMonths = {"January", "February","March","April","May","June",
			"July", "August", "September", "October", "November", "December" };
	private static final String[] arrayOfDays = {"S", "M", "T", "W", "T", "F", "S" }; 

	private GregorianCalendar calendar;
	private String selected; 
	private ArrayList<DayButton> dayButtons; 
	  
	private JPanel calendarPanel; 
	private JLabel monthLabel; 
	private JButton prevMonth; 
	private JButton nextMonth; 


	/**
	 * Constructs and compiles all the components of the clickable calendar 
	 * @param cal the GregorianCalendar whose month the MonthPanel should represent 
	 */
	public MonthPanel(GregorianCalendar cal)
	{	
		calendar = (GregorianCalendar) cal.clone(); 
		
		//Gets the day_of_Month of the calendar 
		selected = cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR); 

		//Construct DayButtons without calendars 
		dayButtons = new ArrayList<DayButton>();   
		for (int i = 0; i < 42; i++)
		{
			dayButtons.add(new DayButton()); 
		}	 
		
		setLayout(new BorderLayout());
		
		//Create and format the calendar panel
		calendarPanel = new JPanel(); 
		calendarPanel.setLayout(new GridLayout(7,7));

		//Add day Labels to top of calendar
		for (String s : arrayOfDays)
		{
			calendarPanel.add(new JLabel(s, SwingConstants.CENTER), BorderLayout.NORTH); 
		}

		//Add buttons to the calendar panel 
		for (DayButton d : dayButtons)
		{
			calendarPanel.add(d); 
		}

		//create and add label that shows the current month / year being represented 
		monthLabel = new JLabel((arrayOfMonths[(calendar.get(Calendar.MONTH))] + " " + calendar.get(Calendar.YEAR)), SwingConstants.CENTER); 
		monthLabel.setPreferredSize(new Dimension(150, 20));
		
		//Create Prev Button
		Dimension buttonDim = new Dimension(20,20); 
		prevMonth = new JButton("<"); 
		prevMonth.setPreferredSize(buttonDim);
		
		//prev button changes the month of the calendar being represented to the previous month when clicked
		prevMonth.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				calendar.add(Calendar.MONTH, -1);
				repaint(); 
			}
		}
				);

		//Create Next Button
		nextMonth = new JButton(">"); 
		nextMonth.setPreferredSize(buttonDim);
		
		//next button changes the month of the calendar being represented to the next month when clicked 
		nextMonth.addActionListener( new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				calendar.add(Calendar.MONTH, 1);
				repaint();  
			}
		}
				);

		//create wrapper panel for correct formatting for the header
		JPanel header = new JPanel(); 
		
		//add the components to the JPanel 
		header.add(monthLabel);
		header.add(prevMonth);
		header.add(nextMonth); 
		add(header, BorderLayout.NORTH);
		add(calendarPanel, BorderLayout.CENTER);
		
	}

	/**
	 * Describes the unique look and feel of the component. Any aspects that need to change whenever the calendar
	 * is updated go here. 
	 * @param g Graphics package 
	 */
	public void paintComponent(Graphics g)
	{
		//Header is updated to the calendar's current month 	
		monthLabel.setText((arrayOfMonths[(calendar.get(Calendar.MONTH))] + " " + calendar.get(Calendar.YEAR)));
		monthLabel.setHorizontalAlignment(SwingConstants.CENTER);
		monthLabel.repaint(); 

		//Temporary calendar initialized to assign days to the DayButtons
		GregorianCalendar temp = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1); 
		
		//get the day of the week of the calendar 
		int weekDayIndex = temp.get(Calendar.DAY_OF_WEEK);

		//calculation to help move the calendar back to the first day of the week so calendar starts on the
		//correct date
		int backTrack = 1 - weekDayIndex;

		//move the calendar to the start of the week 
		temp.add(Calendar.DAY_OF_MONTH, backTrack); 

		//Calendar shows 42 days (6 weeks) - assign calendar's of each day to the day Buttons 
		for (int i = 0; i < 42; i++)
		{
			DayButton db = dayButtons.get(i); 
			db.updateDay(temp);

			//Grays out any dates that are visible but not part of the current month 
			//(days that are part of the previous or next month) 
			if (temp.get(Calendar.MONTH) != calendar.get(Calendar.MONTH))
			{
				db.setForeground(Color.GRAY);
			}
			else
			{
				db.setForeground(null);
			}
			
			//Marks the correct JButton as the selected day (on start selected day is day of runtime) 
			String dbDay = db.getCalendar().get(Calendar.MONTH) + "/" + db.getCalendar().get(Calendar.DAY_OF_MONTH) 
					+ "/" + db.getCalendar().get(Calendar.YEAR); 
			if (dbDay.equals(selected))
			{
				Border selectedBorder = new LineBorder(Color.RED, 1);
				db.setBorder(selectedBorder);
			}
			else
			{
				db.setBorder(new JButton().getBorder());
			}

			temp.add(Calendar.DAY_OF_MONTH, 1);

		super.paintComponent(g);
		}

	}

	/**
	 * updates the calendar being represented by the MonthPanel and repaints
	 * @param cal the new calendar to be represented 
	 */
	public void updateCalendar(GregorianCalendar cal)
	{
		calendar = (GregorianCalendar) cal.clone();  
		this.repaint(); 
	}
	
	/**
	 * updates the selected date (intended to be used when buttons are attached to listeners that 
	 * fetch a clicked buttons date - so the clicked date will be the current selected date)
	 * @param cal the calendar of the selected date 
	 */
	public void updateSelected(GregorianCalendar cal)
	{
		selected = cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR); 
	}
	
	/**
	 * Returns the calendar being represented by the monthPanel 
	 * @return c  
	 */
	public GregorianCalendar getCurrentCalendar()
	{
		return calendar; 
	}

	/**
	 * Attached button listeners to all the buttons - intended so implementing program can decide the buttons' use
	 * @param a the actionListener to be attached to the button 
	 */
	public void attachButtonListeners(ActionListener a)
	{
		for (DayButton b : dayButtons)
		{
			b.addActionListener(a);
		}
	}
	
}
