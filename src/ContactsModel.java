import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.event.ChangeListener;

public class ContactsModel {
    // Private internal class, encapsulating the table
    private class ContactTable {
        private TableView table;
        private ContactListener changeListener;

        public ContactTable(ObservableList<Contact> items) {
            table = new TableView();
            changeListener = new ContactListener();

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

            // Adding context menu
            ContextMenu contactMenu = new ContextMenu();
            MenuItem editSelectedContact = new MenuItem("Edit selected contact");
            contactMenu.getItems().add(editSelectedContact);
            table.setContextMenu(contactMenu);
            editSelectedContact.setOnAction(event -> {
                // Sends the contact to the editor
                editor.editContact((Contact) table.getSelectionModel().getSelectedItem());
            });

        }

        public TableView getTable() {
            return table;
        }

        public void setItems(ObservableList<Contact> items) {
            table.setItems(items);
        }
    }

    private ObservableList<Contact> data;
    private ContactEditor editor;

    public ContactsModel(ContactEditor editor) {
        this.editor = editor;
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
        ContactTable contactTable = new ContactTable(data);

        return contactTable.getTable();
    }


    /**
     * Adds a contact
     *
     * @param c Contact
     */
    public void add(Contact c) {
        data.add(c);
    }

    /** Removes the given contact
     *
     * @param c Contact
     * @return True if remove was successful
     */
    public boolean remove(Contact c) {
        if (exists(c)) {
            data.remove(c);
            return true;
        }
        return false;
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
     * Replaces an old Contact with a new
     *
     * @param cOld Contact
     * @param cNew Contact
     * @return True if successful
     */
    public boolean replace(Contact cOld, Contact cNew) {
        boolean ableTo = remove(cOld);
        if (ableTo) {
            add(cNew);
        }
        return ableTo;
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
