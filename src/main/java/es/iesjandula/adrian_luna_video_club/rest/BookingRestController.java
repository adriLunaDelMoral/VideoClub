package es.iesjandula.adrian_luna_video_club.rest;

import java.time.LocalDate;
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

/**
 * Controlador REST encargado de gestionar las reservas de películas.
 * Permite crear, eliminar y consultar reservas en el videoclub.
 *
 * Esta clase está mapeada con los endpoints bajo el path "/videoclub/bookings".
 * 
 * @author Adrián
 * @since 2025-11-07
 */
@Slf4j
@RestController
@RequestMapping("/videoclub/bookings")
public class BookingRestController
{
	/** Repositorio para interactuar con la tabla de reservas */
    @Autowired
    private IBookingRepository bookingRepository;

    /** Repositorio para interactuar con la tabla de usuarios */
    @Autowired
    private IUserRepository userRepository;

    /** Repositorio para interactuar con la tabla de películas */
    @Autowired
    private IMovieRepository movieRepository;

    /**
     * Crea una nueva reserva de una película para un usuario.
     *
     * @param bookingRequestDto DTO con información del usuario y película a reservar
     * @return ResponseEntity con el resultado de la operación
     */
    @PostMapping(value = "/", consumes = "application/json")
    public ResponseEntity<?> crearReserva(@RequestBody BookingRequestDto bookingRequestDto)
    {
        try
        {
            Long userId = bookingRequestDto.getUserId();
            Long movieId = bookingRequestDto.getMovieId();

            // Validación de ID de usuario y película
            if (userId == null || movieId == null)
            {
                log.error(Constants.ERR_BOOKING_NOT_FOUND);
                throw new VideoClubException(Constants.ERR_BOOKING_CODE, Constants.ERR_BOOKING_NOT_FOUND);
            }
            // Buscar usuario en la BD
            Optional<User> userOptional = this.userRepository.findById(userId);
            if (!userOptional.isPresent())
            {
                log.error(Constants.ERR_USER_NOT_FOUND);
                throw new VideoClubException(Constants.ERR_USER_NOT_FOUND_CODE, Constants.ERR_USER_NOT_FOUND);
            }
            // Buscar película en la BD
            Optional<Movie> movieOptional = this.movieRepository.findById(movieId);
            if (!movieOptional.isPresent())
            {
                log.error(Constants.ERR_MOVIE_NOT_FOUND);
                throw new VideoClubException(Constants.ERR_MOVIE_CODE, Constants.ERR_MOVIE_NOT_FOUND);
            }
            
            // El stock se guarda en pelicula
            // Comprobar stock disponible
            Movie movie = movieOptional.get() ;
            if (movie.getStock() < 1)
            {
            	log.error(Constants.ERR_MOVIE_NOT_FOUND);
            	throw new VideoClubException(Constants.ERR_MOVIE_CODE, Constants.ERR_MOVIE_NOT_FOUND);
            }
            // Componer ID de la reserva
            BookingId bookingId = new BookingId(userId, movieId);
            Optional<Booking> bookingOptional = this.bookingRepository.findById(bookingId);
            
            // Validar si ya existe una reserva con ese ID
            if (bookingOptional.isPresent())
            {
                log.error(Constants.ERR_BOOKING_MOVIE_NOT_AVAILABLE);
                throw new VideoClubException(Constants.ERR_BOOKING_CODE, Constants.ERR_BOOKING_MOVIE_NOT_AVAILABLE);
            }
   
            // Crear nueva reserva
            Booking booking = new Booking();
            booking.setBookingId(bookingId); 
            booking.setUser(userOptional.get());
            booking.setMovie(movieOptional.get());
            booking.setReview(bookingRequestDto.getReview());
            booking.setFechaAlquiler(LocalDate.now()); 

            this.bookingRepository.saveAndFlush(booking);

            // Reduzco del stock
            movie.setStock(movie.getStock() - 1) ;
            this.movieRepository.saveAndFlush(movie);
            
            log.info(Constants.ELEMENTO_AGREGADO);
            return ResponseEntity.status(208).build();
        }
        catch (VideoClubException exception)
        {
            return ResponseEntity.status(400).body(exception.getBodyExceptionMessage());
        }
        catch (Exception exception)
        {
            log.error(exception.getMessage(), exception);
            return ResponseEntity.status(404).body(new VideoClubException(Constants.GENERIC_CODE, Constants.ERR_BOOKING_NOT_FOUND, exception).getBodyExceptionMessage());
        }
    }

    /**
     * Elimina una reserva existente y devuelve el stock de la película.
     *
     * @param userId ID del usuario
     * @param movieId ID de la película
     * @return ResponseEntity con resultado de la operación
     */
    @DeleteMapping(value = "/{userId}/{movieId}") 
    public ResponseEntity<?> borrarReserva(@PathVariable("userId") Long userId, @PathVariable("movieId") Long movieId)
    {
        try
        {
            BookingId bookingId = new BookingId(userId, movieId); 
            // Validar si existe
            if (!this.bookingRepository.existsById(bookingId))
            {
                log.error(Constants.ERR_BOOKING_NOT_FOUND);
                throw new VideoClubException(Constants.ERR_BOOKING_CODE, Constants.ERR_BOOKING_NOT_FOUND);
            }

            // Elimino reserva
            this.bookingRepository.deleteById(bookingId);
            
            Optional<Movie> movieOptional = this.movieRepository.findById(movieId);
            
            // Buscar película
            if (movieOptional.isPresent())
            {
                Movie movie = movieOptional.get();
                // Incremento stock
                movie.setStock(movie.getStock() + 1);
                
                // Save and flush de película
                this.movieRepository.saveAndFlush(movie);
            } 
            else
            {
            	log.error(Constants.ERR_BOOKING_NOT_FOUND);
            	throw new VideoClubException(Constants.ERR_BOOKING_CODE, Constants.ERR_BOOKING_NOT_FOUND);
            }
            
            log.info(Constants.ELEMENTO_ELIMINADO);
            return ResponseEntity.status(200).build();
        }
        catch (VideoClubException exception)
        {
            return ResponseEntity.status(400).body(exception.getBodyExceptionMessage());
        }
        catch (Exception exception) 
        {
        	VideoClubException videoClubException = new VideoClubException(404,Constants.ERR_BOOKING_NOT_FOUND) ;
        	return ResponseEntity.status(404).body(videoClubException.getBodyExceptionMessage());
		}
    }
    /**
     * Obtiene todas las reservas almacenadas en la base de datos.
     *
     * @return ResponseEntity con la lista de reservas
     */
    @GetMapping(value = "/")
    public ResponseEntity<?> obtenerReservas()
    {
    	try
		{
    		return ResponseEntity.ok().body(this.bookingRepository.buscarReservas());
			
		} 
    	catch (Exception exception) 
    	{
    	    log.error("Error inesperado al eliminar la reserva", exception);

    	    VideoClubException videoClubException =
    	            new VideoClubException(Constants.GENERIC_CODE, "Error interno del servidor.", exception);

    	    return ResponseEntity.status(500).body(videoClubException.getBodyExceptionMessage());
    	}
    }
}