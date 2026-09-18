package co.edu.unicauca.cliente.controladores;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UsuarioCllbckInt extends Remote
{	
    public void notificar(String mensaje, int cantidadUsuarios) throws RemoteException;
}


