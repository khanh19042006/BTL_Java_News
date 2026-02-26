package org.example.dao;

import org.example.dto.CategoryDTO;
import org.example.utils.TextUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CategoryDAO {

    public List<CategoryDTO> getCategory(){
        String sql = "Select code, name " +
                "from category";
        List<CategoryDTO> categories = new ArrayList<>();
        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            try(ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    CategoryDTO category = new CategoryDTO();

                    category.setCode(rs.getString("code"));

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
