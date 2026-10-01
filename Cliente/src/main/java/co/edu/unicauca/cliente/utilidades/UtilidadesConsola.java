package co.edu.unicauca.cliente.utilidades;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UtilidadesConsola
{
    //un único lector para toda la aplicación (evita perder texto del buffer de System.in)
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static int leerEntero()
    {
        while (true)
        {
            try
            {
                String linea = br.readLine();
                if (linea == null)
                {
                    throw new IllegalStateException("Se cerró la entrada estándar");
                }
                return Integer.parseInt(linea.trim());
            }
            catch (NumberFormatException | IOException e)
            {
                System.out.println("Error, digite un número. Intente nuevamente...");
            }
        }
    }

    public static String leerCadena()
    {
        while (true)
        {
            try
            {
                String linea = br.readLine();
                if (linea == null)
                {
                    throw new IllegalStateException("Se cerró la entrada estándar");
                }
                return linea;
            }
            catch (IOException e)
            {
                System.out.println("Error intente nuevamente...");
            }
        }
    }
}
