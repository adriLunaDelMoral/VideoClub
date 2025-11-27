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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movies")
/**
 * Entidad que representa una película dentro del videoclub.
 */
public class Movie
{
	/** Identificador único de la película, generado automáticamente */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long movieId;

	/** Título de la película */
	@Column(nullable = false)
	private String title;
	
	/** Duración en formato texto (por ejemplo: "2h 15m") */
	@Column(nullable = false)
	private String duration;
	
	/** Cantidad de ejemplares disponibles en stock */
	@Column(nullable = false)
	private Long stock;
	
	
	/** Lista de reservas en las que aparece esta película (relación 1-N) */
	@OneToMany(mappedBy = "movie")
    private List<Booking> bookings;
}
