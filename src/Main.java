import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

class Car {
    private String carId;
    private String brand;
    private String model;
    private double basePricePerDay;
    private boolean isAvailable;

    public Car(String carId, String brand, String model, double basePricePerDay) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.basePricePerDay = basePricePerDay;
        this.isAvailable = true;
    }

    public String getCarId() {
        return carId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public Double getPrice() {
        return basePricePerDay;
    }

    public Double calculatePrice(int rentalDays) {
        return basePricePerDay * rentalDays;
    }

    public boolean getisAvailable() {
        return isAvailable;
    }

    public void rentThisCar() {
        isAvailable = false;
    }

    public void returnCar() {
        isAvailable = true;
    }

}

class Customer {
    private String name;
    private String customerId;

    public Customer(String name, String customerId) {
        this.name = name;
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public String getCustomerId() {
        return customerId;
    }

}

class Rental {
    private Car car;
    private Customer customer;
    private int days;

    public Rental(Car car, Customer customer, int days) {
        this.car = car;
        this.customer = customer;
        this.days = days;
    }

    public Car getCar() {
        return car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }

}

class CarRentalSystem {
    // To save memory we are just declaring here
    private List<Car> carsArray;
    private List<Customer> customersArray;
    private List<Rental> rentalsArray;

    // We initialize it only when the constructor is called

    public CarRentalSystem() {
        carsArray = new ArrayList<>();
        customersArray = new ArrayList<>();
        rentalsArray = new ArrayList<>();
    }

    public void addCar(Car car) {
        carsArray.add(car);
    }

    public void addCustomer(Customer customer) {
        customersArray.add(customer);
    }

    public void rentCar(Car car, Customer customer, int days) {

        if (car.getisAvailable()) {
            car.rentThisCar();
            carsArray.remove(car);
            rentalsArray.add(new Rental(car, customer, days));
        } else {
            System.out.println("This car is not available for rent.");
        }

    }

    public void returnCar(Car car) {

        for (Rental rentedCar : rentalsArray) {
            if (rentedCar.getCar() == car) {
                rentalsArray.remove(rentedCar);
                carsArray.add(car);
                car.returnCar();
                return;
            }
        }
        System.out.println("Car was not rented");
    }

    public void Menu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("===== Car Rental System =====");
            System.out.println("1. Rent a Car");
            System.out.println("2. Return a Car");
            System.out.println("3. View All Available cars");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {

                System.out.println("\n== Rent a Car ==\n");
                System.out.print("Enter your name: ");
                String customerName = scanner.nextLine();
                System.out.println("\n Available Cars \n");

                for (Car car : carsArray) {
                    if (car.getisAvailable()) {
                        System.out.println(car.getCarId() + " - " + car.getBrand() + " " + car.getModel() + " $"
                                + car.getPrice() + "/day");
                    }
                }

                Car selectedCar = null;
                while (true) {
                    System.out.print("\nEnter the car ID you want to rent: ");
                    String carId = scanner.nextLine();
                    for (Car car : carsArray) {
                        if (car.getCarId().equals(carId)) {
                            selectedCar = car;
                            break;
                        }
                    }
                    if (selectedCar != null) {
                        break;
                    } else {
                        System.out.println("Invalid Car ID. Try again.");
                    }
                }

                System.out.print("Enter the number of days for rental: ");
                int rentalDays = scanner.nextInt();
                scanner.nextLine();
                // That's because the Scanner.nextInt method does not read the newline character
                // in your input created by hitting "Enter," and so the call to Scanner.nextLine
                // returns after reading that newline. so after no of rental days we should use
                // scanner.nextLine() other wise it will skip the confirm user input

                Customer newCustomer = new Customer(customerName, "CUS" + customersArray.size() + 1);

                if (selectedCar != null) {
                    double totalPrice = selectedCar.calculatePrice(rentalDays);
                    System.out.println("\n== Rental Information ==\n");
                    System.out.println("Customer ID: " + newCustomer.getCustomerId());
                    System.out.println("Customer Name: " + newCustomer.getName());
                    System.out.println("Car: " + selectedCar.getBrand() + " " + selectedCar.getModel());
                    System.out.println("Rental Days: " + rentalDays);
                    System.out.printf("Total Price: $%.2f%n", totalPrice);

                    // System.out.print("\nConfirm rental (Y/N): ");
                    // String confirm = scanner.nextLine();
                    // System.out.println();

                    System.out.print("\nConfirm rental (Y/N): ");
                    String confirmRental = scanner.nextLine();

                    if (confirmRental.equalsIgnoreCase("Y")) {
                        rentCar(selectedCar, newCustomer, rentalDays);
                        System.out.println("\nCar rented successfully\n");
                    } else {
                        System.out.println("\nRental canceled\n");
                    }

                }

            } else if (choice == 2) {
                while (true) {
                    System.out.println("\n== Return a Car ==\n");
                    System.out.print("\nEnter the name that you used while renting the car: ");
                    String custName = scanner.nextLine();
                    System.out.print("\nEnter the car ID you want to return: ");
                    String carId = scanner.nextLine();

                    Customer customer = null;

                    for (Rental verifyCustomer : rentalsArray) {
                        if (verifyCustomer.getCustomer().getName().equals(custName)) {
                            customer = verifyCustomer.getCustomer();

                            break;
                        }
                    }

                    Car selectedCar = null;
                    if (customer != null) {
                        for (Rental verifyCar : rentalsArray) {
                            if (verifyCar.getCar().getCarId().equals(carId)) {
                                selectedCar = verifyCar.getCar();
                                break;
                            }
                        }
                    }

                    if (selectedCar != null && customer != null) {
                        returnCar(selectedCar);
                        System.out.println(customer.getName() + ", your car has been successfully returned\n");
                        break;
                    } else {
                        System.out.println("\nInvalid name or car ID");
                        System.out.print("\nPress 1 to try again or press 0 to go back to main menu: ");
                        int repeat = scanner.nextInt();
                        scanner.nextLine();
                        if (repeat == 0) {
                            break;
                        }

                    }
                }

            } else if (choice == 3) {
                System.out.println("\nAvailable Vehicles\n");
                for (int i = 0; i < carsArray.size(); i++) {
                    System.out.println(i + 1 + ". " + carsArray.get(i).getBrand() + " " + carsArray.get(i).getModel());
                }
                System.out.println();
            } else if (choice == 4) {
                break;
            } else {
                System.out.println("\nInvalid input. Please try again. \n");
            }

        }

    }

}

public class Main {
    public static void main(String[] args) {

        // We can access attributes of objects using the get methods since the
        // attributes are private and if they were public we can just use object. to
        // access the value of it (similar to json object)

        CarRentalSystem rentalSystem = new CarRentalSystem();

        Car car1 = new Car("C001", "Toyota", "Camry", 90);
        Car car2 = new Car("C002", "Toyota", "Rav4", 120);
        Car car3 = new Car("C003", "Nissan", "Altima", 80);

        rentalSystem.addCar(car1);
        rentalSystem.addCar(car2);
        rentalSystem.addCar(car3);

        rentalSystem.Menu();

    }
}