package kartingRM.Backend.Services;

import kartingRM.Backend.Entities.ReserveDetailsEntity;
import kartingRM.Backend.Entities.ReserveEntity;
import kartingRM.Backend.Repositories.ReserveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ReserveService {

    @Autowired
    private UserService userService;

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
            int cantidadPersonas = reserve.getCantidad_personas();
            int maxCumpleanos = calcularMaxCumpleanos(cantidadPersonas); 
            int cumpleanosAplicados = 0;
            double montoTotalReserva = 0.0;
            LocalDate fechacumple = reserve.getDetalles().get(0).getDateBirthday();
            System.out.println("kumpleaños: " + fechacumple); // sacar despues
            System.out.println("kumpleaños: " + fechacumple); // sacar despues
    
            for (ReserveDetailsEntity detalle : reserve.getDetalles()) {
                detalle.setReserve(reserve); 
                double descuentoCumpleanos = 0.0;
                if (cumpleanosAplicados < maxCumpleanos && 
                    detalle.getDateBirthday().equals(reserve.getFecha_uso())) {
                    descuentoCumpleanos = 0.50; 
                    cumpleanosAplicados++;
                }

                double descuentoCliente = userService.obtenerDescuentoPorCategoria(detalle.getUserId()); 
                double descuentoGrupo = calcularDescuentoGrupo(cantidadPersonas); 
    
                double descuentoFinal = Math.max(descuentoCumpleanos, Math.max(descuentoCliente, descuentoGrupo));
                detalle.setDiscount(descuentoFinal);

                double tarifaBase = calcularTarifaBase(reserve.getVueltas_o_tiempo(), reserve.getFecha_uso(), cantidadPersonas, detalle.getUserId());
                System.out.println("Tarifa base: " + tarifaBase); // sacar despues
                double montoFinal = tarifaBase * (1 - descuentoFinal);
                detalle.setMontoFinal(montoFinal);
                montoTotalReserva += montoFinal;
            }
    
            reserve.setMontoFinal(montoTotalReserva);
            System.out.println("Tarifa base: " + montoTotalReserva);
        }
        return reserveRepository.save(reserve);
    }

    private int calcularMaxCumpleanos(int cantidadPersonas) {
        if (cantidadPersonas >= 6 && cantidadPersonas <= 10) {
            return 2; 
        } else if (cantidadPersonas >= 3 && cantidadPersonas <= 5) {
            return 1; 
        } else {
            return 0; 
        }
    }

    public ReserveEntity updateReserve(Long id, ReserveEntity reserve) {
        ReserveEntity existingReserve = getReserveById(id);
        existingReserve.setHora_inicio(reserve.getHora_inicio());
        existingReserve.setHora_fin(reserve.getHora_fin());
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

    public double calcularTarifaBase(String vueltasOTiempo, LocalDate fechaUso, int cantidadPersonas, Long userId) {
        double tarifaBase;
        int duracionMaxima;
    
        // Determinar tarifa base y duración máxima
        switch (vueltasOTiempo) {
            case "10 vueltas":
            case "10 minutos":
                tarifaBase = 15000;
                duracionMaxima = 30; // minutos
                break;
            case "15 vueltas":
            case "15 minutos":
                tarifaBase = 20000;
                duracionMaxima = 35; // minutos
                break;
            case "20 vueltas":
            case "20 minutos":
                tarifaBase = 25000;
                duracionMaxima = 40; // minutos
                break;
            default:
                throw new IllegalArgumentException("Vueltas o tiempo no válido");
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