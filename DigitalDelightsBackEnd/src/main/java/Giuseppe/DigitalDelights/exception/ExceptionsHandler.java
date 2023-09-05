package Giuseppe.DigitalDelights.exception;

import java.util.Date;
import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorsPayloadWithList handleValidationErrors(MethodArgumentNotValidException e) {
		List<String> errors = e.getBindingResult().getFieldErrors().stream().map(error -> error.getDefaultMessage())
				.toList();

		return new ErrorsPayloadWithList("Errore Nella Struttura del payload", new Date(), errors);
	}

	@ExceptionHandler(BadRequestException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorsPayload handleBadRequest(BadRequestException e) {
		return new ErrorsPayload(e.getMessage(), new Date());
	}

	@ExceptionHandler(UnauthorizedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ErrorsPayload handleUnauthorized(UnauthorizedException e) {
		return new ErrorsPayload(e.getMessage(), new Date());
	}

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ErrorsPayload handleForbidden(AccessDeniedException e) {
		return new ErrorsPayload("Non hai accesso a questo endpoint", new Date());
	}

	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorsPayload handleNotFound(NotFoundException e) {
		return new ErrorsPayload(e.getMessage(), new Date());
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorsPayload handleGeneric(Exception e) {
		return new ErrorsPayload("Qualcosa è andato storto, Proveremo a risolvere nel minor tempo possibile",
				new Date());
	}
}
