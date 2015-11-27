import java.awt.BorderLayout;
import java.awt.Color;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.DecimalFormat;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


public class CalculatorServer extends UnicastRemoteObject implements CalculatorInterface 
{
	private static final long serialVersionUID = 1L;
	
	private JTextPane textPane = new JTextPane();
	
	public CalculatorServer() throws RemoteException 
	{
		super();
		JFrame frame = new JFrame();
		JScrollPane scrollPane = new JScrollPane(textPane);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Server");
		frame.setSize(500, 300);
		frame.setLayout(new BorderLayout());
		frame.add(scrollPane, BorderLayout.CENTER);
		frame.setVisible(true);
	}
	
	/**
	 * Formats the value to have commas separating the numbers, e.g.
	 * If the value was 1234 then it will become 1,234 or
	 * If the value was 56789 then it will become 56,789
	 * @param value - Answer to be formatted
	 * @return Answer with commas separating if necessary.
	 */
	private String format(int value)
	{
		DecimalFormat decimalFormat = new DecimalFormat("#,###");
		String result = decimalFormat.format(value);
		return result;
	}
	
	public String calculate(String data) throws ServerNotActiveException 
	{	
		List<String> clientData = Arrays.asList(data.split(","));
		
		updateServerLog("Client-01 connected at IP: " + RemoteServer.getClientHost(), Color.RED);
		int answer = 0;
		
		try
		{
			int firstOperand = Integer.parseInt(clientData.get(0));
			String op = clientData.get(1);
			int secondOperand = Integer.parseInt(clientData.get(2));
			
			updateServerLog("Request from Client-01: ", Color.RED);
			updateServerLog("First Operand: " + firstOperand, Color.BLUE);
			updateServerLog("Second Operand: " + secondOperand, Color.BLUE);
			updateServerLog("Operator: " + op, Color.BLUE);
			
			if (op.equals("+"))
			{
				answer = firstOperand + secondOperand;
			}
			else if (op.equals("-"))
			{
				answer = firstOperand - secondOperand;
			}
			else if (op.equals("*"))
			{
				answer = firstOperand * secondOperand;
			}

			else if (op.equals("/"))
			{
				answer = firstOperand / secondOperand;
			}
			
			updateServerLog("Data to Client-01: " + String.valueOf(answer), Color.BLACK);
			
			return format(answer);
		}
		catch (Exception error)
		{
			return "  Invalid data! Please try again using valid inputs";
		}
	}
	
	/**
	 * Ensures that a new line is created in the textPane.
	 * @param log - The string that's being added to the text pane.
	 * @param colorSpecified - Changes the color text in the text pane.
	 */
	private void updateServerLog(String log, Color colorSpecified)
	{
		try
		{
			StyledDocument document = textPane.getStyledDocument();
			SimpleAttributeSet color = new SimpleAttributeSet();
			StyleConstants.setForeground(color, colorSpecified);
			document.insertString(document.getLength(), log + "\n", color);
		}
		catch (Exception error)
		{
			JOptionPane.showMessageDialog(null, "U: " + error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void main(String args[]) 
	{
		try 
		{
			CalculatorServer obj = new CalculatorServer();
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.rebind("CalculatorServer", obj );
		}
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, "M: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}
}
