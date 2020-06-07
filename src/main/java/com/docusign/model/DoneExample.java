package com.docusign.model;

import org.json.JSONObject;
import org.springframework.ui.ModelMap;

import com.docusign.common.DiffField;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;


/**
 * Model which is used in 'done' pages.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@With
public class DoneExample {
    private static final String MODEL_DONE_ATTRIBUTE = "done";
    public static final int JSON_INDENT_FACTOR = 4;

    private String title;
    private String name;
    private String message;
    private String json;
    private StackTraceElement[] stackTrace;
    private Iterable<DiffField> diff;


    /**
     * Creates default DoneExample object. This method initializes title and
     * name members by the same value.
     * @param title the title text
     * @return created instance of this class
     */
    public static DoneExample createDefault(String title) {
        return new DoneExample()
                .withTitle(title)
                .withName(title);
    }

    /**
     * Creates a JSON text from the object and applies it to member
     * @param object the object to convert to JSON string
     * @return this object
     */
    public DoneExample withJsonObject(Object object) {
        if (object != null) {
            JSONObject jsonObject = new JSONObject(object);
            json = jsonObject.toString(JSON_INDENT_FACTOR);
        }
        return this;
    }

    /**
     * Adds this object to model map using name 'done'
     * @param model the model object to add
     */
    public void addToModel(ModelMap model) {
        model.addAttribute(MODEL_DONE_ATTRIBUTE, this);
    }
}
