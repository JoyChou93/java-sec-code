package org.joychou.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;


/**
 * @author  JoyChou (joychou@joychou.org)
 * @date    2018.08.22
 * @desc    SQL Injection
 */

@Controller
@RequestMapping("/sqli")
public class SQLI {

    @RequestMapping("/jdbc")
    @ResponseBody
    public static String jdbc_sqli(HttpServletRequest request){

        String name = request.getParameter("name");
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/sectest";
        String user = "root";
        String password = "woshishujukumima";
        String result = "";
        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url,user,password);

            if(!con.isClosed())
                System.out.println("Connecting to Database successfully.");

            // sqli vuln code 漏洞代码
             Statement statement = con.createStatement();
             String sql = "select * from users where name = '" + name + "'";
             System.out.println(sql);
             ResultSet rs = statement.executeQuery(sql);

            // fix code 用预处理修复SQL注入
//            String sql = "select * from users where name = ?";
//            PreparedStatement st = con.prepareStatement(sql);
//            st.setString(1, name);
//            System.out.println(st.toString());  // 预处理后的sql
//            ResultSet rs = st.executeQuery();

            System.out.println("-----------------");

            while(rs.next()){
                String res_name = rs.getString("name");
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

}
