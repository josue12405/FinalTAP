DROP DATABASE IF EXISTS hotel_db;
CREATE DATABASE hotel_db;
USE hotel_db;

CREATE TABLE usuarios (
                          id_usuario INT PRIMARY KEY AUTO_INCREMENT,
                          username VARCHAR(50) UNIQUE NOT NULL,
                          password_sha1 VARCHAR(40) NOT NULL,
                          nombre_completo VARCHAR(100) NOT NULL,
                          rol ENUM('Administrador', 'Recepcionista') NOT NULL
);

CREATE TABLE habitaciones (
                              id_habitacion INT PRIMARY KEY AUTO_INCREMENT,
                              numero VARCHAR(10) UNIQUE NOT NULL,
                              tipo ENUM('Sencilla', 'Doble', 'Suite') NOT NULL,
                              precio_noche DECIMAL(10,2) NOT NULL,
                              estado ENUM('Disponible', 'Ocupada', 'En Limpieza') DEFAULT 'Disponible'
);

CREATE TABLE huespedes (
                           id_huesped INT PRIMARY KEY AUTO_INCREMENT,
                           nombre VARCHAR(100) NOT NULL,
                           telefono VARCHAR(20) NOT NULL,
                           correo VARCHAR(100)
);

CREATE TABLE reservaciones (
                               id_reservacion INT PRIMARY KEY AUTO_INCREMENT,
                               id_huesped INT NOT NULL,
                               id_habitacion INT NOT NULL,
                               fecha_entrada DATE NOT NULL,
                               fecha_salida DATE NOT NULL,
                               total DECIMAL(10,2) NOT NULL,
                               FOREIGN KEY (id_huesped) REFERENCES huespedes(id_huesped),
                               FOREIGN KEY (id_habitacion) REFERENCES habitaciones(id_habitacion)
);

INSERT INTO usuarios (username, password_sha1, nombre_completo, rol) VALUES
                                                                         ('admin', 'f865b53623b121fd34ee5426c792e5c33af8c227', 'Josue Abraham Barrera', 'Administrador'),
                                                                         ('recep', '60b10085d0303b149746f21a1e8f92e8875b44b1', 'Recepcionista Turno Matutino', 'Recepcionista');

INSERT INTO habitaciones (numero, tipo, precio_noche, estado) VALUES
                                                                  ('101', 'Sencilla', 850.00, 'Disponible'),
                                                                  ('102', 'Sencilla', 850.00, 'Ocupada'),
                                                                  ('201', 'Doble', 1350.00, 'Disponible'),
                                                                  ('202', 'Doble', 1350.00, 'En Limpieza'),
                                                                  ('301', 'Suite', 2500.00, 'Disponible');