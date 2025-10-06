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


CREATE TABLE estudiante (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(8) NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    apellido_paterno VARCHAR(50) NOT NULL,
    apellido_materno VARCHAR(50) NOT NULL,
    direccion VARCHAR(100) null,
    email VARCHAR(100) NULL,
    telefono VARCHAR(12) NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT NOW(),
    fecha_modificacion TIMESTAMP NULL
)

CREATE TABLE concepto_pago(
      id_concepto INT AUTO_INCREMENT PRIMARY KEY,
      nombre VARCHAR(100) NOT NULL,         -- Ejemplo: Matrícula, Certificado
      descripcion VARCHAR(255),
      precio DECIMAL(10,2) NOT NULL,
      estado ENUM('ACTIVO','INACTIVO') DEFAULT 'ACTIVO'
)

CREATE TABLE pagos
(
    id_pago       INT AUTO_INCREMENT PRIMARY KEY,
    id_estudiante INT            NOT NULL,
    id_usuario    INT            NOT NULL, -- Cajero que registró el pago
    id_metodo     INT            NOT NULL,
    monto_total   DECIMAL(10, 2) NOT NULL,
    fecha_pago    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado        ENUM('PENDIENTE','PAGADO','ANULADO') DEFAULT 'PAGADO',

    FOREIGN KEY (id_estudiante) REFERENCES estudiante (id_estudiante),
    FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario),
    FOREIGN KEY (id_metodo) REFERENCES metodo_pago (id_metodo)
)

CREATE TABLE boleta (
    id_boleta INT AUTO_INCREMENT PRIMARY KEY,
    id_pago INT NOT NULL,
    numero_boleta VARCHAR(20) UNIQUE NOT NULL,  -- Ejemplo: BOL-2025-0001
    fecha_emision TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2) NOT NULL,
    estado ENUM('VIGENTE','ANULADA') DEFAULT 'VIGENTE',

    FOREIGN KEY (id_pago) REFERENCES pago(id_pago)
)

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