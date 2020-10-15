package com.example.apptaekwondomonitoring.utils;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static Date dateParse(String value) throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.parse(value);
    }

    public static String dateFormat(Date date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(date);
    }

    public static void addMaskDate(final EditText editText) {

        editText.addTextChangedListener(new TextWatcher() {

            String oldString = "";
            Boolean changed = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                editText.setSelection(s.toString().length());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();

                if (!str.isEmpty()) {

                    //usuário não esta apagando
                    if (!oldString.equals(str)) {

                        if (str.length() == 2) {  //  xx
                            String element0 = String.valueOf(str.charAt(0));
                            String element1 = String.valueOf(str.charAt(1));
                            str = element0 + element1 + "/";
                            editText.setText(str);
                            oldString = element0 + element1;
                            editText.setSelection(str.length());

                        } else if (str.length() == 5) { //  xx/xx

                            String element0 = String.valueOf(str.charAt(0)); //x
                            String element1 = String.valueOf(str.charAt(1)); //-x
                            String element2 = String.valueOf(str.charAt(2)); //--/
                            String element3 = String.valueOf(str.charAt(3)); //--/x
                            String element4 = String.valueOf(str.charAt(4)); //--/-x

                            str = element0 + element1 + element2 + element3 + element4 + "/";
                            editText.setText(str);
                            oldString = element0 + element1 + element2 + element3 + element4;
                            editText.setSelection(str.length());

                        } else if (str.length() > 10) {

                            str = str.substring(0, str.length() - 1);
                            editText.setText(str);
                            editText.setSelection(str.length());

                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                changed = false;
            }
        });
    }
}
