package com.sd.his.controller;

import com.sd.his.enums.InvoiceMessageEnum;
import com.sd.his.enums.ResponseEnum;
import com.sd.his.service.PatientInvoiceService;
import com.sd.his.service.PatientService;
import com.sd.his.service.StaffService;
import com.sd.his.utill.HISCoreUtil;
import com.sd.his.wrapper.GenericAPIResponse;
import com.sd.his.wrapper.request.DoctorPaymentRequestWrapper;
import com.sd.his.wrapper.response.StaffResponseWrapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ResourceBundle;

@RestController
@RequestMapping("/doctorPayment")
public class DoctorPaymentController {

    @Autowired
    private PatientInvoiceService patientInvoiceService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private StaffService staffService;

    private final Logger logger = LoggerFactory.getLogger(StaffAPI.class);
    private ResourceBundle messageBundle = ResourceBundle.getBundle("messages");


    @ApiOperation(httpMethod = "POST", value = "Save Doctor Payment",
            notes = "This method will Save Doctor Payment",
            produces = "application/json", nickname = "Doctor Payment",
            response = GenericAPIResponse.class, protocols = "https")

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<?> saveDoctorPayment(@RequestBody DoctorPaymentRequestWrapper doctorPaymentRequestWrapper) {
        GenericAPIResponse response = new GenericAPIResponse();
        try
        {
         /*   if (doctorPaymentRequestWrapper.getDate().trim().length() == 10) {
                doctorPaymentRequestWrapper.setDate( patientService.convertDateToGMT( doctorPaymentRequestWrapper.getDate().trim(), "yyyy-MM-dd" ) );
            } else {
                doctorPaymentRequestWrapper.setDate( patientService.convertDateToGMT( doctorPaymentRequestWrapper.getDate().trim(), "E MMM dd yyyy HH:mm:ss" ) );
            }*/
            staffService.saveDoctorPayment(doctorPaymentRequestWrapper);

            response.setResponseMessage(messageBundle.getString("doctorPayment.save.success"));
            response.setResponseCode(InvoiceMessageEnum.SUCCESS.getValue());
            response.setResponseStatus(InvoiceMessageEnum.SUCCESS.getValue());
            logger.info("Doctor Payment done successfully...");

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception ex)
        {
            logger.error("Docor Payment Failed.", ex.fillInStackTrace());
            response.setResponseStatus(InvoiceMessageEnum.ERROR.getValue());
            response.setResponseCode(InvoiceMessageEnum.EXCEPTION.getValue());
            response.setResponseMessage(messageBundle.getString("exception.occurs"));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    @ApiOperation(httpMethod = "GET", value = "User By Type",
            notes = "This method will return Users By Type",
            produces = "application/json", nickname = "Get Users By type ",
            response = GenericAPIResponse.class, protocols = "https")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Users fetched successfully", response = GenericAPIResponse.class),
            @ApiResponse(code = 401, message = "Oops, your fault. You are not authorized to access.", response = GenericAPIResponse.class),
            @ApiResponse(code = 403, message = "Oops, your fault. You are forbidden.", response = GenericAPIResponse.class),
            @ApiResponse(code = 404, message = "Oops, my fault System did not find your desire resource.", response = GenericAPIResponse.class),
            @ApiResponse(code = 500, message = "Oops, my fault. Something went wrong on the server side.", response = GenericAPIResponse.class)})
    @RequestMapping(value = "/role", method = RequestMethod.GET)
    public ResponseEntity<?> findUserByRole(@RequestParam(value = "name") String type)
    {
        logger.info("user type..." + type);
        GenericAPIResponse response = new GenericAPIResponse();
        try
        {
            if (!HISCoreUtil.isNull(type))
            {
                List<StaffResponseWrapper> staffResponseWrapper = staffService.findAllByRole(type);
                if (!HISCoreUtil.isListEmpty(staffResponseWrapper)) {
                    response.setResponseMessage(messageBundle.getString("user.fetched.success"));
                    response.setResponseCode(ResponseEnum.USER_FOUND.getValue());
                    response.setResponseStatus(ResponseEnum.SUCCESS.getValue());
                    response.setResponseData(staffResponseWrapper);
                    logger.info("user on base of Type fetched successfully...");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            else
            {
                response.setResponseMessage(messageBundle.getString("insufficient.parameter"));
                response.setResponseCode(ResponseEnum.INSUFFICIENT_PARAMETERS.getValue());
                response.setResponseStatus(ResponseEnum.ERROR.getValue());
                response.setResponseData(null);
                logger.error("Create User insufficient params");

            }
        } catch (Exception ex) {
            logger.error("user by role failed.", ex.fillInStackTrace());
            response.setResponseData("");
            response.setResponseStatus(ResponseEnum.ERROR.getValue());
            response.setResponseCode(ResponseEnum.EXCEPTION.getValue());
            response.setResponseMessage(messageBundle.getString("exception.occurs"));

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @ApiOperation(httpMethod = "GET", value = "Get Doctor Payment List",
            notes = "This method will Get Doctor Payment List",
            produces = "application/json", nickname = " Doctor Payment List",
            response = GenericAPIResponse.class, protocols = "https")

    @ApiResponses({
            @ApiResponse(code = 200, message = "Doctor Payment List data found successfully", response = GenericAPIResponse.class),
            @ApiResponse(code = 401, message = "Oops, your fault. You are not authorized to access.", response = GenericAPIResponse.class),
            @ApiResponse(code = 403, message = "Oops, your fault. You are forbidden.", response = GenericAPIResponse.class),
            @ApiResponse(code = 404, message = "Oops, my fault System did not find your desire resource.", response = GenericAPIResponse.class),
            @ApiResponse(code = 500, message = "Oops, my fault. Something went wrong on the server side.", response = GenericAPIResponse.class)})
    @RequestMapping(value = "/getPaymentList", method = RequestMethod.GET)
    public ResponseEntity<?> getPaymentList() {

        GenericAPIResponse response = new GenericAPIResponse();
        try
        {
            response.setResponseData(staffService.getDocPaymentList());

            response.setResponseMessage(messageBundle.getString("doctorPayment.payment.list.fetched.success"));
            response.setResponseCode(InvoiceMessageEnum.SUCCESS.getValue());
            response.setResponseStatus(InvoiceMessageEnum.SUCCESS.getValue());
            logger.info("Doctor Payment List data fetched successfully...");

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception ex)
        {
            logger.error("Doctor Payment List Fetched Failed.", ex.fillInStackTrace());
            response.setResponseStatus(InvoiceMessageEnum.ERROR.getValue());
            response.setResponseCode(InvoiceMessageEnum.EXCEPTION.getValue());
            response.setResponseMessage(messageBundle.getString("exception.occurs"));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
