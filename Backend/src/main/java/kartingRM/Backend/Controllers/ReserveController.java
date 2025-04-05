package kartingRM.Backend.Controllers;

import kartingRM.Backend.Entities.ReserveEntity;
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