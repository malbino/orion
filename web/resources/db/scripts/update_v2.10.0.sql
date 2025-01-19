/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  malbino
 * Created: Sep 1, 2024
 */

-- funcionalidad Horarios > Aulas
INSERT INTO `recurso` (`ID_RECURSO`, `NOMBRE`, `URLPATTERN`) VALUES ((SELECT MAX(r.id_recurso)+1 FROM recurso r), 'Horarios > Aulas', '/horarios/aula/');
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 1);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 4);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 6);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 7);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 8);

-- funcionalidad Horarios > Periodos
INSERT INTO `recurso` (`ID_RECURSO`, `NOMBRE`, `URLPATTERN`) VALUES ((SELECT MAX(r.id_recurso)+1 FROM recurso r), 'Horarios > Periodos', '/horarios/periodo/');
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 1);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 4);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 6);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 7);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 8);

-- funcionalidad Horarios > Horario Paralelo
INSERT INTO `recurso` (`ID_RECURSO`, `NOMBRE`, `URLPATTERN`) VALUES ((SELECT MAX(r.id_recurso)+1 FROM recurso r), 'Horarios > Horario Paralelo', '/horarios/horarioParalelo.xhtml');
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 1);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 4);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 6);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 7);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 8);

-- funcionalidad Reportes > Horarios > Horario Aula
INSERT INTO `recurso` (`ID_RECURSO`, `NOMBRE`, `URLPATTERN`) VALUES ((SELECT MAX(r.id_recurso)+1 FROM recurso r), 'Reportes > Horarios > Horario Aula', '/reportes/horarios/horarioAula/');
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 1);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 4);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 6);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 7);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 8);

-- funcionalidad Reportes > Horarios > Horario Paralelo
INSERT INTO `recurso` (`ID_RECURSO`, `NOMBRE`, `URLPATTERN`) VALUES ((SELECT MAX(r.id_recurso)+1 FROM recurso r), 'Reportes > Horarios > Horario Paralelo', '/reportes/horarios/horarioParalelo/');
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 1);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 4);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 6);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 7);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 8);

-- funcionalidad Reportes > Horarios > Horario Docente
INSERT INTO `recurso` (`ID_RECURSO`, `NOMBRE`, `URLPATTERN`) VALUES ((SELECT MAX(r.id_recurso)+1 FROM recurso r), 'Reportes > Horarios > Horario Docente', '/reportes/horarios/horarioDocente/');
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 1);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 4);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 6);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 7);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 8);