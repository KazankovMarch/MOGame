package ru.fixiki.mogame_server.model.gamepackage;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import java.util.Objects;

//TODO kotlinize it
public class Atom {
    public ContentType type = ContentType.TEXT;
    public String value;

    @JacksonXmlText()
    public void setValue(String value) {
        this.value = value.trim();
    }

    @JacksonXmlProperty(isAttribute = true, localName = "type")
    public void setType(String type) {
        this.type = ContentType.valueOf(type.toUpperCase());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Atom atom = (Atom) o;
        return type == atom.type &&
                Objects.equals(value, atom.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }
}
