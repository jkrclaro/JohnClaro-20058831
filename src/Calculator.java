import java.awt.*;
import javax.swing.*;

public class Calculator extends JPanel
{
	private JButton[] numButtons;
	private JButton[] opButtons;
	private JTextField uField;
	private JTextField bField;
	
	public Calculator()
	{
		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWidths = new int[] {80, 80, 80, 80};
		gbl.rowHeights = new int[] {80, 80, 80, 80, 80, 80};
		setLayout(gbl);
		GridBagConstraints gbc = new GridBagConstraints();
		
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
		
		// Numbers
		numButtons = new JButton[10];
		for (int i=0; i<numButtons.length; i++)
		{
			numButtons[i] = new JButton("" + i);
			gbc.gridx = numConstraints[i][0];
			gbc.gridy = numConstraints[i][1];
			gbc.gridwidth = numConstraints[i][2];
			gbc.gridheight = numConstraints[i][3];
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
			
		opButtons = new JButton[5];
		opButtons[0] = new JButton("+");
		opButtons[1] = new JButton("-");
		opButtons[2] = new JButton("*");
		opButtons[3] = new JButton("/");
		opButtons[4] = new JButton("Submit");
		
		for (int i=0; i<opButtons.length; i++)
		{
			gbc.gridx = opConstraints[i][0];
			gbc.gridy = opConstraints[i][1];
			gbc.gridwidth = opConstraints[i][2];
			gbc.gridheight = opConstraints[i][3];
			add(opButtons[i], gbc);
		}
		
		// Upper display
		uField = new JTextField(" ");
		uField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		uField.setEditable(false);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 4;
		gbc.gridheight = 1;
		add(uField, gbc);
		
		// Bottom display
		bField = new JTextField(" ");
		bField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		bField.setEditable(false);
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 4;
		gbc.gridheight = 1;
		add(bField, gbc);
	}
	
	public static void main(String[] args)
	{
		JFrame frame = new JFrame("Calculator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLayout(new BorderLayout());
		frame.add(new Calculator(), BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}