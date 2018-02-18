package ru.otus.sokolovsky.renderer;

import java.io.Writer;
import java.net.URL;
import java.util.Map;

public interface Rendered {
    void setRenderer(Renderer renderer);

    Renderer getRenderer();

    void setTemplate(URL template);

    void render(Writer out, Map<String, Object> values);
}
