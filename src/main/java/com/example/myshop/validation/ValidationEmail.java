package com.example.myshop.validation;

import com.example.myshop.constant.Constant;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = ValidationEmail.EmailValidation.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.FIELD})
public @interface ValidationEmail {
    String message() default Constant.InvalidMessageException.INVALID_EMAIL;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class EmailValidation implements ConstraintValidator<ValidationEmail, String> {

        @Override
        public void initialize(ValidationEmail constraintAnnotation) {
        }

        @Override
        public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
            String regexEmail = "^[a-zA-Z]+[a-z0-9]*@{1}[A-Za-z]{1}+mail.com$";
            return email.matches(regexEmail);
        }
    }
}
