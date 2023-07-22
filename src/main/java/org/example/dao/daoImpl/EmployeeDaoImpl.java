package org.example.dao.daoImpl;

import com.sun.tools.jconsole.JConsoleContext;
import org.example.config.Config;
import org.example.dao.EmployeeDao;
import org.example.models.Employee;
import org.example.models.Job;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeDaoImpl implements EmployeeDao {


    @Override
    public void createEmployee() {
        String sql = "create table if not exists employees("+
                "id serial primary key, "+
                "first_name varchar (60) not null, "+
                "last_name varchar (60) not null, "+
                "age int , "+
                "email varchar unique,"+
                "job_id int references jobs(id))";

        try(Connection connection = Config.getConnection();
            Statement statement = connection.createStatement()){
            statement.executeUpdate(sql);
            System.out.println("Employee table is created !!!");
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addEmployee(Employee employee) {
        String sql = """
                insert into employees (first_name,last_name,age,email,job_id)
                values (?,?,?,?,?)
                """;
        try(Connection connection = Config.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,employee.getFirstName());
            preparedStatement.setString(2,employee.getLastName());
            preparedStatement.setInt(3,employee.getAge());
            preparedStatement.setString(4,employee.getEmail());
            preparedStatement.setLong(5,employee.getJobId());
            preparedStatement.executeUpdate();
            System.out.println(employee.getFirstName()+" "+employee.getLastName()+" is saved!!!");
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void dropTable() {
        String sql = " drop table employees";
        try(Connection connection = Config.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.executeUpdate();
            System.out.println("Table is dropped!!!");
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void cleanTable() {
        String sql = " alter table employees alter column jobs";
        try(Connection connection = Config.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.executeUpdate();
            System.out.println(" Table is cleaned!!!");
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateEmployee(Long id, Employee employee) {
        String sql = "update employees set first_name = ?, last_name = ?, age = ?, email = ?, job_id = ? where id = ?";
        try(Connection connection = Config.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,employee.getFirstName());
            preparedStatement.setString(2,employee.getLastName());
            preparedStatement.setInt(3,employee.getAge());
            preparedStatement.setString(4,employee.getEmail());
            preparedStatement.setLong(5,employee.getJobId());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    @Override
    public List<Employee> getAllEmployees() {
        List<Employee>employees = new ArrayList<>();
        String sql = " select * from employees";
        try(Connection connection =  Config.getConnection();
        Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                employees.add(new Employee(
                resultSet.getLong("id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getInt("age"),
                resultSet.getString("email"),
                resultSet.getInt("job_id")
                ));
            }

        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return employees;
    }

    @Override
    public Employee findByEmail(String email) {
        Employee employee = new Employee();
        String sql = "select * from employees where email = ?";
        try(Connection connection = Config.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)){
          preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                 employee = new Employee(
                        resultSet.getLong("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getInt("age"),
                        resultSet.getString("email"),
                        resultSet.getInt("job_id")
                );
                 resultSet.close();
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return employee;
    }

    @Override
    public Map<Employee, Job> getEmployeeById(Long employeeId) {
        Map<Employee,Job>map = new HashMap<>();

        String sql = " select jobs.*,employees.* from employees inner join jobs  on employees.job_id=jobs.id where employees.id = ?";
        try(Connection connection = Config.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setLong(1,employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Employee employee = new Employee(
                resultSet.getLong("id"),
                resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                resultSet.getInt("age"),
                resultSet.getString("email"),
                resultSet.getInt("job_id"));
                Job job = new Job(
                resultSet.getLong("id"),
                resultSet.getString("position"),
                resultSet.getString("profession"),
                resultSet.getInt("experience"));
                map.put(employee,job);
            }


        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return map;
    }


    @Override
    public List<Employee> getEmployeeByPosition(String position) {
        List<Employee>employees = new ArrayList<>();
        String sql = "select employees.* from employees inner join jobs on employees.job_id=jobs.id where jobs.position = ?";
        try(Connection connection = Config.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,position);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                employees.add(new Employee(resultSet.getLong("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getInt("age"),
                        resultSet.getString("email"),
                        resultSet.getInt("job_id")));

            }
            resultSet.close();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return employees;
    }

}
