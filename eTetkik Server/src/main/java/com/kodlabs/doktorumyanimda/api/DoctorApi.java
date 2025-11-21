package com.kodlabs.doktorumyanimda.api;

import com.kodlabs.doktorumyanimda.controller.Managers;
import com.kodlabs.doktorumyanimda.dto.doctor.ConsultationDoctorDTO;
import com.kodlabs.doktorumyanimda.mapper.ModelMapper;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.ckys.CkysRequest;
import com.kodlabs.doktorumyanimda.model.ckys.calisma.CalismaSonuc;
import com.kodlabs.doktorumyanimda.model.ckys.tahsil.TahsilSonuc;
import com.kodlabs.doktorumyanimda.model.doctor.*;
import com.kodlabs.doktorumyanimda.model.doctor.contract.ContractAcceptRequest;
import com.kodlabs.doktorumyanimda.model.doctor.detail.DoctorDetail;
import com.kodlabs.doktorumyanimda.model.doctor.detail.DoctorDetailUpdateRequest;
import com.kodlabs.doktorumyanimda.model.doctor.peak.PeakFeeDetail;
import com.kodlabs.doktorumyanimda.model.doctor.peak.PeakFeeRequest;
import com.kodlabs.doktorumyanimda.model.doctor.recipe.DoctorRecipeInformation;
import com.kodlabs.doktorumyanimda.model.doctor.recipe.DoctorRecipeInformationCreate;
import com.kodlabs.doktorumyanimda.model.doctor.registration.RegistrationInformation;
import com.kodlabs.doktorumyanimda.model.doctor.user.*;
import com.kodlabs.doktorumyanimda.model.social.SocialAccount;
import com.kodlabs.doktorumyanimda.model.social.SocialRequest;
import com.kodlabs.doktorumyanimda.model.social.SocialShare;
import com.kodlabs.doktorumyanimda.model.user.UserDoctor;
import com.kodlabs.doktorumyanimda.model.user.profile.DoctorProfile;
import com.kodlabs.doktorumyanimda.model.user.profile.DoctorProfileUpdate;
import com.kodlabs.doktorumyanimda.model.user.profile.ProfileUpdateRequest;
import com.kodlabs.doktorumyanimda.utils.AdminRole;
import com.kodlabs.doktorumyanimda.utils.Functions;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("/doctor")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DoctorApi {
    @Path("/login")
    @POST
    public ResponseEntity login(DoctorLoginRequest request) {
        return Managers.doctorManager.login(request);
    }

    @GET
    @Path("/registration/information")
    public ResponseEntitySet<List<RegistrationInformation>> registrationInformation(@QueryParam("tcNo") String tcNo,
            @QueryParam("registrationNo") String registrationNo,
            @QueryParam("serialNo") String serialNo) {
        return Managers.doctorManager.registrationInformation(tcNo, registrationNo, serialNo);
    }

    @Path("/v2/login")
    @POST
    public ResponseEntitySet<String> loginV2(@Context HttpServletRequest hsr, DoctorLoginV2Request request) {
        return Managers.doctorManager.loginV2(request, hsr);
    }

    @POST
    @Path("/login/verify")
    public ResponseEntitySet<UserDoctor> loginVerify(@Context HttpServletRequest hsr,
            DoctorLoginVerifyRequest request) {
        return Managers.doctorManager.loginVerify(request, hsr);
    }

    @GET
    @Path("/login/verify/new")
    public ResponseEntity newLoginVerifyCode(@QueryParam("phone") String phone,
            @QueryParam("deviceID") String deviceID) {
        return Managers.doctorManager.newVerifyCode(phone, deviceID);
    }

    @Path("/register")
    @POST
    public ResponseEntity register(@Context HttpServletRequest hsr, DoctorRegister request) {
        return Managers.doctorManager.register(request, hsr);
    }

    @Path("/{doctorID}/delete")
    @DELETE
    public ResponseEntity delete(@Context HttpServletRequest hsr, @QueryParam("userID") String userID,
            @QueryParam("type") String type, @PathParam("doctorID") String doctorID) {
        return Managers.doctorManager.delete(userID, AdminRole.getUserRole(type), doctorID, hsr);
    }

    @Path("/{doctorID}/detail/all")
    @GET
    public ResponseEntitySet<DoctorAllDetail> allDetail(@Context HttpServletRequest request,
            @QueryParam("userID") String userID,
            @QueryParam("type") String type,
            @PathParam("doctorID") String doctorID) {
        System.out.println(Functions.getClientIpAddress(request));
        return Managers.doctorManager.allDetail(userID, AdminRole.getUserRole(type), doctorID);
    }

    @Path("/update/side/admin")
    @POST
    public ResponseEntity updateSideAdmin(@Context HttpServletRequest hsr, DoctorUpdateSideAdminRequest request) {
        return Managers.doctorManager.updateSideAdmin(request, hsr);
    }

    @Path("/contract/accept")
    @POST
    public ResponseEntity contractAccept(@Context HttpServletRequest hsr, ContractAcceptRequest request) {
        return Managers.doctorManager.contractAccept(request, hsr);
    }

    @Path("/list")
    @GET
    public ResponseEntitySet<List<DoctorInformationForAdmin>> getDoctorList(@QueryParam("userID") String userID,
            @QueryParam("type") String type) {
        return Managers.doctorManager.doctorList(userID, type);
    }

    @Path("/consultation/list")
    @GET
    public ResponseEntitySet<List<ConsultationDoctorDTO>> doctorListForAnonymousState(
            @QueryParam("branch") String branch, @QueryParam("city") String city, @QueryParam("os") String os) {
        ResponseEntitySet<List<DoctorInformation>> doctorResponse = Managers.doctorManager
                .notAnonymousDoctorList(branch, city, os);
        if (doctorResponse.isSuccess) {
            List<ConsultationDoctorDTO> doctorDTOList = doctorResponse.getData().stream()
                    .map(item -> ModelMapper.getInstance().map(item, ConsultationDoctorDTO.class))
                    .collect(Collectors.toList());
            return new ResponseEntitySet<>(doctorDTOList);
        } else {
            return new ResponseEntitySet<>(false, doctorResponse.message);
        }
    }

    @Path("/information")
    @GET
    public ResponseEntitySet<DoctorInformation> getDoctorInformation(@QueryParam("doctorID") String doctorID) {
        return Managers.doctorManager.doctorInformation(doctorID);
    }

    @Path("/pay/information")
    @GET
    public ResponseEntitySet<DoctorPayInformation> payInformation(@QueryParam("drCodeOrID") String drCodeOrID) {
        return Managers.doctorManager.payInformation(drCodeOrID);
    }

    @Path("/update")
    @POST
    public ResponseEntity update(DoctorUpdateRequest request) {
        if (request.isValid()) {
            request.setPassword(Functions.toSHA256(request.getPassword()));
        }
        return Managers.doctorManager.doctorUpdate(request);
    }

    /* Branches */
    @Path("/branch/list")
    @GET
    public ResponseEntitySet<List<String>> branches() {
        ResponseEntitySet<List<Branch>> response = Managers.doctorManager.branches();
        if (response.isSuccess) {
            return new ResponseEntitySet<>(
                    response.getData().stream()
                            .map(Branch::getName)
                            .collect(Collectors.toList()));
        }
        return new ResponseEntitySet<>(false, response.message);
    }
    /* End */

    @Path("/branch/detail/list")
    @GET
    public ResponseEntitySet<List<Branch>> fullBranches() {
        return Managers.doctorManager.branches();
    }

    /* Profile */

    @GET
    @Path("/profile")
    public ResponseEntitySet<DoctorProfile> profile(@QueryParam("userID") String userID) {
        return Managers.doctorProfileManager.profile(userID);
    }

    @POST
    @Path("/profile/update")
    public ResponseEntity profileUpdate(@Context HttpServletRequest hsr, ProfileUpdateRequest<DoctorProfile> request) {
        return Managers.doctorProfileManager.update(request, hsr);
    }

    @PUT
    @Path("/{doctorID}/profile/update")
    public ResponseEntity profileUpdateV2(@Context HttpServletRequest hsr, @PathParam("doctorID") String doctorID,
            DoctorProfileUpdate profileUpdate) {
        return Managers.doctorProfileManager.updateV2(doctorID, profileUpdate, hsr);
    }

    /* Profile End */

    @Path("/password/change")
    @POST
    public ResponseEntity changePassword(ChangePasswordRequest request) {
        return Managers.doctorManager.changePassword(request);
    }

    @Path("/password/reset")
    @POST
    public ResponseEntity resetPassword(ResetPasswordRequest request) {
        return Managers.doctorManager.resetPassword(request);
    }

    /* Detail */
    @Path("/detail")
    @GET
    public ResponseEntitySet<DoctorDetail> detail(@QueryParam("doctorID") String doctorID) {
        return Managers.doctorManager.detail(doctorID);
    }

    @Path("/detail/update")
    @POST
    public ResponseEntity detailUpdate(DoctorDetailUpdateRequest request) {
        return Managers.doctorManager.detailUpdate(request);
    }

    @Path("/peak/demand/count")
    @GET
    public ResponseEntitySet<Integer> peakDemandCount(@QueryParam("doctorID") String doctorID) {
        return Managers.doctorManager.peakDemandCount(doctorID);
    }

    /* Peak Fee */
    @Path("/peak/fee")
    @GET
    public ResponseEntitySet<List<PeakFeeDetail>> peakFeeDetail(@QueryParam("doctorID") String doctorID) {
        return Managers.doctorManager.peakFeeDetail(doctorID);
    }

    @Path("/peak/fee/update")
    @PUT
    public ResponseEntity peakFeeDetailUpdate(PeakFeeRequest request) {
        return Managers.doctorManager.peakFeeDetailUpdate(request);
    }

    @Path("/peak/fee/add")
    @POST
    public ResponseEntitySet<String> peakFeeDetailAdd(PeakFeeRequest request) {
        return Managers.doctorManager.peakFeeDetailAdd(request);
    }

    @Path("/{doctorID}/peak/fee/{id}/delete")
    @DELETE
    public ResponseEntity peakFeeDetailDelete(@PathParam("doctorID") String doctorID, @PathParam("id") String id) {
        return Managers.doctorManager.peakFeeDetailDelete(doctorID, id);
    }

    /* Available Province */
    @Path("/available/cities")
    @GET
    public ResponseEntitySet<List<String>> doctorAvailableCities() {
        return Managers.doctorManager.doctorAvailableCities();
    }

    /* Balance */
    @Path("/balance")
    @GET
    public ResponseEntitySet<Integer> doctorBalance(@QueryParam("doctorID") String doctorID) {
        return Managers.doctorManager.drBalance(doctorID);
    }

    /* Social */
    @GET
    @Path("/social/accounts")
    public ResponseEntitySet<List<SocialAccount>> socialAccounts(@QueryParam("userID") String userID) {
        return Managers.doctorManager.socialAccounts(userID);
    }

    @DELETE
    @Path("/social/account/{id}/delete")
    public ResponseEntity socialAccountDelete(@PathParam("id") String id) {
        return Managers.doctorManager.socialAccountDelete(id);
    }

    @POST
    @Path("/social/account/add")
    public ResponseEntitySet<String> socialAccountAdd(SocialRequest<SocialAccount> request) {
        return Managers.doctorManager.socialAccountAdd(request);
    }

    @GET
    @Path("/social/shares")
    public ResponseEntitySet<List<SocialShare>> socialShares(@QueryParam("userID") String userID) {
        return Managers.doctorManager.socialShares(userID);
    }

    @DELETE
    @Path("/social/share/{id}/delete")
    public ResponseEntity socialShareDelete(@PathParam("id") String id) {
        return Managers.doctorManager.socialShareDelete(id);
    }

    @POST
    @Path("/social/share/add")
    public ResponseEntitySet<String> socialShareAdd(SocialRequest<SocialShare> request) {
        return Managers.doctorManager.socialShareAdd(request);
    }

    // Recipe Information
    @GET
    @Path("/{userID}/recipe/information")
    public ResponseEntitySet<DoctorRecipeInformation> recipeInformation(@PathParam("userID") String userID) {
        return Managers.doctorManager.recipeInformation(userID);
    }

    @POST
    @Path("/recipe/information")
    public ResponseEntity recipeInformationCreate(DoctorRecipeInformationCreate doctorRecipeInformationCreate) {
        return Managers.doctorManager.recipeInformationUpdate(doctorRecipeInformationCreate);
    }

    @POST
    @Path("/information/education")
    public ResponseEntitySet<List<TahsilSonuc>> educationInformation(CkysRequest request) {
        return Managers.doctorManager.tahsilBilgisiSorgula(request);
    }

    @GET
    @Path("/information/working")
    public ResponseEntitySet<List<CalismaSonuc>> workingInformation(@QueryParam("tcNo") String tcNo,
            @QueryParam("registrationNo") String registrationNo,
            @QueryParam("serialNo") String serialNo) {
        return Managers.doctorManager.calismaBilgisiSorgula(tcNo, registrationNo, serialNo);
    }
}
