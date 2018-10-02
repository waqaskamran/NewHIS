package com.sd.his.controller.patient;


import com.sd.his.enums.ResponseEnum;
import com.sd.his.model.Patient;
import com.sd.his.model.SmokingStatus;
import com.sd.his.service.AWSService;
import com.sd.his.service.PatientService;
import com.sd.his.utill.HISCoreUtil;
import com.sd.his.wrapper.GenericAPIResponse;
import com.sd.his.wrapper.PatientWrapper;
import com.sd.his.wrapper.SmokingStatusWrapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

/*
 * @author    : Muhammad Jamal
 * @Date      : 5-Jun-18
 * @version   : ver. 1.0.0
 *
 * ________________________________________________________________________________________________
 *
 *  Developer				Date		     Version		Operation		Description
 * ________________________________________________________________________________________________
 *
 *
 * ________________________________________________________________________________________________
 *
 * @Project   : HIS
 * @Package   : com.sd.his.controller.patient
 * @FileName  : PatientAPI
 *
 * Copyright ©
 * SolutionDots,
 * All rights reserved.
 *
 */
@RestController
@RequestMapping("/patient")
public class PatientAPI {

    private final Logger logger = LoggerFactory.getLogger(PatientAPI.class);
    private ResourceBundle messageBundle = ResourceBundle.getBundle("messages");
    @Autowired
    private PatientService patientService;
    @Autowired
    AWSService awsService;

