package ru.otus.sokolovsky.hw16.web.servlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.otus.sokolovsky.hw15.renderer.Rendered;
import ru.otus.sokolovsky.hw15.renderer.Renderer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@Configurable
abstract public class RenderedServlet extends HttpServlet implements Rendered {

    @Autowired
    private Renderer renderer;

    private String template;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        // it`s not what I want, but Spring not ensure methods for post processing
        String template = getServletConfig().getInitParameter("template");
        setTemplate(template);
    }

    @Override
    public Renderer getRenderer() {
        return renderer;
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
