package com.gestion.empleados.repositorio;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.gestion.empleados.entidades.Empleado;

public interface EmpleadoRepository extends PagingAndSortingRepository<Empleado, Long>{

}
