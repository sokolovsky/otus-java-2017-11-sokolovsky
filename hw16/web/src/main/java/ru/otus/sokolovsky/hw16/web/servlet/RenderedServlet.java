package ru.otus.sokolovsky.hw16.web.servlet;

import ru.otus.sokolovsky.hw16.web.renderer.Rendered;
import ru.otus.sokolovsky.hw16.web.renderer.Renderer;

import javax.servlet.http.HttpServlet;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

abstract public class RenderedServlet extends ApplicationHttpServlet implements Rendered {

    private Renderer renderer;

    private String template;

    @Override
    public Renderer getRenderer() {
        return renderer;
    }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void setTemplate(String template) {
        this.template = template;
    }

    @Override
    public void render(Writer out, Map<String, Object> values) {
        renderer.forPage(template, values).render(out);
    }

    void render(Writer out) {
        render(out, new HashMap<>());
    }
}
