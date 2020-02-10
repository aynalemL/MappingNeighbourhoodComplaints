package com.neighborhood.info.mappingcomplaint.controller;

import com.neighborhood.info.mappingcomplaint.model.DropDown;
import com.neighborhood.info.mappingcomplaint.model.GeoData;
import com.neighborhood.info.mappingcomplaint.model.GeoDataDetailed;
import com.neighborhood.info.mappingcomplaint.service.MapDataService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MapDataController {

    private MapDataService dataService = new MapDataService();
    @RequestMapping("/hello")
    public String dummydata(){
        return "helloworld";
    }
    @RequestMapping("/byComplaintTypeZipLevel")
    public GeoData byComplaintTypeZipLevel(@RequestParam(value = "complaintType", defaultValue = "All") String complaintType) {
        return dataService.fetchComplaintCountByTypeByZoom(complaintType, "zip");
    }

    @RequestMapping("/byComplaintTypeDetailedZips")
    public GeoDataDetailed byComplaintTypeDetailedZips(@RequestParam(value = "complaintType", defaultValue = "All") String complaintType) {
        return dataService.findComplaintDetailByTypeByZoom(complaintType, "zip");
    }
    @RequestMapping("/complaintTypes")
    public List<DropDown> getComplaintTypes(){
        return dataService.fetchComplaintTypes();
    }

    @RequestMapping("/trendByTypeZipLevel")
    public GeoData trendByType(@RequestParam(value = "complaintType", defaultValue = "All") String complaintType) {
        return dataService.fetchCompalintByTypeByZoom(complaintType, "zip");
    }

    @RequestMapping("/trendByTypeForZip")
    public GeoData trendByTypeForZip(@RequestParam(value = "complaintType", defaultValue = "All") String complaintType,
                                    @RequestParam(value = "spatialId", defaultValue = "All") String zip) {
        return dataService.fetchComplaintTrendByTypeForZip(complaintType, zip);
    }

    @RequestMapping("/trendByTypeForBorough")
    public GeoData trendByTypeForBorough(@RequestParam(value = "complaintType", defaultValue = "All") String complaintType,
                                     @RequestParam(value = "spatialId", defaultValue = "All") String borough) {
        return dataService.fetchComplaintTrendByTypeForBorough(complaintType, borough);
    }

   @RequestMapping("/complaintByTypeByZoom")
    public GeoData compaliantByTypeByZoom(@RequestParam( value ="complaintType", defaultValue = "Select Complaint Type") String complaintType,
                                          @RequestParam(value = "zoomLevel", defaultValue ="Select Zoom Level" ) String zoomLevel){

      return dataService.fetchCompalintByTypeByZoom(complaintType,zoomLevel);
  }




}
