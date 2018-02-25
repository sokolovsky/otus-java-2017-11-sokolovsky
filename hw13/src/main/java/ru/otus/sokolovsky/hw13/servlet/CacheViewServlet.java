package ru.otus.sokolovsky.hw13.servlet;

import ru.otus.sokolovsky.hw12.cache.Cache;
import ru.otus.sokolovsky.hw12.cache.CacheRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class CacheViewServlet extends RenderedServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Utils.responseOk(resp);
        Cache<Object, Object> cache = CacheRepository.getInstance().getCache(CacheRepository.COMMON_DATA);
        render(resp.getWriter(), new HashMap<String, Object>() {{
            put("list", cache.data());
        }});
    }
}
