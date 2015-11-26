/*
Filename: HelloWorldClient.java
*/

import java.rmi.Naming;

/*
Classname: HelloWorldClient
Comment: The RMI client.
*/

public class HelloWorldClient 
{
	static String message = "blank";
	
	// The HelloWorld object "obj" is the identifier that is
	// used to refer to the remote object that implements
	// the HelloWorld interface.
	
	static HelloWorld obj = null;
	
	public static void main(String args[])
	{
		try 
		{
			obj = (HelloWorld)Naming.lookup("HelloWorld");
			message = obj.helloWorld();
			System.out.println("Message from the RMI-server was: " + message);
		}
		catch (Exception e) 
		{
			System.out.println("HelloWorldClient exception: "+ e.getMessage());
			e.printStackTrace();
		}
	}
}