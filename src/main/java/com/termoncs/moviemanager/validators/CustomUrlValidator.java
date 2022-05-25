package com.termoncs.moviemanager.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CustomUrlValidator implements ConstraintValidator<CustomUrlConstraint, String> {
   @Override
   public void initialize(CustomUrlConstraint constraint) {
   }

   @Override
   public boolean isValid(String url, ConstraintValidatorContext context) {
      if ((url == null ) || (url.length() == 0)) {
         return true;
      }
      try {
            (new java.net.URL(url)).openStream().close();
            return true;
      } catch (Exception ex) { }
      return false;
   }
}