    @ApiOperation(httpMethod = "POST", value = "Save patient",
            notes = "This method will save the patient.",
            produces = "application/json", nickname = "Save patient",
            response = GenericAPIResponse.class, protocols = "https")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Save patient successfully ", response = GenericAPIResponse.class),
            @ApiResponse(code = 401, message = "Oops, your fault. You are not authorized to access.", response = GenericAPIResponse.class),
            @ApiResponse(code = 403, message = "Oops, your fault. You are forbidden.", response = GenericAPIResponse.class),
            @ApiResponse(code = 404, message = "Oops, my fault System did not find your desire resource.", response = GenericAPIResponse.class),
            @ApiResponse(code = 500, message = "Oops, my fault. Something went wrong on the server side.", response = GenericAPIResponse.class)})
    @RequestMapping( value = "/save", method = RequestMethod.POST  )//, consumes = "multipart/form-data"
    public ResponseEntity<?> savePatient(HttpServletRequest request,
                                         @RequestPart("patientRequest") PatientWrapper patientWrapper,
                                         @RequestPart(name = "profileImg", required = false) MultipartFile profileImg,
                                         @RequestPart(name = "photoFront", required = false) MultipartFile photoFront,
                                         @RequestPart(name = "photoBack", required = false) MultipartFile photoBack) {
        logger.info("savePatient API - initiated..");
        GenericAPIResponse response = new GenericAPIResponse();
        try {
            if(profileImg != null) patientWrapper.setProfileImg(profileImg.getBytes());
            if(photoFront != null) patientWrapper.setPhotoFront(photoFront.getBytes());
            if(photoBack != null) patientWrapper.setPhotoBack(photoBack.getBytes());

            if (patientWrapper.getEmail()!=null && !patientWrapper.getEmail().isEmpty() && patientService.isEmailAlreadyExists(patientWrapper.getEmail())) {
                response.setResponseMessage(messageBundle.getString("user.add.email.already-found.error"));
                response.setResponseCode(ResponseEnum.USER_ALREADY_EXIST_ERROR.getValue());
                response.setResponseStatus(ResponseEnum.ERROR.getValue());
                response.setResponseData(null);
                logger.error("savePatient API - email already found.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            /*if (userService.isUserNameAlreadyExists(patientWrapper.getUserName())) {
                response.setResponseMessage(messageBundle.getString("user.add.already-found.error"));
                response.setResponseCode(ResponseEnum.USER_ALREADY_EXIST_ERROR.getValue());
                response.setResponseStatus(ResponseEnum.ERROR.getValue());
                response.setResponseData(null);
                logger.error("savePatient API - user already found.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }*/
            patientService.savePatient(patientWrapper);

            response.setResponseMessage(messageBundle.getString("patient.save.success"));
            response.setResponseCode(ResponseEnum.PATIENT_SAVE_SUCCESS.getValue());
            response.setResponseStatus(ResponseEnum.SUCCESS.getValue());
            logger.error("savePatient API - successfully saved.");

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("savePatient exception.", e.fillInStackTrace());
            response.setResponseStatus(ResponseEnum.ERROR.getValue());
            response.setResponseCode(ResponseEnum.EXCEPTION.getValue());
            response.setResponseMessage(messageBundle.getString("exception.occurs"));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "GET", value = "Paginated Patients",
            notes = "This method will return Paginated Patients",
            produces = "application/json", nickname = "Paginated Patients",
            response = GenericAPIResponse.class, protocols = "https")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Patients fetched successfully.", response = GenericAPIResponse.class),
            @ApiResponse(code = 401, message = "Oops, your fault. You are not authorized to access.", response = GenericAPIResponse.class),
            @ApiResponse(code = 403, message = "Oops, your fault. You are forbidden.", response = GenericAPIResponse.class),
            @ApiResponse(code = 404, message = "Oops, my fault System did not find your desire resource.", response = GenericAPIResponse.class),
            @ApiResponse(code = 500, message = "Oops, my fault. Something went wrong on the server side.", response = GenericAPIResponse.class)})
    @RequestMapping(value = "/{page}", method = RequestMethod.GET)
    public ResponseEntity<?> getAllPaginatedPatients(HttpServletRequest request,
                                                           @PathVariable("page") int page,
                                                           @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {

        logger.error("getAllPaginatedPatients API initiated");
        GenericAPIResponse response = new GenericAPIResponse();
        response.setResponseMessage(messageBundle.getString("patient.fetch.error"));
        response.setResponseCode(ResponseEnum.PATIENT_FETCHED_ERROR.getValue());
        response.setResponseStatus(ResponseEnum.ERROR.getValue());
        response.setResponseData(null);

        try {
            logger.error("findAllPaginatedPatients -  fetching from DB");
            List<PatientWrapper> patients = patientService.findAllPaginatedPatients(page, pageSize);
            int userCount = patientService.countAllPaginatedPatients();
            logger.error("findAllPaginatedPatients - fetched successfully");

            if (!HISCoreUtil.isListEmpty(patients)) {
                Integer nextPage, prePage, currPage;
                int[] pages;

                if (userCount > pageSize) {
                    int remainder = userCount % pageSize;
                    int totalPages = userCount / pageSize;
                    if (remainder > 0) {
                        totalPages = totalPages + 1;
                    }
                    pages = new int[totalPages];
                    pages = IntStream.range(0, totalPages).toArray();
                    currPage = page;
                    nextPage = (currPage + 1) != totalPages ? currPage + 1 : null;
                    prePage = currPage > 0 ? currPage : null;
                } else {
                    pages = new int[1];
                    pages[0] = 0;
                    currPage = 0;
                    nextPage = null;
                    prePage = null;
                }

                Map<String, Object> returnValues = new LinkedHashMap<>();
                returnValues.put("nextPage", nextPage);
                returnValues.put("prePage", prePage);
                returnValues.put("currPage", currPage);
                returnValues.put("pages", pages);
                returnValues.put("data", patients);

                response.setResponseMessage(messageBundle.getString("patient.fetched.success"));
                response.setResponseCode(ResponseEnum.PATIENT_FETCHED_SUCCESS.getValue());
                response.setResponseStatus(ResponseEnum.SUCCESS.getValue());
                response.setResponseData(returnValues);

                logger.error("getAllPaginatedPatients API successfully executed.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception ex) {
            logger.error("getAllPaginatedPatients exception..", ex.fillInStackTrace());
            response.setResponseStatus(ResponseEnum.ERROR.getValue());
            response.setResponseCode(ResponseEnum.EXCEPTION.getValue());
            response.setResponseMessage(messageBundle.getString("exception.occurs"));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "GET", value = "patient",
            notes = "This method will return User on base of id",
            produces = "application/json", nickname = "Get Single User",
            response = GenericAPIResponse.class, protocols = "https")
    @ApiResponses({
            @ApiResponse(code = 200, message = "patient found successfully", response = GenericAPIResponse.class),
            @ApiResponse(code = 401, message = "Oops, your fault. You are not authorized to access.", response = GenericAPIResponse.class),
            @ApiResponse(code = 403, message = "Oops, your fault. You are forbidden.", response = GenericAPIResponse.class),
            @ApiResponse(code = 404, message = "Oops, my fault System did not find your desire resource.", response = GenericAPIResponse.class),
            @ApiResponse(code = 500, message = "Oops, my fault. Something went wrong on the server side.", response = GenericAPIResponse.class)})
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getPatientById(HttpServletRequest request, @PathVariable("id") long id) {

        GenericAPIResponse response = new GenericAPIResponse();
        response.setResponseMessage(messageBundle.getString("patient.search.not.found"));
        response.setResponseCode(ResponseEnum.USER_NOT_FOUND.getValue());
        response.setResponseStatus(ResponseEnum.ERROR.getValue());
        response.setResponseData(null);

        try {
            PatientWrapper patientWrapper = this.patientService.getPatientById(id);
            if (HISCoreUtil.isValidObject(patientWrapper)) {
                response.setResponseData(patientWrapper);
                response.setResponseMessage(messageBundle.getString("patient.found"));
                response.setResponseCode(ResponseEnum.USER_FOUND.getValue());
                response.setResponseStatus(ResponseEnum.SUCCESS.getValue());
                logger.info("User Found successfully...");

                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.setResponseData(null);
                response.setResponseMessage(messageBundle.getString("patient.search.not.found"));
                response.setResponseCode(ResponseEnum.PATIENT_FETCHED_SUCCESS.getValue());
                response.setResponseStatus(ResponseEnum.ERROR.getValue());
                logger.info("User Not Found ...");
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("User Not Found", ex.fillInStackTrace());
            response.setResponseData("");
            response.setResponseStatus(ResponseEnum.ERROR.getValue());
            response.setResponseCode(ResponseEnum.EXCEPTION.getValue());
            response.setResponseMessage(messageBundle.getString("exception.occurs"));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(httpMethod = "POST", value = "Update patient",
            notes = "This method will Update the patient.",
            produces = "application/json", nickname = "Update patient",
            response = GenericAPIResponse.class, protocols = "https")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Update patient successfully ", response = GenericAPIResponse.class),
            @ApiResponse(code = 401, message = "Oops, your fault. You are not authorized to access.", response = GenericAPIResponse.class),
            @ApiResponse(code = 403, message = "Oops, your fault. You are forbidden.", response = GenericAPIResponse.class),
            @ApiResponse(code = 404, message = "Oops, my fault System did not find your desire resource.", response = GenericAPIResponse.class),
            @ApiResponse(code = 500, message = "Oops, my fault. Something went wrong on the server side.", response = GenericAPIResponse.class)})
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<?> updatePatient(HttpServletRequest request,
                                           @RequestBody PatientWrapper patientRequest) {
        logger.info("updatePatient API - initiated.");
        GenericAPIResponse response = new GenericAPIResponse();
        try {
            if (patientRequest.getId() <= 0) {
                response.setResponseMessage(messageBundle.getString("insufficient.parameter"));
                response.setResponseCode(ResponseEnum.INSUFFICIENT_PARAMETERS.getValue());
                response.setResponseStatus(ResponseEnum.ERROR.getValue());
                response.setResponseData(null);
                logger.error("updatePatient API - Please select proper user, userId not available with request patientRequest.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            patientService.savePatient(patientRequest);
            response.setResponseMessage(messageBundle.getString("patient.update.success"));
            response.setResponseCode(ResponseEnum.PATIENT_UPDATE_SUCCESS.getValue());
            response.setResponseStatus(ResponseEnum.SUCCESS.getValue());
            response.setResponseData(null);
            logger.error("updatePatient API - successfully updated.");

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("updatePatient exception.", e.fillInStackTrace());
            response.setResponseStatus(ResponseEnum.ERROR.getValue());
            response.setResponseCode(ResponseEnum.EXCEPTION.getValue());
            response.setResponseMessage(messageBundle.getString("exception.occurs"));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @ApiOperation(httpMethod = "POST", value = "Add/Update patient Smoke Status",
            notes = "This method will Update the patient Smoke Status.",
            produces = "application/json", nickname = "Update Smoke Status",
            response = GenericAPIResponse.class, protocols = "https")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Add/Update patient successfully ", response = GenericAPIResponse.class),
            @ApiResponse(code = 401, message = "Oops, your fault. You are not authorized to access.", response = GenericAPIResponse.class),
            @ApiResponse(code = 403, message = "Oops, your fault. You are forbidden.", response = GenericAPIResponse.class),
            @ApiResponse(code = 404, message = "Oops, my fault System did not find your desire resource.", response = GenericAPIResponse.class),
            @ApiResponse(code = 500, message = "Oops, my fault. Something went wrong on the server side.", response = GenericAPIResponse.class)})
    @RequestMapping(value = "/smokeStatus/addUpdate", method = RequestMethod.POST)
    public ResponseEntity<?> updateSmokeStatus(HttpServletRequest request,
                                           @RequestBody SmokingStatusWrapper smokingStatusRequest) {
        logger.info("updateSmokeStatus API - initiated.");
        GenericAPIResponse response = new GenericAPIResponse();
        try {
            if( smokingStatusRequest.getSmokingId()==null && smokingStatusRequest.getSmokingStatus()!=null && !smokingStatusRequest.getSmokingStatus().isEmpty()){
                SmokingStatus smokeStatus = new SmokingStatus();
                Patient patient = patientService.getPatientById(smokingStatusRequest.getPatientId() );
                patientService.populateSmokeStatus(smokingStatusRequest, smokeStatus);
                smokeStatus.setPatient(patient);
                patientService.savePatientSmokeStatus(smokeStatus);
                response.setResponseMessage(messageBundle.getString("smoke.status.update.success"));
                response.setResponseCode(ResponseEnum.SMOKE_STATUS_SAVE_SUCCESS.getValue());
                response.setResponseStatus(ResponseEnum.SUCCESS.getValue());
                response.setResponseData(null);
                logger.error("smokeStatus API - "+messageBundle.getString("smoke.status.update.success"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("updateSmokeStatus exception.", e.fillInStackTrace());
            response.setResponseStatus(ResponseEnum.ERROR.getValue());
            response.setResponseCode(ResponseEnum.EXCEPTION.getValue());
            response.setResponseMessage(messageBundle.getString("exception.occurs"));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "DELETE", value = "Delete patient",
            notes = "This method will Delete the patient",
            produces = "application/json", nickname = "Delete patient ",
            response = GenericAPIResponse.class, protocols = "https")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Deleted Smoke Status successfully", response = GenericAPIResponse.class),
            @ApiResponse(code = 401, message = "Oops, your fault. You are not authorized to access.", response = GenericAPIResponse.class),
            @ApiResponse(code = 403, message = "Oops, your fault. You are forbidden.", response = GenericAPIResponse.class),
            @ApiResponse(code = 404, message = "Oops, my fault System did not find your desire resource.", response = GenericAPIResponse.class),
            @ApiResponse(code = 500, message = "Oops, my fault. Something went wrong on the server side.", response = GenericAPIResponse.class)})
    @RequestMapping(value = "/smokeStatus/delete/{smokingId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deletePatient(HttpServletRequest request,
                                           @PathVariable("smokingId") Long smokingId) {
        logger.info("deleteSmokingStatus API - Called..");
        GenericAPIResponse response = new GenericAPIResponse();
        response.setResponseMessage(messageBundle.getString("patient.delete.error"));
        response.setResponseCode(ResponseEnum.PATIENT_DELETE_ERROR.getValue());
        response.setResponseStatus(ResponseEnum.ERROR.getValue());
        response.setResponseData(null);

        try {
            if (smokingId <= 0) {
                response.setResponseMessage(messageBundle.getString("insufficient.parameter"));
                response.setResponseCode(ResponseEnum.INSUFFICIENT_PARAMETERS.getValue());
                response.setResponseStatus(ResponseEnum.ERROR.getValue());
                response.setResponseData(null);
                logger.error("deleteSmokingStatus API - insufficient params.");

                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            patientService.deleteSmokeStatusById(smokingId);
            response.setResponseMessage(messageBundle.getString("smoke.status.delete.success"));
            response.setResponseCode(ResponseEnum.SMOKE_STATUS_DELETE_SUCCESS.getValue());
            response.setResponseStatus(ResponseEnum.SUCCESS.getValue());
            response.setResponseData(null);
            logger.info("deleteSmokingStatus API - Deleted Successfully...");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("deleteSmokingStatus API - deleted failed.", ex.fillInStackTrace());
            response.setResponseData("");
            response.setResponseStatus(ResponseEnum.ERROR.getValue());
            response.setResponseCode(ResponseEnum.EXCEPTION.getValue());
            response.setResponseMessage(messageBundle.getString("exception.occurs"));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(httpMethod = "DELETE", value = "Delete patient",
            notes = "This method will Delete the patient",
            produces = "application/json", nickname = "Delete patient ",
            response = GenericAPIResponse.class, protocols = "https")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Deleted patient successfully", response = GenericAPIResponse.class),
            @ApiResponse(code = 401, message = "Oops, your fault. You are not authorized to access.", response = GenericAPIResponse.class),
            @ApiResponse(code = 403, message = "Oops, your fault. You are forbidden.", response = GenericAPIResponse.class),
            @ApiResponse(code = 404, message = "Oops, my fault System did not find your desire resource.", response = GenericAPIResponse.class),
            @ApiResponse(code = 500, message = "Oops, my fault. Something went wrong on the server side.", response = GenericAPIResponse.class)})
    @RequestMapping(value = "/delete/{patientId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deletePatient(HttpServletRequest request,
                                           @PathVariable("patientId") long patientId) {
        logger.info("deletePatient API - Called..");
        GenericAPIResponse response = new GenericAPIResponse();
        response.setResponseMessage(messageBundle.getString("patient.delete.error"));
        response.setResponseCode(ResponseEnum.PATIENT_DELETE_ERROR.getValue());
        response.setResponseStatus(ResponseEnum.ERROR.getValue());
        response.setResponseData(null);

        try {
            if (patientId <= 0) {
                response.setResponseMessage(messageBundle.getString("insufficient.parameter"));
                response.setResponseCode(ResponseEnum.INSUFFICIENT_PARAMETERS.getValue());
                response.setResponseStatus(ResponseEnum.ERROR.getValue());
                response.setResponseData(null);
                logger.error("deletePatient API - insufficient params.");

                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            patientService.deletePatientById(patientId);
            response.setResponseMessage(messageBundle.getString("patient.delete.success"));
            response.setResponseCode(ResponseEnum.PATIENT_DELETE_SUCCESS.getValue());
            response.setResponseStatus(ResponseEnum.SUCCESS.getValue());
            response.setResponseData(null);
            logger.info("deleteServiceTax API - Deleted Successfully...");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("deletePatient API - deleted failed.", ex.fillInStackTrace());
            response.setResponseData("");
            response.setResponseStatus(ResponseEnum.ERROR.getValue());
            response.setResponseCode(ResponseEnum.EXCEPTION.getValue());
            response.setResponseMessage(messageBundle.getString("exception.occurs"));

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ApiOperation(httpMethod = "GET", value = "Paginated Patients Search",
        notes = "This method will return Paginated Patients Search",
        produces = "application/json", nickname = "Paginated Patients Search",
        response = GenericAPIResponse.class, protocols = "https")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Paginated Patients Search fetched successfully.", response = GenericAPIResponse.class),
            @ApiResponse(code = 401, message = "Oops, your fault. You are not authorized to access.", response = GenericAPIResponse.class),
            @ApiResponse(code = 403, message = "Oops, your fault. You are forbidden.", response = GenericAPIResponse.class),
            @ApiResponse(code = 404, message = "Oops, my fault System did not find your desire resource.", response = GenericAPIResponse.class),
            @ApiResponse(code = 500, message = "Oops, my fault. Something went wrong on the server side.", response = GenericAPIResponse.class)})
    @RequestMapping(value = "/search{page}", method = RequestMethod.GET)
    public ResponseEntity<?> getSearchAllPaginatedPatients(HttpServletRequest request,
                                                                 @PathVariable("page") int page,
                                                                 @RequestParam(value = "pageSize",
                                                                         required = false,
                                                                         defaultValue = "10") int pageSize,
                                                                 @RequestParam(value = "searchString") String searchString) { //searchString may contain patient name or cell number

        logger.error("getSearchAllPaginatedPatients API initiated");
        GenericAPIResponse response = new GenericAPIResponse();
        try {
            logger.error("searchAllPaginatedPatient -  fetching from DB");
            List<PatientWrapper> patients = patientService.searchAllPaginatedPatient(page, pageSize, searchString);
            int userCount = patients.size();

            logger.error("searchAllPaginatedPatient - fetched successfully");

            if (!HISCoreUtil.isListEmpty(patients)) {
                Integer nextPage, prePage, currPage;
                int[] pages;

                if (userCount > pageSize) {
                    int remainder = userCount % pageSize;
                    int totalPages = userCount / pageSize;
                    if (remainder > 0) {
                        totalPages = totalPages + 1;
                    }
                    pages = new int[totalPages];
                    pages = IntStream.range(0, totalPages).toArray();
                    currPage = page;
                    nextPage = (currPage + 1) != totalPages ? currPage + 1 : null;
                    prePage = currPage > 0 ? currPage : null;
                } else {
                    pages = new int[1];
                    pages[0] = 0;
                    currPage = 0;
                    nextPage = null;
                    prePage = null;
                }

                Map<String, Object> returnValues = new LinkedHashMap<>();
                returnValues.put("nextPage", nextPage);
                returnValues.put("prePage", prePage);
                returnValues.put("currPage", currPage);
                returnValues.put("pages", pages);
                returnValues.put("data", patients);

                response.setResponseMessage(messageBundle.getString("patient.fetched.success"));
                response.setResponseCode(ResponseEnum.PATIENT_FETCHED_SUCCESS.getValue());
                response.setResponseStatus(ResponseEnum.SUCCESS.getValue());
                response.setResponseData(returnValues);

                logger.error("getSearchAllPaginatedPatients API successfully executed.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.setResponseMessage(messageBundle.getString("patient.fetch.error"));
                response.setResponseCode(ResponseEnum.PATIENT_FETCHED_ERROR.getValue());
                response.setResponseStatus(ResponseEnum.ERROR.getValue());
                response.setResponseData(null);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

        } catch (Exception ex) {
            logger.error("getSearchAllPaginatedUserByUserType exception..", ex.fillInStackTrace());
            response.setResponseStatus(ResponseEnum.ERROR.getValue());
            response.setResponseCode(ResponseEnum.EXCEPTION.getValue());
            response.setResponseMessage(messageBundle.getString("exception.occurs"));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //all patient list Api
    @ApiOperation(httpMethod = "GET", value = "All patient",
            notes = "This method will return all patient",
            produces = "application/json", nickname = "All patient",
            response = GenericAPIResponse.class, protocols = "https")
    @ApiResponses({
            @ApiResponse(code = 200, message = "All Patients fetched successfully.", response = GenericAPIResponse.class),
            @ApiResponse(code = 401, message = "Oops, your fault. You are not authorized to access.", response = GenericAPIResponse.class),
            @ApiResponse(code = 403, message = "Oops, your fault. You are forbidden.", response = GenericAPIResponse.class),
            @ApiResponse(code = 404, message = "Oops, my fault System did not find your desire resource.", response = GenericAPIResponse.class),
            @ApiResponse(code = 500, message = "Oops, my fault. Something went wrong on the server side.", response = GenericAPIResponse.class)})
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> getAllPatients(HttpServletRequest request) {

        logger.error("getAll Patients API initiated");
        GenericAPIResponse response = new GenericAPIResponse();
        response.setResponseMessage(messageBundle.getString("patient.fetch.error"));
        response.setResponseCode(ResponseEnum.PATIENT_FETCHED_ERROR.getValue());
        response.setResponseStatus(ResponseEnum.ERROR.getValue());
        response.setResponseData(null);

        try {
            logger.error("getALL Patients API - Patients fetching from DB");
            List<PatientWrapper> patientList = patientService.getAllPatient();
            if (HISCoreUtil.isListEmpty(patientList)) {
                response.setResponseMessage(messageBundle.getString("patient.not.found"));
                response.setResponseCode(ResponseEnum.PATIENT_NOT_FOUND_ERROR.getValue());
                response.setResponseStatus(ResponseEnum.ERROR.getValue());
                response.setResponseData(null);
                logger.error("getAllPatient API - patient not found");

                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            response.setResponseMessage(messageBundle.getString("patient.fetched.success"));
            response.setResponseCode(ResponseEnum.PATIENT_FETCHED_SUCCESS.getValue());
            response.setResponseStatus(ResponseEnum.SUCCESS.getValue());
            response.setResponseData(patientList);

            logger.error("getAllPatients API - Patients successfully fetched.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("getAllPatients API -  exception..", ex.fillInStackTrace());
            response.setResponseStatus(ResponseEnum.ERROR.getValue());
            response.setResponseCode(ResponseEnum.EXCEPTION.getValue());
            response.setResponseMessage(messageBundle.getString("exception.occurs"));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(httpMethod = "GET", value = "Upload Profile Image",
            notes = "This method will upload the profile image of any user.",
            produces = "application/json", nickname = "Upload Profile Image",
            response = GenericAPIResponse.class, protocols = "https")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Profile image of user uploaded successfully.", response = GenericAPIResponse.class),
            @ApiResponse(code = 401, message = "Oops, your fault. You are not authorized to access.", response = GenericAPIResponse.class),
            @ApiResponse(code = 403, message = "Oops, your fault. You are forbidden.", response = GenericAPIResponse.class),
            @ApiResponse(code = 404, message = "Oops, my fault System did not find your desire resource.", response = GenericAPIResponse.class),
            @ApiResponse(code = 500, message = "Oops, my fault. Something went wrong on the server side.", response = GenericAPIResponse.class)})
    @RequestMapping(value = "/uploadProfileImg/{id}", method = RequestMethod.POST,
            headers = ("content-type=multipart/*"))
    public ResponseEntity<?> uploadProfileImage(HttpServletRequest request,
                                                @PathVariable("id") long id,
                                                @RequestParam("file") MultipartFile file) {
        logger.info("uploadProfileImage API called for user: " + id);
        GenericAPIResponse response = new GenericAPIResponse();
        response.setResponseMessage(messageBundle.getString("user.profile.image.uploaded.error"));
        response.setResponseCode(ResponseEnum.USER_PROFILE_IMG_UPLOAD_FAILED.getValue());
        response.setResponseStatus(ResponseEnum.ERROR.getValue());
        response.setResponseData(null);

        try {
            Patient patient = patientService.findPatientByID(id);

        //    if (HISCoreUtil.isValidObject(user)) {
                if (HISCoreUtil.isValidObject(file)) {
                    byte[] byteArr = new byte[0];
                    try {
                        byteArr = file.getBytes();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    InputStream is = new ByteArrayInputStream(byteArr);
                    Boolean isSaved = awsService.uploadImage(is, id);
                    if (isSaved) {
                        String imgURL = awsService.getProfileImageUrl(id);
                    //    user.getProfile().setProfileImgURL(imgURL);
                        patient.setProfileImgURL(imgURL);
                        patientService.savePatientUpadtedImage(patient);

                        response.setResponseMessage(messageBundle.getString("user.profile.image.uploaded.success"));
                        response.setResponseCode(ResponseEnum.USER_PROFILE_IMG_UPLOAD_SUCCESS.getValue());
                        response.setResponseStatus(ResponseEnum.SUCCESS.getValue());
                   //     response.setResponseData(new ProfileImageUploadResponse(user));

                        return new ResponseEntity<>(response, HttpStatus.OK);
                    } else {
                        response.setResponseMessage(messageBundle.getString("user.profile.invalid.media"));
                        response.setResponseCode(ResponseEnum.USER_PROFILE_INVALID_FILE_ERROR.getValue());
                        response.setResponseStatus(ResponseEnum.ERROR.getValue());
                        response.setResponseData(null);

                        return new ResponseEntity<>(response, HttpStatus.OK);
                    }
                } else {
                    response.setResponseMessage(messageBundle.getString("user.profile.invalid.media"));
                    response.setResponseCode(ResponseEnum.USER_PROFILE_INVALID_FILE_ERROR.getValue());
                    response.setResponseStatus(ResponseEnum.ERROR.getValue());
                    response.setResponseData(null);

              //  }
             //   userService.updateUser(user);
            }
        } catch (Exception ex) {
            logger.error("Admin pr update failed.", ex.fillInStackTrace());
            response.setResponseStatus(ResponseEnum.ERROR.getValue());
            response.setResponseCode(ResponseEnum.EXCEPTION.getValue());
            response.setResponseMessage(messageBundle.getString("exception.occurs"));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



}