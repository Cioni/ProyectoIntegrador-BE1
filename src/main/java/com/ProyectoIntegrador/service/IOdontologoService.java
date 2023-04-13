package com.ProyectoIntegrador.service;

import com.ProyectoIntegrador.exceptions.BadRequestException;
import com.ProyectoIntegrador.exceptions.ResourceNotFoundException;
import com.ProyectoIntegrador.model.OdontologoDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IOdontologoService extends CRUDService<OdontologoDto> {
    OdontologoDto buscar(Integer matricula) throws ResourceNotFoundException,
            BadRequestException;

    List<OdontologoDto> buscar(String nombre);

    List<OdontologoDto> buscar(String nombre, String apellido);
}
