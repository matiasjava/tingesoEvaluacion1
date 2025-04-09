package kartingRM.Backend.Controllers;

import kartingRM.Backend.Entities.ReserveDetailsEntity;
import kartingRM.Backend.Entities.ReserveEntity;
import kartingRM.Backend.Services.ReserveDetailsService;
import kartingRM.Backend.Services.ReserveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public List<Map<String, String>> getAllReserves() {
        return reserveService.getAllReserves().stream().map(reserve -> {
            Map<String, String> formattedReserve = new HashMap<>();
            formattedReserve.put("fecha_reserva", reserve.getFecha_uso().toString()); // Ajusta según el nombre real del campo
            formattedReserve.put("hora_inicio", reserve.getHora_inicio()); // Ajusta según el nombre real del campo
            formattedReserve.put("hora_fin", reserve.getHora_fin()); // Ajusta según el nombre real del campo
            return formattedReserve;
        }).collect(Collectors.toList());
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