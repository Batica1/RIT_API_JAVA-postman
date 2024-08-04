package edu.rit.company.businesslayer;

import static edu.rit.company.businesslayer.Config.COMPANY_NAME;

import companydata.DataLayer;
import companydata.Department;
import companydata.Employee;
import companydata.Timecard;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author Domagoj Kurfurst 
 * 
 */
public class Validator {
    
    
    private DataLayer dl = new DataLayer(COMPANY_NAME);
    
    //arrays for days
    private String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    
    
    public boolean validateDeptNumber(String companyName, String dpet_no) {

        List<Department> departments = dl.getAllDepartment(companyName);

        for (Department department : departments) {
            if (dpet_no.equals(department.getDeptNo())) { return false; }
        }//end of for
        return true;
    }//end of method
    
    public boolean companyExists(String companyName) {
 
        return companyName.equals(Config.COMPANY_NAME);
  
    }//end of method

    public boolean validateDept_id(String companyName, int dept_id) {
        Department department = dl.getDepartment(companyName, dept_id);
        return department != null;
    }
    
     public boolean validateEmp_id(int empId) {
        Employee employee = dl.getEmployee(empId);
        return employee != null;
    }

    public boolean validateMng_id(String companyName, int mng_id) {

        List<Employee> employess = dl.getAllEmployee(companyName);

        if (employess.isEmpty()) {
            mng_id = 0;
            return true;
        }

        for (Employee e : employess) {
            if ((e.getId() == mng_id) || (mng_id == 0)) {  return true; }
        
        }//end of for
        
        return false;
    }

    public boolean validateHireDate(Date date) {
        
        java.util.Date todayDate = new java.util.Date();
        java.sql.Date today = new java.sql.Date(todayDate.getTime());
        
        return !date.after(today);
    }

    public boolean validateHire_date(Date date) {
        
        DateFormat date_1 = new SimpleDateFormat("EEEE", Locale.US);
       
        String weekDay = date_1.format(date);

        for (String day : days) {
            if (weekDay.equals(day)) { return true; }
        }//end of for
        return false;
    }
    
    public boolean validateStartTime(Timestamp start_time) {
        
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            dateFormat.format(start_time);
        } catch (Exception e) {
            return false;
        }

        
        LocalDate local_date = LocalDate.now();
        LocalDate lastWeek = local_date.minusDays(7); // distance between 7 days
        //
        LocalDate date_start = start_time.toLocalDateTime().toLocalDate();

        return date_start.isEqual(local_date) || date_start.isAfter(lastWeek);
    }

    public boolean validateEndTime(Timestamp startTime, Timestamp endTime) {
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            dateFormat.format(endTime);
        } catch (Exception e) { return false; }

        Timestamp oneHourGreater = Timestamp.from(startTime.toInstant().plus(1, ChronoUnit.HOURS));
        if (endTime.before(oneHourGreater)) { return false; }

        if (!(startTime.getDate() == endTime.getDate())) { return false; }

        return true;
    }

    public boolean validateTimestampDayOfWeek(Timestamp timestamp) {
       
        DateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.US);
       
        String weekDay = dateFormat.format(timestamp);

        for (String day : days) {
            if (weekDay.equals(day)) { return true; }
        }//end of for 

        return false;
    }//end of method
    
      public boolean validateEmployeeStartTime(int emp_id, Timestamp time) {
        List<Timecard> timecards = dl.getAllTimecard(emp_id);
        LocalDate startDate = time.toLocalDateTime().toLocalDate();

        for (Timecard t : timecards) {
            LocalDate timecardStartDate = t.getStartTime().toLocalDateTime().toLocalDate();
            if (startDate.equals(timecardStartDate)) {
                return false;
            }//end of if 
        }//end of for 

        return true;
    } // end of method

    public boolean validateTime(Timestamp time) {
        
        LocalTime begin = LocalTime.parse("06:00:00");
        LocalTime end = LocalTime.parse("18:00:00");

        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String stampTime = dateFormat.format(time);
        LocalTime local = LocalTime.parse(stampTime);

        return (local.isAfter(begin.minusMinutes(1))) && local.isBefore(end.plusMinutes(1));
    } // end of method
    
      public boolean validateEmpNnumber(String companyName, String emp_no) {
        List<Employee> employees = dl.getAllEmployee(companyName);

        for (Employee e : employees) {
            if (e.getEmpNo().equals(emp_no)) { return false; }
        }//end of for

        return true;
    }//end of method

    public boolean validateTimecard_id(int card_id) {
        Timecard timecard = dl.getTimecard(card_id);
        
        return timecard != null;
    }//end of method
    
  
    
}//end of class
