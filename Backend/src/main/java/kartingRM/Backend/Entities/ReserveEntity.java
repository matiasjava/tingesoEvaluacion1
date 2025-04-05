package kartingRM.Backend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "reserves")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ReserveEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha_reserva; //Dejo pendiente el tipo de dato de la fecha y hora, ya que no se si es LocalDate o LocalDateTime o Date.

    private LocalTime hora_inicio; 

    private LocalTime hora_fin; 

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private UserEntity cliente; 

    private int cantidad_personas;

    private String estado;

    private double monto_total; 

    private double descuento_aplicado;
}
