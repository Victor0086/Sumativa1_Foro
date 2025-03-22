package com.example.foro.exception;

    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.validation.FieldError;
    import org.springframework.web.bind.MethodArgumentNotValidException;
    import org.springframework.web.bind.annotation.ExceptionHandler;
    import org.springframework.web.bind.annotation.RestControllerAdvice;
    
    import java.util.HashMap;
    import java.util.Map;
    
    @RestControllerAdvice
    public class GlobalExceptionHandler {
    
        private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
            Map<String, String> errors = new HashMap<>();
    
            ex.getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
    
            logger.error("Error de validación: {}", errors);
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
    
        @ExceptionHandler(Exception.class)
        public ResponseEntity<String> handleGlobalExceptions(Exception ex) {
            logger.error("Excepción inesperada: ", ex);
            return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    
        // Método para rastrear el flujo de ejecución y monitorear el sistema
        public static void logInfo(String message) {
            logger.info(message);
        }
    
        public static void logWarning(String message) {
            logger.warn(message);
        }
    
        public static void logError(String message, Exception ex) {
            logger.error(message, ex);
        }
    }
    

