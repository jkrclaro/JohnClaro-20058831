/*
Filename: HelloWorld.java
*/

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/*

Classname: HelloWorld
Comment: The remote interface.

*/

public interface HelloWorld extends Remote 
{
	public String helloWorld(String clientData) throws RemoteException;
}