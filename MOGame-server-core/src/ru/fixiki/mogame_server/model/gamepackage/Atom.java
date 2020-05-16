package ru.fixiki.mogame_server.model.gamepackage;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

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
}
