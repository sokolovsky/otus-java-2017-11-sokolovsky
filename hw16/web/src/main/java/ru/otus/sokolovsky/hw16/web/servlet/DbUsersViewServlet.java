package ru.otus.sokolovsky.hw16.web.servlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import ru.otus.sokolovsky.hw15.db.UserDataSet;
import ru.otus.sokolovsky.hw15.domain.UserDBRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configurable
public class DbUsersViewServlet extends RenderedServlet {

    @Autowired
    private UserDBRepository userDBService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Utils.responseOk(resp);
        List<UserDataSet> userDataSets = userDBService.readAll();
        render(resp.getWriter(), new HashMap<String, Object>() {{
            Map<Long, String> map = userDataSets.stream().collect(Collectors.toMap(UserDataSet::getId, UserDataSet::getLogin));
            put("list", map);
            put("title", "Users");
        }});
    }
}
