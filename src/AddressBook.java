import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AddressBook extends Application implements ContactEditor{
    enum Countries {
        UK,
        US,
        DE,
        CN
    }

    // The contact model
    private ContactsModel contactsModel = new ContactsModel(this);
    // The TabPane and Tabs
    private TabPane tabPane;
    private Tab entryTab;
    private Tab listTab;
    private Tab searchTab;
    // The entry fields
    private TextField nameField;
    private TextField numberField;
    private TextField addressField;
    // The selected contact I DON'T LIKE THIS AAAAAAAA
    private Contact selectedContact;

    public void run(String[] args) {
        launch(args);
    }

    private MenuBar setupMenu() {
        MenuBar menuBar = new MenuBar();

        // FILE
        Menu file = new Menu("File");
        MenuItem save = new MenuItem("Save");
        MenuItem load = new MenuItem("Load");
        file.getItems().addAll(save, load);

        // VIEW
        Menu view = new Menu("View");
        MenuItem entry = new MenuItem("Entry");
        entry.setOnAction(e -> {
            tabPane.getSelectionModel().select(entryTab);
        });
        MenuItem list = new MenuItem("List");
        list.setOnAction(e -> {
            tabPane.getSelectionModel().select(listTab);
        });
        MenuItem search = new MenuItem("Search");
        search.setOnAction(e -> {
            tabPane.getSelectionModel().select(searchTab);
        });
        view.getItems().addAll(entry, list, search);

        menuBar.getMenus().addAll(file, view);

        return menuBar;
    }

    private Tab setupEntryTab() {
        Tab entry = new Tab("Entry");
        HBox buttons = new HBox();
        TilePane tilePane = new TilePane(10, 4);
        BorderPane borderPane = new BorderPane();

        // Setting up the nodes to be added to the tile pane
        Label nameLabel = new Label("Name:");
        Label numberLabel = new Label("Number:");
        Label addressLabel = new Label("Address:");
        Label countryLabel = new Label("Country:");
        nameField = new TextField();
        numberField = new TextField();
        addressField = new TextField();
        ComboBox<Countries> countryBox = new ComboBox();
        countryBox.getItems().addAll(Countries.values());
        countryBox.setValue(Countries.UK);
        tilePane.setPrefColumns(2);
        tilePane.getChildren().addAll(
                nameLabel,
                nameField,
                numberLabel,
                numberField,
                addressLabel,
                addressField,
                countryLabel,
                countryBox
        );

        // Adding the buttons
        Button entryClear = new Button("Clear");
        entryClear.setOnAction(e -> {
            Control.clearText(tilePane);
        });
        Button entryEdit = new Button("Edit");
        entryEdit.setOnAction(event -> {
            // There's a little bit of redundancy between here and the add button, but we
            // can address that later...
            if (selectedContact == null) {
                showAlert("You are not currently editing any contact",
                        "Please select a contact from the List or Search tab, right click, " +
                                "and select 'Edit Selected Contact'");
                return;
            }
            // Check for empty required fields
            if (Control.emptyTextInput(tilePane)) {
                showAlert("Empty Inputs",
                        "Please fill in all required entries");
                return;
            }
            // Create our new contact
            Contact newContact = new Contact(
                    nameField.getText(),
                    numberField.getText(),
                    addressField.getText(),
                    countryBox.getValue()
            );
            // Override the selected contact
            contactsModel.replace(newContact, selectedContact);
            selectedContact = newContact;

        });
        Button entryAdd = new Button("Add");
        entryAdd.setOnAction(e -> {
            // Check for empty required fields
            if (Control.emptyTextInput(tilePane)) {
                showAlert("Empty Inputs",
                        "Please fill in all required entries");
                return;
            }
            // Check for duplicate name/number entries
            else if (contactsModel.exists(nameField.getText(), numberField.getText())) {
                showAlert("Duplicate Input",
                        "An entry with that name and number already exists");
                return;
            }
            // Create our new contact
            Contact newContact = new Contact(
                    nameField.getText(),
                    numberField.getText(),
                    addressField.getText(),
                    countryBox.getValue()
            );
            contactsModel.add(newContact);
        });
        Button entryDelete = new Button("Delete");
        entryDelete.setOnAction(event -> {
            // Remove the matching entry from the data
            if (!contactsModel.remove(nameField.getText(), numberField.getText())) {
                showAlert("No Such Entry",
                        "Could not find an entry matching than name and number");;
            }
        });
        buttons.getChildren().addAll(
                entryClear,
                entryEdit,
                entryAdd,
                entryDelete
        );
        buttons.setAlignment(Pos.CENTER);

        // Putting it all together
        borderPane.setCenter(tilePane);
        borderPane.setBottom(buttons);
        entry.setContent(borderPane);

        return entry;
    }

    private Tab setupListTab() {
        Tab list = new Tab("List");
        TableView table = contactsModel.table();

        list.setContent(table);
        return list;
    }


    private Tab setupSearchTab() {
        Tab search = new Tab("Search");
        TilePane tilePane = new TilePane(10, 4);
        VBox vBox = new VBox();
        TableView table = contactsModel.table();

        // Nodes to go in the TilePane
        Label nameLabel = new Label("Name:");
        Label numberLabel = new Label("Number:");
        TextField nameField = new TextField();
        TextField numberField = new TextField();
        Button clearButton = new Button("Clear");
        Button searchButton = new Button("Search");
        Button editButton = new Button("Edit");

        // Adding nodes to the TilePane
        tilePane.setPrefColumns(2);
        tilePane.getChildren().addAll(
                nameLabel,
                nameField,
                numberLabel,
                numberField,
                clearButton,
                searchButton
        );

        vBox.getChildren().addAll(tilePane, table);
        search.setContent(vBox);
        return search;
    }

    @Override
    public void start(Stage primaryStage) {
        //FlowPane root = new FlowPane(); // The root node of our scene
        BorderPane root = new BorderPane(); // The border pane, to which everything will be added
        Scene scene = new Scene(root); // The scene
        tabPane = new TabPane();

        // Instancing and adding the tabs
        entryTab = setupEntryTab();
        entryTab.setClosable(false);
        listTab = setupListTab();
        listTab.setClosable(false);
        searchTab = setupSearchTab();
        searchTab.setClosable(false);
        tabPane.getTabs().addAll(entryTab, listTab, searchTab);

        root.setCenter(tabPane);
        root.setTop(setupMenu());

        //root.getChildren().add(root);

        primaryStage.setTitle("Address Book");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void editContact(Contact contact) {
        if (contact == null) {
            showAlert("No Item contact",
                    "Please select the item you wish to edit");
            return;
        }
        selectedContact = contact;
        System.out.println(contact.getName());
        this.nameField.setText(contact.getName());
        this.numberField.setText(contact.getNumber());
        this.addressField.setText(contact.getAddress());
        tabPane.getSelectionModel().select(entryTab);
    }

    private void showAlert(String header, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Problem!");
        alert.setHeaderText(header);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
