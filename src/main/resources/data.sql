INSERT INTO usuarios (username, password, nombre_usuario, apellidos_usuario, email, rol) values ('admin', '$2a$10$XsgubHfexQtsv96lfIEca.9j2iEXjw7tmoODUfMeHmIrmX5HjrcX2', 'Administrador', 'CRM', 'admin@mail.com', 'ADMINISTRATOR');
INSERT INTO usuarios (username, password, nombre_usuario, apellidos_usuario, email, rol) values ('treyna', '$2a$10$KL2sAlhxRz061nDYNI0nl.W7VvNKyh597OkKGPUOIDK5hXt7d9yu2', 'Tino', 'Reyna', 'treyna@mail.com', 'USER');
INSERT INTO usuarios (username, password, nombre_usuario, apellidos_usuario, email, rol) values ('glorentzen', '$2a$10$KL2sAlhxRz061nDYNI0nl.W7VvNKyh597OkKGPUOIDK5hXt7d9yu2', 'Gabriela', 'Lorentzen', 'glorentzen@mail.com', 'USER');

INSERT INTO productos (nombre_producto, desc_producto, precio_producto) VALUES ('Nissan Sentra 2023', 'Nissan Sentra 2023', 12000.00);
INSERT INTO productos (nombre_producto, desc_producto, precio_producto) VALUES ('KIA Picanto 2023', 'KIA Picanto 2023', 14000.00);
INSERT INTO productos (nombre_producto, desc_producto, precio_producto) VALUES ('Chevrolet Grand Vitara SZ 2019', 'Chevrolet Grand Vitara SZ 2019', 18700.00);
INSERT INTO productos (nombre_producto, desc_producto, precio_producto) VALUES ('JAC T8 2023', 'JAC T8 2023', 28000.00);
INSERT INTO productos (nombre_producto, desc_producto, precio_producto) VALUES ('Kawasaki Z1000 2023', 'Kawasaki Z1000 2023', 30000.00);
INSERT INTO productos (nombre_producto, desc_producto, precio_producto) VALUES ('Hyundai Tucson 2024', 'Hyundai Tucson 2024', 36400.00);
INSERT INTO productos (nombre_producto, desc_producto, precio_producto) VALUES ('Jeep Wrangler Rubicon 2023', 'Jeep Wrangler Rubicon 2023', 112000.00);
INSERT INTO productos (nombre_producto, desc_producto, precio_producto) VALUES ('Toyota Tundra Limited 2022', 'Toyota Tundra Limited 2022', 189000.00);


INSERT INTO clientes (nombre_cliente, apellidos_cliente, doc_id, fono_cliente, email_cliente) VALUES ('Juan', 'Perez', '48957852', '978487113', 'jperez@mail.com');
INSERT INTO clientes (nombre_cliente, apellidos_cliente, doc_id, fono_cliente, email_cliente) VALUES ('Jose', 'Collado', '74981524', '996315944', 'jcollado@mail.com');
INSERT INTO clientes (nombre_cliente, apellidos_cliente, doc_id, fono_cliente, email_cliente) VALUES ('Luis', 'Beltran', '42667848', '976325910', 'lbeltran@mail.com');

INSERT INTO clientes (nombre_cliente, apellidos_cliente, doc_id, fono_cliente, email_cliente, estado_cliente) VALUES ('Saul', 'Pazos', '10991520', '944528741', 'spazos@mail.com', 'CLIENTE');
INSERT INTO clientes (nombre_cliente, apellidos_cliente, doc_id, fono_cliente, email_cliente, estado_cliente) VALUES ('Roman', 'Bejar', '43117840', '922361024', 'rbejar@mail.com', 'CLIENTE');
INSERT INTO clientes (nombre_cliente, apellidos_cliente, doc_id, fono_cliente, email_cliente, estado_cliente) VALUES ('Alberto', 'Light', '42889563', '921458200', 'alight@mail.com', 'CLIENTE');

INSERT INTO facturas (producto_id, cod_factura, forma_pago, fecha_pago) VALUES (2, 'INV0000000001', 'OTRO', '2023-04-25');
INSERT INTO facturas (producto_id, cod_factura, forma_pago, fecha_pago) VALUES (3, 'INV0000000002', 'OTRO', '2023-02-11');
INSERT INTO facturas (producto_id, cod_factura, forma_pago, fecha_pago) VALUES (4, 'INV0000000003', 'OTRO', '2023-05-23');

INSERT INTO ventas (orden_venta, desc_venta, fecha_reserva, estado_venta, factura_id, cliente_id, usuario_id) VALUES ('OV0000000001', 'Venta del Nissan Sentra 2023', '2023-02-12', 'CIERRE_GANADO', 1, 4, 2);
INSERT INTO ventas (orden_venta, desc_venta, fecha_reserva, estado_venta, factura_id, cliente_id, usuario_id) VALUES ('OV0000000002', 'Kawasaki Z1000 2023', '2023-01-13', 'CIERRE_GANADO', 2, 5, 2);
INSERT INTO ventas (orden_venta, desc_venta, fecha_reserva, estado_venta, factura_id, cliente_id, usuario_id) VALUES ('OV0000000003', 'Hyundai Tucson 2024', '2023-03-12', 'CIERRE_GANADO', 3, 6, 3);

