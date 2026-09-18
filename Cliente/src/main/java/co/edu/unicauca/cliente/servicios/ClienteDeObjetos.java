package co.edu.unicauca.cliente.servicios;

import java.util.List;

import co.edu.unicauca.cliente.controladores.UsuarioCllbckImpl;
import co.edu.unicauca.cliente.utilidades.UtilidadesConsola;
import co.edu.unicauca.cliente.utilidades.UtilidadesRegistroC;
import co.edu.unicauca.servidor.controladores.ControladorServidorChatInt;

public class ClienteDeObjetos
{
    public static void main(String[] args)
    {

        try
        {
            ControladorServidorChatInt servidor;
            int numPuertoRMIRegistry = 0;
            String direccionIpRMIRegistry = "";
            System.out.println("Cual es el la dirección ip donde se encuentra  el rmiregistry ");
            direccionIpRMIRegistry = UtilidadesConsola.leerCadena();
            System.out.println("Cual es el número de puerto por el cual escucha el rmiregistry ");
            numPuertoRMIRegistry = UtilidadesConsola.leerEntero(); 
            System.out.println("Digite el mensaje a enviar al servidor: ");
            String mensaje=UtilidadesConsola.leerCadena();

            servidor = (ControladorServidorChatInt) UtilidadesRegistroC.obtenerObjRemoto(numPuertoRMIRegistry,direccionIpRMIRegistry, "ServidorChat");

            UsuarioCllbckImpl objNuevoUsuario= new UsuarioCllbckImpl();
            servidor.registrarReferenciaUsuario(objNuevoUsuario);
            servidor.enviarMensaje(mensaje);

            boolean salir = false;
            while (!salir)
            {
                mostrarMenu();
                int opcion = UtilidadesConsola.leerEntero();

                switch (opcion)
                {
                    case 1:
                        List<String> usuariosActivos = servidor.consultarUsuariosActivos();
                        System.out.println("Usuarios registrados y activos: " + usuariosActivos);
                        //System.out.println("Cantidad de usuarios activos: " + servidor.consultarCantidadUsuariosActivos());
                        break;

                    case 2:
                        System.out.println("Digite el mensaje a enviar a todos: ");
                        String mensajePublico = UtilidadesConsola.leerCadena();
                        //servidor.enviarMensajePublico(nickName, mensajePublico);
                        break;

                    case 3:
                        System.out.println("Digite el nickName del usuario destino: ");
                        String nickNameDestino = UtilidadesConsola.leerCadena();
                        System.out.println("Digite el mensaje privado: ");
                        String mensajePrivado = UtilidadesConsola.leerCadena();
                        //String respuesta = servidor.enviarMensajePrivado(nickName, nickNameDestino, mensajePrivado);
                        //System.out.println(respuesta);
                        break;

                    case 4:
                        //servidor.salirDelChat(nickName);
                        salir = true;
                        System.out.println("Ha salido del chat.");
                        break;

                    default:
                        System.out.println("Opción inválida.");
                }
            }
        }
        catch(Exception e)
        {
                System.out.println("No se pudo realizar la conexion...");
                System.out.println(e.getMessage());
        }

    }

    private static void mostrarMenu()
    {
        System.out.println("\n----- MENU CHAT -----");
        System.out.println("1. Ver usuarios registrados y activos");
        System.out.println("2. Enviar mensaje público");
        System.out.println("3. Enviar mensaje privado");
        System.out.println("4. Salir del chat");
    }
	
}
