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
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.Scanner



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var theQuerry: String


    private val resultLauncher = registerForActivityResult (ActivityResultContracts.StartActivityForResult() ) {
            result ->
    }

    private fun loadAdviceActivity (id: String, advice: String) {
        val intent = Intent(this, AdviceActivity::class.java)
        intent.putExtra("advice", advice)
        intent.putExtra("id", id)
        resultLauncher.launch(intent)
    }

    private fun loadQuoteActivity (id: String?, quote: String, author: String?, slug: String ) {
        val intent = Intent(this, QuoteActivity::class.java)
        intent.putExtra("_id", id)
        intent.putExtra("quote", quote)
        intent.putExtra("author", author)
        intent.putExtra("authorSlug", slug)
        resultLauncher.launch(intent)
    }

    private fun getAdvice () {
        fetchData("https://api.adviceslip.com/advice")
    }

    private fun getQuote () {
        fetchData("https://api.quotable.io/quotes/random")
    }

    private fun processAdviceJson(jsonString : String) : AdviceData {
        return try {
            val jsonObject = JSONObject(jsonString)
            val slipObject = jsonObject.getJSONObject("slip")
            val id = slipObject.getInt("id").toString()
            val advice = slipObject.getString("advice")

            AdviceData(id, advice)
        } catch (e: Exception) {
            return AdviceData("","")
        }
    }

    private fun processQuoteJson(jsonString: String): QuoteData? {
        return try {
            val jsonArray = JSONArray(jsonString)
            val jsonObject = jsonArray.getJSONObject(0)
            val quoteId = jsonObject.getString("_id")
            val quote = jsonObject.getString("content")
            val author = jsonObject.getString("author")
            val slug = jsonObject.getString("authorSlug")

            QuoteData(quoteId, quote, author, slug)
        }
        catch (e: Exception) {
            return QuoteData("quoteId", "quote", "author", "slug")
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

                    if (theQuerry == "Advice") {
                        val adviceData = processAdviceJson(text)
                        loadAdviceActivity(adviceData.id, adviceData.advice)
                    }
                    if (theQuerry == "Quote") {
                        val quoteData = processQuoteJson(text)
                        val id = quoteData?.id
                        val quote = quoteData?.quote
                        val author = quoteData?.author
                        val authorSlug = quoteData?.authorSlug
                        if (quote != null) {
                            if (authorSlug != null) {
                                loadQuoteActivity(id, quote, author, authorSlug)
                            }
                        } 
                    }
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

    private fun makeSpinner() {
        val choiceRange = mutableListOf("Advice", "Quote")
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, choiceRange)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
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

                updateTextView("$yourPick: $theQuerry")

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.buttonGO.visibility = View.VISIBLE
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.buttonGO.visibility = View.VISIBLE

        makeSpinner()

        binding.buttonGO.setOnClickListener {
            if (theQuerry == "Advice") {
                getAdvice()
            }
            if (theQuerry == "Quote") {
                getQuote()
            }
            binding.buttonGO.visibility = View.INVISIBLE
        }
    }
}