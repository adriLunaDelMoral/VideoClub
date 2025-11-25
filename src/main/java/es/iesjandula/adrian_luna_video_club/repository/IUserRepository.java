package es.iesjandula.adrian_luna_video_club.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.iesjandula.adrian_luna_video_club.models.User;

public interface IUserRepository extends JpaRepository<User, Long>
{

}
