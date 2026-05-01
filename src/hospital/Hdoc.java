package hospital;

import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Hdoc extends JFrame implements ActionListener {

    String doctorName;
    String doctorId;

    Label namePation = new Label("Patient Name:");
    Label idPation   = new Label("Patient SSN:");
    Label dis        = new Label("Prescription:");
    Label testl      = new Label("Test Name:");
    Label insurLabel = new Label("Medical Insurance:");

    TextField namef     = new TextField();
    TextField phonef    = new TextField();
    TextField testName  = new TextField();
    TextField insurf    = new TextField();
    TextArea  disf      = new TextArea();

    JButton sub  = new JButton("Submit");
    JButton back = new JButton("Back");
    JLabel welcomepage = new JLabel();

    public Hdoc(String nameInput, String idInput) {
                setTitle("Doctor");

        this.doctorName = nameInput;
        this.doctorId   = idInput;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 720);
        setLayout(null);

        welcomepage.setText("Hello Dr. " + doctorName);
        welcomepage.setBounds(200, 10, 600, 60);
        welcomepage.setFont(new Font("Arial", Font.BOLD, 30));
        add(welcomepage);

        namePation.setBounds(90, 100, 130, 30);  add(namePation);
        namef.setBounds(250, 100, 200, 30);      add(namef);

        idPation.setBounds(90, 150, 130, 30);    add(idPation);
        phonef.setBounds(250, 150, 200, 30);     add(phonef);

        testl.setBounds(90, 200, 130, 30);       add(testl);
        testName.setBounds(250, 200, 200, 30);   add(testName);

        insurLabel.setBounds(90, 250, 150, 30);  add(insurLabel);
        insurf.setBounds(250, 250, 200, 30);     add(insurf);

        dis.setBounds(90, 300, 130, 30);         add(dis);
        disf.setBounds(250, 300, 350, 150);      add(disf);

        sub.setBounds(400, 500, 100, 35);        add(sub);
        back.setBounds(280, 500, 100, 35);       add(back);

        sub.addActionListener(this);
        back.addActionListener(this);

        setVisible(true);
        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == back) {
            dispose();
            new Hospital();
            return;
        }

        if (e.getSource() == sub) {
            String patName   = namef.getText().trim();
            String patSSN    = phonef.getText().trim();
            String tName     = testName.getText().trim();
            String result    = disf.getText().trim();
            String insurance = insurf.getText().trim();

            String tDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
            String tTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

            if (patName.isEmpty() || patSSN.isEmpty() || tName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill Name, SSN, and Test Name!");
                return;
            }

            try {
                
                if (!insurance.isEmpty()) {
                    String updateInsurance = "UPDATE Patient SET medical_insurance='" 
                                          + insurance + "' WHERE ssn=" + patSSN;
                    NewSqlClass.executeNonQuery(updateInsurance);
                }

               
                String insertTest = "INSERT INTO Test (test_name, test_date, test_time, result, patient_ssn) "
                                  + "VALUES ('" + tName + "', '" + tDate + "', '" + tTime 
                                  + "', '" + result + "', " + patSSN + ")";

                boolean isSaved = NewSqlClass.executeNonQuery(insertTest);

                if (isSaved) {
                    
                    String getID = "SELECT MAX(test_id) AS lastID FROM Test";
                    ResultSet rs = NewSqlClass.getData(getID);

                    if (rs != null && rs.next()) {
                        int lastTestId = rs.getInt("lastID");

                        
                        String insertPerforms = "INSERT INTO Performs (dssn, test_id) VALUES (" 
                                             + doctorId.trim() + ", " + lastTestId + ")";
                        boolean isLinked = NewSqlClass.executeNonQuery(insertPerforms);

                        if (isLinked) {
                            JOptionPane.showMessageDialog(this, "✅ Saved and Linked to Dr. " + doctorName);
                            namef.setText(""); phonef.setText(""); 
                            testName.setText(""); disf.setText(""); 
                            insurf.setText("");
                        } else {
                            JOptionPane.showMessageDialog(this, "⚠️ Test saved, but failed to link with Doctor.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Failed to save Test. Check Patient SSN.");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }
}