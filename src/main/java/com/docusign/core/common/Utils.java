package com.docusign.core.common;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.lang3.ObjectUtils;


public final class Utils {

    private Utils() {}

    /**
     * Examines member values of simple Java objects. All values will be
     * represented as string. This functions creates a list of all non static
     * field values. One of the comparing object can be equal <code>null</code>
     * but not both.
     * @param left the first comparing object
     * @param right the second comparing object
     * @param formatName whether format member names
     * @return list of {@link DiffField} objects
     * @throws IllegalArgumentException if both of comparable objects is
     * <code>null</code> or if objects belong to different classes
     */
    public static List<DiffField> compareFields(Object left, Object right, boolean formatName) {
        if (left == null && right == null) {
            throw new IllegalArgumentException("Both comparing objects are null");
        }

        if (ObjectUtils.allNotNull(left, right) && !left.getClass().equals(right.getClass())) {
            throw new IllegalArgumentException("Can't compare objects because they belong to different classes");
        }

        Field[] classFields = ObjectUtils.defaultIfNull(left, right).getClass().getDeclaredFields();
        List<DiffField> fields = new ArrayList<>();
        for (Field field : classFields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            try {
                if (formatName) {
                    fields.add(DiffField.createWithFormattedName(field, left, right));
                } else {
                    fields.add(DiffField.create(field, left, right));
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                fields.add(new DiffField("?", "?", "?"));
            }
        }
        return fields;
    }

    /**
     * Searches different member values of simple Java objects. All values will
     * be represented as string. This functions creates a list of all non
     * static field values. One of the comparing object can be equal
     * <code>null</code> but not both.
     * @param left the first comparing object
     * @param right the second comparing object
     * @param formatName whether format member names
     * @return list of {@link DiffField} objects
     * @throws IllegalArgumentException if both of comparable objects is
     * <code>null</code> or if objects belong to different classes
     */
    public static List<DiffField> findDifferentFields(Object left, Object right, boolean formatName) {
        return compareFields(left, right, formatName)
                .stream()
                .filter(field -> !StringUtils.equals(field.getLeftValue(), field.getRightValue()))
                .collect(Collectors.toList());
    }
}
