package controller;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ControllerServlet extends HttpServlet{
    private static final Set<Integer> ValueY = IntStream
            .iterate(-3, i -> i <= 5, i -> i + 1)
            .boxed().collect(Collectors.toSet());

    private static final Set<Integer> ValueR = IntStream
            .iterate(1, i -> i <= 5, i -> i + 1)
            .boxed().collect(Collectors.toSet());
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    public void  processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String queryString = request.getQueryString();
        if (queryString == null) {
            response.sendRedirect("/index.jsp");
        } else if (!Pattern.matches("x=-?[0-9]+(.[0-9]+)?&y=-?[0-9]+(.[0-9]+)?&r=[0-9]+(.[0-9]+)?", queryString)) {
            response.setStatus(400);
        } else {
            try {
                request.getServletContext().getRequestDispatcher("/area-check-servlet").forward(request, response);
            } catch (NumberFormatException e){
                sendError(response, e.toString());
            }
        }
    }


    private  boolean validArgs(HttpServletResponse response, double x, int y, int r) throws IOException {
        if (3 < x || x < -3){
            sendError(response, "Значение x должно принадлежать отрезку [-3, 3]");
            return true;
        }
        boolean isYValid = false;
        for (float validValue : ValueY) {
            if (y == validValue) {
                isYValid = true;
                break;
            }
        }
        if (!isYValid) {
            sendError(response, "Значение y должно быть одним из следующих: {-3, -2, -1, 0, 1, 2, 3, 4, 5}");
            return true;
        }

        boolean isRValid = false;
        for (float validValue : ValueR) {
            if (r == validValue) {
                isRValid = true;
                break;
            }
        }
        if (!isRValid) {
            sendError(response, "Значение r должно быть одним из следующих: {1, 2, 3, 4, 5}");
            return true;
        }
        return false;
    }

    private void sendError(HttpServletResponse response, String error) throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, error);
    }
}
