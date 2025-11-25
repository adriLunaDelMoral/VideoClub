package es.iesjandula.adrian_luna_video_club.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieSearchResponsiveDto
{
    private Long movieId;
    private String title;
    private String duration;
}
