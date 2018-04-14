package ru.otus.sokolovsky.hw16.web.renderer;

import java.io.Writer;
import java.util.Map;

public interface Renderer {

    Renderer forPage(String page, Map<String, Object> params);

    void render(Writer writer);
}
