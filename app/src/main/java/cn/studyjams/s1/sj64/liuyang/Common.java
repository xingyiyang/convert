package cn.studyjams.s1.sj64.liuyang;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;


class Common {

    private int findPosition(String[] array, String value) {
        int i = 0;
        while (i < array.length && !value.equals(array[i])) i++;
        return i;
    }

    void initWheelView(final String[] array, final TextView tv, WheelView wv) {
        tv.setText(array[0]);
        wv.setOffset(1);
        wv.setItems(Arrays.asList(array));
        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                tv.setText(item);
            }
        });
    }

    void changePosition(Button btn, final EditText etLeft, final EditText etRight,
                        final TextView tvLeft, final TextView tvRight,
                        final WheelView wvLeft, final WheelView wvRight,
                        final String[] array) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String leftType = (String) tvLeft.getText();
                String rightType = (String) tvRight.getText();

                String leftValue = String.valueOf(etLeft.getText());
                String rightValue = String.valueOf(etRight.getText());


                tvLeft.setText(rightType);
                tvRight.setText(leftType);

                wvLeft.setSeletion(findPosition(array, rightType));
                wvRight.setSeletion(findPosition(array, leftType));

                etLeft.setText(rightValue);
                etRight.setText(leftValue);

                // set the cursor at furthest to the right.
                etLeft.setSelection(etLeft.getText().length());

            }
        });
    }
}
