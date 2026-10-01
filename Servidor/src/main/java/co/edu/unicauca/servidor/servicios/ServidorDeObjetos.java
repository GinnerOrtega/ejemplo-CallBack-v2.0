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
            //i) la dirección IP y el puerto del NS se leen del archivo config.properties
            UtilidadesConfiguracion configuracion = new UtilidadesConfiguracion();
            String direccionIpRMIRegistry = configuracion.getDireccionIpNS();
            int numPuertoRMIRegistry = configuracion.getPuertoNS();

            System.out.println("Configuración leída de: " + configuracion.getOrigen());
            System.out.println("  Dirección IP del NS: " + direccionIpRMIRegistry);
            System.out.println("  Puerto del NS      : " + numPuertoRMIRegistry);

            //IP que se publica en la referencia remota del servidor (útil al trabajar en red)
            String hostname = configuracion.getHostnameRMI() != null ? configuracion.getHostnameRMI() : direccionIpRMIRegistry;
            System.setProperty("java.rmi.server.hostname", hostname);

            ControladorServidorChatImpl objRemoto = new ControladorServidorChatImpl();//se le asigna el puerto de escucha del objeto remoto

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
