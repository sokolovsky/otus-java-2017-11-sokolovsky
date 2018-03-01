package ru.otus.sokolovsky.hw13.servlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import ru.otus.sokolovsky.hw13.cache.Cache;
import ru.otus.sokolovsky.hw13.cache.CacheGenerator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@Configurable
public class CacheViewServlet extends RenderedServlet {

    @Autowired
    private CacheGenerator cacheGenerator;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Utils.responseOk(resp);
        Cache<Integer, String> cache = cacheGenerator.getCache();
        render(resp.getWriter(), new HashMap<String, Object>() {{
            put("list", cache.data());
        }});
    }
}
