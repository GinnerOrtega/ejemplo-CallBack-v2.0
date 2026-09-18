package co.edu.unicauca.servidor.controladores;
import java.util.List;
import co.edu.unicauca.cliente.controladores.UsuarioCllbckInt;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ControladorServidorChatInt extends Remote
{
    public boolean registrarReferenciaUsuario(UsuarioCllbckInt  usuario) throws RemoteException;
    public List<String> consultarUsuariosActivos () throws RemoteException;
    public void enviarMensaje(String mensaje)throws RemoteException;
}


