package kartingRM.Backend.Controllers;

import kartingRM.Backend.Entities.ReserveDetailsEntity;
import kartingRM.Backend.Entities.ReserveEntity;
import kartingRM.Backend.Services.ReserveDetailsService;
import kartingRM.Backend.Services.ReserveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reserves")
@CrossOrigin("*")
public class ReserveController {

    @Autowired
    private ReserveService reserveService;

    @Autowired
    private ReserveDetailsService reserveDetailsService;

    @PostMapping("/confirmar")
    public void confirmarReserva(@RequestBody ReserveEntity reserva) {
        if (reserva.getCliente() == null || reserva.getCliente().getId() == null) {
            throw new RuntimeException("El cliente es obligatorio para confirmar la reserva.");
        }

        if (reserva.getDetalles() == null || reserva.getDetalles().isEmpty()) {
            throw new RuntimeException("Debe incluir al menos un detalle en la reserva.");
        }

        for (ReserveDetailsEntity detalle : reserva.getDetalles()) {
            if (detalle.getMemberName() == null || detalle.getDateBirthday() == null) {
                throw new RuntimeException("Cada detalle debe incluir un nombre y una fecha de cumpleaños.");
            }
        }

        reserveService.saveReserve(reserva);
    }

    @GetMapping("/")
    public List<ReserveEntity> getAllReserves() {
        return reserveService.getAllReserves();
    }

    @GetMapping("/{id}")
    public ReserveEntity getReserveById(@PathVariable("id") Long id) {
        return reserveService.getReserveById(id);
    }

    @PostMapping("/")
    public ReserveEntity addReserve(@RequestBody ReserveEntity reserve) {
        return reserveService.saveReserve(reserve);
    }

    @PutMapping("/{id}")
    public ReserveEntity updateReserve(@PathVariable("id") Long id, @RequestBody ReserveEntity reserve) {
        return reserveService.updateReserve(id, reserve);
    }

    @DeleteMapping("/{id}")
    public void deleteReserve(@PathVariable("id") Long id) {
        reserveService.deleteReserve(id);
    }
}