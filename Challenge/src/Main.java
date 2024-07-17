import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Main {
    private static final String CLAVE_API = "ef97cd9cce1a482a474ab555f";
    private static final String URL_BASE = "https://v6.exchangerate-api.com/v6/" + CLAVE_API + "/latest/";

    public static void main(String[] args) {
        Scanner escaner = new Scanner(System.in);
        boolean ejecutando = true;

        while (ejecutando) {
            System.out.println("Menú de Conversión de Monedas:");
            System.out.println("1. Convertir de USD a MXN");
            System.out.println("2. Convertir de MXN a USD");
            System.out.println("3. Convertir de USD a COP");
            System.out.println("4. Convertir de COP a USD");
            System.out.println("5. Convertir de USD a GBP");
            System.out.println("6. Convertir de GBP a USD");
            System.out.println("7. Terminar la ejecución");
            System.out.print("Selecciona una opción: ");

            int opcion = escaner.nextInt();

            if (opcion == 7) {
                ejecutando = false;
                System.out.println("Terminando la ejecución...");
                break;
            }

            System.out.print("Introduce la cantidad a convertir: ");
            double cantidad = escaner.nextDouble();

            String monedaOrigen = "";
            String monedaDestino = "";

            switch (opcion) {
                case 1:
                    monedaOrigen = "USD";
                    monedaDestino = "MXN";
                    break;
                case 2:
                    monedaOrigen = "MXN";
                    monedaDestino = "USD";
                    break;
                case 3:
                    monedaOrigen = "USD";
                    monedaDestino = "COP";
                    break;
                case 4:
                    monedaOrigen = "COP";
                    monedaDestino = "USD";
                    break;
                case 5:
                    monedaOrigen = "USD";
                    monedaDestino = "GBP";
                    break;
                case 6:
                    monedaOrigen = "GBP";
                    monedaDestino = "USD";
                    break;
                default:
                    System.out.println("Opción no válida. Inténtalo de nuevo.");
                    continue;
            }

            try {
                double cantidadConvertida = convertirMoneda(monedaOrigen, monedaDestino, cantidad);
                System.out.printf("%.2f %s son %.2f %s%n", cantidad, monedaOrigen, cantidadConvertida, monedaDestino);
            } catch (Exception e) {
                System.out.println("Error en la conversión de moneda: " + e.getMessage());
            }
        }

        escaner.close();
    }

    private static double convertirMoneda(String monedaOrigen, String monedaDestino, double cantidad) throws Exception {
        String urlSolicitud = URL_BASE + monedaOrigen;
        URL url = new URL(urlSolicitud);
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setRequestMethod("GET");

        if (conexion.getResponseCode() == 200) {
            InputStreamReader lector = new InputStreamReader(conexion.getInputStream());
            JsonObject jsonObject = new Gson().fromJson(lector, JsonObject.class);
            lector.close();

            double tipoCambio = jsonObject.getAsJsonObject("conversion_rates").get(monedaDestino).getAsDouble();
            return cantidad * tipoCambio;
        } else {
            throw new Exception("Error al obtener los datos de la API: " + conexion.getResponseMessage());
        }
    }
}