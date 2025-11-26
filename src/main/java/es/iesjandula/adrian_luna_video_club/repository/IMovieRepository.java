package es.iesjandula.adrian_luna_video_club.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.iesjandula.adrian_luna_video_club.dtos.MovieListResposiveDto;
import es.iesjandula.adrian_luna_video_club.models.Movie;

public interface IMovieRepository extends JpaRepository<Movie, Long>
{
	@Query(value = "SELECT new es.iesjandula.adrian_luna_video_club.dtos.MovieListResposiveDto(m.title, m.duration, m.stock) " +
		       "FROM Movie m")
	List<MovieListResposiveDto> buscarPeliculas() ;
}
