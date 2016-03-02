package upec.com.myapplication.Register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import upec.com.myapplication.LoginActivity;
import upec.com.myapplication.R;

/**
 * Created by ghost Amine on 06/02/16.
 */
public class RegisterActivity extends Activity {

    private EditText editText_Email;
    private EditText editText_Pswd;
    private Button  button_Register;

    private String email,pswd;
    public final static String EMAIL_MESSAGE = "registered email";
    public final static String PSWD_MESSAGE = "registred password";

    private EmailValidator emailValidator;
    private PasswordValidator passwordValidator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editText_Email = (EditText)findViewById(R.id.editText_Email);
        editText_Pswd = (EditText)findViewById(R.id.editText_Pswd);
        button_Register = (Button)findViewById(R.id.button_Register);

        emailValidator = new EmailValidator();
        passwordValidator = new PasswordValidator();

        button_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validInput()){
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.putExtra(email,EMAIL_MESSAGE);
                    intent.putExtra(pswd,PSWD_MESSAGE);
                    startActivity(intent);
                }
            }
        });

    }

    private boolean validInput() {
        if (editText_Email.getText().toString() != null &&
                editText_Pswd.getText().toString() != null) {

            email = editText_Email.getText().toString();
            pswd = editText_Pswd.getText().toString();
            return true;
            /*if((emailValidator.validate(email)) && (passwordValidator.validate(pswd))){
                //Appel à une classe AsyncTask qui se chargera de se connecter au serveur
                return true;
            }else{
                Toast.makeText(RegisterActivity.this, "Error : Email/Password not valid", Toast.LENGTH_SHORT).show();
            }*/

        }
        return false;
    }


}
