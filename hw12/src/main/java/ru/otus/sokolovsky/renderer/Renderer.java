package ru.otus.sokolovsky.renderer;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Renderer {
    private URL layout;
    private Configuration configuration = new Configuration();
    private URL page;
    private Map<String, Object> params;

    public Renderer(URL layout) {
        this.layout = layout;
    }

    public Renderer forPage(URL page, Map<String, Object> params) {
        this.page = page;
        this.params = params;
        return this;
    }

    public void render(Writer writer) {
        try (Writer stream = new StringWriter()) {
            Template contentTemplate = configuration.getTemplate(page.getPath());
            contentTemplate.process(params, stream);

            Template layoutTemplate = configuration.getTemplate(layout.getPath());
            layoutTemplate.process(new HashMap<String, String>() {
                {
                    put("body", stream.toString());
                    put("title", "Some Title");
                }
            }, writer);
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }
}
