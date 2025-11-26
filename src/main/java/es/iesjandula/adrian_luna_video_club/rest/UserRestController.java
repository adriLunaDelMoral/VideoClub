package es.iesjandula.adrian_luna_video_club.rest;

import java.util.List;
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

import es.iesjandula.adrian_luna_video_club.dtos.UserRequestDto;
import es.iesjandula.adrian_luna_video_club.models.User;
import es.iesjandula.adrian_luna_video_club.repository.IUserRepository;
import es.iesjandula.adrian_luna_video_club.utils.Constants;
import es.iesjandula.adrian_luna_video_club.utils.VideoClubException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/videoclub/users")
public class UserRestController
{
    @Autowired
    private IUserRepository userRepository;

    @PostMapping(value = "/", consumes = "application/json")
    public ResponseEntity<?> crearUsuario(@RequestBody UserRequestDto userRequestDto)
    {
        try
        {
            if (userRequestDto.getUserName() == null || userRequestDto.getUserName().isEmpty())
            {
                log.error(Constants.ERR_USER_EMPTY);
                throw new VideoClubException(Constants.ERR_USER_EMPTY_CODE, Constants.ERR_USER_EMPTY);
            }

            if (userRequestDto.getUserId() != null && this.userRepository.existsById(userRequestDto.getUserId()))
            {
                log.error(Constants.ERR_USER_ALREADY_EXISTS);
                throw new VideoClubException(Constants.ERR_USER_ALREADY_EXISTS_CODE, Constants.ERR_USER_ALREADY_EXISTS);
            }

            User user = new User();
            user.setUserName(userRequestDto.getUserName());
           

            this.userRepository.saveAndFlush(user);

            log.info(Constants.ELEMENTO_AGREGADO);
            return ResponseEntity.ok().build();
        }
        catch (VideoClubException exception)
        {
        	int responseCode = -1 ;
        	if (exception.getCodigo() == Constants.ERR_USER_EMPTY_CODE)
        	{
        		responseCode = 401 ;
        	}
        	else
        	{
        		responseCode = 402 ;
        	}
        	
        	return ResponseEntity.status(responseCode).body(exception.getBodyExceptionMessage());
        }
        catch (Exception exception)
        {
            log.error("Error al crear usuario: " + exception.getMessage());
            
            VideoClubException videoClubException = new VideoClubException(Constants.GENERIC_CODE, "Error al guardar el usuario.");
            		
            return ResponseEntity.status(500).body(videoClubException.getBodyExceptionMessage());
        }
    }

    @PutMapping(value = "/", consumes = "application/json")
    public ResponseEntity<?> modificarUsuario(@RequestBody UserRequestDto userRequestDto)
    {
        try
        {
            if (userRequestDto.getUserId() == null)
            {
                log.error(Constants.ERR_USER_EMPTY);
                throw new VideoClubException(Constants.ERR_USER_CODE, Constants.ERR_USER_EMPTY);
            }

            Optional<User> userOptional = this.userRepository.findById(userRequestDto.getUserId());
            if (!userOptional.isPresent())
            {
                log.error(Constants.ERR_USER_NOT_FOUND);
                throw new VideoClubException(Constants.ERR_USER_CODE, Constants.ERR_USER_NOT_FOUND);
            }

            User user = userOptional.get();
            if (userRequestDto.getUserName() != null && !userRequestDto.getUserName().isEmpty()) {
                user.setUserName(userRequestDto.getUserName());
            }

            this.userRepository.saveAndFlush(user);

            log.info(Constants.ELEMENTO_MODIFICADO);
            return ResponseEntity.ok().build();
        }
        catch (VideoClubException exception)
        {
            return ResponseEntity.badRequest().body(exception.getBodyExceptionMessage());
        }
    }

    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<?> borrarUsuario(@PathVariable("userId") Long userId)
    {
        try
        {
            if (!this.userRepository.existsById(userId))
            {
                log.error(Constants.ERR_USER_NOT_FOUND);
                throw new VideoClubException(Constants.ERR_USER_CODE, Constants.ERR_USER_NOT_FOUND);
            }

            this.userRepository.deleteById(userId);

            log.info(Constants.ELEMENTO_ELIMINADO);
            return ResponseEntity.ok().build();
        }
        catch (VideoClubException exception)
        {
            return ResponseEntity.badRequest().body(exception.getBodyExceptionMessage());
        }
    }

    @GetMapping(value = "/")
    public ResponseEntity<?> obtenerUsuarios()
    {
        List<User> users = this.userRepository.findAll();
        log.info("Consulta de todos los usuarios realizada.");
        return ResponseEntity.ok().body(users);
    }
    
    
    
    
}