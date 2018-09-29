import javax.swing.*;
import java.awt.*;

public class UI {
    private char[] password;
    public UI() {
        JFrame jFrame = new JFrame("登录窗口");
        jFrame.setSize(300,160);
        jFrame.setLocationRelativeTo(null);
        jFrame.setLayout(new BorderLayout());
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel jPanel_name = new JPanel();
        JLabel jLabel_name = new JLabel("账号：");
        JTextField jTextField_name = new JTextField();
        jTextField_name.setPreferredSize(new Dimension(180, 30));
        jPanel_name.add(jLabel_name);
        jPanel_name.add(jTextField_name);
        jFrame.add(jPanel_name, BorderLayout.NORTH);

        JPanel jPanel_password = new JPanel();
        JLabel jLabel_password = new JLabel("密码：");
        JPasswordField jPasswordField_password = new JPasswordField();
        jPasswordField_password.setPreferredSize(new Dimension(180, 30));
        jPanel_password.add(jLabel_password);
        jPanel_password.add(jPasswordField_password);
        jFrame.add(jPanel_password, BorderLayout.CENTER);
        password = jPasswordField_password.getPassword();

        JPanel jPanel_sign = new JPanel();
        JButton jButton_signIn = new JButton("登录");
        JButton jButton_signout = new JButton("没有账号？现在注册！");
        jPanel_sign.add(jButton_signIn);
        jPanel_sign.add(jButton_signout);
        jFrame.add(jPanel_sign, BorderLayout.SOUTH);

        jFrame.setVisible(true);
    }
}
