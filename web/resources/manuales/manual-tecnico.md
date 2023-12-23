

**TABLA DE CONTENIDOS**

[TOC]

# Manual Técnico

## 1. Introducción

El presente manual tiene el objetivo de describir los pasos para la instalación y configuracion del sistema.

## 2. Requisitos

Para la instalación del sistema se debe cumplir con los siguientes requsitos de hardware y software.

### 2.1 Requisisto de Hardware

Es necesario preparar un servidor con las siguientes caracteristicas.

Característica | Especificación técnica
--- | ---
Procesador | Intel® Xeon® E-2224G o superior
Memoria | DDR4 de ECC 8GB o mas
Controladora | RAID Intel RST o superior
Almacenamiento | 2 x HDD Seagate BarraCuda 1TB o superior
Red | NIC de 1 GbE

*Nota.- Las caracteristicas minimas para el servidor dependen directamente del tamaño del instituto.* 

### 2.2 Requisitos de Software

Una vez preparado el servidor se debe instalar el siguiente software.

Característica | Especificación técnica
--- | ---
Sistema Operativo | CentOS 7 o Ubuntu Server 18.04 LTS
Entorno de Ejecución | OpenJDK version 1.8.0_312
Base de Datos | MariaDB version 10.5.21
Panel de Administración | ISPConfig 3
Servidor de Aplicaciones | Payara Server 4.1.2.181

## 3. Base de Datos

### 3.1 Modelo de la Base de Datos

Modelo de la base de datos.

