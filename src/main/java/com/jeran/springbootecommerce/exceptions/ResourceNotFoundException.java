package com.jeran.springbootecommerce.exceptions;

    public class ResourceNotFoundException extends RuntimeException {
        String field;
        String fieldName;
        Long fieldId;
        String resourceName;

        public ResourceNotFoundException() {
        }

        public ResourceNotFoundException( String resourceName, String field, String fieldName) {
            super(String.format("%s not found with %s : %s",resourceName,field,fieldName));
            this.fieldName = fieldName;
            this.field = field;
            this.resourceName = resourceName;
        }

        public ResourceNotFoundException( String resourceName,String field, Long fieldId) {
            super(String.format("%s not found with %s : %s",resourceName,field,fieldId));
            this.field = field;
            this.fieldId = fieldId;
            this.resourceName = resourceName;
        }
    }



