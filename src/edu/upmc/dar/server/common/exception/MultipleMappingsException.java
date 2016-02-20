package edu.upmc.dar.server.common.exception;

public class MultipleMappingsException extends Exception {

    public MultipleMappingsException(String message){
        super("Multiple servlet mappings found:\n" + message);
    }
}
