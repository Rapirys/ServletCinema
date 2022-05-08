package com.servlet.cinema.application.model.repository;


import com.servlet.cinema.application.entities.Role;
import com.servlet.cinema.application.entities.User;
import com.servlet.cinema.framework.data.ConnectionPool;
import com.servlet.cinema.framework.data.Dao;
import com.servlet.cinema.framework.exaptions.RepositoryException;

import org.apache.log4j.Logger;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class UserRepository extends Dao {
    private final static Logger logger = Logger.getLogger(UserRepository.class);
    private final static String findByUsernameOrEmailQueryUE = "SELECT * FROM usr u WHERE u.username = ? OR u.email = ?";
    private final static String findByUsername = "SELECT * FROM usr u WHERE u.username = ?";
    private final static String insertUserAEPU = "INSERT INTO usr (active, email,password, username) " +
            "VALUES (?, ?, ?, ?) RETURNING id";

    private final static String findUserRolesById = "SELECT * FROM user_role u WHERE u.user_id = ?";
    private final static String deleteRole = "DELETE FROM user_role WHERE user_id = ? AND roles = ?";
    private final static String insertRole = "INSERT INTO user_role (user_id, roles) VALUES (?,?)";

    public User findByUsernameOrEmail(String username, String email) {
        try {
            PreparedStatement statement = connection.prepareStatement(findByUsernameOrEmailQueryUE);
            statement.setString(1, username);
            statement.setString(2, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Integer resultId = resultSet.getInt("id");
                boolean resultActive = resultSet.getBoolean("active");
                String resultEmail = resultSet.getString("email");
                String resultPass = resultSet.getString("password");
                String resultUsername = resultSet.getString("username");
                User user = new User(resultId, resultUsername, resultEmail, resultActive, resultPass);
                user.setRoles(findUserRolesById(user.getId()));
                return user;
            } else return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException(e);
        }
    }


    public Set<Role> findUserRolesById(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement(findUserRolesById);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            Set<Role> roles = new HashSet<>();
            while (rs.next()) {
                roles.add(Role.valueOf(rs.getString("roles")));
            }
            return roles;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException(e);
        }
    }


    public void save(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement(insertUserAEPU);
            statement.setBoolean(1, user.isActive());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getUsername());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            user.setId(resultSet.getInt("id"));
            rewriteRoles(user);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException(e);
        }
    }


    public void rewriteRoles(User user) throws  SQLException{
        if (user.getId() == null)
            throw new SQLException("user id cant be null while roles updating");
        if (user.getRoles() == null) {
            Set<Role> roles= new HashSet<>();
            roles.add(Role.USER);
            user.setRoles(roles);
        }
        Set<Role> roles = findUserRolesById(user.getId());
        Set<Role> inserted = new HashSet<>();
        for (Role role : roles){
            if (!user.getRoles().contains(role)){
                PreparedStatement deleteRoleStm = connection.prepareStatement(deleteRole);
                deleteRoleStm.setInt(1,user.getId());
                deleteRoleStm.setString(2,role.toString());
                deleteRoleStm.execute();
            }
            else inserted.add(role);
        }
        for (Role role : user.getRoles()){
            if (!inserted.contains(role)) {
                PreparedStatement insertRoleStm = connection.prepareStatement(insertRole);
                insertRoleStm.setInt(1, user.getId());
                insertRoleStm.setString(2, role.toString());
                insertRoleStm.execute();
            }
        }
    }

    public UserRepository() {connection = ConnectionPool.getInstance().getConnection();}
    private UserRepository(Connection connection) {
        this.connection = connection;
    }
    public static UserRepository bound(Dao dao){
        return new UserRepository(dao.connection);
    };

}
