package org.example.dao.daoImpl;

import org.example.config.Config;
import org.example.dao.JobDao;
import org.example.models.Job;
import org.w3c.dom.ls.LSOutput;

import javax.crypto.CipherInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobDaoImpl implements JobDao {

    @Override
    public void createJobTable() {
        String sql = "create table if not exists jobs("+
                "id serial primary key,"+
                "position varchar (60),"+
                "profession varchar (60),"+
                "description varchar (60),"+
                "experience int )";
        try (Connection connection = Config.getConnection();
                Statement statement = connection.createStatement()){
            statement.executeUpdate(sql);
            System.out.println("Table is created!!!");
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addJob(Job job) {
        String sql = "insert into jobs(" +
                "position,profession,description,experience)"+
                "values(?,?,?,?)";
        try(
                Connection connection = Config.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
                ){
            preparedStatement.setString(1, job.getPosition());
            preparedStatement.setString(2, job.getProfession());
            preparedStatement.setString(3, job.getDescription());
            preparedStatement.setInt(4,job.getExperience());
            preparedStatement.executeUpdate();
            System.out.println(job.getPosition()+" "+job.getProfession()+" is saved!!!");
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Job getJobById(Long jobId) {
        Job job = null;
        String sql = "select * from jobs where id = ?;";
        try(Connection connection = Config.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setLong(1,jobId);
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()){
             job = new Job();
                     job.setId(resultSet.getLong("id"));
                     job.setPosition(resultSet.getString("position"));
                     job.setProfession(resultSet.getString("profession"));
                     job.setDescription(resultSet.getString("description"));
                     job.setExperience(resultSet.getInt("experience"));
         }
         resultSet.close();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return job;
    }

    @Override
    public List<Job> sortByExperience(String ascOrDesc) {
        List<Job> jobs = new ArrayList<>();
        String sql = "select * from jobs order by experience " +ascOrDesc;
        try(Connection connection = Config.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Job job = new Job(
                resultSet.getLong("id"),
                resultSet.getString("position"),
                resultSet.getString("profession"),
                resultSet.getString("description"),
                resultSet.getInt("experience")
                );
                jobs.add(job);
            }

            resultSet.close();

        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return jobs;
    }

    @Override
    public Job getJobByEmployeeId(Long employeeId) {
        Job job1 = new Job();
        String sql = "select * from employees inner join jobs on employees.job_id=jobs.id where jobs.id = ?";
        try(Connection connection = Config.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setLong(1,employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                job1 = new Job();
                job1.setId(resultSet.getLong("id"));
                job1.setPosition(resultSet.getString("position"));
                job1.setProfession(resultSet.getString("profession"));
                job1.setDescription(resultSet.getString("description"));
                job1.setExperience(resultSet.getInt("experience"));
            }
            preparedStatement.close();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return job1;
    }

    @Override
    public void deleteDescriptionColumn() {
        String sql = "alter table jobs drop column description";
        try(Connection connection = Config.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.executeUpdate();
            System.out.println(" The column is deleted!!!");
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
