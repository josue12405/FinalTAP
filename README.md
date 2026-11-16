# Sistema de Reservaciones de Hotel (ProyectoFinalTAP)

Aplicación de escritorio desarrollada en **JavaFX** bajo la arquitectura **MVC** con persistencia en **MySQL**, diseñada para automatizar el control de habitaciones, huéspedes, tarifas y reservaciones de un hotel[cite: 22].

## Autor
* **Josué Abraham Barrera Jiménez** — Instituto Tecnológico de Celaya (Tópicos Avanzados de Programación)[cite: 21]

## Características Principales
* **Autenticación Segura:** Inicio de sesión con validación de credenciales contra base de datos y contraseñas encriptadas mediante el algoritmo **SHA-1**[cite: 22, 23, 25].
* **Gestión Integral (CRUD):** Administración de huéspedes, habitaciones y reservaciones con validación de empalme de fechas (`LocalDate`)[cite: 22, 23].
* **Control de Roles:** Permisos diferenciados para **Administrador** y **Recepcionista**[cite: 23].
* **Patrones de Diseño Implementados:** Singleton, DAO, Strategy y Observer[cite: 22, 26].
* **Programación Genérica:** Implementación de repositorio base genérico (`GenericDAO<T>`)[cite: 26].

## Requisitos y Ejecución
1. Tener instalado **JDK 21 o superior** y **MySQL Server**.
2. Ejecutar el script `hotel_db.sql` incluido en la raíz del repositorio para crear la base de datos y los datos iniciales.
3. Configurar las credenciales de conexión en `ConexionMySQL.java`.
4. Ejecutar la clase principal `Launcher.java` desde IntelliJ IDEA o mediante Maven (`mvn javafx:run`).