# Sistema Inteligente de Detección de Errores

## Descripción
Sistema de escritorio desarrollado en Java que utiliza inteligencia artificial para clasificar descripciones de errores de software mediante el algoritmo Naive Bayes, utilizando la biblioteca Weka 3.8.7.

## Objetivo
Desarrollar una aplicación capaz de analizar descripciones de problemas de software, identificar su categoría y proporcionar una prioridad y una solución sugerida.

## Tecnologías utilizadas
- Java 25.
- NetBeans IDE 31.
- Weka 3.8.7.
- Swing para la interfaz gráfica.
- Algoritmo de aprendizaje automático Naive Bayes.

## Categorías de clasificación
El sistema contempla cuatro categorías principales:

- Conectividad.
- Instalación.
- Inicio o ejecución.
- Rendimiento.

Cuando la descripción no contiene palabras reconocidas por el modelo, el sistema devuelve la categoría **No identificado**.

## Funcionamiento
1. El usuario introduce una descripción del problema.
2. El sistema procesa el texto.
3. El modelo Naive Bayes calcula las probabilidades de clasificación.
4. La aplicación muestra la categoría identificada, prioridad, confianza estimada y solución sugerida.

## Requisitos
- Tener instalado Java JDK 25.
- Tener NetBeans IDE 31 o compatible.
- Descargar Weka 3.8.7.
- Agregar el archivo `weka.jar` a las bibliotecas del proyecto.

## Instrucciones de ejecución
1. Descargar o clonar este repositorio.
2. Abrir NetBeans.
3. Seleccionar **Archivo → Abrir proyecto**.
4. Seleccionar la carpeta `SistemaInteligenteErrores`.
5. Verificar que la biblioteca `weka.jar` esté agregada al proyecto.
6. En las opciones de la máquina virtual (VM Options), configurar:

`--add-opens=java.base/java.lang=ALL-UNNAMED`

7. Ejecutar el proyecto.

## Estructura del proyecto
- `src/`: contiene el código fuente Java.
- `nbproject/`: contiene la configuración del proyecto NetBeans.
- `build.xml`: archivo de construcción del proyecto.
- `manifest.mf`: archivo de manifiesto.

## Pruebas realizadas
Se realizaron cinco pruebas funcionales:

1. Clasificación de un problema de conectividad.
2. Clasificación de un problema de instalación.
3. Clasificación de un problema de inicio o ejecución.
4. Clasificación de un problema de rendimiento.
5. Identificación de una descripción ajena a las categorías del modelo.

## Documentación
El informe completo del proyecto se encuentra en el archivo PDF incluido en este repositorio.

## Autor
Edgar Aguilar C.
