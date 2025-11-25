package es.iesjandula.adrian_luna_video_club.utils;

public class Constants
{
    /* Errores relacionados con películas */
    public static final Integer ERR_MOVIE_CODE = 1;
    public static final String ERR_MOVIE_NOT_FOUND = "La película no fue encontrada";
    public static final String ERR_MOVIE_ALREADY_EXISTS = "La película ya existe";
    public static final String ERR_MOVIE_EMPTY = "Los datos de la película están vacíos o son nulos";

    /* Errores relacionados con usuarios */
    public static final Integer ERR_USER_CODE = 2;
    public static final String ERR_USER_NOT_FOUND = "El usuario no fue encontrado";
    public static final String ERR_USER_ALREADY_EXISTS = "El usuario ya existe";
    public static final String ERR_USER_EMPTY = "Los datos del usuario están vacíos o son nulos";

    /* Errores relacionados con reservas */
    public static final Integer ERR_BOOKING_CODE = 3;
    public static final String ERR_BOOKING_MOVIE_NOT_AVAILABLE = "La película ya está reservada";
    public static final String ERR_BOOKING_NOT_FOUND = "La reserva no fue encontrada";

    /* Mensajes generales */
    public static final Integer GENERIC_CODE = 99; 
    public static final String ELEMENTO_AGREGADO = "Elemento agregado correctamente";
    public static final String ELEMENTO_MODIFICADO = "Elemento modificado correctamente";
    public static final String ELEMENTO_ELIMINADO = "Elemento eliminado correctamente";
}
