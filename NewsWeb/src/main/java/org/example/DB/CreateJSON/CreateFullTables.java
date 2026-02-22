package org.example.DB.CreateJSON;

import org.example.DB.CreateJSON.Create.*;

public class CreateFullTables {
    public static void createTables(){
        CreateContentNewsJSON.exportToJson("src/main/resources/Data/content.json");
        CreateCategoryJSON.exportToJson("src/main/resources/Data/category.json");
        CreateCommentsJSON.exportToJson("src/main/resources/Data/comments.json");
        CreateHistoryJSON.exportToJson("src/main/resources/Data/history.json");
        CreateRememberTokenJSON.exportToJson("src/main/resources/Data/rememberToken.json");
        CreateRoleJSON.exportToJson("src/main/resources/Data/role.json");
        CreateTokenJSON.exportToJson("src/main/resources/Data/token.json");
        CreateUsersJSON.exportToJson("src/main/resources/Data/users.json");
//        CreateNewsJSON.exportToJson("src/main/resources/Data/users.json");
    }
}
