package es.iesjandula.adrian_luna_video_club.rest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.iesjandula.adrian_luna_video_club.dtos.BookingRequestDto;
import es.iesjandula.adrian_luna_video_club.models.Booking;
import es.iesjandula.adrian_luna_video_club.models.Movie;
import es.iesjandula.adrian_luna_video_club.models.User;
import es.iesjandula.adrian_luna_video_club.models.ids.BookingId;
import es.iesjandula.adrian_luna_video_club.repository.IBookingRepository;
import es.iesjandula.adrian_luna_video_club.repository.IMovieRepository;
import es.iesjandula.adrian_luna_video_club.repository.IUserRepository;
import es.iesjandula.adrian_luna_video_club.utils.Constants;
import es.iesjandula.adrian_luna_video_club.utils.VideoClubException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/videoclub/bookings")
public class BookingRestController
{
    @Autowired
    private IBookingRepository bookingRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IMovieRepository movieRepository;

    @PostMapping(value = "/", consumes = "application/json")
    public ResponseEntity<?> crearReserva(@RequestBody BookingRequestDto bookingRequestDto)
    {
        try
        {
            Long userId = bookingRequestDto.getUserId();
            Long movieId = bookingRequestDto.getMovieId();

            if (userId == null || movieId == null)
            {
                log.error(Constants.ERR_BOOKING_NOT_FOUND);
                throw new VideoClubException(Constants.ERR_BOOKING_CODE, Constants.ERR_BOOKING_NOT_FOUND);
            }

            Optional<User> userOptional = this.userRepository.findById(userId);
            if (!userOptional.isPresent())
            {
                log.error(Constants.ERR_USER_NOT_FOUND);
                throw new VideoClubException(Constants.ERR_USER_CODE, Constants.ERR_USER_NOT_FOUND);
            }

            Optional<Movie> movieOptional = this.movieRepository.findById(movieId);
            if (!movieOptional.isPresent())
            {
                log.error(Constants.ERR_MOVIE_NOT_FOUND);
                throw new VideoClubException(Constants.ERR_MOVIE_CODE, Constants.ERR_MOVIE_NOT_FOUND);
            }
            
            // El stock se guarda en pelicula
            Movie movie = movieOptional.get() ;
            if (movie.getStock() < 1)
            {
            	// No hay películas disponibles de este tipo
            }

            BookingId bookingId = new BookingId(userId, movieId);
            Optional<Booking> bookingOptional = this.bookingRepository.findById(bookingId);
            if (bookingOptional.isPresent())
            {
                log.error(Constants.ERR_BOOKING_MOVIE_NOT_AVAILABLE);
                throw new VideoClubException(Constants.ERR_BOOKING_CODE, Constants.ERR_BOOKING_MOVIE_NOT_AVAILABLE);
            }
   
            // Reservo la película
            Booking booking = new Booking();
            booking.setBookingId(bookingId); 
            booking.setUser(userOptional.get());
            booking.setMovie(movieOptional.get());
            booking.setFechaAlquiler(LocalDate.now()); 

            this.bookingRepository.saveAndFlush(booking);

            // Reduzco del stock
            movie.setStock(movie.getStock() - 1) ;
            this.movieRepository.saveAllAndFlush(movie) ;
            
            log.info(Constants.ELEMENTO_AGREGADO);
            return ResponseEntity.status(208).build();
        }
        catch (VideoClubException exception)
        {
            return ResponseEntity.badRequest().body(exception.getBodyExceptionMessage());
        }
        catch (Exception exception)
        {
            log.error(exception.getMessage(), exception);
            return ResponseEntity.badRequest().body(new VideoClubException(Constants.GENERIC_CODE, Constants.ERR_BOOKING_NOT_FOUND, exception).getBodyExceptionMessage());
        }
    }

    @DeleteMapping(value = "/{userId}/{movieId}") 
    public ResponseEntity<?> borrarReserva(@PathVariable("userId") Long userId, @PathVariable("movieId") Long movieId)
    {
        try
        {
            BookingId bookingId = new BookingId(userId, movieId); 

            if (!this.bookingRepository.existsById(bookingId))
            {
                log.error(Constants.ERR_BOOKING_NOT_FOUND);
                throw new VideoClubException(Constants.ERR_BOOKING_CODE, Constants.ERR_BOOKING_NOT_FOUND);
            }

            // Elimino reserva
            this.bookingRepository.deleteById(bookingId);
            
            // Incremento stock
            
            // Buscar película
            
            // Incrementar stock
            
            // Save and flush de película

            log.info(Constants.ELEMENTO_ELIMINADO);
            return ResponseEntity.ok().build();
        }
        catch (VideoClubException exception)
        {
            return ResponseEntity.badRequest().body(exception.getBodyExceptionMessage());
        }
    }

    @GetMapping(value = "/")
    public ResponseEntity<?> obtenerReservas()
    {
        List<Booking> bookings = this.bookingRepository.findAll();
        return ResponseEntity.ok().body(bookings);
    }
}