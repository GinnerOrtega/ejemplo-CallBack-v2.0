package co.edu.unicauca.servidor.controladores;

import co.edu.unicauca.cliente.controladores.UsuarioCllbckInt;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Interfaz remota del SERVIDOR de chat.
 * IMPORTANTE: esta interfaz debe ser IDENTICA en el proyecto Servidor y en el proyecto Cliente.
 */
public interface ControladorServidorChatInt extends Remote
{
    /** a) y b) Registra la referencia remota del cliente junto con un nickName único. */
    public boolean registrarReferenciaUsuario(UsuarioCllbckInt usuario, String nickName) throws RemoteException;

    /** c) Devuelve los nickName de los usuarios registrados y activos. */
    public List<String> obtenerNickNamesActivos() throws RemoteException;

    /** d) El cliente sale del chat y se elimina su referencia en el servidor. */
    public boolean salirDelChat(String nickName) throws RemoteException;

    /** e) y f) Envía un mensaje privado; retorna la respuesta del servidor para el emisor. */
    public String enviarMensajePrivado(String nickNameOrigen, String nickNameDestino, String mensaje) throws RemoteException;

    /** g) Envía un mensaje público a todos los usuarios conectados. */
    public void enviarMensaje(String nickNameOrigen, String mensaje) throws RemoteException;

    /** h) Devuelve la cantidad de usuarios activos. */
    public int obtenerCantidadUsuariosActivos() throws RemoteException;
}
