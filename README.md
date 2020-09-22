# VAID

_Vaid es una plataforma para ayudar a los empleados del hotel a ofrecer un mejor servicio, gracias a sus funciones de control sobre las luces, cerraduras y aire acondicionado a distancia._

## Prototipos

La plataforma está conformada por 3 prototipos:

* **Emulador** - Programa provicional cuya función es emular el comportamient básico de un dispositivo arduino.
* **ServerSocket** - Servidor bidireccional que por medio de sockets recibe las peticiones del cliente movil (VAID), identifica el emulador solicitado y reevia la petición a este, para después recibir una respuesta y devolverla al cliente quien realizo la petición en primer lugar.
* **VAID** - Aplicación android cuya función es mostrar el estado de cada habitación (luces, cerradura, aire acondicionado) y enviar peticiones para modificar un estado específico, para saber los estados de cada habitación hace uso de hilos que solicitan el estado cada 10 segundos, más 5 segundos extra por cada habitación registrada.