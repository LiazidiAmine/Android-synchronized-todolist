package upec.com.myapplication.Register;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ghost Amine on 06/02/16.
 */
public class EmailValidator {

    private Pattern pattern;
    private Matcher matcher;


    private static final String PATTERN_EMAIL =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public EmailValidator() {
        pattern = Pattern.compile(PATTERN_EMAIL);
    }


    /**
     * Validate hex with regular expression
     *
     * @param email
     *            hex for validation
     * @return true valid hex, false invalid hex
     */
    public boolean validate(final String email) {

        matcher = pattern.matcher(email);
        return matcher.matches();

    }


}
