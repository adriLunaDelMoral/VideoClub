package es.iesjandula.adrian_luna_video_club.rest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.iesjandula.adrian_luna_video_club.dtos.MovieRequestDto;
import es.iesjandula.adrian_luna_video_club.models.Movie;
import es.iesjandula.adrian_luna_video_club.repository.IMovieRepository;
import es.iesjandula.adrian_luna_video_club.utils.Constants;
import es.iesjandula.adrian_luna_video_club.utils.VideoClubException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/videoclub/movies")
public class MovieRestController
{
    @Autowired
    private IMovieRepository movieRepository;

    @PostMapping(value = "/", consumes = "application/json")
    public ResponseEntity<?> crearPelicula(@RequestBody MovieRequestDto movieRequestDto)
    {
        try
        {
            if (movieRequestDto.getTitle() == null || movieRequestDto.getTitle().isEmpty())
            {
                log.error(Constants.ERR_MOVIE_EMPTY);
                throw new VideoClubException(Constants.ERR_MOVIE_CODE, Constants.ERR_MOVIE_EMPTY);
            }

            if (movieRequestDto.getMovieId() != null && this.movieRepository.existsById(movieRequestDto.getMovieId()))
            {
                log.error(Constants.ERR_MOVIE_ALREADY_EXISTS);
                throw new VideoClubException(Constants.ERR_MOVIE_CODE, Constants.ERR_MOVIE_ALREADY_EXISTS);
            }

            Movie movie = new Movie();
            movie.setMovieId(movieRequestDto.getMovieId());
            movie.setTitle(movieRequestDto.getTitle());
            movie.setDuration(movieRequestDto.getDuration());
            

            this.movieRepository.saveAndFlush(movie);

            log.info(Constants.ELEMENTO_AGREGADO);
            return ResponseEntity.status(200).build();
        }
        catch (VideoClubException exception)
        {
            return ResponseEntity.status(400).body(exception.getBodyExceptionMessage());
        }
    }

    @PutMapping(value = "/", consumes = "application/json")
    public ResponseEntity<?> modificarPelicula(@RequestBody MovieRequestDto movieRequestDto)
    {
        try
        {
            if (movieRequestDto.getMovieId() == null)
            {
                log.error(Constants.ERR_MOVIE_EMPTY);
                throw new VideoClubException(Constants.ERR_MOVIE_CODE, Constants.ERR_MOVIE_EMPTY);
            }

            Optional<Movie> movieOptional = this.movieRepository.findById(movieRequestDto.getMovieId());
            if (!movieOptional.isPresent())
            {
                log.error(Constants.ERR_MOVIE_NOT_FOUND);
                throw new VideoClubException(Constants.ERR_MOVIE_CODE, Constants.ERR_MOVIE_NOT_FOUND);
            }

            Movie movie = movieOptional.get();
            movie.setTitle(movieRequestDto.getTitle());
            movie.setDuration(movieRequestDto.getDuration());

            this.movieRepository.saveAndFlush(movie);

            log.info(Constants.ELEMENTO_MODIFICADO);
            return ResponseEntity.ok().build();
        }
        catch (VideoClubException exception)
        {
            return ResponseEntity.badRequest().body(exception.getBodyExceptionMessage());
        }
    }

    @DeleteMapping(value = "/{movieId}")
    public ResponseEntity<?> borrarPelicula(@PathVariable("movieId") Long movieId)
    {
        try
        {
            if (!this.movieRepository.existsById(movieId))
            {
                log.error(Constants.ERR_MOVIE_NOT_FOUND);
                throw new VideoClubException(Constants.ERR_MOVIE_CODE, Constants.ERR_MOVIE_NOT_FOUND);
            }

            this.movieRepository.deleteById(movieId);

            log.info(Constants.ELEMENTO_ELIMINADO);
            return ResponseEntity.status(200).build();
        }
        catch (VideoClubException exception)
        {
            return ResponseEntity.status(400).body(exception.getBodyExceptionMessage());
        }
    }

    @GetMapping(value = "/")
    public ResponseEntity<?> obtenerPeliculas()
    {
        
        return ResponseEntity.status(200).body(this.movieRepository.buscarPeliculas());
    }
}
