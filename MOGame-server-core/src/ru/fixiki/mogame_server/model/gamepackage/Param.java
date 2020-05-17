package ru.fixiki.mogame_server.model.gamepackage;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import java.util.Objects;

//TODO kotlinize it
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Param param = (Param) o;
        return name == param.name &&
                Objects.equals(value, param.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}
