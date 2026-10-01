package co.edu.unicauca.servidor.controladores;

import co.edu.unicauca.cliente.controladores.UsuarioCllbckInt;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ControladorServidorChatImpl extends UnicastRemoteObject implements ControladorServidorChatInt
{
    public static final String MSG_RECEPTOR_NO_CONECTADO =
            "El mensaje no se logró enviar porque el usuario receptor no está conectado";

    private final Map<String, UsuarioCllbckInt> usuarios;

    public ControladorServidorChatImpl() throws RemoteException
    {
        super();
        usuarios = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized boolean registrarReferenciaUsuario(UsuarioCllbckInt usuario, String nickName) throws RemoteException
    {
        System.out.println("[SERVIDOR] Invocando al método registrar usuario. nickName solicitado: " + nickName);

        if (usuario == null || nickName == null || nickName.trim().isEmpty())
        {
            System.out.println("[SERVIDOR] Registro rechazado: nickName vacío o referencia nula");
            return false;
        }
        nickName = nickName.trim();

        String nickExistente = buscarNickName(nickName);
        if (nickExistente != null)
        {
            //si el dueño de ese nickName terminó abruptamente, se libera el nickName
            if (verificarConexion(nickExistente, usuarios.get(nickExistente)))
            {
                System.out.println("[SERVIDOR] Registro rechazado: el nickName '" + nickName + "' ya está en uso");
                return false;
            }
        }

        if (usuarios.containsValue(usuario))
        {
            System.out.println("[SERVIDOR] Registro rechazado: la referencia remota ya estaba registrada");
            return false;
        }

        usuarios.put(nickName, usuario);
        System.out.println("[SERVIDOR] Usuario '" + nickName + "' registrado. Usuarios activos: " + usuarios.size());
        notificarUsuarios("*** " + nickName + " se unió al chat ***");
        return true;
    }

    @Override
    public List<String> obtenerNickNamesActivos() throws RemoteException
    {
        System.out.println("[SERVIDOR] Invocando al método obtener nickNames activos");
        depurarUsuariosDesconectados();
        List<String> nickNames = new ArrayList<>(usuarios.keySet());
        Collections.sort(nickNames, String.CASE_INSENSITIVE_ORDER);
        return nickNames;
    }

    @Override
    public synchronized boolean salirDelChat(String nickName) throws RemoteException
    {
        System.out.println("[SERVIDOR] Invocando al método salir del chat para: " + nickName);
        String nick = (nickName == null) ? null : buscarNickName(nickName.trim());
        if (nick == null)
        {
            return false;
        }
        usuarios.remove(nick);
        System.out.println("[SERVIDOR] El usuario '" + nick + "' salió del chat y su referencia fue eliminada. Usuarios activos: " + usuarios.size());
        notificarUsuarios("*** " + nick + " salió del chat ***");
        return true;
    }

    @Override
    public String enviarMensajePrivado(String nickNameOrigen, String nickNameDestino, String mensaje) throws RemoteException
    {
        System.out.println("[SERVIDOR] Mensaje privado de '" + nickNameOrigen + "' para '" + nickNameDestino + "'");

        String origen = (nickNameOrigen == null) ? null : buscarNickName(nickNameOrigen.trim());
        if (origen == null)
        {
            return "Usted no está registrado en el chat";
        }
        String destino = (nickNameDestino == null) ? null : buscarNickName(nickNameDestino.trim());
        if (destino == null)
        {
            return "El usuario '" + nickNameDestino + "' no está registrado en el chat";
        }
        if (destino.equals(origen))
        {
            return "No puede enviarse un mensaje privado a usted mismo";
        }

        UsuarioCllbckInt refDestino = usuarios.get(destino);

        if (!verificarConexion(destino, refDestino))
        {
            return MSG_RECEPTOR_NO_CONECTADO;
        }

        try
        {
            refDestino.notificarMensajePrivado(origen, mensaje);
            return "Mensaje privado entregado a " + destino;
        }
        catch (RemoteException e)
        {
            //se desconectó justo entre la comprobación y el envío
            eliminarReferencia(destino);
            return MSG_RECEPTOR_NO_CONECTADO;
        }
    }

    @Override
    public void enviarMensaje(String nickNameOrigen, String mensaje) throws RemoteException
    {
        System.out.println("[SERVIDOR] Mensaje público de '" + nickNameOrigen + "'");
        notificarUsuarios(nickNameOrigen + " dice: " + mensaje);
    }

    @Override
    public int obtenerCantidadUsuariosActivos() throws RemoteException
    {
        System.out.println("[SERVIDOR] Invocando al método obtener cantidad de usuarios activos");
        depurarUsuariosDesconectados();
        return usuarios.size();
    }

    private void notificarUsuarios(String mensaje)
    {
        System.out.println("[SERVIDOR] Invocando al método notificar usuarios");
        depurarUsuariosDesconectados();
        int cantidad = usuarios.size();
        for (Map.Entry<String, UsuarioCllbckInt> entrada : usuarios.entrySet())
        {
            try
            {
                entrada.getValue().notificar(mensaje, cantidad);
            }
            catch (RemoteException e)
            {
                eliminarReferencia(entrada.getKey());
            }
        }
    }

    private void depurarUsuariosDesconectados()
    {
        for (Map.Entry<String, UsuarioCllbckInt> entrada : usuarios.entrySet())
        {
            verificarConexion(entrada.getKey(), entrada.getValue());
        }
    }

    private boolean verificarConexion(String nickName, UsuarioCllbckInt referencia)
    {
        if (referencia == null)
        {
            return false;
        }
        try
        {
            return referencia.estaConectado();
        }
        catch (RemoteException e)
        {
            eliminarReferencia(nickName);
            return false;
        }
    }

    private void eliminarReferencia(String nickName)
    {
        if (usuarios.remove(nickName) != null)
        {
            System.out.println("[SERVIDOR] El usuario '" + nickName
                    + "' terminó abruptamente. Se eliminó su referencia remota. Usuarios activos: " + usuarios.size());
        }
    }

    private String buscarNickName(String nickName)
    {
        for (String nick : usuarios.keySet())
        {
            if (nick.equalsIgnoreCase(nickName))
            {
                return nick;
            }
        }
        return null;
    }
}
