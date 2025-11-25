package es.iesjandula.adrian_luna_video_club.models.ids;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class BookingId implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4578455861416419425L;
	private Long userId; 
    private Long movieId;
}
