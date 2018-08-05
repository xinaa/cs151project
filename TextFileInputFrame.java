import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * JFrame that receives file path location from user.
 * @author Kevin
 *
 */
public class TextFileInputFrame extends JFrame{
	
	/**
	 * Components of the frame
	 */
	JLabel header;
	JTextField inputField;
	JButton submitButton;	
	
	/**
	 * Constants determining font size and frame's dimensions
	 */
	private static final int FONT_SIZE = 16;
	private static final int WIDTH = 600;
	private static final int HEIGHT = 120;
	
	/**
	 * Constructor that creates the components of the frame and attaches ActionListener to its button.
	 * Once the button is pressed, the text information within the text box is sent to the model
	 * @param model Model representative of the Calendar
	 */
	public TextFileInputFrame(DataModel model) {
		JFrame itself = this;
		
		this.setLayout(new BorderLayout());
		this.setSize(WIDTH, HEIGHT);
		
		header = new JLabel("Input the path file location of your recurring events input.");
		inputField = new JTextField();
		submitButton = new JButton("Confirm");
		changeAllFontSizes(new Component[] {header, inputField, submitButton}, FONT_SIZE);
		
		submitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				model.loadInRecurringEvents(inputField.getText());
				itself.dispose();
			}
			
		});
		
		this.add(header, BorderLayout.NORTH);
		this.add(inputField, BorderLayout.CENTER);
		this.add(submitButton, BorderLayout.EAST);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	void changeAllFontSizes(Component[] components, int n) {
		for(Component c : components) changeFontSize(c, n);
	}
	
	void changeFontSize(Component c, int size) {
		c.setFont(new Font(c.getFont().getName(), Font.PLAIN, size));
	}
	
}
