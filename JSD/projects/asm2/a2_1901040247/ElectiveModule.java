package a2_1901040247;

import java.sql.SQLException;

public class ElectiveModule extends Module {
    private final String department;

    public ElectiveModule(String name, int semester, int credits, String department) throws SQLException {
        super(name, semester, credits);
        generateCode(semester);
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }
}
