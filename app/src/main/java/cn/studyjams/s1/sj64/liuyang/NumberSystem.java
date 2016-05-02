package cn.studyjams.s1.sj64.liuyang;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.method.NumberKeyListener;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class NumberSystem extends AppCompatActivity {
    private static final String[] TYPE = new String[]{
            "Bin 二进制",
            "Oct 八进制",
            "Dec 十进制",
            "Hex 十六进制"
    };

    Common common = new Common();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert_panel);

        /**
         * add back button in the left side of actionBar.
         * this activity's parent is defined in AndroidManifest.xml.
         */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final WheelView wvLeft = (WheelView) findViewById(R.id.wv_left);
        final WheelView wvRight = (WheelView) findViewById(R.id.wv_right);

        final TextView currentTypeTextView = (TextView) findViewById(R.id.current_type);
        final TextView convertTypTextView = (TextView) findViewById(R.id.convert_type);

        final EditText editTextLeft = (EditText) findViewById(R.id.current_value_edit_text);
        final EditText editTextRight = (EditText) findViewById(R.id.convert_value_edit_text);

        editTextLeft.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        editTextRight.setInputType(EditorInfo.TYPE_CLASS_TEXT);

        Button changeBtn = (Button) findViewById(R.id.change_btn);

        View cutLine = findViewById(R.id.cut_line);
        TextView detailTextView = (TextView) findViewById(R.id.detail);

        cutLine.setVisibility(View.INVISIBLE);
        detailTextView.setVisibility(View.INVISIBLE);


        /**
         * initiate WheelView and set OnWheelViewListener function. change the content of the
         * TextView and WheelView when the WheelView scrolled;
         */
        common.initWheelView(TYPE, currentTypeTextView, wvLeft);
        common.initWheelView(TYPE, convertTypTextView, wvRight);

        /**
         * when click the change button, swap the value of currentTypeTextView and convertTypTextView.
         * And at the same time, swap the content of wvLeft and wvRight.
         */
        common.changePosition(changeBtn, editTextLeft, editTextRight, currentTypeTextView, convertTypTextView, wvLeft, wvRight, TYPE);

        editTextLeft.addTextChangedListener(new TextWatcher() {

            int leftTypeIndex;
            int rightTypeIndex;

            String leftValue;
            String rightValue;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                leftTypeIndex = wvLeft.getSeletedIndex();
                rightTypeIndex = wvRight.getSeletedIndex();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!String.valueOf(s).equals("")) {
                    int decValue;
                    leftValue = s.toString();
                    int length = leftValue.length();

                    char[] chars = leftValue.toCharArray();

                    // ascii
                    // 0~9: 48~57
                    // A~E: 65~69
                    // a~e: 97~101
                    if (
                            chars[length - 1] < 48
                                    || (chars[length - 1] > 57 && chars[length - 1] < 65)
                                    || (chars[length - 1] > 69 && chars[length - 1] < 97)
                                    || chars[length - 1] > 101) {
                        leftValue = "";
                    }
                    switch (leftTypeIndex) {
                        case 0:
                            if (chars[length - 1] != '0' && chars[length - 1] != '1') {
                                leftValue = "";
                            }
                            decValue = Integer.valueOf(leftValue, 2);
                            rightValue = numberSystemConvert(rightTypeIndex, decValue);
                            break;
                        case 1:
                            // oct --> dec
                            if (chars[length - 1] == '8' || chars[length - 1] == '9') {
                                leftValue = "";
                            }
                            decValue = Integer.valueOf(leftValue, 8);
                            rightValue = numberSystemConvert(rightTypeIndex, decValue);
                            break;
                        case 2:
                            // dec
                            decValue = Integer.valueOf(leftValue);
                            rightValue = numberSystemConvert(rightTypeIndex, decValue);
                            break;
                        case 3:
                            // hex --> dec
                            decValue = Integer.valueOf(leftValue, 16);
                            rightValue = numberSystemConvert(rightTypeIndex, decValue);
                            break;
                        default:
                            rightValue = "Error";
                            break;
                    }// end-switch

                    editTextRight.setText(rightValue);
                } else {
                    editTextRight.setText("");
                }// end-if (!String.valueOf(s).equals(""))

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextLeft.setKeyListener(DigitsKeyListener.getInstance("0123456789abcdeABCDE"));
    }

    /**
     * convert the decimal number(decValue) to any number system you have demanded(type).
     *
     * @param type     the type of number system, which you want to convert to.
     * @param decValue decimal number
     * @return converted number in string type
     */
    private String numberSystemConvert(int type, int decValue) {
        switch (type) {
            case 0:
                return Integer.toBinaryString(decValue);
            case 1:
                return Integer.toOctalString(decValue);
            case 2:
                return String.valueOf(decValue);
            case 3:
                return Integer.toHexString(decValue);
            default:
                return "false";
        }
    }
}
