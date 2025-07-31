package com.kodlabs.doktorumyanimda.dal;

import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.contact.Contact;
import com.kodlabs.doktorumyanimda.model.patient.Patient;
import com.kodlabs.doktorumyanimda.model.patient.PatientForAdmin;
import com.kodlabs.doktorumyanimda.model.patient.PatientStatusUpdateRequest;
import com.kodlabs.doktorumyanimda.model.patient.enabiz.service.EnabizInformationServiceCreateRequest;
import com.kodlabs.doktorumyanimda.model.patient.enabiz.service.EnabizServiceInformation;
import com.kodlabs.doktorumyanimda.model.patient.inspection.Inspection;
import com.kodlabs.doktorumyanimda.model.patient.inspection.InspectionContent;
import com.kodlabs.doktorumyanimda.model.patient.notes.*;
import com.kodlabs.doktorumyanimda.model.patient.systakipno.PatientSysTakipNo;
import com.kodlabs.doktorumyanimda.model.patient.systakipno.PatientSysTakipNoCreateRequest;
import com.kodlabs.doktorumyanimda.model.patient.systakipno.PatientSysTakipNoUpdateRequest;
import com.kodlabs.doktorumyanimda.model.patient.user.PatientLoginV2Request;
import com.kodlabs.doktorumyanimda.model.patient.user.PatientLoginVerifyRequest;
import com.kodlabs.doktorumyanimda.model.patient.user.PatientSingUpV2Request;
import com.kodlabs.doktorumyanimda.model.user.LoginData;
import com.kodlabs.doktorumyanimda.model.user.UserPatient;

import java.util.List;

public interface IPatientDal {
    ResponseEntitySet<List<PatientForAdmin>> getAllPatients() throws ConnectionException;
    ResponseEntitySet<List<Patient>> getAllDoctorPatients(String doctorID) throws ConnectionException;

    ResponseEntitySet<String> updateVerifyCode(String phone, String verifyCode) throws ConnectionException;
    ResponseEntitySet<String> create(String phone, String verifyCode) throws ConnectionException;
    ResponseEntitySet<UserPatient> loginVerify(PatientLoginVerifyRequest request) throws ConnectionException;

    ResponseEntitySet<Contact> contact(String phoneOrUserID) throws ConnectionException;
    ResponseEntitySet<Patient> information(String patientID) throws ConnectionException;

    ResponseEntity patientStatusUpdate(PatientStatusUpdateRequest request) throws ConnectionException;
    ResponseEntitySet<String> patientGetStatus(String patientID) throws ConnectionException;

    ResponseEntitySet<List<Patient>> doctorOldPatients(String doctorID) throws ConnectionException;

    ResponseEntitySet<LoginData> loginV2(PatientLoginV2Request request, String verifyCode) throws ConnectionException;

    ResponseEntitySet<UserPatient> singUpV2(PatientSingUpV2Request request) throws ConnectionException;


    ResponseEntity inspectionDelete(String inspectionID) throws ConnectionException;

    boolean inspectionExists(String inspectionID) throws ConnectionException;


    ResponseEntitySet<List<Inspection>> inspectionList(String patientID, String doctorID) throws  ConnectionException;

    boolean inspectionContentExists(String contentID) throws ConnectionException;

    ResponseEntitySet<String> inspectionContentCreate(InspectionContent inspectionContent) throws ConnectionException;

    ResponseEntitySet<List<InspectionContent>> inspectionContentList(String inspectionID) throws ConnectionException;

    ResponseEntity inspectionContentDelete(String inspectionContentID) throws ConnectionException;

    ResponseEntity inspectionContentUpdate(InspectionContent inspectionContent) throws ConnectionException;


    /* Patient Notes */

    ResponseEntitySet<String> notesCreate(String patientID, String doctorID) throws ConnectionException;

    boolean noteExist(String patientID, String doctorID) throws ConnectionException;
    ResponseEntitySet<String> notes(String patientID, String doctorID) throws ConnectionException;

    ResponseEntitySet<PatientNotes> note(String patientID, String doctorID) throws ConnectionException;

    ResponseEntitySet<PatientNotesContent> noteContentCreate(PatientNotesContentCreateRequest request) throws ConnectionException;

    ResponseEntity noteContentUpdate(PatientNotesContentUpdateRequest request) throws ConnectionException;

    ResponseEntitySet<List<PatientNotesContent>> noteContents(String noteID) throws ConnectionException;

    ResponseEntity noteContentDelete(String contentID) throws ConnectionException;

    /* Patient Notes End */

    /* Patient SysTakipNo */
    boolean sysTakipNoExists(String tcNumber, String doctorID) throws ConnectionException;
    boolean sysTakipNoExists(String sysTakipNo) throws ConnectionException;

    ResponseEntity sysTakipNoCreate(PatientSysTakipNoCreateRequest request) throws ConnectionException;

    ResponseEntity sysTakipNoUpdate(PatientSysTakipNoUpdateRequest request) throws ConnectionException;
    ResponseEntitySet<List<PatientSysTakipNo>> sysTakipNoList(String doctorID) throws ConnectionException;

    ResponseEntitySet<String> sysTakipNo(String tcNumber, String doctorID) throws ConnectionException;

    /* Patient SysTakipNo End */

    /* Patient Enabiz Service Information*/

    ResponseEntitySet<List<EnabizServiceInformation>> getEnabizServiceInformation(String sysTakipNo) throws ConnectionException;

    ResponseEntity createEnabizServiceInformation(EnabizInformationServiceCreateRequest request) throws ConnectionException;

    boolean existsEnabizServiceReferenceNumber(String serviceReferenceNumber) throws ConnectionException;

    ResponseEntity deleteEnabizServiceInformation(String serviceReferenceNumber) throws ConnectionException;

}
