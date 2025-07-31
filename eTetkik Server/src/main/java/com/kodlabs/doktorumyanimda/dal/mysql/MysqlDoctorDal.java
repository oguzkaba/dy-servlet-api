package com.kodlabs.doktorumyanimda.dal.mysql;

import com.kodlabs.doktorumyanimda.dal.ConnectionException;
import com.kodlabs.doktorumyanimda.dal.IDoctorDal;
import com.kodlabs.doktorumyanimda.messages.ErrorMessages;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.contact.Contact;
import com.kodlabs.doktorumyanimda.model.doctor.*;
import com.kodlabs.doktorumyanimda.model.doctor.detail.DoctorDetail;
import com.kodlabs.doktorumyanimda.model.doctor.detail.DoctorDetailUpdateRequest;
import com.kodlabs.doktorumyanimda.model.doctor.peak.PeakFeeDetail;
import com.kodlabs.doktorumyanimda.model.doctor.peak.PeakFeeRequest;
import com.kodlabs.doktorumyanimda.model.doctor.recipe.DoctorRecipeInformation;
import com.kodlabs.doktorumyanimda.model.doctor.recipe.DoctorRecipeInformationCreate;
import com.kodlabs.doktorumyanimda.model.doctor.user.ChangePasswordRequest;
import com.kodlabs.doktorumyanimda.model.doctor.user.DoctorLoginV2Request;
import com.kodlabs.doktorumyanimda.model.doctor.user.DoctorLoginVerifyRequest;
import com.kodlabs.doktorumyanimda.model.doctor.user.DoctorRegister;
import com.kodlabs.doktorumyanimda.model.social.SocialAccount;
import com.kodlabs.doktorumyanimda.model.social.SocialRequest;
import com.kodlabs.doktorumyanimda.model.social.SocialShare;
import com.kodlabs.doktorumyanimda.model.user.LoginData;
import com.kodlabs.doktorumyanimda.model.user.UserDoctor;
import com.kodlabs.doktorumyanimda.utils.Common;
import com.kodlabs.doktorumyanimda.utils.Functions;
import com.kodlabs.doktorumyanimda.utils.Role;
import com.kodlabs.doktorumyanimda.utils.TextUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MysqlDoctorDal implements IDoctorDal {
    @Override
    public ResponseEntitySet<List<DoctorInformationForAdmin>> list(String userID, byte role) throws ConnectionException{
        if(MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<List<DoctorInformationForAdmin>> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL doctorList(?, ?) }");
            statement.setString(1, userID);
            statement.setByte(2, role);
            resultSet = statement.executeQuery();
            List<DoctorInformationForAdmin> list = new ArrayList<>();
            while(resultSet.next()){
                list.add(
                        new DoctorInformationForAdmin(resultSet.getString("degree"),
                                resultSet.getString("name"),
                                resultSet.getString("surname"),
                                resultSet.getString("email"),
                                resultSet.getString("phone"),
                                resultSet.getString("id"),
                                Functions.encodeBase64(resultSet.getBytes("picture")),
                                resultSet.getString("branch"),
                                resultSet.getString("address"),
                                resultSet.getString("createDate"),
                                resultSet.getString("userID")
                        )
                );
            }
            response = new ResponseEntitySet<>(list);
        }catch (SQLException e){
            response =  new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null) {
                    statement.close();
                }
                if(resultSet != null) {
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<List<DoctorInformation>> notAnonymousList(String branch, String city, String os) throws ConnectionException {
        if(MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<List<DoctorInformation>> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL anonymousDoctorList(?, ?, ?, ?) }");
            statement.setBoolean(1, false);
            statement.setString(2, TextUtils.isEmpty(branch)?branch:branch.trim());
            statement.setString(3, TextUtils.isEmpty(city)?city:city.trim());
            statement.setString(4, TextUtils.isEmpty(os)?os:os.trim());
            resultSet = statement.executeQuery();
            List<DoctorInformation> list = new ArrayList<>();
            while(resultSet.next()){
                list.add(
                        new DoctorInformation(resultSet.getString("degree"),
                                resultSet.getString("name"),
                                resultSet.getString("surname"),
                                resultSet.getString("email"),
                                resultSet.getString("phone"),
                                resultSet.getString("id"),
                                Functions.encodeBase64(resultSet.getBytes("picture")),
                                resultSet.getString("branch"),
                                resultSet.getString("address"),
                                resultSet.getInt("dayHourStart"),
                                resultSet.getInt("dayHourEnd"),
                                resultSet.getInt("dayMinutePeriod")
                        )
                );
            }
            response = new ResponseEntitySet<>(list);
        }catch (SQLException e){
            response =  new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null) {
                    statement.close();
                }
                if(resultSet != null) {
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<DoctorInformation> information(String doctorID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<DoctorInformation> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement(" { CALL doctorInformation(?) } ");
            statement.setString(1, doctorID);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(
                        new DoctorInformation(
                                resultSet.getString("degree"),
                                resultSet.getString("name"),
                                resultSet.getString("surname"),
                                resultSet.getString("email"),
                                resultSet.getString("phone"),
                                resultSet.getString("id"),
                                Functions.encodeBase64(resultSet.getBytes("picture")),
                                resultSet.getString("branch"),
                                resultSet.getString("address"),
                                resultSet.getInt("dayHourStart"),
                                resultSet.getInt("dayHourEnd"),
                                resultSet.getInt("dayMinutePeriod")
                        )
                );
            }else{
                response = new ResponseEntitySet<>(false, ErrorMessages.notAccessUserData);
            }
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null) {
                    statement.close();
                }
                if(resultSet != null) {
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<DoctorPayInformation> payInformation(String payInformation) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<DoctorPayInformation> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL doctorPayInformation(?) }");
            statement.setString(1, payInformation);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                String socialAccounts = resultSet.getString("socialAccounts");
                List<SocialAccount> socialAccountList = new ArrayList<>();
                if(!TextUtils.isEmpty(socialAccounts)){
                    SocialAccount[] socialArr = Common.gson.fromJson(socialAccounts, SocialAccount[].class);
                    socialAccountList = Arrays.asList(socialArr);
                }
                String socialShares = resultSet.getString("socialShares");
                List<SocialShare> socials = new ArrayList<>();
                if(!TextUtils.isEmpty(socialShares)){
                    SocialShare[] socialArr = Common.gson.fromJson(socialShares, SocialShare[].class);
                    socials = Arrays.asList(socialArr);
                }
                String peakFeeDetails = resultSet.getString("peakFeeDetails");
                List<DoctorPeakDetail> details = new ArrayList<>();
                if(!TextUtils.isEmpty(peakFeeDetails)){
                    DoctorPeakDetail[] detailArr = Common.gson.fromJson(peakFeeDetails, DoctorPeakDetail[].class);
                    details = Arrays.asList(detailArr);
                }
                response = new ResponseEntitySet<>(
                        new DoctorPayInformation(
                              resultSet.getString("fullName"),
                              resultSet.getString("doctorID"),
                              resultSet.getString("branch"),
                              resultSet.getString("education"),
                              resultSet.getString("introduceYourSelf"),
                              resultSet.getString("areaExperts"),
                              Functions.encodeBase64(resultSet.getBytes("picture")),
                              socialAccountList,
                              socials,
                              details,
                              resultSet.getString("address"),
                              resultSet.getString("webAddress"),
                              resultSet.getInt("dayHourStart"),
                              resultSet.getInt("dayHourEnd"),
                              resultSet.getInt("dayMinutePeriod")
                        )
                );
            }else{
                response = new ResponseEntitySet<>(false, ErrorMessages.notAccessUserData);
            }
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
                if(resultSet != null){
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntity update(DoctorUpdateRequest request) throws ConnectionException {
        if(MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            String query;
            if(!TextUtils.isEmpty(request.getPassword())){
                query = "update doctor set phone = ?, email = ?, password = ? where id = ?";
            }else{
                query = "update doctor set phone = ?, email = ? where id = ?";
            }
            statement = MysqlConnection.getInstance().prepareStatement(query);
            statement.setString(1, request.getPhone());
            statement.setString(2, request.getMail());
            if(!TextUtils.isEmpty(request.getPassword())){
                statement.setString(3, request.getPassword());
                statement.setString(4, request.getId());
            }else{
                statement.setString(3, request.getId());
            }
            int updateResult = statement.executeUpdate();
            if(updateResult != 0){
                response = new ResponseEntity();
            }else{
                response = new ResponseEntity(false, ErrorMessages.operationFailed);
            }
        }catch (SQLException e){
            response = new ResponseEntity(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null) {
                    statement.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<Contact> contact(String userID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<Contact> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL contact(?, ?) }");
            statement.setString(1, userID);
            statement.setInt(2, Role.DOCTOR.value());
            resultSet = statement.executeQuery();
            if(resultSet != null && resultSet.next()){
                response = new ResponseEntitySet<>(new Contact(resultSet.getString("email"),resultSet.getString("phone")));
            }else{
                response = new ResponseEntitySet<>(false, ErrorMessages.operationFailed);
            }
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null) {
                    statement.close();
                }
                if(resultSet != null) {
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<LoginData> login(DoctorLoginV2Request request, String code) throws ConnectionException {
        return null;
    }

    @Override
    public ResponseEntitySet<UserDoctor> loginVerify(DoctorLoginVerifyRequest request) throws ConnectionException {
        if(MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<UserDoctor> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL doctorLoginVerifyCode(?, ?) }");
            statement.setString(1, request.getPhone());
            statement.setString(2, request.getVerifyCode());
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(
                        new UserDoctor(
                                resultSet.getString("userID"),
                                resultSet.getString("id")
                        )
                );
            }else{
                response = new ResponseEntitySet<>(false, ErrorMessages.operationFailed);
            }
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null) {
                    statement.close();
                }

                if(resultSet != null) {
                    resultSet.close();
                }

            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntity register(DoctorRegister request) throws ConnectionException {
        if(MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        CallableStatement statement = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareCall("{ CALL doctorRegister(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
            statement.setString(1, request.getDoctorCode());
            statement.setString(2, request.getContact().getPhone());
            statement.setString(3, request.getContact().getEmail());
            statement.setString(4, Functions.toSHA1(request.getPassword()));
            statement.setString(5, request.getProfile().getDegree());
            statement.setString(6, Functions.nameFormat(request.getProfile().getName()));
            statement.setString(7, Functions.surnameFormat(request.getProfile().getSurname()));
            statement.setString(8, request.getProfile().getGender());
            statement.setString(9, request.getAddress().getAddress());
            statement.setString(10, request.getAddress().getDistrict());
            statement.setString(11, request.getAddress().getCity());
            statement.setBoolean(12, request.getBase().isAnonymous());
            statement.setString(13, request.getDetail().getBranch());
            statement.setString(14, request.getDetail().getEducation());
            statement.setString(15, request.getDetail().getAreaExperts());
            statement.setString(16, request.getDetail().getIntroduceYourSelf());
            statement.setBoolean(17, request.getBase().isPrivateDoctor());
            statement.setBoolean(18, request.getBase().isHospital());
            statement.setInt(19, request.getBase().getFacilityID());
            statement.setString(20, request.getAdminID());
            statement.setString(21, request.getTcNumber());
            statement.setString(22, request.getSerialNumber());
            statement.setString(23, request.getRegistrationNumber());
            statement.setString(24, request.getRecipeInformation().getMedulaPassword());
            statement.setInt(25, request.getRecipeInformation().getBranchCode());
            statement.setInt(26, request.getRecipeInformation().getCertificateCode());
            statement.execute();
            response = new ResponseEntity();
        }catch (SQLException e){
            response = new ResponseEntity(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null) {
                    statement.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public boolean isExistsID(String ID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean isAvailable;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL doctorExistsID(?) }");
            statement.setString(1, ID);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                isAvailable = resultSet.getBoolean("result");
            }else{
                isAvailable = false;
            }
        }catch (SQLException e){
            isAvailable = false;
        }finally {
            try{
                if(statement != null) {
                    statement.close();
                }
                if(resultSet != null) {
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return isAvailable;
    }

    @Override
    public ResponseEntity changePassword(ChangePasswordRequest request) throws ConnectionException {
        if(MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL changePassword(?, ?, ?) }");
            statement.setString(1, Functions.toSHA1(request.getPassword()));
            statement.setString(2, Functions.toSHA1(request.getNewPassword()));
            statement.setString(3, request.getUserID());
            statement.execute();
            response = new ResponseEntity();
        }catch (SQLException e){
            response = new ResponseEntity(false,e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null) {
                    statement.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<DoctorDetail> detail(String doctorID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<DoctorDetail> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL doctorDetail(?) }");
            statement.setString(1, doctorID);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(
                        new DoctorDetail(
                            resultSet.getString("branch"),
                            resultSet.getString("introduceYourSelf"),
                            resultSet.getString("areaExperts"),
                            resultSet.getString("education")
                        )
                );
            }else{
                response = new ResponseEntitySet<>(false, ErrorMessages.operationFailed);
            }
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
                if(resultSet != null){
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntity detailUpdate(DoctorDetailUpdateRequest request) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            DoctorDetail detail = request.getDetail();
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL doctorDetailUpdate(?, ?, ?, ?, ?) }");
            statement.setString(1, request.getUserID());
            statement.setString(2, detail.getBranch());
            statement.setString(3, detail.getIntroduceYourSelf());
            statement.setString(4, detail.getEducation());
            statement.setString(5, detail.getAreaExperts());
            statement.execute();
            response = new ResponseEntity();
        }catch (SQLException e){
            response = new ResponseEntity(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<List<PeakFeeDetail>> peakFeeDetail(String doctorID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<List<PeakFeeDetail>> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL peakFeeDetail(?) }");
            statement.setString(1, doctorID);
            resultSet = statement.executeQuery();
            final List<PeakFeeDetail> details = new ArrayList<>();
            while(resultSet.next()){
                details.add(
                        new PeakFeeDetail(
                                resultSet.getString("id"),
                                resultSet.getInt("fee"),
                                resultSet.getInt("peakDay")
                        )
                );
            }
            response = new ResponseEntitySet<>(
                details
            );
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
                if(resultSet != null){
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntity peakFeeDetailUpdate(PeakFeeRequest request) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL peakFeeDetailUpdate(?, ?, ?, ?) }");
            statement.setString(1, request.getUserID());
            statement.setString(2, request.getDetail().getId());
            statement.setInt(3, request.getDetail().getFee());
            statement.setInt(4, request.getDetail().getPeakDay());
            statement.execute();
            response = new ResponseEntity();
        }catch (SQLException e){
            response = new ResponseEntity(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<String> peakFeeDetailAdd(PeakFeeRequest request) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<String> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL peakFeeDetailAdd(?, ?, ?) }");
            statement.setString(1, request.getUserID().trim());
            statement.setInt(2, request.getDetail().getFee());
            statement.setInt(3, request.getDetail().getPeakDay());
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(
                        resultSet.getString("id")
                );
            }else{
                response = new ResponseEntitySet<>(false, ErrorMessages.operationFailed);
            }
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
                if(resultSet != null){
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntity peakFeeDetailDelete(String doctorID, String id) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement(" { CALL peakFeeDetailDelete(?, ?) }");
            statement.setString(1, doctorID);
            statement.setString(2, id);
            statement.execute();
            response = new ResponseEntity();
        }catch (SQLException e){
            response = new ResponseEntity(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<String> updateVerifyCode(String phone, String verifyCode) throws ConnectionException{
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntitySet<String> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL doctorUpdateVerifyCode(?, ?) }");
            statement.setString(1, phone);
            statement.setString(2, verifyCode);
            statement.execute();
            response = new ResponseEntitySet<>(
                    verifyCode
            );
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<Integer> drBalance(String doctorID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<Integer> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL drBalance(?) } ");
            statement.setString(1, doctorID);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(
                        resultSet.getInt("drBalance")
                );
            }else{
                throw new SQLException(ErrorMessages.operationFailed);
            }
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
                if(resultSet != null){
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<List<SocialAccount>> socialAccounts(String userID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<List<SocialAccount>> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL doctorSocialAccounts(?) }");
            statement.setString(1, userID);
            resultSet = statement.executeQuery();
            List<SocialAccount> accounts = new ArrayList<>();
            while(resultSet.next()){
                accounts.add(
                        new SocialAccount(
                                resultSet.getString("id"),
                                resultSet.getInt("type"),
                                resultSet.getString("link")
                        )
                );
            }
            response = new ResponseEntitySet<>(accounts);
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
                if(resultSet != null){
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntity socialAccountDelete(String id) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL doctorSocialAccountDelete(?) }");
            statement.setString(1, id);
            statement.execute();
            response = new ResponseEntity();
        }catch (SQLException e){
            response = new ResponseEntity(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<String> socialAccountAdd(SocialRequest<SocialAccount> request) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<String> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL doctorSocialAccountAdd(?, ?, ?) }");
            statement.setString(1, request.getUserID());
            statement.setString(2, request.getSocial().getLink());
            statement.setInt(3, request.getSocial().getType());
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(
                        resultSet.getString("id")
                );
            }else{
                response = new ResponseEntitySet<>(false, ErrorMessages.operationFailed);
            }
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
                if(resultSet != null){
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<List<SocialShare>> socialShares(String userID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<List<SocialShare>> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL doctorSocialShares(?) }");
            statement.setString(1, userID);
            resultSet = statement.executeQuery();
            List<SocialShare> shares = new ArrayList<>();
            while(resultSet.next()){
                shares.add(
                        new SocialShare(
                                resultSet.getString("id"),
                                resultSet.getInt("type"),
                                resultSet.getString("link"),
                                resultSet.getString("title")
                        )
                );
            }
            response = new ResponseEntitySet<>(shares);
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
                if(resultSet != null){
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntity socialShareDelete(String id) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL doctorSocialShareDelete(?) }");
            statement.setString(1, id);
            statement.execute();
            response = new ResponseEntity();
        }catch (SQLException e){
            response = new ResponseEntity(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<String> socialShareAdd(SocialRequest<SocialShare> request) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<String> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL doctorSocialShareAdd(?, ?,  ?, ?) }");
            statement.setString(1, request.getUserID());
            statement.setString(2, request.getSocial().getTitle());
            statement.setString(3, request.getSocial().getLink());
            statement.setInt(4, request.getSocial().getType());
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(
                        resultSet.getString("id")
                );
            }else{
                response = new ResponseEntitySet<>(false, ErrorMessages.operationFailed);
            }
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
                if(resultSet != null){
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<List<String>> doctorAvailableCities() throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<List<String>> response;
        try {
            statement = MysqlConnection.getInstance().prepareStatement(" { CALL availableDoctorCities() } ");
            resultSet = statement.executeQuery();
            List<String> provinceList = new ArrayList<>();
            while(resultSet.next()){
                provinceList.add(resultSet.getString("address"));
            }
            response = new ResponseEntitySet<>(
                    provinceList
            );
        } catch (SQLException e) {
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
                if(resultSet != null){
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntity contractAccept(String doctorID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement(" { CALL doctorContractAccept(?) } ");
            statement.setString(1, doctorID);
            statement.execute();
            response = new ResponseEntity();
        }catch (SQLException e){
            response = new ResponseEntity(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public byte isContractAccepted(String doctorID) throws SQLException, ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        byte result;
        try{
            statement = MysqlConnection.getInstance().prepareStatement(" { CALL doctorIsContractAccepted(?) } ");
            statement.setString(1, doctorID);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                result = resultSet.getByte("result");
            }else{
                throw new SQLException(ErrorMessages.operationFailed);
            }
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
                if(resultSet != null){
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public ResponseEntitySet<Integer> peakDemandCount(String doctorID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<Integer> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL doctorPeakDemandCount(?) }");
            statement.setString(1, doctorID);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(resultSet.getInt("result"));
            }else{
                response = new ResponseEntitySet<>(false, ErrorMessages.operationFailed);
            }
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
                if(resultSet != null){
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<List<Branch>> branches() throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<List<Branch>> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("SELECT * FROM branch_list");
            resultSet = statement.executeQuery();
            List<Branch> branches = new ArrayList<>();
            while(resultSet.next()){
                branches.add(
                        new Branch(
                                resultSet.getString("name"),
                                String.format("%04d", resultSet.getInt("branch_code"))
                        )
                );
            }
            response = new ResponseEntitySet<>(branches);
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
                if(resultSet != null){
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<LoginData> loginV2(DoctorLoginV2Request request, String verifyCode) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<LoginData> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL doctorLoginV2(?, ?, ?, ?) }");
            statement.setString(1, request.getUname());
            statement.setString(2, Functions.toSHA1(request.getPassword()));
            statement.setString(3, request.getType());
            statement.setString(4, verifyCode);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(new LoginData(resultSet.getString("phone"), verifyCode));
            }else{
                throw new SQLException(ErrorMessages.operationFailed);
            }
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false, e.getErrorCode(), e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
                if(resultSet != null){
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntity delete(String userID, byte role, String doctorID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL doctorDelete(?, ?, ?) }");
            statement.setString(1, userID);
            statement.setByte(2, role);
            statement.setString(3, doctorID);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                if(resultSet.getBoolean("result")){
                    response = new ResponseEntity();
                }else{
                    throw new SQLException(ErrorMessages.operationFailed);
                }
            }else{
                throw new SQLException(ErrorMessages.operationFailed);
            }
        }catch (SQLException e){
            response = new ResponseEntity(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
                if(resultSet != null){
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<DoctorAllDetail> allDetail(String userID, Byte userRole, String doctorID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<DoctorAllDetail> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL doctorFullDetail(?, ?, ?) }");
            statement.setString(1, userID);
            statement.setByte(2, userRole);
            statement.setString(3, doctorID);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(
                        new DoctorAllDetail(
                                resultSet.getString("userID"),
                                resultSet.getString("doctorCode"),
                                resultSet.getString("degree"),
                                resultSet.getString("phone"),
                                resultSet.getString("email"),
                                Functions.encodeBase64(resultSet.getBytes("picture")),
                                resultSet.getString("branch"),
                                resultSet.getString("introduceYourSelf"),
                                resultSet.getString("education"),
                                resultSet.getString("areaExperts"),
                                resultSet.getString("address"),
                                resultSet.getString("webAddress"),
                                resultSet.getInt("facilityID"),
                                resultSet.getInt("doctorType"),
                                resultSet.getBoolean("anonymous"),
                                resultSet.getString("name"),
                                resultSet.getString("surname"),
                                resultSet.getString("tc_number")
                                )
                );
            }else{
                throw new SQLException(ErrorMessages.notAccessDoctorInformation);
            }
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
                if(resultSet != null){
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntity updateSideAdmin(String userID, byte role, DoctorBaseProfile profile) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL  doctorDetailUpdateSideAdmin(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) }");
            statement.setString(1, userID);
            statement.setByte(2, role);
            statement.setString(3, profile.getDoctorID());
            statement.setString(4, profile.getDegree());
            statement.setObject(5, Functions.decodeBase64(profile.getPicture()));
            statement.setString(6, profile.getPhone());
            statement.setString(7, profile.getEmail());
            statement.setString(8, profile.getAddress());
            statement.setString(9, profile.getWebAddress());
            statement.setBoolean(10, profile.isAnonymous());
            statement.setInt(11, profile.getDoctorType());
            statement.setString(12, profile.getBranch());
            statement.setString(13, profile.getIntroduceYourSelf());
            statement.setString(14, profile.getEducation());
            statement.setString(15, profile.getAreaExperts());
            statement.setInt(16, profile.getFacilityID());
            statement.setString(17, profile.getTcNumber());
            statement.execute();
            response = new ResponseEntity();
        }catch (SQLException e){
            response = new ResponseEntity(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public Optional<DoctorRecipeInformation> recipeInformation(String userID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Optional<DoctorRecipeInformation> recipeInformation;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("SELECT facilityID, medula_password, branch_code, certificate_code FROM doctor LEFT JOIN doctor_recipe_information ON doctor.userID = doctor_recipe_information.doctorID WHERE doctorID = ?");
            statement.setString(1, userID);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                recipeInformation =  Optional.of(new DoctorRecipeInformation(
                      resultSet.getString("medula_password"),
                      resultSet.getInt("branch_code"),
                      resultSet.getInt("certificate_code"),
                      resultSet.getInt("facilityID")
                ));
            }else{
                recipeInformation = Optional.empty();
            }
        }catch (SQLException e){
            e.printStackTrace();
            recipeInformation = Optional.empty();
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
                if(resultSet != null) {
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return recipeInformation;
    }

    @Override
    public void recipeInformationCreate(DoctorRecipeInformationCreate doctorRecipeInformationCreate) throws ConnectionException, SQLException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        DoctorRecipeInformation information = doctorRecipeInformationCreate.getRecipeInformation();
        PreparedStatement statement = MysqlConnection.getInstance().prepareStatement("INSERT INTO doctor_recipe_information(medula_password, branch_code, certificate_code, doctorID, create_date) VALUE (?, ?, ?, ?, NOW())");
        statement.setString(1, information.getMedulaPassword());
        statement.setInt(2, information.getBranchCode());
        statement.setInt(3, information.getCertificateCode());
        statement.setString(4, doctorRecipeInformationCreate.getDoctorID());
        statement.execute();
    }

    @Override
    public void recipeInformationUpdate(DoctorRecipeInformationCreate doctorRecipeInformationCreate) throws ConnectionException, SQLException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        DoctorRecipeInformation information = doctorRecipeInformationCreate.getRecipeInformation();
        PreparedStatement statement = MysqlConnection.getInstance().prepareStatement("UPDATE doctor_recipe_information SET medula_password = ?, branch_code = ?, certificate_code = ? WHERE doctorID = ?");
        statement.setString(1, information.getMedulaPassword());
        statement.setInt(2, information.getBranchCode());
        statement.setInt(3, information.getCertificateCode());
        statement.setString(4, doctorRecipeInformationCreate.getDoctorID());
        statement.execute();
    }

    @Override
    public boolean isExistsRecipeInformation(String userID) throws ConnectionException, SQLException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = MysqlConnection.getInstance().prepareStatement("SELECT IF(EXISTS(SELECT * FROM doctor_recipe_information WHERE doctorID = ?), 1, 0) AS result");
        statement.setString(1, userID);
        ResultSet resultSet = statement.executeQuery();
        boolean result = false;
        if(resultSet.next()){
            result = resultSet.getBoolean("result");
        }
        try{
            statement.close();
            resultSet.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }
}
