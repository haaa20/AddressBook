import javafx.beans.property.SimpleStringProperty;

public class Contact {
    private SimpleStringProperty name;
    private SimpleStringProperty number;
    private SimpleStringProperty address;
    private AddressBook.Countries country;

    // This is another change

    public Contact(
            SimpleStringProperty name,
            SimpleStringProperty number,
            SimpleStringProperty address,
            AddressBook.Countries country
    ) {
        this.name = name;
        this.number = number;
        this.address = address;
        this.country = country;
    }
    public Contact(
            String name,
            String number,
            String address,
            AddressBook.Countries country
    ) {
        this.name = new SimpleStringProperty(name);
        this.number = new SimpleStringProperty(number);
        this.address = new SimpleStringProperty(address);
        this.country = country;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getNumber() {
        return number.get();
    }

    public void setNumber(String number) {
        this.number.set(number);
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public AddressBook.Countries getCountry() {
        return country;
    }

    public void setCountry(AddressBook.Countries country) {
        this.country = country;
    }
}
