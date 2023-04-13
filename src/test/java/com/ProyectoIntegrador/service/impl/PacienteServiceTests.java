package com.ProyectoIntegrador.service.impl;

import com.ProyectoIntegrador.exceptions.ResourceNotFoundException;
import com.ProyectoIntegrador.model.DomicilioDto;
import com.ProyectoIntegrador.model.PacienteDto;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
public class PacienteServiceTests {

    @Autowired
    private PacienteService pacienteService;
    private PacienteDto paciente;

    @BeforeEach
    public void setUp() {
        DomicilioDto domicilio = new DomicilioDto("Calle Falsa", 123, "Pinamor", "Buenos Aires");
        paciente = new PacienteDto("Harry", "Postre", 123456789, domicilio);
    }

    @Test
    public void test01AgregarPaciente() throws Exception {
        PacienteDto p = pacienteService.crear(paciente);
        assertNotNull(pacienteService.buscarPorId(p.getId()));
    }

    @Test
    public void test02ActualizarPaciente() throws Exception {
        PacienteDto p = pacienteService.crear(paciente);
        PacienteDto original = pacienteService.buscarPorId(p.getId());
        p.setNombre("Pepax");
        PacienteDto actualizado = pacienteService.actualizar(p);
        assertNotEquals(actualizado, original);
    }

    @Test
    public void test03EliminarPaciente() throws Exception {
        PacienteDto p = pacienteService.crear(paciente);
        assertNotNull(pacienteService.buscarPorId(p.getId()));
        pacienteService.eliminar(p.getId());
        assertThrows(ResourceNotFoundException.class, () -> pacienteService.buscarPorId(p.getId()));
    }

    @Test
    public void test04ObtenerTodosLosPacientes() throws Exception {
        pacienteService.crear(paciente);
        assertNotEquals(0, pacienteService.consultarTodos().size());
    }
}
