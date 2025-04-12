package kartingRM.Backend.Controllers;

import kartingRM.Backend.Entities.ReserveDetailsEntity;
import kartingRM.Backend.Entities.ReserveEntity;
import kartingRM.Backend.Services.ReserveDetailsService;
import kartingRM.Backend.Services.ReserveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> confirmarReserva(@RequestBody ReserveEntity reserva) {
        if (reserva.getDetalles() == null || reserva.getDetalles().isEmpty()) {
            return ResponseEntity.badRequest().body("La reserva debe incluir al menos un detalle.");
        }

        for (ReserveDetailsEntity detalle : reserva.getDetalles()) {
            if (detalle.getUserId() == null) {
                return ResponseEntity.badRequest().body("Cada detalle debe incluir un userId válido.");
            }
        }

        try {
            ReserveEntity savedReserva = reserveService.saveReserve(reserva);
            return ResponseEntity.ok(savedReserva);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al confirmar la reserva.");
        }
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