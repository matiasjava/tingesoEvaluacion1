package kartingRM.Backend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "reserves")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReserveEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String codigo_reserva;

    private LocalDate fecha_uso;

    private String hora_inicio;

    private String hora_fin;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private UserEntity cliente;

    private int cantidad_personas;

    private String vueltas_o_tiempo;
    
    private double montoFinal;

    @OneToMany(mappedBy = "reserve", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReserveDetailsEntity> detalles;




    // para generar el codigo de reserva aleatorio
    @PrePersist
    private void generateCodigoReserva() {
        if (this.codigo_reserva == null || this.codigo_reserva.isEmpty()) {
            this.codigo_reserva = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
    }
}