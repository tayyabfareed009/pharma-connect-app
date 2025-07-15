import java.util.*;

// ======== Model Classes ========

class Medicine {
    String name;
    double price;
    int quantity;

    Medicine(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}

class PharmacyBranch {
    String branchName;
    String location;
    List<Medicine> inventory;

    PharmacyBranch(String branchName, String location) {
        this.branchName = branchName;
        this.location = location;
        this.inventory = new ArrayList<>();
    }

    void addMedicine(String name, double price, int qty) {
        for (Medicine med : inventory) {
            if (med.name.equalsIgnoreCase(name)) {
                med.quantity += qty;
                return;
            }
        }
        inventory.add(new Medicine(name, price, qty));
    }

    void removeMedicine(String name) {
        inventory.removeIf(m -> m.name.equalsIgnoreCase(name));
    }

    boolean hasMedicine(String name) {
        for (Medicine med : inventory) {
            if (med.name.equalsIgnoreCase(name) && med.quantity > 0) {
                return true;
            }
        }
        return false;
    }

    void showInventory() {
        if (inventory.isEmpty()) {
            System.out.println("No medicines available.");
        } else {
            for (Medicine med : inventory) {
                System.out.println("- " + med.name + " | Price: $" + med.price + " | Qty: " + med.quantity);
            }
        }
    }
}

class Admin {
    String username;
    String password;
    Pharmacy pharmacy;

    Admin(String username, String password, Pharmacy pharmacy) {
        this.username = username;
        this.password = password;
        this.pharmacy = pharmacy;
    }
}

class Staff {
    String username;
    String password;
    PharmacyBranch branch;

    Staff(String username, String password, PharmacyBranch branch) {
        this.username = username;
        this.password = password;
        this.branch = branch;
    }
}

class User {
    String username;
    String password;
    String location;

    User(String username, String password, String location) {
        this.username = username;
        this.password = password;
        this.location = location;
    }
}

class Pharmacy {
    String name;
    List<PharmacyBranch> branches;
    List<Admin> admins;

    Pharmacy(String name) {
        this.name = name;
        this.branches = new ArrayList<>();
        this.admins = new ArrayList<>();
    }

    void addBranch(PharmacyBranch branch) {
        branches.add(branch);
    }

    void addAdmin(Admin admin) {
        admins.add(admin);
    }
}

// ======== Application Class ========

public class PharmaConnectApp {
    static List<User> users = new ArrayList<>();
    static List<Pharmacy> pharmacies = new ArrayList<>();
    static List<Staff> staffList = new ArrayList<>();

    public static void main(String[] args) {
        setupInitialData();
        Scanner sc = new Scanner(System.in);
        boolean run = true;

        while (run) {
            System.out.println("\n--- PharmaConnect ---");
            System.out.println("1. User Sign Up");
            System.out.println("2. User Login");
            System.out.println("3. Staff Login");
            System.out.println("4. Admin Login");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            String input = sc.nextLine();

            switch (input) {
                case "1": userSignUp(sc); break;
                case "2": userLogin(sc); break;
                case "3": staffLogin(sc); break;
                case "4": adminLogin(sc); break;
                case "5": run = false; break;
                default: System.out.println("Invalid input.");
            }
        }
        sc.close();
    }

    static void setupInitialData() {
        Pharmacy p1 = new Pharmacy("HealthCare");
        Pharmacy p2 = new Pharmacy("MediPlus");
        Pharmacy p3 = new Pharmacy("LifeLine");

        Admin a1 = new Admin("admin1", "pass1", p1);
        Admin a2 = new Admin("admin2", "pass2", p2);
        Admin a3 = new Admin("admin3", "pass3", p3);

        p1.addAdmin(a1);
        p2.addAdmin(a2);
        p3.addAdmin(a3);

        pharmacies.add(p1);
        pharmacies.add(p2);
        pharmacies.add(p3);
    }

    static void userSignUp(Scanner sc) {
        System.out.print("Enter username: ");
        String uname = sc.nextLine();
        System.out.print("Enter password: ");
        String pass = sc.nextLine();
        System.out.print("Enter location: ");
        String loc = sc.nextLine();

        for (User u : users) {
            if (u.username.equals(uname)) {
                System.out.println("Username already exists.");
                return;
            }
        }

        users.add(new User(uname, pass, loc));
        System.out.println("Sign up successful!");
    }

    static void userLogin(Scanner sc) {
        System.out.print("Username: ");
        String uname = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();

        for (User u : users) {
            if (u.username.equals(uname) && u.password.equals(pass)) {
                System.out.println("Login successful.");
                userMenu(sc, u.location);
                return;
            }
        }

        System.out.println("Invalid credentials.");
    }

