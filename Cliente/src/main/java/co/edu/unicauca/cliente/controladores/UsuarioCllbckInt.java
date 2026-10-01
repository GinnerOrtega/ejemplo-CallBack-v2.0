package co.edu.unicauca.cliente.controladores;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaz remota del CLIENTE (objeto callback).
 * El servidor la usa para "devolver la llamada" al cliente.
 * IMPORTANTE: esta interfaz debe ser IDENTICA en el proyecto Servidor y en el proyecto Cliente.
 */
public interface UsuarioCllbckInt extends Remote
{
    /** Callback para los mensajes públicos (a todos). */
    public void notificar(String mensaje, int cantidadUsuarios) throws RemoteException;

    /** Callback para los mensajes privados (requerimiento e). */
    public void notificarMensajePrivado(String nickNameOrigen, String mensaje) throws RemoteException;

    /**
     * Permite al servidor comprobar si el cliente sigue conectado (requerimientos f y g).
     * Si el cliente terminó abruptamente, la invocación lanza RemoteException.
     */
    public boolean estaConectado() throws RemoteException;
}
