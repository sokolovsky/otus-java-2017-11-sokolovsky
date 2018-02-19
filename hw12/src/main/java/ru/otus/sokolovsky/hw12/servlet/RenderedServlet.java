package ru.otus.sokolovsky.hw12.servlet;

import ru.otus.sokolovsky.hw12.renderer.Rendered;
import ru.otus.sokolovsky.hw12.renderer.Renderer;

import javax.servlet.http.HttpServlet;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

abstract public class RenderedServlet extends HttpServlet implements Rendered {

    private Renderer renderer;
    private String template;

    @Override
    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public Renderer getRenderer() {
        return renderer;
    }

    @Override
    public void setTemplate(String path) {
        this.template = path;
    }

    @Override
    public void render(Writer out, Map<String, Object> values) {
        renderer.forPage(template, values).render(out);
    }

    void render(Writer out) {
        render(out, new HashMap<>());
    }
}
