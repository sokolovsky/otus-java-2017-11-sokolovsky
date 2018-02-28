package ru.otus.sokolovsky.hw13.servlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.otus.sokolovsky.hw13.cache.Cache;
import ru.otus.sokolovsky.hw13.cache.CacheGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@Configurable
public class CacheViewServlet extends RenderedServlet {

    @Autowired
    private CacheGenerator cacheGenerator;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Utils.responseOk(resp);
        Cache<Integer, String> cache = cacheGenerator.getCache();
        render(resp.getWriter(), new HashMap<String, Object>() {{
            put("list", cache.data());
        }});
    }


    @Override
    @Autowired
    public void setTemplate(@Value("#{templates.cache}") String template) {
        super.setTemplate(template);
    }
}
