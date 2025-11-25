package es.iesjandula.adrian_luna_video_club.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.iesjandula.adrian_luna_video_club.models.Movie;

public interface IMovieRepository extends JpaRepository<Movie, Long>
{

}
