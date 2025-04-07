package kartingRM.Backend.Services;

import kartingRM.Backend.Entities.ReserveDetailsEntity;
import kartingRM.Backend.Repositories.ReserveDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReserveDetailsService {

    @Autowired
    private ReserveDetailsRepository reserveDetailsRepository;

    public List<ReserveDetailsEntity> getAllReserveDetails() {
        return reserveDetailsRepository.findAll();
    }

    public ReserveDetailsEntity getReserveDetailById(Long id) {
        Optional<ReserveDetailsEntity> optionalDetail = reserveDetailsRepository.findById(id);
        if (optionalDetail.isPresent()) {
            return optionalDetail.get();
        } else {
            throw new RuntimeException("Detalle de reserva no encontrado con ID: " + id);
        }
    }

    public ReserveDetailsEntity saveReserveDetail(ReserveDetailsEntity reserveDetail) {
        return reserveDetailsRepository.save(reserveDetail);
    }
    
    public ReserveDetailsEntity updateReserveDetail(Long id, ReserveDetailsEntity updatedDetail) {
        ReserveDetailsEntity existingDetail = getReserveDetailById(id);
        existingDetail.setMemberName(updatedDetail.getMemberName());
        existingDetail.setDateBirthday(updatedDetail.getDateBirthday());
        existingDetail.setDiscount(updatedDetail.getDiscount());
        return reserveDetailsRepository.save(existingDetail);
    }

    public void deleteReserveDetail(Long id) {
        reserveDetailsRepository.deleteById(id);
    }

    // Calcular descuento por cliente frecuente
    public double calcularDescuentoFrecuente(String categoriaFrecuencia) {
        switch (categoriaFrecuencia) {
            case "Muy frecuente":
                return 0.30; // 30%
            case "Frecuente":
                return 0.20; // 20%
            case "Regular":
                return 0.10; // 10%
            default:
                return 0.0; // 0%
        }
    }

    // Aplicar descuento por cumpleaños
    public void aplicarDescuentoCumpleanos(List<ReserveDetailsEntity> detalles, LocalDate fechaUso) {
        int cantidadPersonas = detalles.size();
        int limiteDescuento = 0;
    
    
        if (cantidadPersonas >= 3 && cantidadPersonas <= 5) {
            limiteDescuento = 1;
        } else if (cantidadPersonas >= 6 && cantidadPersonas <= 10) {
            limiteDescuento = 2;
        } else if (cantidadPersonas >= 11) {
            limiteDescuento = 3;
        }
    
        int descuentosAplicados = 0;
        for (ReserveDetailsEntity detalle : detalles) {
            if (detalle.isCumpleanos(fechaUso) && descuentosAplicados < limiteDescuento) {
                detalle.setDiscount(0.50); 
                descuentosAplicados++;
            }
        }
    }
}