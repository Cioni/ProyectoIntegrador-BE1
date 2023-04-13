package com.ProyectoIntegrador.service;

import com.ProyectoIntegrador.exceptions.BadRequestException;
import com.ProyectoIntegrador.exceptions.ResourceNotFoundException;
import com.ProyectoIntegrador.model.PacienteDto;

import java.util.List;

public interface IPacienteService extends CRUDService<PacienteDto> {
    PacienteDto buscar(Integer dni) throws ResourceNotFoundException, BadRequestException;

    List<PacienteDto> buscar(String nombre);

    List<PacienteDto> buscar(String nombre, String apellido);
}
