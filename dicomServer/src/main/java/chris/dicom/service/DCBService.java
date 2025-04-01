package chris.dicom.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import chris.dicom.bean.ParamsBean;
import chris.dicom.bean.RequestBean;
import chris.dicom.bean.ResponseBean;
import chris.dicom.util.SocketUtil;

@RestController
@RequestMapping("/Ally")
@CrossOrigin(origins = "http://localhost:3000")
public class DCBService {
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	 @RequestMapping(value="/GetBeneficiaryData", method=RequestMethod.POST)
	 @ResponseBody
	 public String getBeneficiaryData(@RequestBody  ParamsBean req) {
		
		Gson gson=new Gson();
		
		List<Map<String,Object>> result = jdbcTemplate.queryForList("select division_name as \"Division Name\",beneficiary_type,panchayat_name,"
				+ " outstanding_due_wc,collection_wc,demand_wc,balance_wc,balance from full_view_new where "
						+ " bill_sno=(select max(bill_sno) from full_view_new where   beneficiary_sno='"+req.getBen_no()+"') limit 1");
		 
		 return  gson.toJson(result);
		 
	 }
	 
	 
	 @RequestMapping(value="/RuleEngine", method=RequestMethod.POST)
	 @ResponseBody
	 public String ruleEngine(@RequestBody  ParamsBean req) {
		
		Gson gson=new Gson();
		Map<String,Object> data = new HashMap<String,Object>();	
		
		data.put("Result", "Eligible for Postmatric");
		
		ArrayList<Map<String,Object>> result=new ArrayList<Map<String,Object>>();		
		result.add(data);
		 return  gson.toJson(result);
		 
	 }
	 
	 @RequestMapping(value="/ApplicationStatus", method=RequestMethod.POST)
	 @ResponseBody
	 public String applicationStatus(@RequestBody  ParamsBean req) {
		
		Gson gson=new Gson();
		Map<String,Object> data = new HashMap<String,Object>();	
		
		data.put("Status", "Application Processed For 40% Payment");
		
		ArrayList<Map<String,Object>> result=new ArrayList<Map<String,Object>>();		
		result.add(data);
		 return  gson.toJson(result);
		 
	 }
}


