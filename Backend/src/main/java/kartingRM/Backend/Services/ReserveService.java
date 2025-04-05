package kartingRM.Backend.Services;

import kartingRM.Backend.Entities.ReserveEntity;
import kartingRM.Backend.Repositories.ReserveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReserveService {

    @Autowired
    private ReserveRepository reserveRepository;

    public List<ReserveEntity> getAllReserves() {
        return reserveRepository.findAll();
    }

    public ReserveEntity getReserveById(Long id) {
        Optional<ReserveEntity> optionalReserve = reserveRepository.findById(id);
        if (optionalReserve.isPresent()) {
            return optionalReserve.get();
        } else {
            throw new RuntimeException("Reserva no encontrada con ID: " + id);
        }
    }

    public ReserveEntity saveReserve(ReserveEntity reserve) {
        return reserveRepository.save(reserve);
    }

    public ReserveEntity updateReserve(Long id, ReserveEntity reserve) {
        ReserveEntity existingReserve = getReserveById(id);
        existingReserve.setFecha_reserva(reserve.getFecha_reserva());
        existingReserve.setHora_inicio(reserve.getHora_inicio());
        existingReserve.setHora_fin(reserve.getHora_fin());
        existingReserve.setCliente(reserve.getCliente());
        existingReserve.setCantidad_personas(reserve.getCantidad_personas());
        existingReserve.setEstado(reserve.getEstado());
        existingReserve.setMonto_total(reserve.getMonto_total());
        existingReserve.setDescuento_aplicado(reserve.getDescuento_aplicado());
        return reserveRepository.save(existingReserve);
    }

    public void deleteReserve(Long id) {
        reserveRepository.deleteById(id);
    }
}