package es.iesjandula.adrian_luna_video_club.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.iesjandula.adrian_luna_video_club.dtos.UserListResponsiveDto;
import es.iesjandula.adrian_luna_video_club.models.User;

public interface IUserRepository extends JpaRepository<User, Long>
{
	@Query(value = "SELECT new es.iesjandula.adrian_luna_video_club.dtos.UserListResponsiveDto(u.userName) " +
		       "FROM User u")
	List<UserListResponsiveDto> buscarUsuarios() ;
}
