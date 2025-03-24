package ru.rmntim;

import ru.rmntim.beans.PointBean;
import ru.rmntim.beans.RBean;
import ru.rmntim.beans.ResultBean;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import ru.rmntim.sql.SQLConnection;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Named("resultsManager")
@SessionScoped
public class ResultsManager implements Serializable {
    long startTime = System.currentTimeMillis();

    @Inject
    private RBean rBean;

    @Inject
    private ResultBean pointsListBean;

    @Inject
    SQLConnection sqlConnection;

    String GET_POINTS_DATA = "SELECT * FROM attempts";
    String CLEAR_DATA = "DELETE FROM attempts";
    String ADD_POINT_DATA = "INSERT INTO attempts (is_hit, x, y, r, curtime, executetime) VALUES (?, ?, ?, ?, ?, ?)";


    public List<PointBean> getAllPoints() {
        List<PointBean> points = new ArrayList<>();
        try (Statement statement = sqlConnection.connect().createStatement()) {
            ResultSet resultSet = statement.executeQuery(GET_POINTS_DATA);
            while (resultSet.next()) {
                PointBean pointBean = new PointBean();
                pointBean.setX(resultSet.getDouble("x"));
                pointBean.setY(resultSet.getDouble("y"));
                pointBean.setR(resultSet.getInt("r"));
                pointBean.setCurTime(resultSet.getDate("curtime"));
                pointBean.setIs_hit();
                pointBean.setExecuteTime(resultSet.getLong("executetime"));
                points.add(pointBean);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return points;
    }


    public void clearResults() {
        try (Connection connection = sqlConnection.connect();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(CLEAR_DATA);
            pointsListBean.clearPoints();
            System.out.println("Clear results took ");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void addPoint(final PointBean point) {
        double x = point.getX();
        double y = point.getY();
        int r = point.getR();
        boolean result = Checker.CheckHit(x,y,r);


        try (PreparedStatement statement = sqlConnection.connect().prepareStatement(ADD_POINT_DATA)) {
            statement.setBoolean(1, result);
            statement.setDouble(2, x);
            statement.setDouble(3, y);
            statement.setInt(4, r);

            Date currentDate = new Date(System.currentTimeMillis());
            statement.setDate(5, currentDate);

            PointBean newPoint = new PointBean();
            newPoint.setX(x);
            newPoint.setY(y);
            newPoint.setR(r);
            newPoint.setCurTime(currentDate);
            newPoint.setIs_hit();

            long executionTime = System.currentTimeMillis() - startTime;
            statement.setLong(6, executionTime);
            statement.executeUpdate();
            newPoint.setExecuteTime(executionTime);

            pointsListBean.addPoint(newPoint);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public void updateAllPoints(int r) {
        List<PointBean> points = getAllPoints();
        System.out.println(points);
        clearResults();
        for (PointBean point : points) {
            point.setR(r);
            rBean.setR(r);
            addPoint(point);
        }
//        try (PreparedStatement statement = sqlConnection.connect().prepareStatement(UPDATE_POINT_DATA)) {
//            List<PointBean> points = pointsListBean.getPoints();
//
//            for (PointBean point : points) {
//                double x = point.getX();
//                double y = point.getY();
//                boolean result = Checker.CheckHit(x, y, r);
//
//                statement.setBoolean(1, result);
//                statement.setInt(2, r);
//                statement.setDate(3, new Date(System.currentTimeMillis()));
//                statement.setDouble(4, x);
//                statement.setDouble(5, y);
//
//                statement.addBatch();
//            }
//
//            statement.executeBatch();
//
//            for (PointBean point : points) {
//                point.setR(r);
//                pointsListBean.updatePoints(point);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
    }
}
