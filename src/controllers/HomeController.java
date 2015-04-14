package controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import models.User;

@Controller
@RequestMapping("/")
public class HomeController {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	protected final Gson gson = new Gson();

	
	@Autowired
    MongoTemplate mongoTemplate;

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	
	
	@RequestMapping(method = RequestMethod.GET)
    public String handleGetRequest(ModelMap model) {
		return "home";
    }

	
	@RequestMapping(value = UsrRestURIConstant.GET_ALL_EMP, method = RequestMethod.GET)
	@ResponseBody
    public String getAllEmployees(@PathVariable("page") int pageNo) {
        logger.info("Start getAllEmployees of pageNo "+ pageNo);
        System.out.println("Start getAllEmployees of pageNo "+ pageNo);
        String TenItems;
        int n;
        
        if(pageNo<0) return null;
        	n=pageNo*10;
        	TenItems = gson.toJson(mongoTemplate.find(new Query().skip(n).limit(10), User.class));
        	//System.out.println(TenItems);
        	return TenItems;
      
       // return gson.toJson(mongoTemplate.findAll(User.class));
    }

	
	@RequestMapping(value = UsrRestURIConstant.CREATE_EMP, method = RequestMethod.POST)
	@ResponseBody
    public String createEmployee(@RequestBody String empString) {
        logger.info("Start createEmployee.");
        System.out.println("Start createEmployee. ");
        User emp = gson.fromJson(empString, User.class);
        System.out.println(emp);
        try{
            mongoTemplate.insert(emp);
            }catch(DuplicateKeyException e){
            	System.out.println("Duplicate");
            	//e.printStackTrace(System.out);
            	return "duplictae";
            }catch(Exception e){
            	e.printStackTrace(System.out);
            	return "e";
            }
        return gson.toJson(emp);
    }
	
	@RequestMapping(value = UsrRestURIConstant.DUMMY_EMP, method = RequestMethod.GET)
	@ResponseBody
    public String getDummyEmployee() {
        logger.info("Start getDummyEmployee");
        System.out.println("Start getDummyEmployee");
        User emp = new User();
        emp.setfullname("Dummy");
        emp.setEmail("dummy@mail.com");
        emp.setAge(0);
        emp.setGender("Male");
        emp.setLocation("India");
        emp.setusername("DummyUser");
        emp.setPassword("DummyUser");
        try{
            mongoTemplate.insert(emp);
            System.out.println("iNSERTED "+emp);
        }catch(DuplicateKeyException e){
        	System.out.println("Duplicate");
        	//e.printStackTrace(System.out);
        	//return null;
        }catch(Exception e){
        	e.printStackTrace(System.out);
        	//return null;
        }
        
        try{
            mongoTemplate.insert(emp);
            System.out.println("iNSERTED "+emp);
        }catch(DuplicateKeyException e){
        	System.out.println("Duplicate");
        	//e.printStackTrace(System.out);
        	//return null;
        }catch(Exception e){
        	e.printStackTrace(System.out);
        	//return null;
        }
        
        logger.info("Mimicking createEmployee.");
        System.out.println("Mimicking createEmployee. ");
        //empData.put(emp.getId(), emp);
        emp.setfullname("Pankaj Kumar");
        emp.setEmail("pankaj@mail.com");
        emp.setAge(27);
        emp.setGender("Male");
        emp.setLocation("Delhi");
        emp.setusername("pankaj");
        emp.setPassword("pankaj");
        try{
            mongoTemplate.insert(emp);
            System.out.println("iNSERTED "+emp);
            }catch(DuplicateKeyException e){
            	System.out.println("Duplicate");
            	//e.printStackTrace(System.out);
            	System.out.println("cannot print finish, since return null");
            	return null;
            }catch(Exception e){
            	e.printStackTrace(System.out);
            	return null;
            }
        System.out.println("Finish");
        return gson.toJson(emp);
    }
	
	@RequestMapping(value = UsrRestURIConstant.GET_EMP, method = RequestMethod.GET)
	@ResponseBody
    public String getEmployee(@PathVariable("id") String empId) {
        logger.info("Start getEmployee. ID="+empId);
        System.out.println("Start getEmployee. ID="+empId);
        
        return gson.toJson(mongoTemplate.findOne(new Query(Criteria.where("username").is(empId)), User.class));
    }
     
	
	@RequestMapping(value = UsrRestURIConstant.DELETE_EMP, method = RequestMethod.DELETE)
	@ResponseBody 
    public void deleteEmployee(@PathVariable("id") String empId) {
        logger.info("Start deleteEmployee.");
        System.out.println("Start deleteEmployee. ID="+empId);
       
        mongoTemplate.remove(new Query(Criteria.where("username").is(empId)),User.class);
    }
	
	@RequestMapping(value = UsrRestURIConstant.UPDATE, method = RequestMethod.PUT, consumes ="application/json")
	@ResponseBody 
    public void UpdateEmployee(@PathVariable("id") String empId, @PathVariable("m") int setMail, @PathVariable("l") int setLocation, @PathVariable("p") int setPassword, @RequestBody String empString) {
        logger.info("Start updateEmployee.");
        System.out.println("Start updateEmployee. ID="+empId);
        
        User emp = gson.fromJson(empString, User.class);
        Update updateUser = new Update();
       
        if(setMail==1){
        	logger.info("Update Email.");
            System.out.println("Update Email");
        	updateUser.set("email", emp.getEmail());
        }
        if(setLocation ==1){
        	logger.info("Update Location.");
            System.out.println("Update Location");
        	updateUser.set("location", emp.getLocation());
        }
        if(setPassword ==1){
        	logger.info("Update Password.");
            System.out.println("Update Password");
        	updateUser.set("password", emp.getPassword());
        }
        mongoTemplate.updateFirst(new Query(Criteria.where("username").is(empId)),updateUser,User.class);
    }
	
	@RequestMapping(value = UsrRestURIConstant.AUTHENTICATE_EMP, method = RequestMethod.GET)
	@ResponseBody 
    public String AuthenticateEmployee(@PathVariable("uid") String empId, @PathVariable("pass") String password) {
        logger.info("Start authenticateEmployee.");
        System.out.println("Start authenticateEmployee. ID="+empId+" Password= "+password);
        
        return gson.toJson(mongoTemplate.findOne(new Query(Criteria.where("username").is(empId).and("password").is(password)), User.class));
    }

}
