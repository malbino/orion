/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  malbino
 * Created: Jul 6, 2023
 */

INSERT INTO recurso (ID_RECURSO,NOMBRE,URLPATTERN) VALUES (63,'Reportes > Centralizador Calificaciones PRAE','/reportes/centralizadorCalificacionesPRAE/');

INSERT INTO privilegio (id_recurso, id_rol) VALUES 
(63, 1),
(63, 4),
(63, 6),
(63, 7),
(63, 8);
