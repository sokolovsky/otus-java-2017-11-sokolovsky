package ru.otus.sokolovsky.servlet;

import ru.otus.sokolovsky.renderer.Rendered;
import ru.otus.sokolovsky.renderer.Renderer;

import javax.servlet.http.HttpServlet;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

abstract public class RenderedServlet extends HttpServlet implements Rendered {

    private Renderer renderer;
    private URL template;

    @Override
    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public Renderer getRenderer() {
        return renderer;
    }

    @Override
    public void setTemplate(URL template) {
        this.template = template;
    }

    @Override
    public void render(Writer out, Map<String, Object> values) {
        renderer.forPage(template, values);
    }

    public void render(Writer out) {
        renderer.forPage(template, new HashMap<>());
    }
}
