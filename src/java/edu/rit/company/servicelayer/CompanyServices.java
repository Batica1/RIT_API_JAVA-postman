package edu.rit.company.servicelayer;

import edu.rit.company.businesslayer.Company;
import javax.ws.rs.core.*;
import javax.ws.rs.*;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * RESTful Web Service for Company Departments. WADL:
 * http://localhost:8080/Project2/webresources/application.wadl
 *
 * @author Domagoj Kurfurst
 */
@Path("CompanyServices")
public class CompanyServices {
    
    @Context
    private UriInfo context;

    private Company company = null;
    /**
     * Creates a new instance of CompanyServices
     */
    public CompanyServices() {
        company = new Company();
    }
    
    @Path("company")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAll(@QueryParam("company") String companyName) {
        return Response.ok(company.deleteCompany(companyName)).build();
    }

    //departments

    /**
     *
     * @param companyName
     * @return
     */
    @GET
    @Path("departments") //http://localhost:8080/Project2/webresources/CompanyServices/departments?company=dk4368
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDepartments(@QueryParam("company") String companyName) {
        return Response.ok(company.getDepartments(companyName)).build();
    }
    
    //department
    //   /Project2/webresources/CompanyServices/department

    /**
     *
     * @param companyName
     * @param id
     * @return
     */
    @GET
    @Path("department")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDepartment(@QueryParam("company") String companyName, @QueryParam("dept_id") int id) {
        return Response.ok(company.getDepartment(companyName, id)).build();
    }
    
    //update department

    /**
     *
     * @param jsonCompany
     * @return
     */
    @Path("department")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDepartment(String jsonCompany) {
        return Response.ok(company.updateDepartment(jsonCompany)).build();
    }

    /**
     *
     * @param companyName
     * @param dept_name
     * @param dept_no
     * @param location
     * @return
     */
    @Path("department")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertDepartment(@FormParam("company") String companyName, @FormParam("dept_name") String dept_name, @FormParam("dept_no") String dept_no, @FormParam("location") String location) {
        return Response.ok(company.createDepartment(companyName, dept_name, dept_no, location)).build();
    }
    
    /**
     *
     * @param companyName
     * @param dept_id
     * @return
     */
    @Path("department")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDepartment(@QueryParam("company") String companyName, @QueryParam("dept_id") int dept_id) {
        return Response.ok(company.deleteDepartment(companyName, dept_id)).build();
    }
   
    //employees

    /**
     *
     * @param companyName
     * @return
     */
    @GET
    @Path("employees") //http://localhost:8080/Project2/webresources/CompanyServices/employees?company=dk4368
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEmployees(@QueryParam("company") String companyName) {
        return Response.ok(company.getEmployees(companyName)).build();
    }
    
    /**
     *
     * @param emp_id
     * @return
     */
    @GET
    @Path("employee") //http://localhost:8080/Project2/webresources/CompanyServices/employee?emp_id=1
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEmployee(@QueryParam("emp_id") int emp_id) {
        return Response.ok(company.getEmployee(emp_id)).build();
    }
    
    /**
     *
     * @param companyName
     * @param emp_name
     * @param emp_no
     * @param hire_date
     * @param job
     * @param salary
     * @param dept_id
     * @param mng_id
     * @return
     */
    @POST
    @Path("employee") //http://localhost:8080/Project2/webresources/CompanyServices/employee
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertEmployee(@FormParam("company") String companyName, @FormParam("emp_name") String emp_name, @FormParam("emp_no") String emp_no, @FormParam("hire_date") Date hire_date, @FormParam("job") String job, @FormParam("salary") Double salary, @FormParam("dept_id") int dept_id, @FormParam("mng_id") int mng_id) {
        return Response.ok(company.createEmployee(companyName, emp_name, emp_no, hire_date, job, salary, dept_id, mng_id)).build();
    }//end of insertEmployee

    /**
     *
     * @param jsonCompany
     * @return
     */
    @PUT
    @Path("employee") //http://localhost:8080/Project2/webresources/CompanyServices/employee
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateEmployee(String jsonCompany) {
        return Response.ok(company.updateEmployee(jsonCompany)).build();
    }
    
    /**
     *
     * @param emp_id
     * @return
     */
    @Path("employee") //http://localhost:8080/Project2/webresources/CompanyServices/employee
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteEmployee(@QueryParam("emp_id") int emp_id) {
        
       //if delete DB record successful send ok, otherwise Repsonse.Status.NO_CONTENT with no msg
        return Response.ok(company.deleteEmployee(emp_id)).build();
    }
    
    //timecards

    /**
     *
     * @param emp_id
     * @return
     */
    @GET
    @Path("timecards") //http://localhost:8080/Project2/webresources/CompanyServices/timecards?emp_id=dk4368
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTimecards(@QueryParam("emp_id") int emp_id) {
        return Response.ok(company.getTimecards(emp_id)).build();
    }
    
    /**
     *
     * @param timecard_id
     * @return
     */
    @GET
    @Path("timecard") //http://localhost:8080/Project2/webresources/CompanyServices/timecard?emp_id=dk4368
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTimecard(@QueryParam("timecard_id") int timecard_id) {
        return Response.ok(company.getTimecard(timecard_id)).build();
    }
    
    /**
     *
     * @param emp_id
     * @param start_time
     * @param end_time
     * @return
     */
    @Path("timecard") //http://localhost:8080/Project2/webresources/CompanyServices/timecard
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertTimecard(@FormParam("emp_id") int emp_id, @FormParam("start_time") Timestamp start_time, @FormParam("end_time") Timestamp end_time) {
        return Response.ok(company.insertTimecard(emp_id, start_time, end_time)).build();
    }

    /**
     *
     * @param timecard
     * @return
     */
    @PUT
    @Path("timecard") //http://localhost:8080/Project2/webresources/CompanyServices/timecard
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTimecard( String timecard) {
        return Response.ok(company.updateTimecard(timecard)).build();
    }

    /**
     *
     * @param timecard_id
     * @return
     */
    @Path("timecard") //http://localhost:8080/Project2/webresources/CompanyServices/timecard
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTimecard(@QueryParam("timecard_id") int timecard_id) {
        return Response.ok(company.deleteTimecard(timecard_id)).build();
    }

    
}//end of class
