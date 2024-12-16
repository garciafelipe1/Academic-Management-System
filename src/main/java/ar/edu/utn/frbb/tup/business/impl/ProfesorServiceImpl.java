package ar.edu.utn.frbb.tup.business.impl;
import ar.edu.utn.frbb.tup.business.ProfesorService;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.ProfesorDto;
import ar.edu.utn.frbb.tup.model.exception.MateriaNoEncontradaException;
import ar.edu.utn.frbb.tup.model.exception.ProfesorNoEncontradoException;
import ar.edu.utn.frbb.tup.model.exception.ProfesorYaExisteException;
import ar.edu.utn.frbb.tup.persistence.MateriaDaoMemoryImpl;
import ar.edu.utn.frbb.tup.persistence.ProfesorDaoMemoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class ProfesorServiceImpl implements ProfesorService {
    @Autowired
    private ProfesorDaoMemoryImpl profesorDaoMemoryimpl;

    @Autowired
    private MateriaDaoMemoryImpl materiaDaoMemoryimpl;

    @Override
    public Profesor crearProfesor(ProfesorDto profesordto) throws IllegalArgumentException,ProfesorYaExisteException {
        // Validar campos obligatorios
        if (profesordto.getNombre() == null || profesordto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del profesor no puede estar vacío.");
        }

        if (profesordto.getApellido() == null || profesordto.getApellido().trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido del profesor no puede estar vacío.");
        }

        if (profesordto.getTitulo() == null || profesordto.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título del profesor no puede estar vacío.");
        }

        // Verificar si ya existe un profesor con el mismo nombre y apellido
        List<Profesor> profesoresExistentes = profesorDaoMemoryimpl.buscarProfesores();
        for (Profesor p : profesoresExistentes) {
            if (p.getNombre().equalsIgnoreCase(profesordto.getNombre()) &&
                    p.getApellido().equalsIgnoreCase(profesordto.getApellido())) {
                throw new ProfesorYaExisteException("Ya existe un profesor con el mismo nombre y apellido.");
            }
        }

        // Crear y guardar el profesor
        Profesor profesor = new Profesor(profesordto.getNombre(), profesordto.getApellido(), profesordto.getTitulo());
        profesorDaoMemoryimpl.guardarProfesor(profesor);
        return profesor; // Retornar el profesor creado
    }


    public Profesor borrarprofesor(long id) {
        // primero salgo a buscarlo.
        Profesor profesorexistente = profesorDaoMemoryimpl.buscarProfesorporid(id);
        if (profesorexistente != null)
        {
           profesorDaoMemoryimpl.borrarProfesorporid(id);
        }
       return profesorexistente;
    }


    @Override
    public Profesor borrarProfesorporid(long id) {

        Profesor profesorExistente = profesorDaoMemoryimpl.buscarProfesorporid(id);

        if (profesorExistente != null) {
            profesorDaoMemoryimpl.borrarProfesorporid(id);
            return profesorExistente; // Retornar el profesor eliminado.
        }

        // Si el profesor no existe, puedes lanzar una excepción o retornar null.
        return null;
    }



    @Override
    public List<Profesor> buscarProfesores() {
        List<Profesor> lista_de_profesor = profesorDaoMemoryimpl.buscarProfesores();
        return lista_de_profesor;
    }

    @Override
    public Profesor buscaProfesorporid(long id) {
        Profesor profesorid=profesorDaoMemoryimpl.buscarProfesorporid(id);
        return profesorid;
    }


    @Override
    public Profesor modificarProfesor(long id, ProfesorDto profesor) {


        // Buscar si existe el profesor  a través del id
        Profesor profesorExistente= profesorDaoMemoryimpl.buscarProfesorporid(id);

        if (profesorExistente != null) {
            // Actualizar los datos del profesor existente con los nuevos datos del DTO
            profesorExistente.setNombre(profesor.getNombre());
            profesorExistente.setApellido(profesor.getApellido());
            profesorExistente.setTitulo(profesor.getTitulo());

            // Aquí puedes seguir actualizando otros campos que tengas en ProfesorDto

            // Guardar los cambios
            profesorDaoMemoryimpl.modificarProfesor(profesorExistente);

            // Retornar el profesor modificado
            return profesorExistente;
        } else {
            // Si no se encuentra el profesor, retornar null o lanzar una excepción
            System.out.println("No se encontró un profesor con el id proporcionado");
            return null;
        }
    }

    @Override
    public List<Materia> buscarMateriasPorProfesorId(long idProfesor) throws ProfesorNoEncontradoException, MateriaNoEncontradaException {
        // Validar el ID del profesor
        if (idProfesor <= 0) {
            throw new IllegalArgumentException("El ID del profesor debe ser un valor positivo.");
        }

        // Verificar si el profesor existe
        Profesor profesor = profesorDaoMemoryimpl.buscarProfesorporid(idProfesor);
        if (profesor == null) {
            throw new ProfesorNoEncontradoException("El profesor con el ID especificado no existe.");
        }

        // Buscar las materias asociadas al profesor
        List<Materia> materiasEncontradas = materiaDaoMemoryimpl.buscarMateriasPorProfesorId(idProfesor);

        // Verificar si se encontraron materias
        if (materiasEncontradas == null || materiasEncontradas.isEmpty()) {
            throw new MateriaNoEncontradaException("No se encontraron materias asociadas al profesor con ID: " + idProfesor);
        }

        // Ordenar las materias por nombre alfabético
        materiasEncontradas.sort(Comparator.comparing(Materia::getNombre));

        return materiasEncontradas;
    }

}
