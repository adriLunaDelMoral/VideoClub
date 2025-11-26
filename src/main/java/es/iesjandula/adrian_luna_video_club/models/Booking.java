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
public class Booking
{
	@EmbeddedId
    private BookingId bookingId;

    @ManyToOne
    @MapsId("userId") 
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("movieId")
    @JoinColumn(name = "movie_id")
    private Movie movie;
    
    @Column
    private LocalDate fechaAlquiler;
    
    @Column
    private String review;
    

}