package ru.otus.sokolovsky.hw15.servlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import ru.otus.sokolovsky.hw15.domain.UserDBService;
import ru.otus.sokolovsky.hw15.domain.UserDataSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configurable
public class ChatInitServlet extends RenderedServlet {

    @Autowired
    private UserDBService userDBService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Utils.responseOk(resp);
        List<UserDataSet> userDataSets = userDBService.readAll();
        render(resp.getWriter(), new HashMap<String, Object>() {{
            Map<Long, String> map = userDataSets.stream().collect(Collectors.toMap(UserDataSet::getId, UserDataSet::getName));
            put("list", map);
            put("title", "Users");
        }});
    }
}
