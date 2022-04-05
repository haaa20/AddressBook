import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ContactsModel {
    private ObservableList<Contact> data;

    public ContactsModel() {
        // EXAMPLE DATA
        data = FXCollections.observableArrayList(
                new Contact("Rob", "123", "Manchester", AddressBook.Countries.UK),
                new Contact("Sean", "321", "Manchester", AddressBook.Countries.UK)
        );
    }

    /**
     * Adds a contact
     *
     * @param c Contact
     */
    public void add(Contact c) {
        data.add(c);
    }

    /**
     * Checks if the given contact exists in the data
     *
     * @param c Contact
     * @return True if found
     */
    public boolean exists(Contact c) {
        return data.contains(c);
    }

    /**
     * Checks if a contact if a contact with matching name and number exists
     *
     * @param name String
     * @param number String
     * @return True if found
     */
    public boolean exists(String name, String number) {
        for (Contact c: data) {
            if (c.getName().equals(name) && c.getNumber().equals(number)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes the contact that matches the name and number, if it exists
     *
     * @param name String
     * @param number String
     * @return True if the contact has been successfully removed
     */
    public boolean remove(String name, String number) {
        for (Contact c: data) {
            if (c.getName().equals(name) && c.getNumber().equals(number)) {
                return data.remove(c);
            }
        }
        return false;
    }

    public ObservableList<Contact> getData() {
        return data;
    }
}
