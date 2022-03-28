# Operación Fuego de Quasar
### Introducción
Este proyecto ha sido creado usando Java Spring Boot y 
Maven como herramienta para administrar el proyecto.

Version de Java: 8

La implementación siguió las mejores prácticas de 
ingeniería de software y dividió el alcance de 
cada clase según su propósito con paquetes.

### Estructura del proyecto
* #### /Config
Contiene la clase de configuración que usa IoC e inyección
de dependencias para obtener las propiedades de la 
aplicación del proyecto del archivo application.yml.

* #### /Controller
Contiene la API REST de la aplicación con la clase 
RebelCommunicationController.java Actualmente la 
aplicación tiene los siguientes endpoints:

* #### (POST) https://quasar-fire-operation-app.herokuapp.com/api/topsecret <br>
El request de la petición del endpoint topsecret es un objeto 
json que contiene una lista de satélites y para cada uno 
de ellos los siguientes campos: 
distancia, mensaje y posición.

Ejemplo request:<br>
<code>
{<br>
"satellites":[ <br>
{<br>
"name":"kenobi",<br>
"distance":100.0,<br>
"message":["este","","","mensaje",""]<br>
}, <br>
{<br>
"name":"skywalker",<br>
"distance":115.5,<br>
"message":["","es","","","secreto"]<br>
},<br>
{<br>
"name":"sato",<br>
"distance":142.7,<br>
"message":["este","","un","",""]<br>
}<br>
]<br>
}<br>
</code>

* #### (POST) https://quasar-fire-operation-app.herokuapp.com/api/topsecret_split/{NOMBRE_SATELITE}
La aplicación analiza los mensajes entrantes divididos 
y agrupados por nombre de satélite. 
La aplicación verifica si el nombre del satélite existe 
en la lista de satélites, si no existe
devolverá una InexistentSatelliteException.

Ejemplo request
<code>
<br>
{<br>
"distance": 100.0,<br>
"message": ["este", "", "", "mensaje", ""]<br>
}<br>
</code>

* #### (GET) https://quasar-fire-operation-app.herokuapp.com/api/topsecret_split
Este endpoint analiza los datos recibidos por el endpoint
/topsecret_split/{NOMBRE_SATELITE} y determina si es posible
, el mensaje completo y la posición de la nave. 
Si el mensaje no se puede descifrar, 
devolverá un 404.

* #### (GET) https://quasar-fire-operation-app.herokuapp.com/api/topsecret_split/clear
Dado que la aplicación no tiene una base de datos, 
los datos recibidos por /topsecret_split/{NOMBRE_SATELITE}
se almacenan en memoria y deben borrarse para poder 
descifrar los nuevos mensajes. 
Este endpoint borra el mensaje anterior y prepara la a
plicación para nuevos mensajes entrantes.

* #### /Exception
El paquete de excepciones contiene todas las excepciones 
personalizadas creadas para la aplicación. 
Extienden de la clase Exception.

* #### /Model
El paquete de modelos contiene los modelos de negocio que
usa la aplicación.

* #### /Services
Este paquete contiene los servicios necesarios para la 
lógica de negocio de la aplicación:

- DecipherService.java Contiene toda la lógica de negocio
para descifrar los mensajes recibidos de los satelites, 
usa triangulación para determinar la posición de la nave
y es llamado directamente por la clase controladora. 

Para determinar la posición de la nave mediante 
triangulación, el proyecto utiliza una biblioteca 
externa com.lemmingapex.trilateration importada desde maven
así como también org.apache.commons.commons-math3

### Despliegue
Para desplegar la aplicación he utilizado el plan de 
alojamiento gratuito de Heroku. Después de crear un 
nuevo proyecto llamado quasar-fire-operation-app 
conecté el proyecto a este repositorio de github.

Heroku implementará y actualizará automáticamente la 
aplicación cuando
se haya realizado un nuevo commit 
en el branch [main] del repositorio, 
pero también se puede implementar manualmente con 
el botón "Deploy Branch":

![](C:\Users\salam\Downloads\111.jpg)


