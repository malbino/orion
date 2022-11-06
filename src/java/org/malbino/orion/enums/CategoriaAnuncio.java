/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.enums;

/**
 *
 * @author Martin
 */
public enum CategoriaAnuncio {
    ADMINISTRACION_Y_OFICINA("ADMINISTRACIÓN Y OFICINA"),
    AGRICULTURA_Y_CAMPO("AGRICULTURA Y CAMPO"),
    ARQUITECTURA("ARQUITECTURA"),
    ATENCIÓN_AL_CLIENTE("ATENCIÓN AL CLIENTE"),
    CIENTIFICO_E_INVESTIGACION("CIENTÍFICO E INVESTIGACIÓN"),
    COCINA_Y_REPOSTERIA("COCINA Y REPOSTERÍA"),
    CONSTRUCCION("CONSTRUCCIÓN"),
    CONSULTORIAS_Y_PROYECTOS("CONSULTORÍAS Y PROYECTOS"),
    CONTABILIDA_Y_ECONOMIA("CONTABILIDA Y ECONOMÍA"),
    DIRECCION_Y_GERENCIA("DIRECCIÓN Y GERENCIA"),
    DISEÑO_Y_MEDIOS("DISEÑO Y MEDIOS"),
    EDUCACION("EDUCACIÓN"),
    INDUSTRIA_ALIMENTARIA("INDUSTRIA ALIMENTARIA"),
    INDUSTRIA_MINERA("INDUSTRIA MINERA"),
    INDUSTRIA_TEXTIL("INDUSTRIA TEXTIL"),
    INFORMATICA("INFORMÁTICA"),
    INGENIERIA("INGENIERÍA"),
    INMOBILIARIA("INMOBILIARIA"),
    INTERNET("INTERNET"),
    LEGAL_Y_ASEDORIA("LEGAL Y ASEDORÍA"),
    LOGISTICA_Y_ALMACEN("LOGÍSTICA Y ALMACÉN"),
    MANO_DE_OBRA_CALIFICADA("MANO DE OBRA CALIFICADA"),
    MANO_DE_OBRA_NO_CALIFICADA("MANO DE OBRA NO CALIFICADA"),
    MARKETING_Y_VENTAS("MARKETING Y VENTAS"),
    MEDICINA_Y_SALUD("MEDICINA Y SALUD"),
    MODA_Y_BELLEZA("MODA Y BELLEZA"),
    ONG_Y_ORG_BENEFICAS("ONG Y ORG. BENÉFICAS"),
    OTROS("OTROS"),
    RECURSOS_HUMANOS("RECURSOS HUMANOS"),
    SECTOR_PUBLICO("SECTOR PÚBLICO"),
    SEGURIDAD_INDUSTRIAL("SEGURIDAD INDUSTRIAL"),
    SERVICIOS_PETROLEROS("SERVICIOS PETROLEROS"),
    SOCIALES_Y_HUMANIDADES("SOCIALES Y HUMANIDADES"),
    TELECOMUNICACIONES("TELECOMUNICACIONES"),
    TURISMO_Y_HOTELERIA("TURISMO Y HOTELERÍA"),
    TECNICO("TÉCNICO");

    private String nombre;

    private CategoriaAnuncio(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
