package upec.com.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import upec.com.myapplication.DataBase.Data;
import upec.com.myapplication.Register.EmailValidator;
import upec.com.myapplication.Register.PasswordValidator;

public class LoginActivity extends Activity {

    private Data db;
    private EditText editText_Email, editText_Pswd;
    private Button button;
    private EmailValidator emailValidator;
    private PasswordValidator passwordValidator;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        editText_Email = (EditText) (findViewById(R.id.editText_Email));
        editText_Pswd = (EditText) (findViewById(R.id.editText_Pswd));
        button = (Button)(findViewById(R.id.button_Register));

        db = new Data(this);
        db.open();


        emailValidator = new EmailValidator();
        passwordValidator = new PasswordValidator();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validInput()){
                    Intent intent = new Intent(LoginActivity.this, AccueilActivity.class);
                    intent.putExtra("email",email);
                    startActivity(intent);
                }

            }
        });

    }

    private boolean validInput() {

        email = editText_Email.getText().toString();
        String pswd = editText_Pswd.getText().toString();

        if (email != null && pswd != null) {

            if(emailValidator.validate(email)){
                if(!db.userExist(email)) {
                    db.addUser(email, pswd, "amine", null);
                    return true;
                }
                else{
                    if(db.userPassword(pswd,email))
                        return true;
                }
            }else{
                Toast.makeText(LoginActivity.this, "Error : Email/Password not valid"+email+" "+pswd, Toast.LENGTH_SHORT).show();
            }

        }
        return false;
    }


}







