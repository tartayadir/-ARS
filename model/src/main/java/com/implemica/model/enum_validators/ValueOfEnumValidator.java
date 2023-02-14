package com.implemica.model.enum_validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class for validation of enum elements presented as string.
 * Provides a method to initialize the list of valid values and validate the validated value.
 */
public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, String> {

    /**
     * List of valid string values to be used for validation.
     */
    private List<String> acceptedValues;

    /**
     * Gets a list of all additional string values for the installed
     * enum class and assigns a list with which to check the values
     * when validating.
     *
     * @param annotation annotation instance for a given constraint declaration
     */
    @Override
    public void initialize(ValueOfEnum annotation) {
        acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    /**
     * Verifies that the string provided is a valid value for enum.
     *
     * @param value object to validate
     * @param context context in which the constraint is evaluated
     *
     * @return result of the verification
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return acceptedValues.contains(value);
    }
}