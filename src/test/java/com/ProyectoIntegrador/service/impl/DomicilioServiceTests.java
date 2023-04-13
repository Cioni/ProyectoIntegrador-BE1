package com.ProyectoIntegrador.service.impl;

import com.ProyectoIntegrador.exceptions.ResourceNotFoundException;
import com.ProyectoIntegrador.model.DomicilioDto;
import com.ProyectoIntegrador.service.IDomicilioService;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
public class DomicilioServiceTests {

    @Autowired
    private IDomicilioService domicilioService;
    private DomicilioDto domicilio;

    @BeforeEach
    public void setUp() {
        domicilio = new DomicilioDto();
        domicilio.setCalle("Calle Falsa");
        domicilio.setNumero(123);
        domicilio.setLocalidad("Pinamor");
        domicilio.setProvincia("Buenos Aires");
    }

    @Test
    public void test01AgregarDomicilio() throws Exception {
        DomicilioDto d = domicilioService.crear(domicilio);
        assertNotNull(domicilioService.buscarPorId(d.getId()));
    }

    @Test
    public void test02ActualizarDomicilio() throws Exception {
        DomicilioDto d = domicilioService.crear(domicilio);
        DomicilioDto original = domicilioService.buscarPorId(d.getId());
        d.setCalle("Falsita");
        DomicilioDto actualizado = domicilioService.actualizar(d);
        assertNotEquals(actualizado, original);
    }

    @Test
    public void test03EliminarDomicilio() throws Exception {
        DomicilioDto d = domicilioService.crear(domicilio);
        assertNotNull(domicilioService.buscarPorId(d.getId()));
        domicilioService.eliminar(d.getId());
        assertThrows(ResourceNotFoundException.class, () -> domicilioService.buscarPorId(d.getId()));
    }

    @Test
    public void test04ObtenerTodosLosDomicilios() throws Exception {
        domicilioService.crear(domicilio);
        assertNotEquals(0, domicilioService.consultarTodos().size());
    }
}
