package kartingRM.Backend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "reserves")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReserveEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
}