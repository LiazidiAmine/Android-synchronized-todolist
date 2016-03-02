package upec.com.myapplication.Register;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ghost Amine on 06/02/16.
 */
public class PasswordValidator {

    private Pattern pattern;
    private Matcher matcher;

    private static final String PATTERN_PSWD =
            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";

    public PasswordValidator(){
        pattern = Pattern.compile(PATTERN_PSWD);
    }


    /**
     * Validate password with regular expression
     * @param pswd password for validation
     * @return true valid password, false invalid password
     */
    public boolean validate(final String pswd){

        matcher = pattern.matcher(pswd);
        return matcher.matches();

    }

}
