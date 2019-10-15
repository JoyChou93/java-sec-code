package org.joychou.controller;


import org.joychou.mapper.UserMapper;
import org.joychou.dao.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.List;


/**
 * @author  JoyChou (joychou@joychou.org)
 * @date    2018.08.22
 * @desc    SQL Injection
 */

@SuppressWarnings("Duplicates")
@RestController
@RequestMapping("/sqli")
public class SQLI {

    private static String driver = "com.mysql.jdbc.Driver";
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String user;
    @Value("${spring.datasource.password}")
    private String password;

    @Autowired
    private UserMapper userMapper;


    /**
     * Vul Code.
     * http://localhost:8080/sqli/jdbc/vul?username=joychou
     *
     * @param username username
     */
    @RequestMapping("/jdbc/vul")
    public String jdbc_sqli_vul(@RequestParam("username") String username){
        String result = "";
        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url, user, password);

            if(!con.isClosed())
                System.out.println("Connecting to Database successfully.");

            // sqli vuln code 漏洞代码
             Statement statement = con.createStatement();
             String sql = "select * from users where username = '" + username + "'";
             System.out.println(sql);
             ResultSet rs = statement.executeQuery(sql);


            System.out.println("-----------------");

            while(rs.next()){
                String res_name = rs.getString("username");
                String res_pwd = rs.getString("password");
                result +=  res_name + ": " + res_pwd + "\n";
                System.out.println(res_name + ": " + res_pwd);

            }
            rs.close();
            con.close();


        }catch (ClassNotFoundException e) {
            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();
        }catch (SQLException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();

        }finally{
            System.out.println("-----------------");
            System.out.println("Connect database done.");
        }
        return result;
    }


    /**
     * Security Code.
     * http://localhost:8080/sqli/jdbc/sec?username=joychou
     *
     * @param username username
     */
    @RequestMapping("/jdbc/sec")
    public String jdbc_sqli_sec(@RequestParam("username") String username){

        String result = "";
        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url, user, password);

            if(!con.isClosed())
                System.out.println("Connecting to Database successfully.");


            // fix code
            String sql = "select * from users where username = ?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, username);
            System.out.println(st.toString());  // sql after prepare statement
            ResultSet rs = st.executeQuery();

            System.out.println("-----------------");

            while(rs.next()){
                String res_name = rs.getString("username");
                String res_pwd = rs.getString("password");
                result +=  res_name + ": " + res_pwd + "\n";
                System.out.println(res_name + ": " + res_pwd);

            }
            rs.close();
            con.close();


        }catch (ClassNotFoundException e) {
            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();
        }catch (SQLException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();

        }finally{
            System.out.println("-----------------");
            System.out.println("Connect database done.");
        }
        return result;
    }

    /**
     * vul code
     * http://localhost:8080/sqli/mybatis/vul01?username=joychou' or '1'='1
     *
     * @param username username
     */
    @GetMapping("/mybatis/vul01")
    public List<User> mybatis_vul1(@RequestParam("username") String username) {
        return userMapper.findByUserNameVul(username);
    }

    /**
     * vul code
     * http://localhost:8080/sqli/mybatis/vul02?username=joychou' or '1'='1' %23
     *
     * @param username username
     */
    @GetMapping("/mybatis/vul02")
    public List<User> mybatis_vul2(@RequestParam("username") String username) {
        return userMapper.findByUserNameVul2(username);
    }


    /**
     * security code
     * http://localhost:8080/sqli/mybatis/sec01?username=joychou
     *
     * @param username username
     */
    @GetMapping("/mybatis/sec01")
    public User mybatis_sec1(@RequestParam("username") String username) {
        return userMapper.findByUserName(username);
    }

    /**
     * security code
     * http://localhost:8080/sqli/mybatis/sec02?id=1
     *
     * @param id id
     */
    @GetMapping("/mybatis/sec02")
    public User mybatis_sec2(@RequestParam("id") Integer id) {
        return userMapper.findById(id);
    }


    /**
     * security code
     * http://localhost:8080/sqli/mybatis/sec03
     **/
    @GetMapping("/mybatis/sec03")
    public User mybatis_sec3() {
        return userMapper.OrderByUsername();
    }


}
