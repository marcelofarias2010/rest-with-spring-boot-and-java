package br.com.farias.rest_with_spring_boot_and_java.exception;

public class BadRquestException extends RuntimeException {

    public BadRquestException(){
        super("Unsupported file extension!");
    }
    public BadRquestException(String message) {
        super(message);
    }
}
