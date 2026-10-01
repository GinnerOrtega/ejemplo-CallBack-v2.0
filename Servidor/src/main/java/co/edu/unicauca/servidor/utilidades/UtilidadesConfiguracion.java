package co.edu.unicauca.servidor.utilidades;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class UtilidadesConfiguracion
{
    public static final String NOMBRE_ARCHIVO = "config.properties";

    private final Properties propiedades = new Properties();
    private String origen;

    public UtilidadesConfiguracion() throws IOException
    {
        Path archivoExterno = Paths.get(NOMBRE_ARCHIVO);
        if (Files.isRegularFile(archivoExterno))
        {
            try (InputStreamReader lector = new InputStreamReader(new FileInputStream(archivoExterno.toFile()), StandardCharsets.UTF_8))
            {
                propiedades.load(lector);
                origen = archivoExterno.toAbsolutePath().toString();
                return;
            }
        }
        try (InputStream entrada = UtilidadesConfiguracion.class.getClassLoader().getResourceAsStream(NOMBRE_ARCHIVO))
        {
            if (entrada == null)
            {
                throw new IOException("No se encontró el archivo " + NOMBRE_ARCHIVO);
            }
            propiedades.load(new InputStreamReader(entrada, StandardCharsets.UTF_8));
            origen = "classpath:" + NOMBRE_ARCHIVO;
        }
    }

    public String getDireccionIpNS()
    {
        return obtenerObligatoria("ns.ip");
    }

    public int getPuertoNS()
    {
        String valor = obtenerObligatoria("ns.puerto");
        try
        {
            return Integer.parseInt(valor);
        }
        catch (NumberFormatException e)
        {
            throw new IllegalArgumentException("La propiedad ns.puerto debe ser un número entero: " + valor);
        }
    }

    public String getHostnameRMI()
    {
        String valor = propiedades.getProperty("rmi.hostname");
        return (valor == null || valor.trim().isEmpty()) ? null : valor.trim();
    }

    public String getOrigen()
    {
        return origen;
    }

    private String obtenerObligatoria(String clave)
    {
        String valor = propiedades.getProperty(clave);
        if (valor == null || valor.trim().isEmpty())
        {
            throw new IllegalArgumentException("Falta la propiedad '" + clave + "' en " + NOMBRE_ARCHIVO);
        }
        return valor.trim();
    }
}
