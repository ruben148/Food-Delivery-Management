package businesslayer;

import datalayer.Serializer;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class User implements Serializable {
    protected final String username;
    protected final String password;
    protected int numberOfOrders;
    protected int numberOfOrdersTemporary;
    protected final int role;
    protected static ArrayList<User> users;
    protected static ArrayList<Employee> employees;
    static {
        User.setUsers(Serializer.readUserInfo());
        User.setEmployees(Serializer.readEmployeeInfo());
        if(users.size()==0) {
            new User("admin", "admin", 2);
            employees.add(new Employee("emp", "emp"));
            Serializer.writeEmployeeInfo(employees);
            Serializer.writeUserInfo(users);
        }
    }

    public User(String u, String p, int r){
        username = u;
        password = p;
        role = r;
        users.add(this);
    }
    public String getUsername() {
        return username;
    }
    public static User getUser(String username){
        for(User user: users)
            if(user.username.equals(username))
                return user;
        return null;
    }
    public static void setUsers(ArrayList<User> users){
        User.users = users;
    }
    public static ArrayList<Employee> getEmployees(){
        return employees;
    }
    public Employee getEmployee(){
        for(Employee employee: employees)
            if(username.equals(employee.username))
                return employee;
        return null;
    }
    public static void setEmployees(ArrayList<Employee> employees){
        User.employees = employees;
    }
    public String getPassword(){
        return password;
    }
    public int getRole(){
        return role;
    }
    public static boolean exists(String username){
        for(User user: users)
            if(user.username.equals(username))
                return true;
        return false;
    }
    public static ArrayList<User> getUsers(){
        return users;
    }
    public int getNumberOfOrders(){
        return numberOfOrders;
    }
    public void increaseOrderNumber(){
        numberOfOrders++;
    }
    public int getTemporaryNumberOfOrders(){
        return numberOfOrdersTemporary;
    }
    public boolean setTemporaryNumberOfOrders(int numberOfOrdersTemporary) {
        this.numberOfOrdersTemporary = numberOfOrdersTemporary;
        return true;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username);
    }
    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
