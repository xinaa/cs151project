
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent; 

public class SimpleCalendar {
	
	public enum ViewType{
		
		DAY, WEEK, MONTH, YEAR, AGENDA 
	}
	
	static final String[] arrayOfMonths = {"January", "February","March","April","May","June",
			"July", "August", "September", "October", "November", "December" };
	static final String[] arrayOfDays = {"S", "M", "T", "W", "T", "F", "S" }; 

	private static GregorianCalendar c; 
	private static ViewStrategy v; 
	private static ViewType currentView; 
	
	
	/**
	 * Updates the main calendar of the program 
	 * @param cal
	 */
	public void updateCalendar(GregorianCalendar cal)
	{
		c = (GregorianCalendar) cal.clone(); 
	}
	
	

	public static void main(String[] args) throws IOException
	{
		
		c = new GregorianCalendar(); 
		DataModel myCal = new DataModel(); 
		
		//program starts with day view
		v = new DayView();	
		currentView = ViewType.DAY; 
		
		ViewPanel viewPanel = new ViewPanel(v, c, myCal);
		
		//attach views to data model 
		myCal.attach(viewPanel);


		//Load Events from events file if any previously scheduled events exist 
		//user will be informed if the calendar doesn't have any events yet 
		myCal.loadEvents(); 

		//Create the frame to hold the entire program
		JFrame frame = new JFrame(); 
		frame.setLayout(new BorderLayout());
		frame.setSize(new Dimension (1300, 1000 ));

		//Create the click-able calendarPanel
		MonthPanel monthPanel = new MonthPanel(c); 
		Dimension monthDimension = new Dimension(250,200);
		monthPanel.setPreferredSize(monthDimension);
		
		//Wrapper class to keep click-able calendar component consistently sized
		JPanel wrapperPanel = new JPanel(); 
		wrapperPanel.setMaximumSize(monthDimension);
		wrapperPanel.add(monthPanel); 

		//Create and format panel holding buttons - (create, quit, prev view, next view)
		JPanel buttonPanel = new JPanel(); 
		buttonPanel.setLayout( new BorderLayout());
		
		//create executive function buttons
		JButton createButton = new JButton("Create");
		JButton quitButton = new JButton("Quit");
		JButton fromFileButton = new JButton("From File"); 

		//Add executive function buttons to panel
		JPanel westButtonPanel = new JPanel(); 
		westButtonPanel.add(createButton); 
		westButtonPanel.add(quitButton);
		westButtonPanel.add(fromFileButton); 
		
//		buttonPanel.setAlignmentX(Component.RIGHT_ALIGNMENT); 
		
		
		fromFileButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent event)
					{
						myCal.loadInRecurringEvents();
					}
				});

		//When quit button is pressed, all events in the model are saved to the events.txt file
		quitButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event) 
			{
				myCal.saveEventsToFile(); 
				System.exit(0);
			}
		}); 

		//when create button is pressed, a new event is added to the model 
		createButton.addActionListener(new ActionListener() 
			{
			
		
				public void actionPerformed(ActionEvent event)
			{
				//gets the selected date from the monthPanel (clickable calendar)
				GregorianCalendar eventCal = monthPanel.getCurrentCalendar(); 

				String dateOfEvent = DataModel.translateDateToString((eventCal.get(Calendar.MONTH) + 1),
							eventCal.get(Calendar.DAY_OF_MONTH),eventCal.get(Calendar.YEAR));
			
				//Build components for a pop up box where user can input event details
				JPanel parent = new JPanel();
				parent.setLayout( new BorderLayout());
				JTextField eventTitle = new JTextField("Untitled Event");
				JTextField eventDate = new JTextField(dateOfEvent); 
				JTextField startTimeInput = new JTextField("Start Time");
				startTimeInput.setPreferredSize(new Dimension(100,20));
				JLabel to = new JLabel("to"); 
				JTextField endTimeInput = new JTextField("End Time"); 
				endTimeInput.setPreferredSize(new Dimension(100,20)); 

				JPanel p = new JPanel();
				
				p.add(eventDate); 
				p.add(startTimeInput);
				p.add(to); 
				p.add(endTimeInput); 
				parent.add(eventTitle, BorderLayout.NORTH); 
				parent.add(p, BorderLayout.CENTER); 
				
				//pop up box for user input and create Event
				int result = JOptionPane.showConfirmDialog(null,parent, "Please enter event details", JOptionPane.OK_CANCEL_OPTION); 
				if (result == JOptionPane.OK_OPTION)
				{
					Event eventToSchedule = new Event(eventTitle.getText(), eventDate.getText(), startTimeInput.getText(), endTimeInput.getText()); 
					myCal.addEvent(eventToSchedule); 
				}
			}
			}); 
		
		
		//attach button listeners to the buttons in the clickable calendar 
		monthPanel.attachButtonListeners( new ActionListener()
		{
			@Override
			//buttons get the calendar of the day button clicked and updates the entire program 
			//so all views correspond to the clicked date 
			public void actionPerformed(ActionEvent event)
			{
				DayButton d = (DayButton) event.getSource(); 
				c = (GregorianCalendar) d.getCalendar().clone();
				monthPanel.updateCalendar(c);
				monthPanel.updateSelected(c);
				viewPanel.updateCalendar(c); 
				frame.repaint(); 
			}
		}); 

		//create buttons to change the current view 
		JButton prevView = new JButton("<");
		JButton nextView = new JButton(">"); 
		JButton todayButton = new JButton("Today");
		JButton dayViewButton = new JButton("Day");
		JButton weekViewButton = new JButton("Week");
		JButton monthViewButton = new JButton("Month");
		JButton yearViewButton = new JButton("Year"); 
		JButton agendaButton = new JButton("Agenda"); 


		todayButton.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent event)
			{
				c = new GregorianCalendar(); 
				
				viewPanel.updateCalendar(c);
				monthPanel.updateSelected(c); 
				monthPanel.updateCalendar(c);
			}
		});
		
		//Action Listener for the agenda Button
				agendaButton.addActionListener(new ActionListener() 
				{
					//ActionPerformed for the pop up window when Agenda button is selected
					public void actionPerformed(ActionEvent event)
					{

						final JFrame agendaDateFetcherFrame = new JFrame();
			            final JTextField startDate;
			            final JTextField endDate;
			            agendaDateFetcherFrame.setTitle("Agenda Date Lookup");
			            final JPanel panel = new JPanel();
			            panel.setLayout(new GridLayout(5, 5));
			            panel.add(new JLabel("     Start Date: (MM/DD/YYYY) "));
			            startDate = new JTextField(5);
			            panel.add(startDate);
			            panel.add(new JLabel("     End Date: (MM/DD/YYYY) "));
			            endDate = new JTextField(5);
			            panel.add(endDate);
			            panel.add(new JLabel());
			            JButton getAgenda = new JButton("Get Agenda");
			            panel.add(getAgenda);
			            agendaDateFetcherFrame.add(panel);
			            agendaDateFetcherFrame.pack();
			            agendaDateFetcherFrame.setVisible(true);
			            agendaDateFetcherFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);   
			            getAgenda.addActionListener(new ActionListener()
			            {
			            	@Override
			            		public void actionPerformed(ActionEvent e)
			            		{
			            			agendaDateFetcherFrame.setVisible(false);
			            	
			            			String enteredStartDate = startDate.getText(); 
			            			String enteredEndDate = endDate.getText(); 
			            			
			            			 if (enteredStartDate.length() == 10 && enteredEndDate.length() == 10)
			 			         {
			            				currentView = ViewType.AGENDA;
			 			            	viewPanel.changeView(new AgendaView(enteredStartDate, enteredEndDate));
			 		            		dayViewButton.setForeground(Color.BLACK);
			 		            		weekViewButton.setForeground(Color.BLACK); 
			 		            		monthViewButton.setForeground(Color.BLACK);
			 		            		yearViewButton.setForeground(Color.BLACK);
			 		            		agendaButton.setForeground(Color.RED);
			 		            		frame.repaint(); 
			 			          }
			 			          else
			 			          {
			 			            		javax.swing.JOptionPane.showMessageDialog(null, "Dates for Agenda View not entered properly. Please try again.");
			 			          }
			            		}

			            	});

					}


				}); 
			
		
		dayViewButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				currentView = ViewType.DAY; 
				viewPanel.changeView(new DayView()); 
				
				dayViewButton.setForeground(Color.RED);
				weekViewButton.setForeground(Color.BLACK); 
				monthViewButton.setForeground(Color.BLACK);
				yearViewButton.setForeground(Color.BLACK);
				agendaButton.setForeground(Color.BLACK);

				frame.repaint();  
			}
		}); 
		
		weekViewButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				currentView = ViewType.WEEK;  
				viewPanel.changeView(new WeekView()); 
				
				dayViewButton.setForeground(Color.BLACK);
				weekViewButton.setForeground(Color.RED); 
				monthViewButton.setForeground(Color.BLACK);
				yearViewButton.setForeground(Color.BLACK);
				agendaButton.setForeground(Color.BLACK);
				
				frame.repaint(); 
			}
		}); 
