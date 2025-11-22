-- Crear base de datos
CREATE DATABASE IF NOT EXISTS sistema_pagos;
USE sistema_pagos;

-- Tabla usuarios
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

-- Tabla estudiante
CREATE TABLE estudiante (
        id INT AUTO_INCREMENT PRIMARY KEY,
        dni VARCHAR(8) NOT NULL UNIQUE,
        tipo_persona VARCHAR(20) NOT NULL,
        nombre VARCHAR(50) NOT NULL,
        apellido_paterno VARCHAR(50) NOT NULL,
        apellido_materno VARCHAR(50) NOT NULL,
        direccion VARCHAR(100) NULL,
        email VARCHAR(100) NULL,
        telefono VARCHAR(12) NULL,
        activo BOOLEAN DEFAULT TRUE,
        fecha_creacion TIMESTAMP DEFAULT NOW(),
        fecha_modificacion TIMESTAMP NULL
);

-- Tabla concepto_pago
CREATE TABLE concepto_pago (
       id_concepto INT AUTO_INCREMENT PRIMARY KEY,
       nombre VARCHAR(100) NOT NULL,
       descripcion VARCHAR(255),
       precio DECIMAL(10,2) NOT NULL,
       estado ENUM('ACTIVO','INACTIVO') DEFAULT 'ACTIVO'
);

-- Tabla metodo_pago
CREATE TABLE metodo_pago (
         id_metodo INT AUTO_INCREMENT PRIMARY KEY,
         nombre VARCHAR(50) NOT NULL UNIQUE,
         descripcion VARCHAR(100) NULL,
         activo BOOLEAN DEFAULT TRUE
);

-- Tabla pagos
CREATE TABLE pagos (
       id_pago INT AUTO_INCREMENT PRIMARY KEY,
       id_estudiante INT NOT NULL,
       id_usuario INT NOT NULL,
       id_metodo INT NOT NULL,
       monto_total DECIMAL(10, 2) NOT NULL,
       fecha_pago TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       estado ENUM('PENDIENTE','PAGADO','ANULADO') DEFAULT 'PAGADO',
       FOREIGN KEY (id_estudiante) REFERENCES estudiante(id),
       FOREIGN KEY (id_usuario) REFERENCES usuarios(id),
       FOREIGN KEY (id_metodo) REFERENCES metodo_pago(id_metodo)
);

-- Tabla boleta
CREATE TABLE boleta (
    id_boleta INT AUTO_INCREMENT PRIMARY KEY,
    id_pago INT NOT NULL,
    numero_boleta VARCHAR(20) UNIQUE NOT NULL,
    fecha_emision TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2) NOT NULL,
    estado ENUM('VIGENTE','ANULADA') DEFAULT 'VIGENTE',
    FOREIGN KEY (id_pago) REFERENCES pagos(id_pago)
);

-- Tabla detalle_boleta
CREATE TABLE detalle_boleta (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_boleta INT NOT NULL,
    id_concepto INT NOT NULL,
    cantidad INT DEFAULT 1,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_boleta) REFERENCES boleta(id_boleta),
    FOREIGN KEY (id_concepto) REFERENCES concepto_pago(id_concepto)
);

-- Datos de prueba
INSERT INTO usuarios (username, password, email, rol)
VALUES ('admin', SHA2('admin123', 256), 'admin@sistema.com', 'ADMIN');

INSERT INTO usuarios (username, password, email, rol)
VALUES ('cajero1', SHA2('cajero123', 256), 'cajero1@sistema.com', 'USER');

-- Estudiantes de prueba
INSERT INTO estudiante (dni, nombre, apellido_paterno, apellido_materno, email, telefono)
VALUES
    ('12345678', 'Juan', 'Pérez', 'García', 'juan.perez@email.com', '987654321'),
    ('87654321', 'María', 'López', 'Martínez', 'maria.lopez@email.com', '912345678'),
    ('11223344', 'Carlos', 'Rodríguez', 'Sánchez', 'carlos.rodriguez@email.com', '965432109');

-- Conceptos de pago
INSERT INTO concepto_pago (nombre, descripcion, precio, estado)
VALUES
    ('Matrícula', 'Pago de matrícula semestral', 350.00, 'ACTIVO'),
    ('Pensión Mensual', 'Pago mensual de estudios', 250.00, 'ACTIVO'),
    ('Certificado de Estudios', 'Emisión de certificado', 50.00, 'ACTIVO'),
    ('Carnet Estudiantil', 'Emisión de carnet', 20.00, 'ACTIVO'),
    ('Examen de Suficiencia', 'Inscripción a examen', 100.00, 'ACTIVO'),
    ('Constancia de Matrícula', 'Emisión de constancia', 15.00, 'ACTIVO'),
    ('Duplicado de Boleta', 'Duplicado de boleta de notas', 30.00, 'ACTIVO');

-- Métodos de pago
INSERT INTO metodo_pago (nombre, descripcion, activo)
VALUES
    ('EFECTIVO', 'Pago en efectivo', TRUE),
    ('TARJETA', 'Pago con tarjeta de crédito/débito', TRUE),
    ('TRANSFERENCIA', 'Transferencia bancaria', TRUE),
    ('YAPE', 'Pago mediante Yape', TRUE),
    ('PLIN', 'Pago mediante Plin', TRUE);

-- Crear índices para mejor rendimiento
CREATE INDEX idx_estudiante_dni ON estudiante(dni);
CREATE INDEX idx_pagos_estudiante ON pagos(id_estudiante);
CREATE INDEX idx_pagos_fecha ON pagos(fecha_pago);
CREATE INDEX idx_boleta_numero ON boleta(numero_boleta);