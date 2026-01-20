package org.example.dao;

import org.example.entity.Category;
import org.example.utils.TextUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CategoryDAO {

    public List<Category> getCategory(){
        String sql = "Select id, name " +
                "from category";
        List<Category> categories = new ArrayList<>();
        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            try(ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    Category category = new Category();

                    category.setId(rs.getString("id"));

                    category.setName(TextUtils.capitalizeFully
                            (rs.getString("name"))
                    );


                    categories.add(category);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return categories;
    }
}
