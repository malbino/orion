

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

Modelo de la base de datos elaborado en [SQL Power Architect](https://bestofbi.com/architect-download/).

![](https://raw.githubusercontent.com/malbino/orion/master/web/resources/db/modelo/modelo.png)

### 3.2 Tablas de la Base de Datos

Tabla | Descripción
--- | ---
actividad | Actividades del calendario académico
adjunto | Archivos adjuntos para las pasantias
asistencia | Asistencia del cuadernillo de pasantias
campus | Campus donde se llevan a cabo las clases
carrera | Carreras del instituto
comprobante | Comprobante de pago para las matriculas y otros conceptos
cuenta | Cuenta de usuario
cursa | Carrera que cursa un estudiante
detalle | Detalle del comprobante de pago
empleado | Empleados del instituto
empresa | Empresas para las pasantias
estudiante | Estudiantes del instituto
gestionacademica | Gestiones académicas anuales y semestrales
grupo | Grupos para las materias
grupopasantia | Grupos para las pasantias
indicadorpasantia | Indicadores de evaluación para las pasantias
inscrito | Inscritos en un gestión académica
instituto | Datos generarles del instituto
log | Registro del log del sistema
materia | Materias de las carreras del instituto
mencion | Menciones de las carreras
nota | Notas de los estudiantes
pago | Pago de un estudiante
pasantia | Pasantias de los estudiantes
persona | Datos personales
postulante | Postulantes al instituto
prerequisito | Prerequisito de las materias
privilegio | Privilegios de un rol
recurso | Recursos del sistema
rol | Roles del sistema
usuario | Usuarios del sistema


## 4. Configuracion del sistema

Para el despliegue del sistema se deb seguir los siguientes pasos.

### 4.1 Conexion con la base de datos

Ingrese a la consola de administracion de Payara con su nombre de usuario y contraseña y siga los siguiente pasos.

1. En el menu principal seleccione **Resources > JDBC > JDBC Connection Pools > New**. 
2. Llene los siguientes campos y haga clic en **Next**.

![](https://raw.githubusercontent.com/malbino/orion/master/web/resources/manuales/manual-tecnico/images/01.png)

3. En la sección de **Additional Properties** añada las siguientes propiedades y luego haga clic en **Finish**.

![](https://raw.githubusercontent.com/malbino/orion/master/web/resources/manuales/manual-tecnico/images/02.png)

4. Para probar que la conexion esta funcionando seleccione **Resources > JDBC > JDBC Connection Pools > orionPool** y haga clic en **Ping**.

![](https://raw.githubusercontent.com/malbino/orion/master/web/resources/manuales/manual-tecnico/images/03.png)

5. En el menu principal seleccione **Resources > JDBC > JDBC Resources > New**.
6. Llene los siguientes campos y haga clic en **OK**.

![](https://raw.githubusercontent.com/malbino/orion/master/web/resources/manuales/manual-tecnico/images/04.png)

### 4.2 JavaMail Session

Ingrese a la consola de administracion de Payara con su nombre de usuario y contraseña y siga los siguiente pasos.

1. En el menu principal seleccione **Resources > JavaMail Sessions > New**.
2. Llene los siguientes campos y haga clic en **OK**.

![](https://raw.githubusercontent.com/malbino/orion/master/web/resources/manuales/manual-tecnico/images/05.png)

## 5. Desplegar el sistema

Ingrese a la consola de administracion de Payara con su nombre de usuario y contraseña y siga los siguiente pasos.

1. En el menu principal seleccione **Applications > Deploy**.
2. Seleccione el archivo **orion.war** que se encuentra en el DVD de instalación del sistema y haga clic en **OK**.

![](https://raw.githubusercontent.com/malbino/orion/master/web/resources/manuales/manual-tecnico/images/06.png)

## 6. Ingresar al sistema

Ingrese al panel de administracion de phpMyAdmin con su nombre de usuario y contraseña y siga los siguientes pasos.

1. Seleccione la base de datos del sistema **c0orion**.
2. En el menu principal seleccione **Import**.
3. Seleccione el archivo **init_v2.6.4.sql** que se encuentra en el DVD de instalación del sistema y haga clic en **Go**.

![](https://raw.githubusercontent.com/malbino/orion/master/web/resources/manuales/manual-tecnico/images/07.png)

4. Abra un navegador e ingrese **localhost:8080/orion**.

![](https://raw.githubusercontent.com/malbino/orion/master/web/resources/manuales/manual-tecnico/images/08.png)

5. Ingrese **admin** como nombre de usuario y **Pa$$w0rd** como contraseña para ingresar al sistema por primera vez.

![](https://raw.githubusercontent.com/malbino/orion/master/web/resources/manuales/manual-tecnico/images/09.png)

*Nota.- Una vez en el sistema cambie inmediatamente la contraseña del usuario **admin**.*