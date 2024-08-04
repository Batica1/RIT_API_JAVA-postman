package edu.rit.company.businesslayer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import companydata.*;
import static edu.rit.company.businesslayer.Config.COMPANY_NAME;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author Domagoj Kurfurst
 * 
 */
public class Company {
    
    
    private Gson gson = null;
    private DataLayer dl = null;
    private Validator validate = null;

    public Company() {
        try {
            this.dl = new DataLayer(COMPANY_NAME);
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

            validate = new Validator();
        } catch (Exception e) { 
            System.out.println("Query exception: ");
        } finally { dl.close(); }
    }//end of constructor
    
    public String deleteCompany(String companyName) {
        int recordsDeleted = dl.deleteCompany(companyName);

        if (recordsDeleted >= 1) {
            return "{\"success:\": \"Deleted all records for the given company " + companyName + ".\"}";
        }
        return "{\"error:\": \"No company deleted. Company " + companyName + " was not found.\"}";
    }

    //departments
    public String getDepartments(String companyName) {
        List<Department> departments = dl.getAllDepartment(companyName);
        if (departments.isEmpty() || !validate.companyExists(companyName)) {
            return "{\"error:\": \"No department found for company " + companyName + ".\"}";
        }
        return gson.toJson(departments);
    }

    //getDepartment
    public String getDepartment(String companyName, int id) {
        Department department = dl.getDepartment(companyName, id);
        if (department == null) {
            return "{\"error:\": \"No department found for company " + companyName + ".\"}";
        }
        return gson.toJson(department);
    }
    
    //createDepartment
    //INSERT
    public String createDepartment(String companyName, String dept_name, String deptNo, String location) {

        if (!validate.validateDeptNumber(companyName, deptNo)) {
            return "{\"error:\": \"No department created for company " + companyName + ". Department number is not unique.\"}";
        }

        Department createdDepartment = dl.insertDepartment(new Department(companyName, dept_name, deptNo, location));

        if (createdDepartment == null) {
            return "{\"error:\": \"No department created for company " + companyName + " because it is null.\"}";
        }

        return "{\"success:\": " + gson.toJson(createdDepartment) + "}";
    }
    
    //updateDepartment
    public String updateDepartment(String jsonCompany) {

        Department department = gson.fromJson(jsonCompany, Department.class);

        if (!validate.validateDeptNumber(department.getCompany(), department.getDeptNo())) {
            return "{\"error:\": \"No department updated for company " + department.getCompany() + ". Department number is not unique.\"}";
        }
        if (!validate.validateDept_id(department.getCompany(), department.getId())) {
            return "{\"error:\": \"No department updated for company " + department.getCompany() + ". Department with id " + department.getId() + " does not exist.\"}";
        }
        
        Department newDepartment = dl.updateDepartment(department);
        if (newDepartment == null) {
            return "{\"error:\": \"No department updated for company " + department.getCompany() + " because it is null.\"}";
        }
        return "{\"success:\": " + gson.toJson(newDepartment) + "}";
    }
    
    //delete department
    public String deleteDepartment(String companyName, int id) {
        int rowsAffected = dl.deleteDepartment(companyName, id);

        if (rowsAffected >= 1) {
            return "{\"success:\": \"Deparment " + id + " from " + companyName + " deleted.\"}";
        }

        return "{\"error:\": \"Department with id " + id + " " + companyName + " does not exist.\"}";
    }
    
    //employees
    public String getEmployees(String companyName) {
        List<Employee> employees = dl.getAllEmployee(companyName);
        if (employees.isEmpty() || !validate.companyExists(companyName)) {
            return "{\"error:\": \"No employees found for company " + companyName + ".\"}";
        }
        return gson.toJson(employees);
    }
    
    public String getEmployee(int emp_id) {
        Employee employee = dl.getEmployee(emp_id);

        if (employee == null) {
            return "{\"error:\": \"No employees for id " + emp_id + ".\"}";
        }
        return gson.toJson(employee);
    }
    
    //create employee
    public String createEmployee(String companyName, String emp_name, String emp_no, Date hire_date, String job, Double salary, int dept_id, int mng_id) {

        if (!validate.validateDept_id(companyName, dept_id)) {
            return "{\"error:\": \"dept_id does not exist Employee can't be created .\"}";
        }
        if (!validate.validateMng_id(companyName, mng_id)) {
            return "{\"error:\": \"mng_id does not exist Employee can't be created.\"}";
        }
        if (!validate.validateHireDate(hire_date)) {
            return "{\"error:\": \"hire_date must be a valid date equal to the current date or earlier - Employee not created.\"}";
        }
        if (!validate.validateHire_date(hire_date)) {
            return "{\"error:\": \"hire_date must be a working day Employee not created.\"}";
        }
        if (!validate.validateEmpNnumber(companyName, emp_no)) {
            return "{\"error:\": \"emp_no already exist Employee not created.\"}";
        }
        Employee employee = dl.insertEmployee(new Employee(emp_name, emp_no, hire_date, job, salary, dept_id, mng_id));

        if (employee == null) {
            return "{\"error:\": \"Employee could not created.\"}";
        }

        return "{\"success:\":" + gson.toJson(employee) + "}";
    }

