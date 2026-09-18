package co.edu.unicauca.cliente.servicios;

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

        }
        catch(Exception e)
        {
                System.out.println("No se pudo realizar la conexion...");
                System.out.println(e.getMessage());
        }

    }
	
}
