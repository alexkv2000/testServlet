package KVO.test1;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/hello")
public class MainServlet extends HttpServlet {
    public static int calculateSum(Map<String, Integer> map) {
        int sum = 0;
        for (int value : map.values()) {
            sum += value;
        }
        return sum;
    }

    private Map<String, Integer> visitNameCount = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        Integer visitCounter = (Integer) session.getAttribute("visitCounter");

        if (visitCounter == null) {
            visitCounter = 1;
        } else {
            visitCounter++;
        }
        session.setAttribute("visitCounter", visitCounter);

        String username = req.getParameter("username");

        // Обработка username
        if (username == null || username.trim().isEmpty() || username.isBlank() || username.equalsIgnoreCase("anonymous")) {
            username = "Anonymous";
        }

        // Увеличиваем счетчик для username
        int count = visitNameCount.getOrDefault(username, 0);
        if (!username.equalsIgnoreCase("anonymous")) {
            count++; // Увеличиваем значение только для реальных пользователей
        }
        visitNameCount.put(username, count);

        resp.setCharacterEncoding("Cp1251");
        resp.setContentType("text/html; charset=Cp1251");
        PrintWriter printWriter = resp.getWriter();

        printWriter.write("Привет, " + username + "<br><br>");
        printWriter.write("Всего пользователей: " + visitNameCount.size() + " раз<br>");
        printWriter.write("Страница была посещена всего (сессий): " + calculateSum(visitNameCount) + " раз<br>");
        printWriter.write("Страниц было посещено " + username + ": " + visitNameCount.get(username) + " раз<br><br>");

        printWriter.write("Список пользователей страницы<br>");
        for (Map.Entry<String, Integer> entry : visitNameCount.entrySet()) {
            printWriter.write("Пользовтаель " + entry.getKey() + " посещений : " + entry.getValue() + "<br>");
        }
    }
}
