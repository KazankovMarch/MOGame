package ru.fixiki.mogame_server.model.gamepackage;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

public class Param {
    public QuestionParameterType name;
    public String value;

    @JacksonXmlProperty(isAttribute = true, localName = "name")
    @JsonValue
    public void setName(String name) {
        this.name = QuestionParameterType.valueOf(name.toUpperCase());
    }

    @JacksonXmlText
    public void setValue(String value) {
        this.value = value;
    }
}
