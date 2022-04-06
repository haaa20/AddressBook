import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ContactsModel {
    // Private internal class, encapsulating the table
    private class ContactTable {
        private TableView table;
        private TableListener changeListener;

        public ContactTable(ObservableList<Contact> items) {
            table = new TableView();
            changeListener = new TableListener();

            // Adding table columns
            TableColumn nameColumn = new TableColumn("Name");
            TableColumn numberColumn = new TableColumn("Number");
            TableColumn addressColumn = new TableColumn("Address");
            TableColumn countryColumn = new TableColumn("Country");
            table.getColumns().addAll(
                    nameColumn,
                    numberColumn,
                    addressColumn,
                    countryColumn
            );

            // The value Factories
            nameColumn.setCellValueFactory(
                    // Will look in a Contact object for a SimpleStringProperty called "name"
                    new PropertyValueFactory<Contact, SimpleStringProperty>("name")
            );
            numberColumn.setCellValueFactory(
                    new PropertyValueFactory<Contact, SimpleStringProperty>("number")
            );
            addressColumn.setCellValueFactory(
                    new PropertyValueFactory<Contact, SimpleStringProperty>("address")
            );
            countryColumn.setCellValueFactory(
                    new PropertyValueFactory<Contact, AddressBook.Countries>("country")
            );

            // Adding items
            setItems(items);
        }

        public TableView getTable() {
            return table;
        }

        public void setItems(ObservableList<Contact> items) {
            table.setItems(items);
        }
    }

    private ObservableList<Contact> data;

    public ContactsModel() {
        // EXAMPLE DATA
        data = FXCollections.observableArrayList(
                new Contact("Rob", "123", "Manchester", AddressBook.Countries.UK),
                new Contact("Sean", "321", "Manchester", AddressBook.Countries.UK)
        );
    }

    /**
     * Instances a table based on the ContactModel's data
     *
     * @return The table
     */
    public TableView table() {
        // We need to instance a new one every time, otherwise we could only have ONE table
        return new ContactTable(data).getTable();
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
