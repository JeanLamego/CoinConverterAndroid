package com.example.coinconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    private lateinit var result:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        result = findViewById<TextView>(R.id.txt_result)

        val buttonConverter = findViewById<Button>(R.id.btn_converter)

        buttonConverter.setOnClickListener {
        converter();
        }
    }

    private fun converter(){
        val selectedCurrency = findViewById<RadioGroup>(R.id.radioGroup)

        val checked = selectedCurrency.checkedRadioButtonId

        val currency = when(checked){
            R.id.radio_usd ->{
            "USD"
            }
            R.id.radio_eur ->{
                "EUR"
            }
            else ->{
                "JPY"
            }

        }
        val editField = findViewById<EditText>(R.id.edit_field)
        val value = editField.text.toString()
        if(value.isEmpty())
            return
        result.text = value
        result.visibility = View.VISIBLE

        Thread{
            val url= URL("https://api.frankfurter.app/latest?amount=${value}&from=${currency}&to=BRL")
            val connection = url.openConnection() as HttpsURLConnection
            try{

                val data=connection.inputStream.bufferedReader().readText()

                val obj = JSONObject(data)
                runOnUiThread {
                    val res = obj.getJSONObject("rates").getDouble("BRL")
                    result.text= "R$${ "%.2f".format(res.toDouble()) }"
                    result.visibility = View.VISIBLE
                }


            }finally{
                connection.disconnect()
            }
        }.start()


//        if (checked== R.id.radio_usd){
//
//        }else if (checked==R.id.radio_eur){
//
//        }else{}

    }
}