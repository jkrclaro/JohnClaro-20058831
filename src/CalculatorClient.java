import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import javax.swing.*;

public class CalculatorClient extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	private JButton[] numButtons;
	private JButton[] opButtons;
	private JTextField uField;
	private JTextField bField;
	private String num1;
	private String num2;
	private String op;
	
	public CalculatorClient()
	{
		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWidths = new int[] {80, 80, 80, 80};
		gbl.rowHeights = new int[] {80, 80, 80, 80, 80, 80};
		setLayout(gbl);
		GridBagConstraints gbc = new GridBagConstraints();
		
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
		
		numButtons = new JButton[10];
		for (int i=0; i<numButtons.length; i++)
		{
			numButtons[i] = new JButton("" + i);
			numButtons[i].addActionListener(this);
			
			gbc.gridx = numConstraints[i][0]; // Apply the x coordinate of the button
			gbc.gridy = numConstraints[i][1]; // Apply the y coordinate of the button
			gbc.gridwidth = numConstraints[i][2]; // Apply the width of the button
			gbc.gridheight = numConstraints[i][3]; // Apply the height of the button
			gbc.fill = GridBagConstraints.BOTH;
			gbc.insets = new Insets(2, 2, 2, 2);
			add(numButtons[i], gbc);
		}
		
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
		opButtons = new JButton[5];
		opButtons[0] = new JButton("+");
		opButtons[1] = new JButton("-");
		opButtons[2] = new JButton("*");
		opButtons[3] = new JButton("/");
		opButtons[4] = new JButton("Submit");
		
		// Same as the number button
		for (int i=0; i<opButtons.length; i++)
		{
			gbc.gridx = opConstraints[i][0];
			gbc.gridy = opConstraints[i][1];
			gbc.gridwidth = opConstraints[i][2];
			gbc.gridheight = opConstraints[i][3];
			
			opButtons[i].setEnabled(false);
			opButtons[i].addActionListener(this);
			
			add(opButtons[i], gbc);
		}
		
		/**
		 * Upper display, this is the display that gets updated when adding in numbers
		 */
		uField = new JTextField();
		uField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		uField.setEditable(false);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 4;
		gbc.gridheight = 1;
		add(uField, gbc);
		
		/**
		 * Bottom display, this is the display that gets updated when the server
		 * sends you back the answer
		 */
		bField = new JTextField();
		bField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		bField.setEditable(false);
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 4;
		gbc.gridheight = 1;
		add(bField, gbc);
	}
	
	/**
	 * A method to go through each operator buttons and assign them to be enabled or not.
	 * This does not include the Submit button.
	 * @param setting - False to disable all buttons or True to enable all of them
	 */
	public void adjustOperatorButtons(Boolean setting)
	{
		for (int i=0; i<opButtons.length - 1; i++)
		{
			opButtons[i].setEnabled(setting);
		}
	}
	
	/**
	 * Listen for buttons pressed in the calculator GUI.
	 * At the start, the operator buttons are disabled so you first must have to
	 * build the first operand.
	 * Once a number is pressed, you can now press an operator or build the operand more.
	 * After an operator is pressed
	 */
	public void actionPerformed(ActionEvent e) 
	{	
		for (int i=0; i<numButtons.length; i++)
		{
			if (e.getSource() == numButtons[i])
			{
				uField.setText(uField.getText() + i);
			}
		}
		
		if (uField.getText() != null && !uField.getText().isEmpty())
		{
			adjustOperatorButtons(true);
		}
		
		if (op != null && !op.isEmpty())
		{
			opButtons[4].setEnabled(true);
		}
			
		if (e.getSource() == opButtons[0])
		{
			num1 = uField.getText();
			op = "+";
			uField.setText("");
			adjustOperatorButtons(false);
		}
		
		if (e.getSource() == opButtons[1])
		{
			num1 = uField.getText();
			op = "-";
			uField.setText("");
			adjustOperatorButtons(false);
		}
		
		if (e.getSource() == opButtons[2])
		{
			num1 = uField.getText();
			op = "*";
			uField.setText("");
			adjustOperatorButtons(false);
		}
		
		if (e.getSource() == opButtons[3])
		{
			num1 = uField.getText();
			op = "/";
			uField.setText("");
			adjustOperatorButtons(false);
		}
		
		if (e.getSource() == opButtons[4]) // When submitted
		{
			try 
			{	
				num2 = uField.getText();
				String clientData = num1 + "," + op + "," + num2;
				CalculatorInterface obj = (CalculatorInterface)Naming.lookup("CalculatorServer");
				String ans = obj.calculate(clientData);
				bField.setText("Data received from Server: " + ans);
				uField.setText("");
			}
			catch (Exception e1) 
			{
				JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
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