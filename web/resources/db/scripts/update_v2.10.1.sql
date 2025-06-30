/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  malbino
 * Created: Jun 29, 2025
 */

ALTER TABLE `NOTA`
	ADD COLUMN `NUMEROLIBRO` INT(11) NULL DEFAULT NULL COLLATE 'utf8_spanish_ci' AFTER `CONDICION`;

ALTER TABLE `NOTA`
	ADD COLUMN `NUMEROFOLIO` INT(11) NULL DEFAULT NULL COLLATE 'utf8_spanish_ci' AFTER `NUMEROLIBRO`;