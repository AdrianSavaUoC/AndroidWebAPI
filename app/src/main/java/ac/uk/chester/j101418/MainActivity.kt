package ac.uk.chester.j101418

import ac.uk.chester.j101418.databinding.ActivityMainBinding
import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.Scanner



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var theQuerry: String
    private lateinit var transferText : String



    private val resultLauncher = registerForActivityResult (ActivityResultContracts.StartActivityForResult() ) {
            result ->
    }

    private fun loadAdviceAcrivity (id: String, advice: String) {

        val intent = Intent(this, AdviceActivity::class.java)
        intent.putExtra("advice", advice)
        intent.putExtra("id", id)
        resultLauncher.launch(intent)
    }

    fun getAdvice () {
        fetchData("https://api.adviceslip.com/advice")
    }

    private fun processAdviceJson(jsonString : String) : AdviceData {
        return try {
            val jsonObject = JSONObject(jsonString)
            val slipObject = jsonObject.getJSONObject("slip")
            val id = slipObject.getInt("id").toString()
            val advice = slipObject.getString("advice")

            val newAdvice = AdviceData(id, advice)

            newAdvice

        } catch (e: Exception) {
            return AdviceData("","")
        }
    }

    private fun fetchData(urlString: String) {
        val thread = Thread {
            try {
                val url = URL(urlString)
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 3000
                connection.readTimeout = 3000
                connection.requestMethod = "GET"
                connection.connect()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {

                    val scanner = Scanner(connection.inputStream).useDelimiter("\\A")
                    val text = if (scanner.hasNext()) scanner.next() else ""

                    val adviceData = processAdviceJson(text)

                    loadAdviceAcrivity(adviceData.id, adviceData.advice)
                }
            }
            catch (e: IOException) {
                updateTextView("An error has occurred while retrieving data from the server. $e")
            }
        }
        thread.start()
    }


    private fun updateTextView(text: String) {
        runOnUiThread {
            binding.textView.text = text
        }
    }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val choiceRange = mutableListOf("Advice", "Quote")
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, choiceRange)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spFirstChoice.adapter = adapter

        binding.spFirstChoice.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val choice = choiceRange[position]
                theQuerry = choice
                val yourPick = "Your pick"

                if (theQuerry == "Quote") {
                    binding.textView.text = "$yourPick: $theQuerry"
                }
                else {
                    binding.textView.text = "$yourPick: $theQuerry"
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.buttonGO.setOnClickListener {

            if (theQuerry == "Advice") {
                getAdvice()
            }
            if (theQuerry == "Quote") {
                loadAdviceAcrivity("118","dummyQuote! is the first word in this dummy sentence")
            }

        }



    }
}