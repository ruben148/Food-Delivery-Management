import businesslayer.*;
import datalayer.Serializer;
import presentationlayer.AdminGUI;
import presentationlayer.ClientGUI;
import presentationlayer.EmployeeGUI;
import presentationlayer.LoginGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Main {
    public Main() {
        User.getUsers();
        LoginGUI loginGUI = new LoginGUI();
        String username = loginGUI.showLogin(true);
        User user = User.getUser(username);
        if (user == null) {
            System.exit(0);
        }
        switch (user.getRole()) {
            case 0:
                ClientGUI clientGUI = new ClientGUI(user);
                break;
            case 1:
                user.getEmployee().execute();
                break;
            case 2:
                AdminGUI adminGUI = new AdminGUI();
                break;
            default:
                break;
        }
    }
    public static void main(String[] args){
        Main main = new Main();
    }
}
