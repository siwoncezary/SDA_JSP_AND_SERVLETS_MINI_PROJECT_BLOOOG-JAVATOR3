package servlet;

import dao.Daos;
import entity.user.NewUser;
import helper.Encoding;
import helper.Parse;
import helper.user.UserRegistration;
import repository.UserRepository;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/user")
public class UserServlet extends HttpServlet {
    private static final String ACTION = "action";
    private UserRepository repo = new UserRepository(Daos.USER);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            UserAction.valueOf(req.getParameter(ACTION)).process(req, resp);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid action name");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserAction.valueOf(req.getParameter(ACTION)).process(req,resp);
        String action = req.getParameter(ACTION);

        switch(action){
            case ACTION_ADD_USER:{
                String email = Encoding.encode(req.getParameter("email"));
                String password = Encoding.encode(req.getParameter("password"));
                String repeatedPassword = Encoding.encode(req.getParameter("repeatedPassword"));
                String nick = Encoding.encode(req.getParameter("nick"));
                if (repo.existEmail(email)){
                    resp.getWriter().println("Taki adres email już istnieje. Zmień email.");
                    return;
                }
                if (password.equals(repeatedPassword)) {
                    repo.add(new NewUser(email, nick, password));
                    repo.findByEmail(email).ifPresent(user -> repo.get(user.id).ifPresent(UserRegistration::sendRegistrationMail));
                    resp.sendRedirect("user?"+ACTION+"="+ACTION_VIEW_ALL);
                } else {
                    resp.sendRedirect("registration_error.jsp");
                }
            }
            break;
            case ACTION_LOGIN_USER:{
                String email = Encoding.encode(req.getParameter("email"));
                String password = Encoding.encode(req.getParameter("password"));
                repo.login(email, password).ifPresent(logedUser -> {
                    req.getSession().setAttribute("loggedUser", logedUser);
                    System.out.println("LOGGED USER: " + logedUser);
                    req.getSession().setMaxInactiveInterval(3000);
                    //TODO kierujemy zalogowanego użytkownika - dopisać
                });
                if (req.getSession().getAttribute("loggedUser") == null){
                    resp.getWriter().println("Nie zalogowano, niepoprawny login lub hasło");
                } else {
                    resp.getWriter().println("Zostałeś pomyślnie zalogowany");
                }
            }
            break;
        }
    }

}
