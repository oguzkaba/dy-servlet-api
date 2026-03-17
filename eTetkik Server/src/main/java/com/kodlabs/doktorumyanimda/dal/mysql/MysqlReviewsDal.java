package com.kodlabs.doktorumyanimda.dal.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kodlabs.doktorumyanimda.dal.ConnectionException;
import com.kodlabs.doktorumyanimda.dal.IReviewsDal;
import com.kodlabs.doktorumyanimda.messages.ErrorMessages;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.reviews.DoctorSummary;
import com.kodlabs.doktorumyanimda.model.reviews.Review;
import com.kodlabs.doktorumyanimda.model.reviews.ReviewCreateRequest;
import com.kodlabs.doktorumyanimda.model.reviews.ReviewReply;
import com.kodlabs.doktorumyanimda.model.reviews.ReviewReplyRequest;
import com.kodlabs.doktorumyanimda.model.reviews.ReviewsEditRequest;

public class MysqlReviewsDal implements IReviewsDal {

    @Override
    public ResponseEntity addReview(ReviewCreateRequest request)
            throws ConnectionException {
        if (MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }

        ResponseEntity response;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String sql = "{CALL addReview(?, ?, ?, ?, ?, ?)}";

        try {
            statement = MysqlConnection.getInstance().prepareStatement(sql);
            statement.setString(1, request.getPatientID());
            statement.setString(2, request.getDoctorID());
            statement.setLong(3, request.getAppointmentID());
            statement.setInt(4, request.getRating());
            statement.setString(5, request.getTitle());
            statement.setString(6, request.getBody());
            statement.executeQuery();
            response = new ResponseEntity();

        } catch (SQLException e) {
            response = new ResponseEntity(false, e.getLocalizedMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntity editReview(ReviewsEditRequest request)
            throws ConnectionException {
        if (MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }

        ResponseEntity response;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String sql = "{CALL editReview(?, ?, ?, ?, ?)}";

        try {
            statement = MysqlConnection.getInstance().prepareStatement(sql);
            statement.setString(1, request.getPatientID());
            statement.setLong(2, request.getReviewID());
            statement.setInt(3, request.getRating());
            statement.setString(4, request.getTitle());
            statement.setString(5, request.getBody());
            statement.executeQuery();
            response = new ResponseEntity();

        } catch (SQLException e) {
            response = new ResponseEntity(false, e.getLocalizedMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntity deleteReview(String requesterID, Long reviewID, boolean isAdmin) throws ConnectionException {
        if (MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }

        ResponseEntity response;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String sql = "{CALL deleteReview(?, ?, ?)}";

        try {
            statement = MysqlConnection.getInstance().prepareStatement(sql);
            statement.setString(1, requesterID);
            statement.setLong(2, reviewID);
            statement.setInt(3, isAdmin ? 1 : 0);
            statement.executeQuery();
            response = new ResponseEntity();

        } catch (SQLException e) {
            response = new ResponseEntity(false, e.getLocalizedMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntity replyReview(ReviewReplyRequest request) throws ConnectionException {
        if (MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }

        ResponseEntity response;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String sql = "{CALL replyReview(?, ?, ?)}";

        try {
            statement = MysqlConnection.getInstance().prepareStatement(sql);
            statement.setString(1, request.getDoctorID());
            statement.setLong(2, request.getReviewID());
            statement.setString(3, request.getBody());
            statement.executeQuery();
            response = new ResponseEntity();

        } catch (SQLException e) {
            response = new ResponseEntity(false, e.getLocalizedMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<List<Review>> getDoctorReviews(String doctorID, int page, int pageSize)
            throws ConnectionException {
        if (MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }

        ResponseEntitySet<List<Review>> response;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String sql = "{CALL getDoctorReviews(?, ?, ?)}";

        try {
            statement = MysqlConnection.getInstance().prepareStatement(sql);
            statement.setString(1, doctorID);
            statement.setInt(2, page);
            statement.setInt(3, pageSize);
            resultSet = statement.executeQuery();

            List<Review> reviews = new ArrayList<>();

            while (resultSet.next()) {
                reviews.add(
                        new Review(
                                resultSet.getLong("id"),
                                resultSet.getString("patient_id"),
                                resultSet.getString("patient_name"),
                                resultSet.getString("doctor_id"),
                                resultSet.getLong("appointment_id"),
                                resultSet.getInt("rating"),
                                resultSet.getString("title"),
                                resultSet.getString("body"),
                                resultSet.getLong("reply_id") != 0 ? new ReviewReply(
                                        resultSet.getLong("reply_id"),
                                        resultSet.getLong("id"),
                                        resultSet.getString("doctor_id"),
                                        resultSet.getString("reply_body"),
                                        resultSet.getTimestamp("reply_created_at")) : null,
                                resultSet.getTimestamp("created_at"),
                                resultSet.getTimestamp("updated_at")));
            }

            response = new ResponseEntitySet<>(reviews);

        } catch (SQLException e) {
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<DoctorSummary> getDoctorSummary(String doctorID) throws ConnectionException {
        if (MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }

        ResponseEntitySet<DoctorSummary> response;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String sql = ""
                + "SELECT s.doctor_id, s.avg_rating, s.rating_count, s.last_review_at "
                + "FROM doctor_ratings_summary s "
                + "JOIN doctor d ON s.doctor_id = d.userID "
                + "WHERE d.id = ? OR d.userID = ? "
                + "LIMIT 1";

        try {
            statement = MysqlConnection.getInstance().prepareStatement(sql);
            statement.setString(1, doctorID);
            statement.setString(2, doctorID);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                DoctorSummary summary = new DoctorSummary(
                        resultSet.getString("doctor_id"),
                        resultSet.getDouble("avg_rating"),
                        resultSet.getInt("rating_count"),
                        resultSet.getTimestamp("last_review_at"));
                response = new ResponseEntitySet<>(summary);
            } else {
                response = new ResponseEntitySet<>(false, ErrorMessages.operationFailed);
            }

        } catch (SQLException e) {
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<Review> getReviewByAppointment(Long appointmentID, String requesterID,
            boolean requesterIsAdmin) throws ConnectionException {
        if (MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }

        ResponseEntitySet<Review> response;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String sql = "{CALL getReviewByAppointment(?,?,?)}";

        try {
            statement = MysqlConnection.getInstance().prepareStatement(sql);
            statement.setLong(1, appointmentID);
            statement.setString(2, requesterID);
            statement.setInt(3, requesterIsAdmin ? 1 : 0);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Review review = new Review(
                        resultSet.getLong("id"),
                        resultSet.getString("patient_id"),
                        resultSet.getString("patient_name"),
                        resultSet.getString("doctor_id"),
                        resultSet.getLong("appointment_id"),
                        resultSet.getInt("rating"),
                        resultSet.getString("title"),
                        resultSet.getString("body"),
                        resultSet.getLong("reply_id") != 0 ? new ReviewReply(
                                resultSet.getLong("reply_id"),
                                resultSet.getLong("id"),
                                resultSet.getString("doctor_id"),
                                resultSet.getString("reply_body"),
                                resultSet.getTimestamp("reply_created_at")) : null,
                        resultSet.getTimestamp("created_at"),
                        resultSet.getTimestamp("updated_at"));
                response = new ResponseEntitySet<>(review);
            } else {
                response = new ResponseEntitySet<>(false, ErrorMessages.operationFailed);
            }

        } catch (SQLException e) {
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

}