    static void userMenu(Scanner sc, String userLocation) {
        System.out.print("Enter medicine name to search: ");
        String medName = sc.nextLine();

        boolean found = false;
        for (Pharmacy pharmacy : pharmacies) {
            for (PharmacyBranch branch : pharmacy.branches) {
                if (branch.location.equalsIgnoreCase(userLocation) && branch.hasMedicine(medName)) {
                    System.out.println("Found at " + branch.branchName + " (" + pharmacy.name + ") in " + branch.location);
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("Medicine not found in nearby pharmacies.");
        }
    }

    static void staffLogin(Scanner sc) {
        System.out.print("Username: ");
        String uname = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();

        for (Staff s : staffList) {
            if (s.username.equals(uname) && s.password.equals(pass)) {
                System.out.println("Welcome Staff " + s.username);
                staffMenu(sc, s.branch);
                return;
            }
        }

        System.out.println("Invalid staff credentials.");
    }

    static void staffMenu(Scanner sc, PharmacyBranch branch) {
        boolean inStaff = true;
        while (inStaff) {
            System.out.println("\n-- Staff Menu (" + branch.branchName + ") --");
            System.out.println("1. Add Medicine");
            System.out.println("2. Remove Medicine");
            System.out.println("3. View Inventory");
            System.out.println("4. Logout");
            System.out.print("Choose: ");
            String ch = sc.nextLine();

            switch (ch) {
                case "1":
                    System.out.print("Enter medicine name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter price: ");
                    double price = Double.parseDouble(sc.nextLine());
                    System.out.print("Enter quantity: ");
                    int qty = Integer.parseInt(sc.nextLine());
                    branch.addMedicine(name, price, qty);
                    System.out.println("Medicine added.");
                    break;
                case "2":
                    System.out.print("Enter medicine to remove: ");
                    String rmed = sc.nextLine();
                    branch.removeMedicine(rmed);
                    System.out.println("Medicine removed.");
                    break;
                case "3":
                    branch.showInventory();
                    break;
                case "4":
                    inStaff = false;
                    break;
                default:
                    System.out.println("Invalid input.");
            }
        }
    }

    static void adminLogin(Scanner sc) {
        System.out.print("Admin username: ");
        String uname = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();

        for (Pharmacy p : pharmacies) {
            for (Admin a : p.admins) {
                if (a.username.equals(uname) && a.password.equals(pass)) {
                    System.out.println("Welcome Admin of " + p.name);
                    adminMenu(sc, p);
                    return;
                }
            }
        }

        System.out.println("Invalid admin credentials.");
    }

    static void adminMenu(Scanner sc, Pharmacy pharmacy) {
        boolean inAdmin = true;
        while (inAdmin) {
            System.out.println("\n-- Admin Menu (" + pharmacy.name + ") --");
            System.out.println("1. Add Branch");
            System.out.println("2. Add Staff");
            System.out.println("3. View Branches");
            System.out.println("4. View Staff");
            System.out.println("5. Logout");
            System.out.print("Choose: ");
            String ch = sc.nextLine();

            switch (ch) {
                case "1":
                    System.out.print("Branch name: ");
                    String bname = sc.nextLine();
                    System.out.print("Location: ");
                    String loc = sc.nextLine();
                    PharmacyBranch newBranch = new PharmacyBranch(bname, loc);
                    pharmacy.addBranch(newBranch);
                    System.out.println("Branch added.");
                    break;
                case "2":
                    System.out.print("Staff username: ");
                    String sname = sc.nextLine();
                    System.out.print("Password: ");
                    String spass = sc.nextLine();
                    System.out.print("Assign to branch name: ");
                    String assignB = sc.nextLine();
                    PharmacyBranch assignedBranch = null;
                    for (PharmacyBranch b : pharmacy.branches) {
                        if (b.branchName.equalsIgnoreCase(assignB)) {
                            assignedBranch = b;
                            break;
                        }
                    }
                    if (assignedBranch != null) {
                        Staff s = new Staff(sname, spass, assignedBranch);
                        staffList.add(s);
                        System.out.println("Staff added.");
                    } else {
                        System.out.println("Branch not found.");
                    }
                    break;
                case "3":
                    if (pharmacy.branches.isEmpty()) {
                        System.out.println("No branches added.");
                    } else {
                        for (PharmacyBranch b : pharmacy.branches) {
                            System.out.println(b.branchName + " - " + b.location);
                        }
                    }
                    break;
                case "4":
                    for (Staff s : staffList) {
                        if (pharmacy.branches.contains(s.branch)) {
                            System.out.println(s.username + " (Branch: " + s.branch.branchName + ")");
                        }
                    }
                    break;
                case "5":
                    inAdmin = false;
                    break;
                default:
                    System.out.println("Invalid input.");
            }
        }
    }
}
