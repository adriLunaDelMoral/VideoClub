package es.iesjandula.adrian_luna_video_club.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa a un usuario del videoclub.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User
{
	/** Identificador único del usuario, generado automáticamente */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long userId;

    /** Nombre de usuario único */
    @Column(nullable = false, unique = true)
    private String userName;

    /** Lista de reservas realizadas por este usuario (relación 1-N) */
    @OneToMany(mappedBy = "user")
    private List<Booking> bookings;
}
