package com.ProyectoIntegrador.service.impl;

import com.ProyectoIntegrador.exceptions.BadRequestException;
import com.ProyectoIntegrador.exceptions.ResourceNotFoundException;
import com.ProyectoIntegrador.model.DomicilioDto;
import com.ProyectoIntegrador.model.OdontologoDto;
import com.ProyectoIntegrador.model.PacienteDto;
import com.ProyectoIntegrador.model.TurnoDto;
import com.ProyectoIntegrador.service.IOdontologoService;
import com.ProyectoIntegrador.service.IPacienteService;
import com.ProyectoIntegrador.service.ITurnoService;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
public class TurnoServiceTests {

    @Autowired
    private IPacienteService pacienteService;
    private PacienteDto paciente;

    @Autowired
    private IOdontologoService odontologoService;
    private OdontologoDto odontologo;

    @Autowired
    private ITurnoService turnoService;

    @BeforeEach
    public void setUp() {
        DomicilioDto domicilio = new DomicilioDto();
        domicilio.setCalle("Calle Falsa");
        domicilio.setNumero(123);
        domicilio.setLocalidad("Pinamor");
        domicilio.setProvincia("Buenos Aires");

        paciente = new PacienteDto();
        paciente.setNombre("Harry");
        paciente.setApellido("Postre");
        paciente.setDni(123456789);
        paciente.setDomicilio(domicilio);

        odontologo = new OdontologoDto();
        odontologo.setNombre("Harry");
        odontologo.setApellido("Postre");
        odontologo.setMatricula(123456);
    }

    @Test
    public void test01CrearTurnoConPacienteYOdontologoExistente() throws
            BadRequestException, ResourceNotFoundException {
        PacienteDto pacienteCreado = pacienteService.crear(paciente);
        OdontologoDto odontologoCreado = odontologoService.crear(odontologo);
        TurnoDto turno = new TurnoDto();
        turno.setFecha(LocalDateTime.now());
        turno.setPaciente(pacienteCreado);
        turno.setOdontologo(odontologoCreado);
        TurnoDto turnoCreado = turnoService.crear(turno);
        assertNotNull(turnoService.buscarPorId(turnoCreado.getId()));
    }

    @Test
    public void test02CrearTurnoConPacienteYOdontologoInexistente() throws BadRequestException, ResourceNotFoundException {
        PacienteDto pacienteCreado = pacienteService.crear(paciente);
        TurnoDto turno = new TurnoDto();
        turno.setFecha(LocalDateTime.now());
        turno.setPaciente(pacienteCreado);
        turno.setOdontologo(odontologo);
        assertThrows(BadRequestException.class, () -> turnoService.crear(turno));
    }

    @Test
    public void test04CrearTurnoConPacienteInexistenteOdontologoExistente() throws BadRequestException, ResourceNotFoundException {
        OdontologoDto odontologoCreado = odontologoService.crear(odontologo);
        TurnoDto turno = new TurnoDto();
        turno.setFecha(LocalDateTime.now());
        turno.setPaciente(paciente);
        turno.setOdontologo(odontologoCreado);
        assertThrows(BadRequestException.class, () -> turnoService.crear(turno));
    }

    @Test
    public void test05CrearTurnoSinPacienteNiOdontologo() {
        TurnoDto turno = new TurnoDto();
        turno.setFecha(LocalDateTime.now());
        assertThrows(BadRequestException.class, () -> turnoService.crear(turno));
    }

    @Test
    public void test06NoSePuedeCrearTurnoConHorarioRepetidoParaOdontologo() throws BadRequestException, ResourceNotFoundException {
        PacienteDto pacienteCreado = pacienteService.crear(paciente);
        OdontologoDto odontologoCreado = odontologoService.crear(odontologo);
        TurnoDto turno = new TurnoDto();
        turno.setFecha(LocalDateTime.of(2023, 4, 6, 18, 30));
        turno.setPaciente(pacienteCreado);
        turno.setOdontologo(odontologoCreado);
        TurnoDto turnoCreado = turnoService.crear(turno);
        assertNotNull(turnoService.buscarPorId(turnoCreado.getId()));
        assertThrows(BadRequestException.class, () -> turnoService.crear(turno));
    }

    @Test
    public void test07ActualizarTurno() throws BadRequestException, ResourceNotFoundException {
        TurnoDto turno = new TurnoDto();
        turno.setFecha(LocalDateTime.of(2023, 4, 6, 18, 30));
        turno.setPaciente(pacienteService.crear(paciente));
        turno.setOdontologo(odontologoService.crear(odontologo));
        TurnoDto turnoCreado = turnoService.crear(turno);
        turno.setId(turnoCreado.getId());
        turno.setFecha(LocalDateTime.of(2023, 4, 9, 18, 30));
        TurnoDto turnoActualizado = turnoService.actualizar(turno);
        assertNotEquals(turnoActualizado, turnoCreado);
    }

