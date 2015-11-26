import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;

public interface CalculatorInterface extends Remote 
{
	public String calculate(String clientData) throws RemoteException, ServerNotActiveException;
}