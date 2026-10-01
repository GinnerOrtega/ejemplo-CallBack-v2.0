package co.edu.unicauca.servidor.servicios;

import co.edu.unicauca.servidor.controladores.ControladorServidorChatImpl;
import co.edu.unicauca.servidor.utilidades.UtilidadesConfiguracion;
import co.edu.unicauca.servidor.utilidades.UtilidadesRegistroS;

public class ServidorDeObjetos
{
    public static void main(String args[])
    {
        try
        {
            UtilidadesConfiguracion configuracion = new UtilidadesConfiguracion();
            String direccionIpRMIRegistry = configuracion.getDireccionIpNS();
            int numPuertoRMIRegistry = configuracion.getPuertoNS();

            System.out.println("Configuración leída de: " + configuracion.getOrigen());
            System.out.println("  Dirección IP del NS: " + direccionIpRMIRegistry);
            System.out.println("  Puerto del NS      : " + numPuertoRMIRegistry);

            String hostname = configuracion.getHostnameRMI() != null ? configuracion.getHostnameRMI() : direccionIpRMIRegistry;
            System.setProperty("java.rmi.server.hostname", hostname);

            ControladorServidorChatImpl objRemoto = new ControladorServidorChatImpl();

            UtilidadesRegistroS.arrancarNS(numPuertoRMIRegistry);
            UtilidadesRegistroS.RegistrarObjetoRemoto(objRemoto, direccionIpRMIRegistry, numPuertoRMIRegistry, "ServidorChat");
            System.out.println("Servidor de chat listo y esperando clientes...");
        }
        catch (Exception e)
        {
            System.err.println("No fue posible Arrancar el NS o Registrar el objeto remoto: " + e.getMessage());
        }
    }
}