//		
//		monthViewButton.addActionListener(new ActionListener()
//		{
//			public void actionPerformed(ActionEvent event)
//			{
//				currentView = ViewType.MONTH; 
//				updateView(monthView); 
//			}
//		}); 
//		
		yearViewButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				currentView = ViewType.YEAR; 
				viewPanel.changeView(new YearView()); 
				
				dayViewButton.setForeground(Color.BLACK);
				weekViewButton.setForeground(Color.BLACK); 
				monthViewButton.setForeground(Color.BLACK);
				yearViewButton.setForeground(Color.RED);
				agendaButton.setForeground(Color.BLACK);
				
				frame.repaint(); 
			}
		}); 
//		
//		agendaButton.addActionListener(new ActionListener()
//		{
//			public void actionPerformed(ActionEvent event)
//			{
//				currentView = ViewType.AGENDA; 
//				updateView(agendaView); 
//			}
//		}); 
		
		
		dayViewButton.setForeground(Color.RED);
		
		//if previous button is pressed, program's calendar moves to the previous day - all views updated
		prevView.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent event)
					{
						if (currentView == ViewType.DAY)
						{
							c.add(Calendar.DAY_OF_MONTH, -1);
						}
						
						else if (currentView == ViewType.WEEK)
						{
							c.add(Calendar.WEEK_OF_MONTH, -1);
						}
						
						else if (currentView == ViewType.MONTH)
						{
							c.add(Calendar.MONTH, -1);
						}
						
						else if (currentView == ViewType.YEAR)
						{
							c.add(Calendar.YEAR, -1);
						}
						//AGENDA?
							
						
						viewPanel.updateCalendar(c); 
						monthPanel.updateSelected(c); 
						monthPanel.updateCalendar(c);
					}
				});
		
		//if next button is pressed, program's calendar moves to the next day - all views updated
		nextView.addActionListener( new ActionListener()
			{
			
				public void actionPerformed(ActionEvent event)
				{
					if (currentView == ViewType.DAY)
					{
						c.add(Calendar.DAY_OF_MONTH, 1);
					}
					
					else if (currentView == ViewType.WEEK)
					{
						c.add(Calendar.WEEK_OF_MONTH, 1);
					}
					
					else if (currentView == ViewType.MONTH)
					{
						c.add(Calendar.MONTH, 1);
					}
					
					else if (currentView == ViewType.YEAR)
					{
						c.add(Calendar.YEAR, 1);
					}
					//AGENDA?
					
					viewPanel.updateCalendar(c); 
					monthPanel.updateSelected(c);
					monthPanel.updateCalendar(c);
				}
			}); 

		//Format and add all components 
		JPanel eastButtonPanel = new JPanel(); 
		eastButtonPanel.add(todayButton);
		eastButtonPanel.add(dayViewButton); 
		eastButtonPanel.add(weekViewButton);
		eastButtonPanel.add(monthViewButton);
		eastButtonPanel.add(yearViewButton); 
		eastButtonPanel.add(agendaButton); 
		eastButtonPanel.add(prevView); 
		eastButtonPanel.add(nextView); 
		

		buttonPanel.add(westButtonPanel, BorderLayout.WEST);
		buttonPanel.add(eastButtonPanel, BorderLayout.EAST); 
		frame.add(buttonPanel, BorderLayout.NORTH);
		frame.add(wrapperPanel, BorderLayout.WEST); 
		frame.add(viewPanel, BorderLayout.CENTER); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.pack(); 
		frame.setVisible(true);

	}
}
