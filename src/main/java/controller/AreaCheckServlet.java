package controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Point;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@WebServlet("/area-check-servlet")
public class AreaCheckServlet extends HttpServlet {
    Logger logger = Logger.getLogger(AreaCheckServlet.class.getName());
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    @SuppressWarnings("unchecked")
    private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String queryString = request.getQueryString();
            List<String> parsedData = new java.util.ArrayList<>(Arrays.stream(queryString.split("&"))
                    .map(x -> x.split("=")[1])
                    .toList());
            String xParam = parsedData.get(0);
            String yParam = parsedData.get(1);
            String rParam = parsedData.get(2);

            if (xParam == null || yParam == null || rParam == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
                return;
            }

            double x = Double.parseDouble(xParam);
            double y = Double.parseDouble(yParam);
            int r = Integer.parseInt(rParam);
            logger.info(x + "  " + y + " " + r);

            if (!validValues(x, y, r)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            ServletContext context = getServletContext();
            @SuppressWarnings("unchecked")
            List<Point> contents = (List<Point>) context.getAttribute("sessionData");

            if (contents == null) {
                contents = new ArrayList<>();
            }

            contents.add(ResultRow(x, y, r));
            logger.info("contents: " + contents);
            context.setAttribute("sessionData", contents);
            request.getRequestDispatcher("/result.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            logger.warning("Invalid number format: " + e.getMessage());
            request.setAttribute("error", "Invalid number format: " + e.getMessage());
            request.getRequestDispatcher("/result.jsp").forward(request, response);
        }
    }

        public boolean getInArea ( double x, double y, int r){
            if ((x >= 0 && y >= 0) && (x <= r && 2 * y <= r)) {
                return true;
            }
            if ((x >= 0 && y <= 0) && (x * x + y * y <= r * r)) {
                return true;
            }
            if (((x <= 0) && (y >= 0)) && (y <= (x + r))) {
                return true;
            }
            return false;
        }

        public Point ResultRow ( double x, double y, int r){
            long startTime = System.nanoTime() / 1000;
            boolean isHit = getInArea(x, y, r);
            String currTime = new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis()));
            long dur = System.nanoTime() / 1000 - startTime;
            logger.info("isHit: " + isHit);
            logger.info(currTime);
            return new Point(x, y, r, currTime, dur, isHit);
        }

        public boolean validValues ( double x, double y, int r){
            if (x < -3 || x > 3) {
                return false;
            }
            return true;
        }
    }
