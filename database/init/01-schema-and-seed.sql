CREATE TABLE categorias (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE productos (
    id BIGSERIAL PRIMARY KEY,
    categoria_id BIGINT NOT NULL REFERENCES categorias(id),
    nombre VARCHAR(160) NOT NULL,
    precio_actual NUMERIC(12,2) NOT NULL CHECK (precio_actual >= 0),
    costo_unitario NUMERIC(12,2) NOT NULL DEFAULT 0 CHECK (costo_unitario >= 0),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE sucursales (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL UNIQUE,
    ciudad VARCHAR(100) NOT NULL
);

CREATE TABLE inventario (
    id BIGSERIAL PRIMARY KEY,
    sucursal_id BIGINT NOT NULL REFERENCES sucursales(id),
    producto_id BIGINT NOT NULL REFERENCES productos(id),
    cantidad_stock INTEGER NOT NULL DEFAULT 0 CHECK (cantidad_stock >= 0),
    CONSTRAINT uq_inventario_sucursal_producto UNIQUE (sucursal_id, producto_id)
);

CREATE TABLE clientes (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(160) NOT NULL,
    correo VARCHAR(180) UNIQUE
);

CREATE TABLE compras (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT REFERENCES clientes(id),
    sucursal_id BIGINT NOT NULL REFERENCES sucursales(id),
    fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(30) NOT NULL DEFAULT 'COMPLETADA'
);

CREATE TABLE detalle_compra (
    id BIGSERIAL PRIMARY KEY,
    compra_id BIGINT NOT NULL REFERENCES compras(id),
    producto_id BIGINT NOT NULL REFERENCES productos(id),
    cantidad INTEGER NOT NULL CHECK (cantidad > 0),
    precio_unitario NUMERIC(12,2) NOT NULL CHECK (precio_unitario >= 0),
    descuento_unitario NUMERIC(12,2) NOT NULL DEFAULT 0 CHECK (descuento_unitario >= 0 AND descuento_unitario <= precio_unitario)
);

CREATE INDEX idx_compras_fecha ON compras(fecha);
CREATE INDEX idx_detalle_compra_compra_id ON detalle_compra(compra_id);
CREATE INDEX idx_detalle_compra_producto_id ON detalle_compra(producto_id);

INSERT INTO categorias (id, nombre) VALUES
(1, 'Audio'), (2, 'Accesorios'), (3, 'Periféricos');

INSERT INTO productos (id, categoria_id, nombre, precio_actual, costo_unitario) VALUES
(15, 1, 'Audífonos Bluetooth', 35.00, 17.00),
(20, 3, 'Teclado USB', 30.00, 14.00),
(25, 2, 'Mouse inalámbrico', 12.00, 5.50);

INSERT INTO sucursales (id, nombre, ciudad) VALUES
(1, 'Sucursal Centro', 'Manta'), (2, 'Sucursal Norte', 'Montecristi');

INSERT INTO inventario (sucursal_id, producto_id, cantidad_stock) VALUES
(1, 15, 20), (1, 20, 12), (1, 25, 40), (2, 15, 8), (2, 25, 17);

INSERT INTO clientes (id, nombre, correo) VALUES
(8, 'Cliente Demo', 'cliente.demo@example.com'),
(9, 'Cliente Prueba', 'cliente.prueba@example.com');

INSERT INTO compras (id, cliente_id, sucursal_id, fecha, estado) VALUES
(101, 8, 1, '2026-10-03 10:15:00', 'COMPLETADA'),
(102, 9, 1, '2026-10-15 14:30:00', 'COMPLETADA'),
(103, 8, 2, '2026-11-02 09:00:00', 'COMPLETADA');

INSERT INTO detalle_compra (compra_id, producto_id, cantidad, precio_unitario, descuento_unitario) VALUES
(101, 15, 2, 25.00, 0.00),
(101, 20, 1, 30.00, 0.00),
(102, 25, 3, 10.00, 0.00),
(103, 15, 1, 50.00, 0.00);

SELECT setval(pg_get_serial_sequence('categorias', 'id'), (SELECT MAX(id) FROM categorias));
SELECT setval(pg_get_serial_sequence('productos', 'id'), (SELECT MAX(id) FROM productos));
SELECT setval(pg_get_serial_sequence('sucursales', 'id'), (SELECT MAX(id) FROM sucursales));
SELECT setval(pg_get_serial_sequence('clientes', 'id'), (SELECT MAX(id) FROM clientes));
SELECT setval(pg_get_serial_sequence('compras', 'id'), (SELECT MAX(id) FROM compras));
