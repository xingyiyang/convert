package cn.studyjams.s1.sj64.liuyang;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;

import org.json.JSONObject;

import java.text.DecimalFormat;

public class Currency extends AppCompatActivity {
    private static final String[] TYPE = new String[]{
            "￥ 人民币",
            "$ 美元",
            "\u00A5 日元",
            "NT$ 台币",
            "\u20A3 法国法郎",
            "\u00A3 镑",
            "\u20AC 欧元",
            "\u20BD 卢布",
            "\u20AB 越南盾",
            "\u20A8 卢比",
            "\u20A9 朝鲜圆",
            "\u0E3F 泰铢"
    };
    private static final String[] API_TYPE = new String[]{
            "CNY",
            "USD",
            "JPY",
            "TWD",
            "FRF",
            "GBP",
            "EUR",
            "RUB",
            "VND",
            "INR",
            "KPW",
            "THB"
    };

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

        Button changeBtn = (Button) findViewById(R.id.change_btn);

        final View cutLine = findViewById(R.id.cut_line);
        final TextView currencyDetailTextView = (TextView) findViewById(R.id.detail);

        cutLine.setVisibility(View.INVISIBLE);
        currencyDetailTextView.setVisibility(View.INVISIBLE);

        common.initWheelView(TYPE, currentTypeTextView, wvLeft);
        common.initWheelView(TYPE, convertTypTextView, wvRight);

        common.changePosition(changeBtn, editTextLeft, editTextRight, currentTypeTextView,
                convertTypTextView, wvLeft, wvRight, TYPE);

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

                    leftValue = s.toString();

                    Parameters para = new Parameters();
                    para.put("fromCurrency", API_TYPE[leftTypeIndex]);
                    para.put("toCurrency", API_TYPE[rightTypeIndex]);
                    para.put("amount", leftValue);

                    ApiStoreSDK.execute(
                            "http://apis.baidu.com/apistore/currencyservice/currency",
                            ApiStoreSDK.GET,
                            para,
                            new ApiCallBack() {
                                @Override
                                public void onSuccess(int status, String responseString) {

                                    try {
                                        JSONObject retData = new JSONObject(responseString).getJSONObject("retData");
                                        String date = retData.getString("date");
                                        String time = retData.getString("time");
                                        String currencyRate = retData.getString("currency");
                                        double convertedamount = retData.getDouble("convertedamount");
                                        rightValue = new DecimalFormat("0.00").format(convertedamount);

                                        String detail = "当前汇率 [" + API_TYPE[leftTypeIndex] + "] → [" + API_TYPE[rightTypeIndex] + "]: " + currencyRate;
                                        detail += "\n更新时间: " + date + " " + time;

                                        editTextRight.setText(rightValue);
                                        currencyDetailTextView.setText(detail);

                                        cutLine.setVisibility(View.VISIBLE);
                                        currencyDetailTextView.setVisibility(View.VISIBLE);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onComplete() {

                                }

                                @Override
                                public void onError(int status, String responseString, Exception e) {

                                }

                            }
                    );// ApiStoreSDK.execute

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
