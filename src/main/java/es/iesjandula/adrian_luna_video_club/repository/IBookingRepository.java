package es.iesjandula.adrian_luna_video_club.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.iesjandula.adrian_luna_video_club.models.Booking;
import es.iesjandula.adrian_luna_video_club.models.ids.BookingId;

public interface IBookingRepository extends JpaRepository<Booking, BookingId>
{

}
