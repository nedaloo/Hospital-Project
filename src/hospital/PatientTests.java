package hospital;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;

public class PatientTests extends JFrame {

    String patientSSN;
    String patientName;
    JTable table;
    DefaultTableModel model;
    JButton back = new JButton("Back");

    public PatientTests(String ssn, String name) {
        this.patientSSN  = ssn;
        this.patientName = name;

        setTitle("My Medical Tests");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 600);
        setLayout(new BorderLayout(10, 10));

        String insurance = getInsurance();

        JLabel header = new JLabel(
            "Welcome " + patientName + "  |  Medical Insurance: " + insurance,
            JLabel.CENTER
        );
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(header, BorderLayout.NORTH);

        
        String[] columns = {"Test ID", "Test Name", "Date", "Time", "Doctor Name", "Result/Prescription"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(back);
        add(buttonPanel, BorderLayout.SOUTH);

        back.addActionListener(e -> {
            dispose();
            new Patien(patientSSN, patientName).setVisible(true);
        });

        loadTestData();
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private String getInsurance() {
        try {
            String query = "SELECT medical_insurance FROM Patient WHERE ssn=" + patientSSN;
            ResultSet rs = NewSqlClass.getData(query);
            if (rs != null && rs.next()) {
                String val = rs.getString("medical_insurance");
                return (val != null && !val.trim().isEmpty()) ? val : "N/A";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "N/A";
    }

    private void loadTestData() {
        try {
            String query = "SELECT T.test_id, T.test_name, T.test_date, T.test_time, " +
                           "D.name AS docName, T.result " +
                           "FROM Test T " +
                           "LEFT JOIN Performs PR ON T.test_id = PR.test_id " +
                           "LEFT JOIN Doctor D ON PR.dssn = D.dssn " +
                           "WHERE T.patient_ssn = " + patientSSN;

            ResultSet rs = NewSqlClass.getData(query);

            while (rs != null && rs.next()) {
                Object[] row = {
                    rs.getInt("test_id"),
                    rs.getString("test_name"),
                    rs.getDate("test_date"),
                    rs.getTime("test_time"),
                    rs.getString("docName"),
                    rs.getString("result")
                };
                model.addRow(row);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage());
        }
    }
}