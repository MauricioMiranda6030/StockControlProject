
# Stock Control Project

Este proyecto se hizo con fines de uso reales y remunerado, a su vez sirve como ejemplo de mis conocimientos en Java y Backend con Spring. También sirvió como desafío a aprender tecnologías nuevas como JavaFX, una BD embebida, SQLite y la creación de una Aplicación de Escritorio con Spring.


## Sobre la Aplicación

La aplicación nos permite guardar editar y eliminar productos con sus respectivos controles y registrar Ventas utilizando los productos almacenados, actualizando el Stock con cada nueva venta e impidiendo vender productos sin stock o una cantidad mayor al disponible. También nos permite hacer un reporte en PDF del Stock.
 
**Tiempo de desarrollo aproximado**

1-2 Meses


## Tecnologías Utilizadas
- JDK 17
- JavaFX 21
- Spring Data JPA
- Spring boot
- SQLite
- ControlFX
- Lombok
- MapStruct
- CSS

## Patrones de Diseño
- Service Layer
- Inyección de Dependencia
- Singleton
- DAO y DTO

## Modelado BD
![DB Model](https://github.com/MauricioMiranda6030/StockControlProject/blob/main/Stock%20Control%20UML.jpg?raw=true "DB Model")

## Como probar la Aplicación en Intellij IDEA

Una vez clonado el proyecto deberemos hacer las siguientes configuraciones:

1. File > Project Structure > Libraries > Clic en + > en la carpeta del proyecto/javafx > Seleccionar la carpeta "lib" > Ok > Apply.
2. Run > Edit Configurations > Modify Options > Seleccionar VM Options > En la nueva label agregar las siguientes opciones:

		--module-path
		"javafx-sdk-21.0.6/lib"
		--add-modules
		javafx.controls,javafx.fxml 
3. Posible solución a errores con MapStruct y Lombok con las últimas versiones de Intellij IDEA (Suele desconfigurarse con cambios en el POM):
Ir a Settings > Build, Execution, Deployment > Compiler > Annotation Processors > Annotations profile for StockControlProject > Marcamos la casilla de "Enable annotation processing" y seleccionar "Obtain processors from project classpath"

4. Tener instalado JDK 17 como mínimo y ejecutar la aplicación.

## Créditos a Imágenes Gratis Utilizadas

* arrow.png made by chehuna from flaticon.com/free-icons/back-arrow
* check.png made by hqrloveq from flaticon.com/free-icons/foursquare-check-in
* folder.png made by Freepik flaticon.com/free-icons/folder
* form logo.png made by Flat icons from flaticon.com/free-icons/consent
* logo.png made by Freepik from flaticon.com/free-icons/product
* reset.png made by Maxim Basinski Premium from flaticon.com/free-icons/refresh
* search.png made by freepik from cdn-icons-png.freepik.com/512/5358/5358562.png

## Mejoras/Refactorizaciones

* Consulta de ventas SQL más ligera, actualmente utiliza EAGER, lo cual trae datos de más.
* Opciones de edición en ventas.
* Responsive, actualmente las ventanas tienen un tamaño fijo.
* Optimizar el código con respuestas más rápidas.
* Customizar más los mensajes para estar en sintonía con el estilo de la app.
