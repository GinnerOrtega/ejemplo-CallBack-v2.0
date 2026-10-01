package co.edu.unicauca.cliente.controladores;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class UsuarioCllbckImpl extends UnicastRemoteObject implements UsuarioCllbckInt
{
    public UsuarioCllbckImpl() throws RemoteException
    {
        super();
    }

    @Override
    public void notificar(String mensaje, int cantidadUsuarios) throws RemoteException
    {
        System.out.println();
        System.out.println(">> [CHAT PÚBLICO] " + mensaje);
        System.out.println(">> Cantidad de usuarios conectados: " + cantidadUsuarios);
    }

    @Override
    public void notificarMensajePrivado(String nickNameOrigen, String mensaje) throws RemoteException
    {
        System.out.println();
        System.out.println(">> [MENSAJE PRIVADO de " + nickNameOrigen + "] " + mensaje);
    }

    @Override
    public boolean estaConectado() throws RemoteException
    {
        //si este método responde, el cliente está vivo
        return true;
    }
}
