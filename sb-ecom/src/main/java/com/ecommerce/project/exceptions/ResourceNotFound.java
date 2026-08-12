package com.ecommerce.project.exceptions;

public class ResourceNotFound extends RuntimeException{

    String ResourceName;
    String field;
    String filedName;
    Long fieldID;

    public ResourceNotFound(String resourceName, String field, String filedName) {
        super(String.format("%s not found with %s: %s ", resourceName,field,filedName));
        ResourceName = resourceName;
        this.field = field;
        this.filedName = filedName;
    }

    public ResourceNotFound(String resourceName, String field, Long fieldID) {
        super(String.format("%s not found with %s: %d ", resourceName,field,fieldID));
        ResourceName = resourceName;
        this.field = field;
        this.fieldID = fieldID;
    }
}
