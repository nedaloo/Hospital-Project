package hospital;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
public class Hospital extends JFrame implements ActionListener {
    JButton exet = new JButton("Exit");
    JButton submit = new JButton("Submit");
    JLabel welcomepage = new JLabel("Welcome");
    JLabel name = new JLabel("Name:");
    JLabel id = new JLabel("ID/SSN:");
    JTextField tname = new JTextField();
    JTextField tid = new JTextField();
    Hospital() {
        setTitle("Home");
        setBounds(100, 100, 600, 400);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        welcomepage.setBounds(150, 20, 400, 80);
        welcomepage.setFont(new Font("Arial", Font.BOLD, 50));
        add(welcomepage);
        name.setBounds(100, 130, 100, 30);
        add(name);
        tname.setBounds(200, 130, 200, 30);
        add(tname);
        id.setBounds(100, 180, 100, 30);
        add(id);
        tid.setBounds(200, 180, 200, 30);
        add(tid);
        submit.setBounds(200, 250, 100, 35);
        add(submit);
        exet.setBounds(320, 250, 80, 35);
        add(exet);
        submit.addActionListener(this);
        exet.addActionListener(this);
        setVisible(true);
    }
    public static void main(String[] args) {
        new Hospital();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exet) {
            System.exit(0);
        }
        if (e.getSource() == submit) {
            String nameInput = tname.getText();
            String ssnInput  = tid.getText().trim();

            if (nameInput.isEmpty() || ssnInput.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Name and ID/SSN");
                return;
            }

            if (nameInput.equalsIgnoreCase("admin") && ssnInput.equals("0000")) {
                new Admin().setVisible(true);
                this.dispose();
                return;
            }

            try {
                String sqlDoc = "SELECT * FROM Doctor WHERE LOWER(name)='"
                              + nameInput.toLowerCase() + "' AND dssn=" + ssnInput;

                System.out.println("Doctor Query: " + sqlDoc);

                ResultSet rsDoc = NewSqlClass.getData(sqlDoc);

                if (rsDoc == null) {
                    System.out.println("rsDoc is NULL");
                } else if (!rsDoc.next()) {
                    System.out.println("rsDoc is EMPTY - no rows found");
                } else {
                    System.out.println("Found doctor: " + rsDoc.getString("name"));
                    String docName = rsDoc.getString("name");
                    String docSSN  = rsDoc.getString("dssn");
                    JOptionPane.showMessageDialog(this, "Welcome Dr. " + docName);
                    new Hdoc(docName, docSSN).setVisible(true);
                    this.dispose();
                    return;
                }

                String sqlPat = "SELECT * FROM Patient WHERE LOWER(name)='"
                              + nameInput.toLowerCase() + "' AND ssn=" + ssnInput;

                System.out.println("Patient Query: " + sqlPat);

                ResultSet rsPat = NewSqlClass.getData(sqlPat);

                if (rsPat == null) {
                    System.out.println("rsPat is NULL");
                } else if (!rsPat.next()) {
                    System.out.println("rsPat is EMPTY - no rows found");
                } else {
                    System.out.println("Found patient: " + rsPat.getString("name"));
                    String patSSN  = rsPat.getString("ssn");
                    String patName = rsPat.getString("name");
                    new Patien(patSSN, patName).setVisible(true);
                    this.dispose();
                    return;
                }

                JOptionPane.showMessageDialog(this, "Invalid Name or ID!", 
                                              "Error", JOptionPane.ERROR_MESSAGE);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}