package com.G2T8.CS203WebApp.service;

import com.G2T8.CS203WebApp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.G2T8.CS203WebApp.model.*;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class OfficeRequestService {

    @Autowired
    private OfficeRequestRepository officeRequestRepository;

    public List<OfficeRequest> getAllOfficeRequests() {
        return officeRequestRepository.findAll();
    }

    // returns list because one person can go to office several times
    public List<OfficeRequest> getAllOfficeRequestFromOneUser(Long ID) {

        // getting the office request of each user by custom query findByUserId in OfficeRequestRepo
        // do not use findById bc that uses OfficeRequestId which does dont point to
        // which user it is referring to

        List<Optional<OfficeRequest>> origList = officeRequestRepository.findByUserId(ID);
        List<OfficeRequest> toReturn = new ArrayList<>();

        if(origList != null){
            for (int i = 0; i <= origList.size() - 1; i++) {
                if (origList.get(i).isPresent()) {
                    Optional<OfficeRequest> opOfficeReq = origList.get(i);
                    OfficeRequest officeReq = opOfficeReq.get();
                    toReturn.add(officeReq);
                } else {
                    continue;
                }
            }
            return toReturn;
        }
        return null; 
    }

    // returns a particular OfficeRequest record of one user
    public OfficeRequest getOneOfficeRequestFromOneUser(Long ID, LocalDateTime startDateTime){
        Optional<OfficeRequest> temp = officeRequestRepository.findByUserIdAndStartDateTime(ID, startDateTime);
        if (temp.isPresent()) {
            OfficeRequest toReturn = temp.get();
            return toReturn;
        }
        return null; 

    }

    public OfficeRequest updateApproval(Long ID, LocalDateTime startDateTime, boolean approved) {
        Optional<OfficeRequest> b = officeRequestRepository.findByUserIdAndStartDateTime(ID, startDateTime);
        if (b.isPresent()) {
            OfficeRequest OfficeRequest = b.get();
            OfficeRequest.setApproved(approved);
            return officeRequestRepository.save(OfficeRequest);
        } else
            return null;
    }

    public OfficeRequest addOfficeRequest(OfficeRequest officeRequest) {
        // check if there is an existing instance w same user id and startdatetime
        Long ID = officeRequest.getID();
        LocalDateTime startDateTime = officeRequest.getStartDateTime();
        Optional<OfficeRequest> b = officeRequestRepository.findByUserIdAndStartDateTime(ID, startDateTime);
        if (b.isPresent()) {
            // duplicate entry
            return null;
        } else             
            return officeRequestRepository.save(officeRequest);
    }


    
}
