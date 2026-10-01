package co.edu.unicauca.cliente.servicios;

import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import co.edu.unicauca.cliente.controladores.UsuarioCllbckImpl;
import co.edu.unicauca.cliente.utilidades.UtilidadesConfiguracion;
import co.edu.unicauca.cliente.utilidades.UtilidadesConsola;
import co.edu.unicauca.cliente.utilidades.UtilidadesRegistroC;
import co.edu.unicauca.servidor.controladores.ControladorServidorChatInt;

public class ClienteDeObjetos
{
    public static void main(String[] args)
    {
        UsuarioCllbckImpl objNuevoUsuario = null;
        try
        {
            //i) la dirección IP y el puerto del NS se leen del archivo config.properties
            UtilidadesConfiguracion configuracion = new UtilidadesConfiguracion();
            String direccionIpRMIRegistry = configuracion.getDireccionIpNS();
            int numPuertoRMIRegistry = configuracion.getPuertoNS();
            System.out.println("Configuración leída de: " + configuracion.getOrigen());
            System.out.println("  Dirección IP del NS: " + direccionIpRMIRegistry);
            System.out.println("  Puerto del NS      : " + numPuertoRMIRegistry);

            if (configuracion.getHostnameRMI() != null)
            {
                System.setProperty("java.rmi.server.hostname", configuracion.getHostnameRMI());
            }

            ControladorServidorChatInt servidor = (ControladorServidorChatInt)
                    UtilidadesRegistroC.obtenerObjRemoto(numPuertoRMIRegistry, direccionIpRMIRegistry, "ServidorChat");
            if (servidor == null)
            {
                System.out.println("No se pudo obtener la referencia del servidor de chat. ¿Está corriendo el servidor?");
                System.exit(1);
            }

            objNuevoUsuario = new UsuarioCllbckImpl();

            //a) y b) registrar la referencia remota junto con un nickName único
            String nickName;
            while (true)
            {
                System.out.println("Digite su nickName: ");
                nickName = UtilidadesConsola.leerCadena().trim();
                if (servidor.registrarReferenciaUsuario(objNuevoUsuario, nickName))
                {
                    System.out.println("Bienvenido(a) al chat, " + nickName + "!");
                    break;
                }
                System.out.println("El nickName '" + nickName + "' no es válido o ya está en uso. Intente con otro.");
            }

            boolean salir = false;
            while (!salir)
            {
                mostrarMenu();
                int opcion = UtilidadesConsola.leerEntero();

                switch (opcion)
                {
                    case 1: //c) ver nickName de usuarios registrados y activos
                        List<String> usuariosActivos = servidor.obtenerNickNamesActivos();
                        System.out.println("Usuarios registrados y activos:");
                        for (String nick : usuariosActivos)
                        {
                            System.out.println("  - " + nick + (nick.equals(nickName) ? " (usted)" : ""));
                        }
                        break;

                    case 2: //h) consultar la cantidad de usuarios activos
                        System.out.println("Cantidad de usuarios activos: " + servidor.obtenerCantidadUsuariosActivos());
                        break;

                    case 3: //g) enviar mensaje público
                        System.out.println("Digite el mensaje a enviar a todos: ");
                        String mensajePublico = UtilidadesConsola.leerCadena();
                        servidor.enviarMensaje(nickName, mensajePublico);
                        break;

                    case 4: //e) y f) enviar mensaje privado
                        System.out.println("Digite el nickName del usuario destino: ");
                        String nickNameDestino = UtilidadesConsola.leerCadena();
                        System.out.println("Digite el mensaje privado: ");
                        String mensajePrivado = UtilidadesConsola.leerCadena();
                        String respuesta = servidor.enviarMensajePrivado(nickName, nickNameDestino, mensajePrivado);
                        System.out.println("Respuesta del servidor: " + respuesta);
                        break;

                    case 5: //d) salir del chat y eliminar la referencia en el servidor
                        servidor.salirDelChat(nickName);
                        salir = true;
                        System.out.println("Ha salido del chat. Su referencia fue eliminada del servidor.");
                        break;

                    default:
                        System.out.println("Opción inválida.");
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("No se pudo realizar la conexion...");
            System.out.println(e.getMessage());
        }
        finally
        {
            //se deja de exportar el objeto callback para que la JVM del cliente pueda terminar
            if (objNuevoUsuario != null)
            {
                try
                {
                    UnicastRemoteObject.unexportObject(objNuevoUsuario, true);
                }
                catch (Exception ignored)
                {
                }
            }
        }
        System.exit(0);
    }

    private static void mostrarMenu()
    {
        System.out.println("\n----- MENU CHAT -----");
        System.out.println("1. Ver usuarios registrados y activos");
        System.out.println("2. Consultar cantidad de usuarios activos");
        System.out.println("3. Enviar mensaje público");
        System.out.println("4. Enviar mensaje privado");
        System.out.println("5. Salir del chat");
        System.out.println("Ingrese la opción: ");
    }
}
