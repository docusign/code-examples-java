package com.docusign.core.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.gson.Gson;
import org.codehaus.jackson.map.SerializationConfig;
import org.json.JSONObject;
import org.springframework.ui.ModelMap;

import com.docusign.core.common.DiffField;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.text.SimpleDateFormat;


/**
 * Model which is used in 'done' pages.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@With
public class DoneExample {
    private static final String MODEL_DONE_ATTRIBUTE = "done";
    private static final String DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ssz";
    public static final int JSON_INDENT_FACTOR = 4;

    private String title;
    private String name;
    private String message;
    private String json;
    private StackTraceElement[] stackTrace;
    private Iterable<DiffField> diff;



    /**
     * Creates DoneExample object with title. This method initializes title.
     * @param String title
     * @return created instance of this class
     */
    public DoneExample withTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Creates DoneExample object with name. This method initializes name.
     * @param String name
     * @return created instance of this class
     */
    public DoneExample withName(String name) {
        this.name = name;
        return this;
    }


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
     * Creates DoneExample object with message. This method initializes message.
     * @param String message
     * @return created instance of this class
     */
    public DoneExample withMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * Creates DoneExample object with stackTrace. This method initializes error stack trace.
     * @param StackTraceElement[] stackTrace
     * @return created instance of this class
     */
    public DoneExample withStackTrace(StackTraceElement[] stackTrace) {
        this.stackTrace = stackTrace;
        return this;
    }

    /**
     * Creates a JSON text from the object and applies it to member
     * @param object the object to convert to JSON string
     * @return this object
     */
    public DoneExample withJsonObject(Object object) {
        if (object != null) {
            ObjectWriter writer = new ObjectMapper()
                    .registerModule(new JodaModule())
                    .setDateFormat(new SimpleDateFormat(DATETIME_PATTERN))
                    .setDefaultPrettyPrinter(new DefaultPrettyPrinter())
                    .writerWithDefaultPrettyPrinter();
            try {
                json = writer.writeValueAsString(object);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
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
