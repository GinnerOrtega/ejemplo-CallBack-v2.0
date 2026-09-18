

package co.edu.unicauca.servidor.controladores;

import co.edu.unicauca.cliente.controladores.UsuarioCllbckInt;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ControladorServidorChatInt extends Remote
{
    public boolean registrarReferenciaUsuario(UsuarioCllbckInt  usuario, String nickName) throws RemoteException;
    public List<String> obtenerNickNamesActivos() throws RemoteException;
    public boolean salirDelChat(String nickName) throws RemoteException;
    public void enviarMensajePrivado(String nickOrigen, String nickDestino, String mensaje) throws RemoteException;
    public void enviarMensaje(String nickOrigen, String mensaje)throws RemoteException;
    public int obtenerCantidadUsuariosActivos() throws RemoteException;
}


