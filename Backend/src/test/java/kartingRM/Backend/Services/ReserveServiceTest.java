package kartingRM.Backend.Services;

import kartingRM.Backend.Entities.ReserveDetailsEntity;
import kartingRM.Backend.Entities.ReserveEntity;
import kartingRM.Backend.Entities.UserEntity;
import kartingRM.Backend.Repositories.ReserveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReserveServiceTest {

    @Mock
    private ReserveRepository reserveRepository;

    @Mock
    private UserService userService;
    @InjectMocks
    private ReserveService reserveService;

    @Mock
    private JavaMailSender mailSender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reserveService = Mockito.spy(reserveService);
    }

    @Test
    void testGetReporteIngresosPorVueltasOTiempo() {
        
        List<ReserveEntity> reservas = new ArrayList<>();
        ReserveEntity reserva1 = new ReserveEntity();
        reserva1.setDetalles(new ArrayList<>(List.of(new ReserveDetailsEntity(), new ReserveDetailsEntity())));
        reserva1.setFecha_uso(LocalDate.of(2024, 1, 15));
        reserva1.setVueltas_o_tiempo("10 vueltas");
        reserva1.setMontoFinal(4000.0);
        reservas.add(reserva1);

        when(reserveRepository.findAll()).thenReturn(reservas);

        
        var reporte = reserveService.getReporteIngresosPorVueltasOTiempo(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 31)
        );

        
        assertEquals(4000.0, reporte.get("10 vueltas o máx 10 min").get("Enero"));
        assertEquals(4000.0, reporte.get("10 vueltas o máx 10 min").get("TOTAL"));
    }
    @Test
    void testGetReporteIngresosPorCantidadDePersonas() {
        ReserveEntity reserva = new ReserveEntity();
        reserva.setFecha_uso(LocalDate.of(2024, 1, 15));
        reserva.setDetalles(new ArrayList<>(List.of(new ReserveDetailsEntity())));
        reserva.getDetalles().get(0).setMontoFinal(3000.0);

        when(reserveRepository.findAll()).thenReturn(List.of(reserva));

        var reporte = reserveService.getReporteIngresosPorCantidadDePersonas(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 31)
        );

        assertEquals(3000.0, reporte.get("1-2 personas").get("Enero"));
    }
    @Test
    void testUpdateReserve() {
        Long id = 1L;
        ReserveEntity existingReserva = new ReserveEntity();
        existingReserva.setId(id);
        existingReserva.setMontoFinal(4000.0);

        ReserveEntity updatedReserva = new ReserveEntity();
        updatedReserva.setMontoFinal(6000.0);

        when(reserveRepository.findById(id)).thenReturn(java.util.Optional.of(existingReserva));
        when(reserveRepository.save(existingReserva)).thenReturn(existingReserva);

        ReserveEntity result = reserveService.updateReserve(id, updatedReserva);

        assertEquals(6000.0, result.getMontoFinal());
    }
    @Test
    void testDeleteReserve() {
        Long id = 1L;

        reserveService.deleteReserve(id);

        Mockito.verify(reserveRepository).deleteById(id);
    }
    @Test
    void testGetReserveById() {
        Long id = 1L;
        ReserveEntity reserva = new ReserveEntity();
        reserva.setId(id);

        when(reserveRepository.findById(id)).thenReturn(java.util.Optional.of(reserva));

        ReserveEntity result = reserveService.getReserveById(id);

        assertEquals(id, result.getId());
    }
    @Test
    void testGetAllReserves() {
        List<ReserveEntity> reservas = new ArrayList<>();
        reservas.add(new ReserveEntity());
        reservas.add(new ReserveEntity());

        when(reserveRepository.findAll()).thenReturn(reservas);

        List<ReserveEntity> result = reserveService.getAllReserves();

        assertEquals(2, result.size());
    }
    @Test
    void testCalcularTarifaBase() {
        double tarifa10Vueltas = reserveService.calcularTarifaBase("10 vueltas");
        double tarifa15Vueltas = reserveService.calcularTarifaBase("15 vueltas");
        double tarifa20Vueltas = reserveService.calcularTarifaBase("20 vueltas");

        assertEquals(15000, tarifa10Vueltas);
        assertEquals(20000, tarifa15Vueltas);
        assertEquals(25000, tarifa20Vueltas);
    }
    @Test
    void testCalcularDescuentoGrupo() {
        double descuento1Persona = reserveService.calcularDescuentoGrupo(1);
        double descuento5Personas = reserveService.calcularDescuentoGrupo(5);
        double descuento7Personas = reserveService.calcularDescuentoGrupo(7);
        double descuento12Personas = reserveService.calcularDescuentoGrupo(12);

        assertEquals(0.0, descuento1Persona);
        assertEquals(0.10, descuento5Personas);
        assertEquals(0.20, descuento7Personas);
        assertEquals(0.30, descuento12Personas);
    }
}