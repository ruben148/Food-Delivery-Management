package presentationlayer;

import businesslayer.User;
import datalayer.Serializer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

public class LoginGUI extends JDialog {

    String username;
    String password;
    ArrayList<User> users;

    public LoginGUI() {
        super();
        setSize(230, 130);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());
        setResizable(false);

        JLabel usernameLabel = new JLabel("Username");
        JTextField usernameTextField = new JTextField("emp");
        usernameTextField.setColumns(12);
        JLabel passwordLabel = new JLabel("Password");
        JTextField passwordTextField = new JTextField("emp");
        passwordTextField.setColumns(12);
        JButton registerButton = new JButton("Register");
        JButton loginButton = new JButton("Login");
        getRootPane().setDefaultButton(loginButton);
        registerButton.addActionListener(e -> {
            username = usernameTextField.getText();
            password = passwordTextField.getText();
            if (!User.exists(username)) {
                new User(username, password, 0);
                usernameTextField.setText("");
                passwordTextField.setText("");
                Serializer.writeUserInfo(User.getUsers());
            }
        });

        loginButton.addActionListener(e -> {
            username = usernameTextField.getText();
            password = passwordTextField.getText();
            User user;
            if ((user = User.getUser(username)) != null)
                if (user.getPassword().equals(password)) {
                    setVisible(false);
                    dispose();
                }
        });

        add(usernameLabel);
        add(usernameTextField);
        add(passwordLabel);
        add(passwordTextField);
        add(registerButton);
        add(loginButton);
    }

    public String showLogin(boolean v) {
        if (v) {
            setModal(true);
            setVisible(true);
        }
        return username;
    }
}