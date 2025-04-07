package kartingRM.Backend.Services;

import kartingRM.Backend.Entities.ReserveDetailsEntity;
import kartingRM.Backend.Entities.ReserveEntity;
import kartingRM.Backend.Repositories.ReserveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    if (reserve.getDetalles() != null) {
        for (ReserveDetailsEntity detalle : reserve.getDetalles()) {
            detalle.setReserve(reserve);
        }
    }

    return reserveRepository.save(reserve);
    }

    public ReserveEntity updateReserve(Long id, ReserveEntity reserve) {
        ReserveEntity existingReserve = getReserveById(id);
        existingReserve.setHora_inicio(reserve.getHora_inicio());
        existingReserve.setHora_fin(reserve.getHora_fin());
        existingReserve.setCliente(reserve.getCliente());
        existingReserve.setCantidad_personas(reserve.getCantidad_personas());
        return reserveRepository.save(existingReserve);
    }

    public void deleteReserve(Long id) {
        reserveRepository.deleteById(id);
    }

    private boolean esFinDeSemana(LocalDate fecha) {
        DayOfWeek dia = fecha.getDayOfWeek();
        return dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY;
    }

    private boolean esDiaFeriado(LocalDate fecha) {
        Set<LocalDate> diasFeriados = Set.of(
            LocalDate.of(2025, 9, 18), // Fiestas Patrias
            LocalDate.of(2025, 12, 25) // Navidad
        ); //dejo pendiente agregar más feriados
        return diasFeriados.contains(fecha);
    } 

    public double calcularTarifaBase(String vueltasOTiempo, LocalDate fechaUso) {
        double tarifaBase;
        switch (vueltasOTiempo) {
            case "10 vueltas":
            case "10 minutos":
                tarifaBase = 15000;
                break;
            case "15 vueltas":
            case "15 minutos":
                tarifaBase = 20000;
                break;
            case "20 vueltas":
            case "20 minutos":
                tarifaBase = 25000;
                break;
            default:
                throw new IllegalArgumentException("Vueltas o tiempo no válido");
        }

        
        if (esFinDeSemana(fechaUso) || esDiaFeriado(fechaUso)) {
            tarifaBase *= 1.2;
        }

        return tarifaBase;
    }

    
    public double calcularDescuentoGrupo(int cantidadPersonas) {
        if (cantidadPersonas >= 11) {
            return 0.30; // 30%
        } else if (cantidadPersonas >= 6) {
            return 0.20; // 20%
        } else if (cantidadPersonas >= 3) {
            return 0.10; // 10%
        } else {
            return 0.0; // 0%
        }
    }
}