import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import javax.swing.*;

public class CalculatorClient extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	private JButton[] numberButtons;
	private JButton[] operatorButtons;
	private JTextField upperField;
	private JTextField bottomField;
	private String firstOperand;
	private String secondOperand;
	private String operator;
	private GridBagConstraints gbc;
	
	public CalculatorClient()
	{
		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWidths = new int[] {80, 80, 80, 80};
		gbl.rowHeights = new int[] {80, 80, 80, 80, 80, 80};
		setLayout(gbl);
		gbc = new GridBagConstraints();
		
		createNumberButtons();
		createOperatorButtons();
		createUpperDisplay();
		createBottomDisplay();
	}
	
	private void createNumberButtons()
	{
		/**
		 *  An organized and efficient way to assign number buttons with coordinates.
		 */
		int[][] numConstraints = new int[][] {
		//   x,y,w,h
			{1,4,1,1}, // 0
			{1,3,1,1}, // 1
			{2,3,1,1}, // 2
			{3,3,1,1}, // 3
			{1,2,1,1}, // 4
			{2,2,1,1}, // 5
			{3,2,1,1}, // 6
			{1,1,1,1}, // 7
			{2,1,1,1}, // 8
			{3,1,1,1}, // 9
		};
		
		numberButtons = new JButton[10];
		for (int i=0; i<numberButtons.length; i++)
		{
			numberButtons[i] = new JButton("" + i);
			numberButtons[i].addActionListener(this);
			
			gbc.gridx = numConstraints[i][0]; // Apply the x coordinate of the button
			gbc.gridy = numConstraints[i][1]; // Apply the y coordinate of the button
			gbc.gridwidth = numConstraints[i][2]; // Apply the width of the button
			gbc.gridheight = numConstraints[i][3]; // Apply the height of the button
			gbc.fill = GridBagConstraints.BOTH;
			gbc.insets = new Insets(2, 2, 2, 2);
			add(numberButtons[i], gbc);
		}
	}
	
	private void createOperatorButtons()
	{
		// Operators
		int[][] opConstraints = new int[][] {
			//   x,y,w,h
				{0,4,1,1}, // +
				{0,3,1,1}, // -
				{0,2,1,1}, // *
				{0,1,1,1}, // /
				{2,4,2,1}, // Submit
			};
		
		// Instantiate the operator buttons
		operatorButtons = new JButton[5];
		operatorButtons[0] = new JButton("+");
		operatorButtons[1] = new JButton("-");
		operatorButtons[2] = new JButton("*");
		operatorButtons[3] = new JButton("/");
		operatorButtons[4] = new JButton("Submit");
		
		// Same as the number button
		for (int i=0; i<operatorButtons.length; i++)
		{
			gbc.gridx = opConstraints[i][0];
			gbc.gridy = opConstraints[i][1];
			gbc.gridwidth = opConstraints[i][2];
			gbc.gridheight = opConstraints[i][3];
			
			operatorButtons[i].setEnabled(false);
			operatorButtons[i].addActionListener(this);
			
			add(operatorButtons[i], gbc);
		}
	}
	
	private void createUpperDisplay()
	{
		upperField = new JTextField();
		upperField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		upperField.setEditable(false);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 4;
		gbc.gridheight = 1;
		add(upperField, gbc);
	}
	
	private void createBottomDisplay()
	{
		/**
		 * Bottom display, this is the display that gets updated when the server
		 * sends you back the answer
		 */
		bottomField = new JTextField();
		bottomField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		bottomField.setEditable(false);
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 4;
		gbc.gridheight = 1;
		add(bottomField, gbc);
	}
	
	/**
	 * A method to go through each operator buttons and assign them to be enabled or not.
	 * This does not include the Submit button.
	 * @param setting - False to disable all buttons or True to enable all of them
	 */
	private void adjustOperatorButtons(Boolean setting)
	{
		for (int i=0; i<operatorButtons.length - 1; i++)
		{
			operatorButtons[i].setEnabled(setting);
		}
	}
	
	/**
	 * Listens for when a button is pressed.
	 * If it has been pressed then append the number of the number button that was pressed to the
	 * upperField. Enable the operator buttons automatically.
	 * If the operator is not empty or null then disable the operator buttons and enable the submit button.
	 * @param event
	 */
	private void checkIfNumberIsPressed(ActionEvent event)
	{
		for (int i=0; i<numberButtons.length; i++)
		{
			if (event.getSource() == numberButtons[i])
			{
				upperField.setText(upperField.getText() + i);
				
				adjustOperatorButtons(true); // Enable operator buttons when a number is pressed
				if (operator != null && !operator.isEmpty())
				{
					adjustOperatorButtons(false); // Disable the operator buttons
					operatorButtons[4].setEnabled(true); // Enable the submit button
				}
			}
		}
	}
	
	/**
	 * Listens for when an operator button is pressed.
	 * If it has been pressed then the first operand is equal to the upperField,
	 * Then assign the operator to which operator button was pressed then clear the upperField to make
	 * space for the secondOperand. Also disable the operator buttons when it has been pressed.
	 * If the submit button was pressed then get the firstOperand, operator and secondOperand and use the RMI
	 * channel to send these data to the CalculatorServer. Get an answer back from the Calculator Server
	 * and set the answer to the bottomField.
	 * 
	 * operatorButtons[0] is +
	 * operatorButtons[1] is -
	 * operatorButtons[2] is *
	 * operatorButtons[3] is /
	 * operatorButtons[4] is the Submit button
	 * 
	 * @param event - The event that occurred in the GUI
	 */
	private void checkIfOperatorIsPressed(ActionEvent event)
	{
		if (event.getSource() == operatorButtons[0]) // Addition
		{
			firstOperand = upperField.getText();
			operator = "+";
			upperField.setText("");
			adjustOperatorButtons(false);
		}
		
		if (event.getSource() == operatorButtons[1]) // Subtraction
		{
			firstOperand = upperField.getText();
			operator = "-";
			upperField.setText("");
			adjustOperatorButtons(false);
		}
		
		if (event.getSource() == operatorButtons[2]) // Multiplication
		{
			firstOperand = upperField.getText();
			operator = "*";
			upperField.setText("");
			adjustOperatorButtons(false);
		}
		
		if (event.getSource() == operatorButtons[3]) // Division
		{
			firstOperand = upperField.getText();
			operator = "/";
			upperField.setText("");
			adjustOperatorButtons(false);
		}
		
		if (event.getSource() == operatorButtons[4]) // Submit
		{
			try 
			{	
				secondOperand = upperField.getText();
				String clientData = firstOperand + "," + operator + "," + secondOperand;
				CalculatorInterface calculatorObject = (CalculatorInterface)Naming.lookup("CalculatorServer");
				String answer = calculatorObject.calculate(clientData);
				bottomField.setText(answer);
				upperField.setText("");
				operator = "";
			}
			catch (Exception e1) 
			{
				JOptionPane.showMessageDialog(null, "1: " + e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * Listen for buttons pressed in the calculator GUI.
	 * At the start, the operator buttons are disabled so you first must have to
	 * build the first operand.
	 * Once a number is pressed, you can now press an operator or build the operand more.
	 * After an operator is pressed
	 */
	public void actionPerformed(ActionEvent event) 
	{	
		checkIfNumberIsPressed(event);
		checkIfOperatorIsPressed(event);
	}
	
	public static void main(String[] args)
	{
		JFrame frame = new JFrame("Client-01");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLayout(new BorderLayout());
		frame.add(new CalculatorClient(), BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}