package org.example;

import org.example.config.Config;
import org.example.models.Employee;
import org.example.models.Job;
import org.example.services.EmployeeService;
import org.example.services.serviceImpl.EmployeeServiceImpl;
import org.example.services.serviceImpl.JobServiceImpl;

import java.nio.file.FileSystemAlreadyExistsException;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ){
        JobServiceImpl jobService = new JobServiceImpl();
        EmployeeService employeeService = new EmployeeServiceImpl();

        try{
            int a = 0;
            while(a<22){
                int l = new Scanner(System.in).nextInt();
                System.out.println(" \t     Choose these commands:  " +
                        "\n 1-> Create and save Job tables"+
                        "\n 2-> Create and save Employee tables"+
                        "\n 3-> Input the list of jobs " +
                        "\n 4-> Input the list of employees "+
                        "\n 5-> Get jobs by ID "+
                        "\n 6-> Sort by experience "+
                        "\n 7-> Get jobs by employee ID "+
                        "\n 8-> Delete description column"+
                        "\n 9-> Get all employees "+
                        "\n 10-> Search employees by email"+
                        "\t  11-> Get employees by ID "+
                        "\t  12-> Search by position "+
                        "\t  13-> Delete the table");


                switch (l){
                    case 1 ->  jobService.createJobTable();
                    case 2 -> employeeService.createEmployee();
                    case 3 -> {
                        System.out.print("Enter a job position: ");
                        String position = new Scanner(System.in).nextLine();
                        System.out.print("Enter a profession: ");
                        String profession = new Scanner(System.in).nextLine();
                        System.out.print("Write a job description: ");
                        String description = new Scanner(System.in).nextLine();
                        System.out.print("Input an experiences: ");
                        int experience = new Scanner(System.in).nextInt();
                        jobService.addJob(new Job(position,profession,description,experience));
                    }
                    case 4 -> {
                        System.out.print("Input an employees first name: ");
                        String firstName = new Scanner(System.in).nextLine();
                        System.out.print("Input an employees last name: ");
                        String lastName = new Scanner(System.in).nextLine();
                        System.out.print("Input an employees age: ");
                        int age = new Scanner(System.in).nextInt();
                        System.out.print("Write an employees email: ");
                        String email = new Scanner(System.in).nextLine();
                        System.out.print("Assign a job to employees by job ID: ");
                        int jobId = new Scanner(System.in).nextInt();
                        employeeService.addEmployee(new Employee(firstName,lastName,age,email,jobId));
                    }
                    case 5 -> {
                        System.out.print("Input the Jobs ID to search: ");
                        Long jobId = new Scanner(System.in).nextLong();
                        System.out.println(jobService.getJobById(jobId));
                    }
                    case 6 ->{
                        System.out.print(" Asc or Desc " +
                                "Choose the command to sort by experience: ");
                        String sortCommand = new Scanner(System.in).nextLine();
                        jobService.sortByExperience(sortCommand).forEach(System.out::println);
                    }
                    case 7 -> {
                        System.out.print("Input the employee ID to get jobs: ");
                        Long employeeId = new Scanner(System.in).nextLong();
                        System.out.println(jobService.getJobByEmployeeId(employeeId));
                    }
                    case 8 -> jobService.deleteDescriptionColumn();
                    case 9 -> employeeService.getAllEmployees().forEach(System.out::println);
                    case 10 ->{
                        System.out.print("Input the employees email to search by email: ");
                        String email = new Scanner(System.in).nextLine();
                        System.out.println(employeeService.findByEmail(email));
                    }
                    case 11 -> {
                        System.out.print("Input the employees ID to get info: ");
                        Long empID = new Scanner(System.in).nextLong();
                        System.out.println(employeeService.getEmployeeById(empID));
                    }
                    case 12 ->{
                        System.out.print("Input the position to search by position: ");
                        String position = new Scanner(System.in).nextLine();
                        System.out.println(employeeService.getEmployeeByPosition(position));
                    }
                    case 13 -> employeeService.dropTable();

                }

            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }


    }
}
