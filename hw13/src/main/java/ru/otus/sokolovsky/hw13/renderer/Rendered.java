package ru.otus.sokolovsky.hw13.renderer;

import java.io.Writer;
import java.util.Map;

public interface Rendered {
    void setRenderer(Renderer renderer);

    Renderer getRenderer();

    void setTemplate(String template);

    void render(Writer out, Map<String, Object> values);
}
