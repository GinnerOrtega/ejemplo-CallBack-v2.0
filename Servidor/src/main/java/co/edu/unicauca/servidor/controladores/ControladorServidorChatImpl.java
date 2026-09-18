package co.edu.unicauca.servidor.controladores;

import co.edu.unicauca.cliente.controladores.UsuarioCllbckInt;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ControladorServidorChatImpl extends UnicastRemoteObject implements ControladorServidorChatInt {

    private final Map<String, UsuarioCllbckInt> usuarios;//nickName -> referencia remota del cliente

    public ControladorServidorChatImpl() throws RemoteException
    {
        super();//asignamos el puerto 
        usuarios = new ConcurrentHashMap<>();
    }
    
    /*
    @Override
    public synchronized boolean registrarReferenciaUsuario(UsuarioCllbckInt usuario, String nickName) throws RemoteException
    {
       //método que unicamente puede ser accedido por un hilo
    System.out.println("Invocando al método registrar usuario desde el servidor");
        if (nickName == null || nickName.trim().isEmpty() || usuarios.containsKey(nickName))
        {
            return false;
        }
        usuarios.put(nickName, usuario);
        return true;
    }
    */
    /* 
    @Override
    public List<String> consultarUsuariosActivos() throws RemoteException
    {
        System.out.println("Invocando al método consultar usuarios activos desde el servidor");
        return new ArrayList<>(usuarios.keySet());
    }
    

    @Override
    public int consultarCantidadUsuariosActivos() throws RemoteException
    {
        return usuarios.size();
    }

    @Override
    public synchronized void salirDelChat(String nickName) throws RemoteException
    {
        usuarios.remove(nickName);
        System.out.println("El usuario " + nickName + " salió del chat y su referencia fue eliminada");
    }

    @Override
    public String enviarMensajePrivado(String nickNameEmisor, String nickNameDestino, String mensaje) throws RemoteException
    {
        UsuarioCllbckInt destino = usuarios.get(nickNameDestino);
        if (destino == null)
        {
            return "El mensaje no se logró enviar porque el usuario receptor no está conectado";
        }
        try
        {
            destino.notificarMensajePrivado(nickNameEmisor, mensaje);
            return "Mensaje enviado correctamente a " + nickNameDestino;
        }
        catch (RemoteException e)
        {
            //el usuario destino terminó abruptamente, se elimina su referencia
            usuarios.remove(nickNameDestino);
            return "El mensaje no se logró enviar porque el usuario receptor no está conectado";
        }
    }

    @Override
    public void enviarMensajePublico(String nickNameEmisor, String mensaje) throws RemoteException
    {
        notificarUsuarios(nickNameEmisor + " dice: " + mensaje);
    }
    
    private void notificarUsuarios(String mensaje) throws RemoteException 
    {
        System.out.println("Invocando al método notificar usuarios desde el servidor");
        List<String> desconectados = new ArrayList<>();
        for (Map.Entry<String, UsuarioCllbckInt> entrada : usuarios.entrySet())
        {
            try
            {
                entrada.getValue().notificar(mensaje, usuarios.size());//el servidor hace el callback
            }
            catch (RemoteException e)
            {
                //el usuario terminó abruptamente, se marca para eliminar su referencia
                desconectados.add(entrada.getKey());
            }
        }
        for (String nickName : desconectados)
        {
            usuarios.remove(nickName);
            System.out.println("Se eliminó la referencia del usuario " + nickName + " por desconexión abrupta");
        }
        
    }
        */
}