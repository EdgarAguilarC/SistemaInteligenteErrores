
package sistemainteligenteerrores;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Locale;

import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

public class ClasificadorIA {

    private NaiveBayes modelo;
    private Instances estructura;
    private ArrayList<String> vocabulario;

    private final String[] categorias = {
        "Conectividad",
        "Instalacion",
        "Inicio o ejecucion",
        "Rendimiento"
    };

    private final String[][] ejemplos = {
        {"No tengo conexion a internet", "Conectividad"},
        {"El internet no funciona", "Conectividad"},
        {"Problemas con la red wifi", "Conectividad"},
        {"No puedo conectarme al servidor", "Conectividad"},
        {"La conexion se pierde constantemente", "Conectividad"},
        {"Error de red al abrir la aplicacion", "Conectividad"},
        {"No hay acceso a internet", "Conectividad"},
        {"El router no conecta", "Conectividad"},
        {"La red esta desconectada", "Conectividad"},
        {"Falla la conexion del sistema", "Conectividad"},
        {"El servidor no responde por la red", "Conectividad"},
        {"No puedo acceder al servicio en linea", "Conectividad"},

        {"No puedo instalar el programa", "Instalacion"},
        {"Error durante la instalacion", "Instalacion"},
        {"El instalador no funciona", "Instalacion"},
        {"Faltan archivos de instalacion", "Instalacion"},
        {"No tengo permisos para instalar", "Instalacion"},
        {"La instalacion se interrumpe", "Instalacion"},
        {"No se puede completar la instalacion", "Instalacion"},
        {"El programa no termina de instalarse", "Instalacion"},
        {"El asistente de instalacion falla", "Instalacion"},
        {"No se instala correctamente la aplicacion", "Instalacion"},
        {"Error al configurar el instalador", "Instalacion"},
        {"La instalacion muestra un mensaje de error", "Instalacion"},

        {"El programa no abre", "Inicio o ejecucion"},
        {"La aplicacion falla al iniciar", "Inicio o ejecucion"},
        {"El software se cierra al ejecutarse", "Inicio o ejecucion"},
        {"Error al iniciar el sistema", "Inicio o ejecucion"},
        {"La aplicacion no arranca", "Inicio o ejecucion"},
        {"El programa muestra un error al iniciar", "Inicio o ejecucion"},
        {"No puedo ejecutar el programa", "Inicio o ejecucion"},
        {"El sistema se cierra al abrirlo", "Inicio o ejecucion"},
        {"La aplicacion se cierra inesperadamente", "Inicio o ejecucion"},
        {"El programa no inicia correctamente", "Inicio o ejecucion"},
        {"Falla al abrir la aplicacion", "Inicio o ejecucion"},
        {"El sistema presenta errores al ejecutarse", "Inicio o ejecucion"},

        {"El programa esta muy lento", "Rendimiento"},
        {"La aplicacion tarda mucho en responder", "Rendimiento"},
        {"El sistema se congela constantemente", "Rendimiento"},
        {"El software consume mucha memoria", "Rendimiento"},
        {"La computadora trabaja lentamente", "Rendimiento"},
        {"La aplicacion se queda bloqueada", "Rendimiento"},
        {"El programa tarda demasiado en cargar", "Rendimiento"},
        {"El sistema presenta lentitud", "Rendimiento"},
        {"La aplicacion consume demasiados recursos", "Rendimiento"},
        {"El programa responde lentamente", "Rendimiento"},
        {"El sistema se congela al trabajar", "Rendimiento"},
        {"La memoria del equipo esta saturada", "Rendimiento"}
    };

    public ClasificadorIA() throws Exception {
        entrenarModelo();
    }

    private String normalizar(String texto) {
        String resultado = Normalizer.normalize(
            texto.toLowerCase(Locale.ROOT),
            Normalizer.Form.NFD
        );

        return resultado.replaceAll("\\p{M}", "");
    }

    private Set<String> obtenerPalabras(String texto) {
        String normalizado = normalizar(texto);

        String[] palabras = normalizado.split("[^a-z0-9]+");

        Set<String> resultado = new HashSet<>();

        for (String palabra : palabras) {
            if (palabra.length() > 1) {
                resultado.add(palabra);
            }
        }

        return resultado;
    }

