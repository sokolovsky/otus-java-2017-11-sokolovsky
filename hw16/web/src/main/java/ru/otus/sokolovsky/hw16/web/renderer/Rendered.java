package ru.otus.sokolovsky.hw16.web.renderer;

import java.io.Writer;
import java.util.Map;

public interface Rendered {

    Renderer getRenderer();

    void setTemplate(String template);

    void render(Writer out, Map<String, Object> values);
}
