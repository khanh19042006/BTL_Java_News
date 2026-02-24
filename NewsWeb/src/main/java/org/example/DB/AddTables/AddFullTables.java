package org.example.DB.AddTables;

import org.example.DB.AddTables.Add.*;

public class AddFullTables {
    public static void addTable(){
        AddContent.importFromJson("src/main/resources/Data/content.json");
        AddCategory.importFromJson("src/main/resources/Data/category.json");
        AddComments.importFromJson("src/main/resources/Data/comments.json");
        AddHistory.importFromJson("src/main/resources/Data/history.json");
        AddRememberToken.importFromJson("src/main/resources/Data/rememberToken.json");
        AddRole.importFromJson("src/main/resources/Data/role.json");
        AddToken.importFromJson("src/main/resources/Data/token.json");
        AddUsers.importFromJson("src/main/resources/Data/users.json");
//        AddNews.importFromJson("src/main/resources/Data/users.json");
    }
}