    public String updateEmployee(String jsonCompany) {
        
        
        Employee employee = gson.fromJson(jsonCompany, Employee.class);

        JsonObject jsonObject = gson.fromJson(jsonCompany, JsonObject.class);
        String companyName = jsonObject.get("company").getAsString();

        if (!validate.validateDept_id(companyName, employee.getDeptId())) {
           return "{\"error:\": \"department with a given dept_id does not exist - Employee is not updated.\"}";
        }
          
        if (!validate.validateEmp_id(employee.getId())) {
            return "{\"error:\": \"emp_id does not exist - Employee is not updated.\"}";
        }
      
        if (!validate.validateMng_id(companyName, employee.getMngId())) {
            return "{\"error:\": \"mng_id does not exist - Employee is not updated.\"}";
        }
        
        if (!validate.validateHireDate(employee.getHireDate())) {
            return "{\"error:\": \"hire_date must be either current date or earlier - Employee is not updated .\"}";
        }
        
        if (!validate.validateHire_date(employee.getHireDate())) {
            return "{\"error:\": \"hire_date is not a working week day - No employee updated .\"}";
        }
        
        if (!validate.validateEmpNnumber(companyName, employee.getEmpNo())) {
            return "{\"error:\": \"emp_no already exists.\"}";
        }
        
        
        Employee edit = dl.updateEmployee(employee);

        if (edit != null) { return "{\"success:\":" + gson.toJson(edit) + "}"; }
        return "{\"error:\": \" Employee cant update.\"}";
    
    }//end of update
    
    

    public String deleteEmployee(int emp_id) {

        int del = dl.deleteEmployee(emp_id);

        if (del >= 1) {
            return "{\"success:\": \"Deleted employee with id " + emp_id + ".\"}";
        } else {
            return "{\"error:\": \"Employee does not exist.\"}";
        }
    }
    
     //timecards
    public String getTimecards(int emp_id) {
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

        List<Timecard> timecards = dl.getAllTimecard(emp_id);
        if (timecards.isEmpty() ) {
            return "{\"error:\": \"No timecards found for company " + emp_id + ".\"}";
        }
        return gson.toJson(timecards);
    }
    
    public String getTimecard(int emp_id) {
       gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

        Timecard timecard = dl.getTimecard(emp_id);
        if (timecard == null) {
            return "{\"error:\": \"No timecard for id " + emp_id + ".\"}";
        }
        return gson.toJson(timecard);
    }
    
     public String insertTimecard(int empID, Timestamp startTime, Timestamp endTime) {
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

        if (!validate.validateEmp_id(empID)) {
            return "{\"error:\": \"No timecard created because employee with id " + empID + " does not exist.\"}";
        }
     
        if (!validate.validateTimestampDayOfWeek(startTime)) {
            return "{\"error:\": \"No timecard created because start time is not valid: needs to be working day.\"}";
        }
        if (!validate.validateTimestampDayOfWeek(endTime)) {
            return "{\"error:\": \"No timecard created because end time is not valid: 111\"}";
        }
        
        if (!validate.validateStartTime(startTime)) {
            return "{\"error:\": \"No timecard created because start time is not valid.\"}";
        }
        
        if (!validate.validateEndTime(startTime, endTime)) {
            return "{\"error:\": \"No timecard created because end time is not valid.\"}";
        }
        
        if (!validate.validateTime(startTime)) {
            return "{\"error:\": \"start_time must be between the hours of 06:00:00 and 18:00:00 inclusive Timecard not created.\"}";
        }
        if (!validate.validateTime(endTime)) {
            return "{\"error:\": \"end_time must be between the hours of 06:00:00 and 18:00:00 inclusive Timecard not created.\"}";
        }
        if (!validate.validateEmployeeStartTime(empID, startTime)) {
            return "{\"error:\": \"Start time for emp_id " + empID + " already exist.\"}";
        }
        
        
        Timecard timecard = dl.insertTimecard(new Timecard(startTime, endTime, empID));
        
        if (timecard != null) { return "{\"success:\":" + gson.toJson(timecard) + "}"; }

        return "{\"error:\": \" Timecard Appropriate error message could not be created.\"}";
    }

    public String updateTimecard(String timecard) {
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
        Timecard jsonTimecard = gson.fromJson(timecard, Timecard.class);
        
        if (!validate.validateTimecard_id(jsonTimecard.getId())) {
            return "{\"error:\": \" Timecard id doesn't exist.\"}";
        }
        if (!validate.validateEmp_id(jsonTimecard.getEmpId())) {
            return "{\"error:\": \"emp id: " + jsonTimecard.getEmpId() + " does not exist.\"}";
        }
        if (!validate.validateStartTime(jsonTimecard.getStartTime())) {
            return "{\"error:\": \" Start time is not valid.\"}";
        }
        if (!validate.validateEndTime(jsonTimecard.getStartTime(), jsonTimecard.getEndTime())) {
            return "{\"error:\": \"End time is not valid.\"}";
        }
        if (!validate.validateTimestampDayOfWeek(jsonTimecard.getStartTime())) {
            return "{\"error:\": \"Needs to be a working day.\"}";
        }
        if (!validate.validateTimestampDayOfWeek(jsonTimecard.getEndTime())) {
            return "{\"error:\": \"Needs to be a working day.\"}";
        }
        if (!validate.validateTime(jsonTimecard.getStartTime())) {
            return "{\"error:\": \"Needs to be between 06:00:00 and 18:00:00 inclusive.\"}";
        }
        if (!validate.validateTime(jsonTimecard.getEndTime())) {
            return "{\"error:\": \"Needs to be between 06:00:00 and 18:00:00 inclusive.\"}";
        }
        
        Timecard updatedTimecard = dl.updateTimecard(jsonTimecard);

        if (updatedTimecard != null) {
            return "{\"success:\":" + gson.toJson(updatedTimecard) + "}";
        }

        return "{\"error:\": \" Timecard Appropriate error message could not update.\"}";
    }

    public String deleteTimecard(int id) {
        int rowsAffected = dl.deleteTimecard(id);

        if (rowsAffected >= 1) {
            return "{\"success:\": \"Timecard with id " + id + " is deleted.\"}";
        }
        return "{\"error:\": \"Timecard id " + id + " is not found.\"}";
    }
    
}//end of class
