package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.business.impl.AlumnoServiceImpl;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.persistence.AlumnoDaoMemoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AlumnoServiceImplTest {

    @Mock
    private AlumnoDaoMemoryImpl alumnoDaoMemoryImpl;

    @InjectMocks
    private AlumnoServiceImpl alumnoService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCrearAlumno() {
        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setNombre("Juan");
        alumnoDto.setApellido("Perez");
        alumnoDto.setDni(12345678);
        Alumno alumno = new Alumno("Juan", "Perez", 12345678);

         //when(alumnoDaoMemoryImpl.guardarAlumno(any(Alumno.class))).thenReturn(alumno);

        Alumno result = alumnoService.crearAlumno(alumnoDto);

        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
        assertEquals("Perez", result.getApellido());
        assertEquals(12345678, result.getDni());
        verify(alumnoDaoMemoryImpl, times(1)).guardarAlumno(any(Alumno.class));
    }

    @Test
    public void testBorrarAlumnoId() {
        long id = 4L;
        Alumno alumno = new Alumno("Juan", "Perez", 12345678);

        System.out.println("b1");
        when(alumnoDaoMemoryImpl.buscarAlumnoporid(id)).thenReturn(alumno);
        System.out.println("b2");
        /*
        * rg.mockito.exceptions.base.MockitoException:
Only void methods can doNothing()!
Example of correct use of doNothing():
    doNothing().
    doThrow(new RuntimeException())
    .when(mock).someVoidMethod();
Above means:
someVoidMethod() does nothing the 1st time but throws an exception the 2nd time is called

	at ar.edu.utn.frbb.tup.service.AlumnoServiceImplTest.testBorrarAlumnoId(AlumnoServiceImplTest.java:60)
        * */
        doNothing().when(alumnoDaoMemoryImpl).borrarAlumnoporid(id);
        System.out.println("b3");

        Alumno result = alumnoService.borraralumnoId(id);

        assertNotNull(result);
        assertEquals(alumno, result);
        verify(alumnoDaoMemoryImpl, times(1)).buscarAlumnoporid(id);
        verify(alumnoDaoMemoryImpl, times(1)).borrarAlumnoporid(id);
    }

    @Test
    public void testBuscarAlumnoId() {
        long id = 1L;
        Alumno alumno = new Alumno("Juan", "Perez", 12345678);

        when(alumnoDaoMemoryImpl.buscarAlumnoporid(id)).thenReturn(alumno);

        Alumno result = alumnoService.buscarAlumnoId(id);

        assertNotNull(result);
        assertEquals(alumno, result);
        verify(alumnoDaoMemoryImpl, times(1)).buscarAlumnoporid(id);
    }

    @Test
    public void testModificarAlumno() {
        long id = 1L;
        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setNombre("Carlos");
        alumnoDto.setApellido("Lopez");
        alumnoDto.setDni(87654321);
        Alumno alumnoExistente = new Alumno("Juan", "Perez", 12345678);

        when(alumnoDaoMemoryImpl.buscarAlumnoporid(id)).thenReturn(alumnoExistente);
        when(alumnoDaoMemoryImpl.modificarAlumno(any(Alumno.class))).thenReturn(alumnoExistente);

        Alumno result = alumnoService.modificarAlumno(id, alumnoDto);

        assertNotNull(result);
        assertEquals("Carlos", result.getNombre());
        assertEquals("Lopez", result.getApellido());
        assertEquals(87654321, result.getDni());
        verify(alumnoDaoMemoryImpl, times(1)).buscarAlumnoporid(id);
        verify(alumnoDaoMemoryImpl, times(1)).modificarAlumno(any(Alumno.class));
    }

    @Test
    public void testBuscarAlumnos() {
        List<Alumno> alumnos = new ArrayList<>();
        alumnos.add(new Alumno("Juan", "Perez", 12345678));
        alumnos.add(new Alumno("Ana", "Garcia", 87654321));

        when(alumnoDaoMemoryImpl.buscarAlumnos()).thenReturn(alumnos);

        List<Alumno> result = alumnoService.buscarAlumnos();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(alumnoDaoMemoryImpl, times(1)).buscarAlumnos();
    }
}