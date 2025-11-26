package es.iesjandula.adrian_luna_video_club.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.iesjandula.adrian_luna_video_club.dtos.BookingListResponsiveDto;
import es.iesjandula.adrian_luna_video_club.models.Booking;
import es.iesjandula.adrian_luna_video_club.models.ids.BookingId;

public interface IBookingRepository extends JpaRepository<Booking, BookingId>
{
	@Query(value = "SELECT new es.iesjandula.adrian_luna_video_club.dtos.BookingListResponsiveDto(b.movie.title, b.user.userName) " +
		       "FROM Booking b")
	List<BookingListResponsiveDto> buscarReservas() ;
}


