package cn.studyjams.s1.sj64.liuyang;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class Memory extends AppCompatActivity {

    private static final String[] TYPE = new String[]{"bit", "B", "KB", "MB", "GB", "TB", "PB"};

    Common common = new Common();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert_panel);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        final WheelView wvLeft = (WheelView) findViewById(R.id.wv_left);
        final WheelView wvRight = (WheelView) findViewById(R.id.wv_right);
        final TextView currentTypeTextView = (TextView) findViewById(R.id.current_type);
        final TextView convertTypTextView = (TextView) findViewById(R.id.convert_type);
        final EditText editTextLeft = (EditText) findViewById(R.id.current_value_edit_text);
        final EditText editTextRight = (EditText) findViewById(R.id.convert_value_edit_text);

        // 设置键盘输入类型
        editTextLeft.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        editTextRight.setInputType(EditorInfo.TYPE_CLASS_NUMBER);

        Button changeBtn = (Button) findViewById(R.id.change_btn);

        TextView detailTextView = (TextView) findViewById(R.id.detail);
        String detail = "1 PB = 1024 TB" +
                "\n1 TB = 1024 GB" +
                "\n1 GB = 1024 MB" +
                "\n1 MB = 1024 K" +
                "\n1 KB = 1024 B" +
                "\n1 B = 8 bit";
        detailTextView.setText(detail);

        common.initWheelView(TYPE, currentTypeTextView, wvLeft);
        common.initWheelView(TYPE, convertTypTextView, wvRight);

        common.changePosition(changeBtn, editTextLeft, editTextRight, currentTypeTextView,
                convertTypTextView, wvLeft, wvRight, TYPE);

        // 单位转换
        editTextLeft.addTextChangedListener(new TextWatcher() {
            int leftTypeIndex;
            int rightTypeIndex;

            double leftValue;
            double rightValue;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 获取左右两边的存储单位编号
                leftTypeIndex = wvLeft.getSeletedIndex();
                rightTypeIndex = wvRight.getSeletedIndex();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int distance = Math.abs(leftTypeIndex - rightTypeIndex);

                if (!String.valueOf(s).equals("")) {
                    leftValue = Double.parseDouble(s.toString());

                    // 左右两边的单位一致
                    if (leftTypeIndex == rightTypeIndex) {
                        rightValue = leftValue;
                        editTextRight.setText(new DecimalFormat("0").format(rightValue));
                    }

                    // 左边的单位为 bit，且右边不是 bit
                    if (leftTypeIndex == 0 && rightTypeIndex != 0) {
                        rightValue = leftValue / (8 * Math.pow(1024, distance - 1));
                        editTextRight.setText(new DecimalFormat("0.00").format(rightValue));
                    }

                    // 左边的单位不是 bit，且右边也不是 bit
                    if (leftTypeIndex != 0 && rightTypeIndex != 0) {
                        if (leftTypeIndex > rightTypeIndex) {
                            rightValue = leftValue * Math.pow(1024, distance);
                            editTextRight.setText(new DecimalFormat("0").format(rightValue));
                        } else if (leftTypeIndex < rightTypeIndex) {
                            rightValue = leftValue / Math.pow(1024, distance);
                            editTextRight.setText(new DecimalFormat("0.00").format(rightValue));
                        } else {
                            editTextRight.setText(new DecimalFormat("0").format(leftValue));
                        }
                    }

                    // 左边的单位不是 bit，但右边是 bit
                    if (leftTypeIndex != 0 && rightTypeIndex == 0) {
                        rightValue = leftValue * 8 * Math.pow(1024, distance - 1);
                        editTextRight.setText(new DecimalFormat("0").format(rightValue));
                    }

                } else {
                    editTextRight.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
