package ru.otus.sokolovsky.hw13.servlet;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.otus.sokolovsky.hw13.db.Accounts;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

@Configurable
public class LoginServlet extends RenderedServlet {

    private static final String LOGIN_PARAM = "login";
    private static final String PASSWORD_PARAM = "pass";

    @Autowired
    private Accounts accounts;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Utils.responseOk(resp);
        render(resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String login = req.getParameter(LOGIN_PARAM);
        String pass = Utils.generateHash(req.getParameter(PASSWORD_PARAM));
        if (Objects.isNull(login)) {
            render(resp.getWriter(), new HashMap<String, Object>() {{
                put("error", "Login is needed not empty");
            }});
            Utils.responseOk(resp);
            return;
        }

        if (accounts.hasPassword(login, pass)) {
            req.getSession().setAttribute("login", login);
            resp.sendRedirect("/");
            return;
        }

        render(resp.getWriter(), new HashMap<String, Object>() {{
            put("error", "User with sent data doesn't exist");
        }});
        Utils.responseOk(resp);
    }

    @Override
    @Autowired
    public void setTemplate(@Value("#{templates.login}") String template) {
        System.out.println(template + "===============");
        super.setTemplate(template);
    }
}
