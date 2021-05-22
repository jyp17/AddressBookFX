package application;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    final String ADD_URL = "http://addressbook-env-1.eba-um8nxvxt.us-east-1.elasticbeanstalk.com/address/addservice";
    private OkHttpClient client = new OkHttpClient();
	
	@FXML
	TextField firstNEntry;
	@FXML
	TextField lastNEntry;
	@FXML
	TextField addressEntry;
	@FXML
	TextField phoneEntry;
	@FXML
	Label message;
	
	public void createContact(ActionEvent event) {
		Map<String, String> params = new HashMap<String, String>();
        params.put("first_name", firstNEntry.getText());
        params.put("last_name", lastNEntry.getText());
        params.put("address", addressEntry.getText());
        params.put("phone", phoneEntry.getText());
        
        JSONObject obj = new JSONObject(params);
        
        executeTask(ADD_URL, obj, JSON);
        
        message.setText("Contact successfully created.");

        firstNEntry.setText("");
        lastNEntry.setText("");
        addressEntry.setText("");
        phoneEntry.setText("");
	}
	
	public void switchToMain(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("ContactList.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
    private void executeTask (String url, JSONObject parameter, MediaType m) {
    	ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                RequestBody body = RequestBody.create(parameter.toString(), m);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code: " + response);
                } catch (IOException e) {
                	System.out.println("Unable to post request.");
                    e.printStackTrace();
                }
            }
        });

        executor.shutdown();
    }

}
