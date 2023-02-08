package com.docusign.core.common;

import java.lang.reflect.Field;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import lombok.Value;


/**
 * This class represents 'diff' between values of two objects. The 'diff' is a
 * simple object consists field name and fields value of comparing objects. The
 * value is represented as a string.
 */
@Value
public class DiffField {

    private String name;
    private String leftValue;
    private String rightValue;

   

	/**
     * Creates an instance of the DiffField by a {@link Field} and two objects.
     * @param field the object which provides information about a single field of a class
     * @param left the first comparing object, can be <code>null</code>
     * @param right the second comparing object, can be <code>null</code>
     * @return created object containing field name and string representation of values
     * @throws IllegalAccessException when an application tries to reflectively get a field
     */
    public static DiffField create(Field field, Object left, Object right) throws IllegalAccessException {
        field.setAccessible(true);
        return new DiffField(field.getName(), getValue(field, left), getValue(field, right));
    }

    /**
     * Creates an instance of the DiffField by {@link Field} and two objects.
     * Field name is formatted to better readability.
     * @param field the object which provides information about a single field of a class
     * @param left the first comparing object, can be <code>null</code>
     * @param right the second comparing object, can be <code>null</code>
     * @return created object containing field name and string representation of values
     * @throws IllegalAccessException when an application tries to reflectively get a field
     */
    public static DiffField createWithFormattedName(Field field, Object left, Object right)
            throws IllegalAccessException {
        field.setAccessible(true);
        return new DiffField(formatMemberName(field.getName()), getValue(field, left), getValue(field, right));
    }

    /**
     * Convert a member name represented as string in a Camel style for better
     * readability. E.g. name 'permissionProfileName' will be transformed to
     * "permission profile name" string.
     * @param name the name of the field
     * @return formatted name
     */
    public static String formatMemberName(String name) {
        String[] tokens = StringUtils.splitByCharacterTypeCamelCase(name);
        return String.join(" ", tokens).toLowerCase(Locale.ENGLISH);
    }

    private static String getValue(Field field, Object object) throws IllegalAccessException {
        if (object == null) {
            return "-";
        }
        return "" + field.get(object);
    }


}