    private void entrenarModelo() throws Exception {

        Set<String> palabras = new TreeSet<>();

        for (String[] ejemplo : ejemplos) {
            palabras.addAll(obtenerPalabras(ejemplo[0]));
        }

        vocabulario = new ArrayList<>(palabras);

        ArrayList<Attribute> atributos = new ArrayList<>();

        ArrayList<String> valoresBinarios = new ArrayList<>();
        valoresBinarios.add("no");
        valoresBinarios.add("si");

        for (String palabra : vocabulario) {
            atributos.add(
                new Attribute("palabra_" + palabra, valoresBinarios)
            );
        }

        ArrayList<String> valoresClase =
            new ArrayList<>(Arrays.asList(categorias));

        atributos.add(new Attribute("clase", valoresClase));

        estructura = new Instances(
            "ErroresSoftware",
            atributos,
            ejemplos.length
        );

        estructura.setClassIndex(estructura.numAttributes() - 1);

        for (String[] ejemplo : ejemplos) {

            Set<String> palabrasEjemplo =
                obtenerPalabras(ejemplo[0]);

            double[] valores = new double[estructura.numAttributes()];

            for (int i = 0; i < vocabulario.size(); i++) {
                valores[i] = palabrasEjemplo.contains(vocabulario.get(i))
                    ? 1.0 : 0.0;
            }

            valores[estructura.classIndex()] =
                valoresClase.indexOf(ejemplo[1]);

            estructura.add(new DenseInstance(1.0, valores));
        }

        modelo = new NaiveBayes();
        modelo.buildClassifier(estructura);
    }

    public Resultado analizar(String descripcion) throws Exception {

        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Describe el error antes de analizar."
            );
        }

        Set<String> palabras = obtenerPalabras(descripcion);

        int palabrasReconocidas = 0;

        for (String palabra : palabras) {
            if (vocabulario.contains(palabra)) {
                palabrasReconocidas++;
            }
        }

        if (palabrasReconocidas == 0) {
            return new Resultado(
                "No identificado",
                "POR DETERMINAR",
                "No disponible",
                "La descripcion no contiene palabras relacionadas "
                + "con las categorias conocidas. Proporciona "
                + "mas detalles sobre el problema."
            );
        }

        double[] valores = new double[estructura.numAttributes()];

        for (int i = 0; i < vocabulario.size(); i++) {
            valores[i] = palabras.contains(vocabulario.get(i))
                ? 1.0 : 0.0;
        }

        DenseInstance nuevoError = new DenseInstance(1.0, valores);
        nuevoError.setDataset(estructura);
        nuevoError.setMissing(estructura.classIndex());

        double[] probabilidades =
            modelo.distributionForInstance(nuevoError);

        int mejorIndice = 0;

        for (int i = 1; i < probabilidades.length; i++) {
            if (probabilidades[i] > probabilidades[mejorIndice]) {
                mejorIndice = i;
            }
        }

        String categoria = categorias[mejorIndice];

        double confianza = probabilidades[mejorIndice] * 100;

        String prioridad;
        String solucion;

        switch (categoria) {

            case "Conectividad":
                prioridad = "ALTA";
                solucion = "Verificar la conexion de red, "
                    + "el router y la configuracion de internet.";
                break;

            case "Instalacion":
                prioridad = "MEDIA";
                solucion = "Verificar los permisos, los requisitos "
                    + "del sistema y los archivos de instalacion.";
                break;

            case "Inicio o ejecucion":
                prioridad = "ALTA";
                solucion = "Revisar los archivos del programa, "
                    + "los permisos de ejecucion y los requisitos "
                    + "del sistema.";
                break;

            default:
                prioridad = "MEDIA";
                solucion = "Revisar el consumo de memoria, "
                    + "los procesos activos y los recursos "
                    + "del equipo.";
                break;
        }

        return new Resultado(
            categoria,
            prioridad,
            String.format(Locale.US, "%.2f%%", confianza),
            solucion
        );
    }

    public static class Resultado {

        private final String categoria;
        private final String prioridad;
        private final String confianza;
        private final String solucion;

        public Resultado(
            String categoria,
            String prioridad,
            String confianza,
            String solucion
        ) {
            this.categoria = categoria;
            this.prioridad = prioridad;
            this.confianza = confianza;
            this.solucion = solucion;
        }

        public String getCategoria() {
            return categoria;
        }

        public String getPrioridad() {
            return prioridad;
        }

        public String getConfianza() {
            return confianza;
        }

        public String getSolucion() {
            return solucion;
        }
    }
}
