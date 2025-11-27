package es.iesjandula.adrian_luna_video_club.models;

import java.time.LocalDate;

import es.iesjandula.adrian_luna_video_club.models.ids.BookingId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "booking") 
/**
 * Entidad que representa una reserva/alquiler de una película por parte
 * de un usuario. Utiliza una clave primaria compuesta contenida en BookingId.
 */
public class Booking
{
	/** Clave primaria compuesta formada por userId y movieId */
	@EmbeddedId
    private BookingId bookingId;

	/** Relación muchos-a-uno con User, usando su ID como parte de la clave compuesta */
    @ManyToOne
    @MapsId("userId") 
    @JoinColumn(name = "user_id")
    private User user;

    /** Relación muchos-a-uno con Movie, usando su ID como parte de la clave compuesta */
    @ManyToOne
    @MapsId("movieId")
    @JoinColumn(name = "movie_id")
    private Movie movie;
    
    /** Fecha en que se realiza el alquiler */
    @Column
    private LocalDate fechaAlquiler;
    
    /** Reseña opcional que el usuario deja sobre la película */
    @Column
    private String review;
    

}