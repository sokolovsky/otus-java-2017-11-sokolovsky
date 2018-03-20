package ru.otus.sokolovsky.hw15.renderer;

import java.io.Writer;
import java.util.Map;

public interface Rendered {

    Renderer getRenderer();

    void setTemplate(String template);

    void render(Writer out, Map<String, Object> values);
}
