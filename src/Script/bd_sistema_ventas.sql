/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  Manuel
 * Created: 21 dic. 2022
 */

-- DROP DATABASE bd_sistema_ventas;
create database bd_sistema_ventas;
use bd_sistema_ventas;
-- crear tabla usuarios

create table tb_usuario(
idUsuario int(11) auto_increment primary key,
nombre varchar(30) not null,
apellido varchar(30) not null,
usuario varchar(30) not null,
password varchar(15) not null,
telefono varchar(9) not null,
estado int(1) not null
);

-- crear tabla cliente

create table tb_cliente(
idCliente int (11) auto_increment primary key,
nombre varchar(30) not null,
apellido varchar(30) not null,
dni varchar(8) not null,
telefono varchar(9) not null,
direccion varchar(30) not null,
estado int(1) not null
);

-- crear tabla categoria

create table tb_categoria(
idCategoria int (11) auto_increment primary key,
descripcion varchar(100) not null,
estado int(1) not null
);

create table tb_producto(
idProducto int (11) auto_increment primary key,
nombre varchar(50) not null,
cantidad int(11) not null,
precio double(10,2) not null,
descripcion varchar(100) not null,
igv int(2) not null,
idCategoria int(11) not null,
estado int(1) not null,
foreign key (idCategoria) references tb_categoria(idCategoria)
);

-- crear tabla cabecera de venta

create table tb_cabecera_venta(
idCabeceraVenta int (11) auto_increment primary key,
idCliente int(11) not null,
valorPagar double(10,2) not null,
fechaVenta date not null,
estado int(1) not null,
foreign key (idCliente) references tb_cliente(idCliente)
);

-- crear tabla detalle de venta

create table tb_detalle_venta(
idDetalleVenta int (11) auto_increment primary key,
idCabeceraVenta int(11) not null,
idProducto int(11) not null,
cantidad int(11) not null,
precioUnitario double(10, 2) not null,
subtotal double(10, 2) not null,
descuento double(10, 2) not null,
montoIgv double(10, 2) not null,
totalPagar double(10, 2) not null,
estado int(1) not null,
foreign key (idCabeceraVenta) references tb_cabecera_venta(idCabeceraVenta),
foreign key (idProducto) references tb_producto(idProducto)
);

