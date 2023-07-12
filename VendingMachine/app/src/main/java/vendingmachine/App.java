package vendingmachine;

public class App {

    public static void main(String[] args) {

		/**
		 * The path to database and card info file.
		 * */
		String dbPath = "VM.db";
		String JSONpath = "credit_cards.json";

		// Create the database handler
		JDBC jdbc = new JDBC(dbPath);

		/**
		 * Comment to stop init the database when starting the app.
		 * */
		jdbc.initDB();

		/**
		* Create the model, for handling run time data 
		* and interacting with the database, by calling jdbc methods
		 * */
		Model model = new Model(jdbc, JSONpath);

		// Start the applicatoin with the DefaultPageView
		DefaultPageView defaultPageView = new DefaultPageView();

		// Init the controller
		Controller controller = new Controller(model, defaultPageView);

		// Set up the DefaultPageView
		defaultPageView.setController(controller);
		defaultPageView.setModel(model);

		// Launch the applicatoin window
		controller.launchWindow();
    }
}
