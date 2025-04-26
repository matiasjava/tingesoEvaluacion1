package kartingRM.Backend.Services;

import kartingRM.Backend.Entities.UserEntity;
import kartingRM.Backend.Repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        List<UserEntity> users = new ArrayList<>();
        users.add(new UserEntity());
        users.add(new UserEntity());

        when(userRepository.findAll()).thenReturn(users);

        List<UserEntity> result = userService.getAllUsers();

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testFindUserByRut() {
        String rut = "12345678-9";
        UserEntity user = new UserEntity();
        user.setRut(rut);

        when(userRepository.findByRut(rut)).thenReturn(Optional.of(user));

        Optional<UserEntity> result = userService.findUserByRut(rut);

        assertTrue(result.isPresent());
        assertEquals(rut, result.get().getRut());
        verify(userRepository, times(1)).findByRut(rut);
    }

    @Test
    void testIncrementVisits() {
        String rut = "12345678-9";
        UserEntity user = new UserEntity();
        user.setRut(rut);
        user.setNumberVisits(3);

        when(userRepository.findByRut(rut)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        UserEntity result = userService.incrementVisits(rut);

        assertEquals(4, result.getNumberVisits());
        verify(userRepository, times(1)).findByRut(rut);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testSaveUser() {
        UserEntity user = new UserEntity();
        user.setRut("12345678-9");

        when(userRepository.save(user)).thenReturn(user);

        UserEntity result = userService.saveUser(user);

        assertEquals("12345678-9", result.getRut());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetUserByRut() {
        String rut = "12345678-9";
        UserEntity user = new UserEntity();
        user.setRut(rut);

        when(userRepository.findByRut(rut)).thenReturn(Optional.of(user));

        UserEntity result = userService.getUserByRut(rut);

        assertNotNull(result);
        assertEquals(rut, result.getRut());
        verify(userRepository, times(1)).findByRut(rut);
    }

    @Test
    void testObtenerDescuentoPorCategoria() {
        Long userId = 1L;
        UserEntity user = new UserEntity();
        user.setCategory_frecuency("Muy frecuente");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        double descuento = userService.obtenerDescuentoPorCategoria(userId);

        assertEquals(0.20, descuento);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testObtenerCategoriaCliente() {
        Long userId = 1L;
        UserEntity user = new UserEntity();
        user.setNumberVisits(6);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        String categoria = userService.obtenerCategoriaCliente(userId);

        assertEquals("Frecuente", categoria);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testFindUserById() {
        Long userId = 1L;
        UserEntity user = new UserEntity();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserEntity result = userService.findUserById(userId);

        assertEquals(userId, result.getId());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testUpdateCategoryFrequency() {
        Long userId = 1L;
        UserEntity user = new UserEntity();
        user.setNumberVisits(5);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        UserEntity result = userService.updateCategoryFrequency(userId);

        assertEquals("Frecuente", result.getCategory_frecuency());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateNumberVisits() {
        Long userId = 1L;
        UserEntity user = new UserEntity();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        UserEntity result = userService.updateNumberVisits(userId, 8);

        assertEquals(8, result.getNumberVisits());
        assertEquals("Muy frecuente", result.getCategory_frecuency());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testIncrementVisitsAndUpdateCategory() {
        Long userId = 1L;
        UserEntity user = new UserEntity();
        user.setNumberVisits(6);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        UserEntity result = userService.incrementVisitsAndUpdateCategory(userId);

        assertEquals(7, result.getNumberVisits());
        assertEquals("Muy frecuente", result.getCategory_frecuency());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(user);
    }
}