    @Test
    public void test08NoSePuedeActualizarTurnoSinId() throws BadRequestException, ResourceNotFoundException {
        TurnoDto turno = new TurnoDto();
        turno.setFecha(LocalDateTime.of(2023, 4, 9, 18, 30));
        turno.setPaciente(pacienteService.crear(paciente));
        turno.setOdontologo(odontologoService.crear(odontologo));
        turnoService.crear(turno);
        turno.setFecha(LocalDateTime.of(2023, 4, 9, 18, 30));
        assertThrows(BadRequestException.class, () -> turnoService.actualizar(turno));
    }

    @Test
    public void test08NoSePuedeActualizarTurnoConIdInexistente() throws BadRequestException, ResourceNotFoundException {
        TurnoDto turno = new TurnoDto();
        turno.setFecha(LocalDateTime.of(2023, 4, 9, 18, 30));
        turno.setPaciente(pacienteService.crear(paciente));
        turno.setOdontologo(odontologoService.crear(odontologo));
        turnoService.crear(turno);
        turno.setId(51);
        turno.setFecha(LocalDateTime.of(2023, 4, 9, 18, 30));
        assertThrows(ResourceNotFoundException.class, () -> turnoService.actualizar(turno));
    }

    @Test
    public void test10NoSePuedeActualizarTurnoSinPaciente() throws BadRequestException, ResourceNotFoundException {
        TurnoDto turno = new TurnoDto();
        turno.setFecha(LocalDateTime.of(2023, 4, 9, 18, 30));
        turno.setPaciente(pacienteService.crear(paciente));
        turno.setOdontologo(odontologoService.crear(odontologo));
        turnoService.crear(turno);
        turno.setPaciente(null);
        turno.setFecha(LocalDateTime.of(2023, 4, 9, 18, 30));
        assertThrows(BadRequestException.class, () -> turnoService.actualizar(turno));
    }

    @Test
    public void test11NoSePuedeActualizarTurnoSinOdontologo() throws BadRequestException, ResourceNotFoundException {
        TurnoDto turno = new TurnoDto();
        turno.setFecha(LocalDateTime.of(2023, 4, 9, 18, 30));
        turno.setPaciente(pacienteService.crear(paciente));
        turno.setOdontologo(odontologoService.crear(odontologo));
        turnoService.crear(turno);
        turno.setOdontologo(null);
        turno.setFecha(LocalDateTime.of(2023, 4, 9, 18, 30));
        assertThrows(BadRequestException.class, () -> turnoService.actualizar(turno));
    }

    @Test
    public void test12EliminarTurno() throws BadRequestException, ResourceNotFoundException {
        TurnoDto turno = new TurnoDto();
        turno.setFecha(LocalDateTime.of(2023, 4, 9, 18, 30));
        turno.setPaciente(pacienteService.crear(paciente));
        turno.setOdontologo(odontologoService.crear(odontologo));
        turnoService.crear(turno);
        turnoService.eliminar(1);
        assertThrows(ResourceNotFoundException.class, () -> turnoService.buscarPorId(1));
    }

    @Test
    public void test13ConsultarTodos() throws BadRequestException, ResourceNotFoundException {
        TurnoDto turno = new TurnoDto();
        turno.setFecha(LocalDateTime.of(2023, 4, 9, 18, 30));
        turno.setPaciente(pacienteService.crear(paciente));
        turno.setOdontologo(odontologoService.crear(odontologo));
        turnoService.crear(turno);
        assertNotEquals(0, turnoService.consultarTodos().size());
    }

    @Test
    public void test13ConsultarTodosPorNombreYApellidoOdontologo() throws BadRequestException, ResourceNotFoundException {
        TurnoDto turno = new TurnoDto();
        turno.setFecha(LocalDateTime.of(2023, 4, 9, 18, 30));
        turno.setPaciente(pacienteService.crear(paciente));
        turno.setOdontologo(odontologoService.crear(odontologo));
        turnoService.crear(turno);
        assertNotEquals(0, turnoService.buscar(odontologo.getNombre(), odontologo.getApellido()).size());
    }

    @Test
    public void test14ConsultarTodosPorDniPacienteYMatriculaOdontologo() throws BadRequestException, ResourceNotFoundException {
        TurnoDto turno = new TurnoDto();
        turno.setFecha(LocalDateTime.of(2023, 4, 9, 18, 30));
        turno.setPaciente(pacienteService.crear(paciente));
        turno.setOdontologo(odontologoService.crear(odontologo));
        turnoService.crear(turno);
        assertNotEquals(0, turnoService.buscar(odontologo.getMatricula(), paciente.getDni()).size());
    }
}
