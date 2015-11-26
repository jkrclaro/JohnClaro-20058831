/*
Filename: HelloWorldServer.java
*/

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

/*
Classname: HelloWorldServer
Purpose: The RMI server.
*/

public class HelloWorldServer extends UnicastRemoteObject implements HelloWorld 
{
	public HelloWorldServer() throws RemoteException 
	{
		super();
	}

	public String helloWorld(String data) 
	{
		System.out.println("Invocation to helloWorld was succesful!");
		
		List<String> clientData = Arrays.asList(data.split(","));
		System.out.println(clientData.get(0));
		System.out.println(clientData.get(1));
		System.out.println(clientData.get(2));
		
		return "Stephortless";
	}

	public static void main(String args[]) 
	{
		try 
		{
			HelloWorldServer obj = new HelloWorldServer();
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.rebind("HelloWorld", obj );
			System.out.println("HelloWorld bound in registry");
		}
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
