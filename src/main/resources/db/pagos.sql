-- Crear tabla de usuarios
CREATE TABLE usuarios (
      id INT AUTO_INCREMENT PRIMARY KEY,
      username VARCHAR(50) NOT NULL UNIQUE,
      password VARCHAR(255) NOT NULL,
      email VARCHAR(100) NOT NULL,
      rol VARCHAR(20) DEFAULT 'USER',
      activo BOOLEAN DEFAULT TRUE,
      salt VARCHAR(255),
      fecha_creacion TIMESTAMP DEFAULT NOW(),
      ultimo_acceso TIMESTAMP NULL
);

INSERT INTO usuarios (username, password, email, rol)
VALUES ('admin', SHA2('admin123', 256), 'admin@sistema.com', 'ADMIN');


CREATE TABLE estudiante {
    id INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(8) NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    apellido_Paterno VARCHAR(50) NOT NULL,
    apellido_Paterno VARCHAR(50) NOT NULL,
    direccion VARCHAR(100) null,
    email VARCHAR(100) NULL,
    telefono VARCHAR(12) NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT NOW(),
    ultimo_acceso TIMESTAMP NULL
}