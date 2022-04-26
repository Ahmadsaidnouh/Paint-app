package Views;

import java.awt.Font;
import java.awt.Point;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class About extends JFrame {

    private JPanel contentPane;

    public About() {
        setBounds(100, 100, 504, 539);
        setLocation(new Point(500, 111));
        setTitle("About");
        setResizable(false);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();

        panel.setBounds(10, 10, 480, 491);
        contentPane.add(panel);
        panel.setLayout(null);

        JLabel lblNewLabel = new JLabel("Unpublished Work \u00A9 2021 teamX");
        lblNewLabel.setBounds(137, 468, 232, 13);
        panel.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Welcome to Paint v1.0.0");
        lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblNewLabel_1.setBounds(155, 22, 232, 13);
        panel.add(lblNewLabel_1);

        JLabel icon = new JLabel("");
        icon.setBounds(159, 60, 128, 145);
        panel.add(icon);
        ImageIcon icon1 = new ImageIcon(this.getClass().getResource("/Assets/Images/paint-app-icon.png"));
        icon.setIcon(icon1);

        JLabel lblNewLabel_2 = new JLabel("This project was created by :");
        lblNewLabel_2.setFont(new Font("Tahome", Font.BOLD, 14));
        lblNewLabel_2.setBounds(31, 215, 425, 28);
        panel.add(lblNewLabel_2);

        JLabel lblNewLabel_3 = new JLabel("Amr Abdelsamee Youssef");
        lblNewLabel_3.setBounds(31, 253, 225, 13);
        panel.add(lblNewLabel_3);

        JLabel lblNewLabel_4 = new JLabel("ID: 7126");
        lblNewLabel_4.setBounds(276, 253, 100, 13);
        panel.add(lblNewLabel_4);

        JLabel lblNewLabel_3_1 = new JLabel("Mohamed Saeed Abdelhafez");
        lblNewLabel_3_1.setBounds(31, 278, 225, 13);
        panel.add(lblNewLabel_3_1);

        JLabel lblNewLabel_4_1 = new JLabel("ID: 7034");
        lblNewLabel_4_1.setBounds(276, 278, 100, 13);
        panel.add(lblNewLabel_4_1);

        JLabel lblNewLabel_3_2 = new JLabel("Ahmed Saeed Nouh");
        lblNewLabel_3_2.setBounds(31, 306, 225, 13);
        panel.add(lblNewLabel_3_2);

        JLabel lblNewLabel_4_2 = new JLabel("ID: 7086");
        lblNewLabel_4_2.setBounds(276, 306, 100, 13);
        panel.add(lblNewLabel_4_2);

        JLabel lblNewLabel_3_3 = new JLabel("Hussein Mourad Kassem");
        lblNewLabel_3_3.setBounds(31, 334, 225, 13);
        panel.add(lblNewLabel_3_3);

        JLabel lblNewLabel_4_3 = new JLabel("ID: 6729");
        lblNewLabel_4_3.setBounds(276, 334, 100, 13);
        panel.add(lblNewLabel_4_3);
    }
}
