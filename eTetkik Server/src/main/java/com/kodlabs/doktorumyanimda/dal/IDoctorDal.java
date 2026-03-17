package com.kodlabs.doktorumyanimda.dal;

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

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IDoctorDal {
        ResponseEntitySet<List<DoctorInformationForAdmin>> list(String userID, byte role) throws ConnectionException;

        ResponseEntitySet<List<DoctorInformation>> notAnonymousList(String branch, String city, String os)
                        throws ConnectionException;

        ResponseEntitySet<DoctorInformation> information(String doctorID) throws ConnectionException;

        ResponseEntitySet<DoctorPayInformation> payInformation(String code) throws ConnectionException;

        ResponseEntity update(DoctorUpdateRequest request) throws ConnectionException;

        ResponseEntitySet<Contact> contact(String userID) throws ConnectionException;

        ResponseEntitySet<LoginData> login(DoctorLoginV2Request request, String code) throws ConnectionException;

        ResponseEntitySet<UserDoctor> loginVerify(DoctorLoginVerifyRequest request) throws ConnectionException;

        ResponseEntity register(DoctorRegister request) throws ConnectionException;

        boolean isExistsID(String ID) throws ConnectionException;

        ResponseEntity changePassword(ChangePasswordRequest request) throws ConnectionException;

        ResponseEntitySet<DoctorDetail> detail(String doctorID) throws ConnectionException;

        ResponseEntity detailUpdate(DoctorDetailUpdateRequest request) throws ConnectionException;

        /* Peak Fee */
        ResponseEntitySet<List<PeakFeeDetail>> peakFeeDetail(String doctorID) throws ConnectionException;

        ResponseEntity peakFeeDetailUpdate(PeakFeeRequest request) throws ConnectionException;

        ResponseEntitySet<String> peakFeeDetailAdd(PeakFeeRequest request) throws ConnectionException;

        ResponseEntity peakFeeDetailDelete(String doctorID, String id) throws ConnectionException;

        ResponseEntitySet<String> updateVerifyCode(String phone, String verifyCode) throws ConnectionException;

        ResponseEntitySet<Integer> drBalance(String doctorID) throws ConnectionException;

        ResponseEntitySet<List<SocialAccount>> socialAccounts(String userID) throws ConnectionException;

        ResponseEntity socialAccountDelete(String id) throws ConnectionException;

        ResponseEntitySet<String> socialAccountAdd(SocialRequest<SocialAccount> request) throws ConnectionException;

        ResponseEntitySet<List<SocialShare>> socialShares(String userID) throws ConnectionException;

        ResponseEntity socialShareDelete(String id) throws ConnectionException;

        ResponseEntitySet<String> socialShareAdd(SocialRequest<SocialShare> request) throws ConnectionException;

        ResponseEntitySet<List<String>> doctorAvailableCities() throws ConnectionException;

        ResponseEntity contractAccept(String uNameOrDoctorID) throws ConnectionException;

        byte isContractAccepted(String uNameOrDoctorID) throws SQLException, ConnectionException;

        ResponseEntitySet<Integer> peakDemandCount(String doctorID) throws ConnectionException;

        ResponseEntitySet<List<Branch>> branches() throws ConnectionException;

        ResponseEntitySet<LoginData> loginV2(DoctorLoginV2Request request, String verifyCode)
                        throws ConnectionException;

        ResponseEntity delete(String userID, byte role, String doctorID) throws ConnectionException;

        ResponseEntitySet<DoctorAllDetail> allDetail(String userID, Byte userRole, String doctorID)
                        throws ConnectionException;

        ResponseEntity updateSideAdmin(String userID, byte role, DoctorBaseProfile profile) throws ConnectionException;

        Optional<DoctorRecipeInformation> recipeInformation(String userID) throws ConnectionException;

        boolean isExistsRecipeInformation(String userID) throws ConnectionException, SQLException;

        void recipeInformationCreate(DoctorRecipeInformationCreate doctorRecipeInformationCreate)
                        throws ConnectionException, SQLException;

        void recipeInformationUpdate(DoctorRecipeInformationCreate doctorRecipeInformationCreate)
                        throws ConnectionException, SQLException;

        ResponseEntity updatePrice(String doctorID, BigDecimal price) throws ConnectionException;
}
