package ru.otus.sokolovsky.hw13.renderer;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class Renderer {
    private String layout;
    private String page;
    private Configuration configuration = new Configuration();
    private Map<String, Object> params;

    public Renderer(String layout) {
        this.layout = layout;
    }

    public Renderer forPage(String page, Map<String, Object> params) {
        this.page = page;
        this.params = params;
        return this;
    }

    public void render(Writer writer) {
        try (Writer stream = new StringWriter()) {
            Template contentTemplate = configuration.getTemplate(page);
            contentTemplate.process(params, stream);

            Template layoutTemplate = configuration.getTemplate(layout);
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
