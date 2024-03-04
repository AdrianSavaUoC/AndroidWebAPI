package ac.uk.chester.j101418

import ac.uk.chester.j101418.databinding.ActivityQuotePhase2Binding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.Scanner

class QuotePhase2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityQuotePhase2Binding
    private lateinit var firstWord: String
    private val adviceFromQuoteList = mutableListOf<AdviceData>()
    private lateinit var decisionFragment : String
    private var authorSlug: String = "" // Initialize authorSlug here

    private val quotesByAuthor = mutableListOf<QuoteItems>()
    private lateinit var author : String

    private fun getAdviceFromQuote() {
        fetchData("https://api.adviceslip.com/advice/search/$firstWord")
    }

    private fun getQuotesByAuthor() {
        fetchData("https://api.quotable.io/quotes?author=${authorSlug}&limit=150")
    }

    private fun processJson4Advice(jsonString: String): List<AdviceData> {
        val adviceFromQuoteList = mutableListOf<AdviceData>()
        try {
            val jsonObject = JSONObject(jsonString)
            val slipsArray = jsonObject.getJSONArray("slips")

            for (i in 0 until slipsArray.length()) {
                val slipObject = slipsArray.getJSONObject(i)
                val id = slipObject.getInt("id").toString()
                val advice = slipObject.getString("advice")
                adviceFromQuoteList.add(AdviceData(id, advice))
            }
        } catch (e: Exception) {
            updateTextView("An error occurred while retrieving data from the server. This one: $e")
        }
        return adviceFromQuoteList
    }

    private fun processQuoteJson(jsonString: String): List<QuoteItems> {
        val quotesList = mutableListOf<QuoteItems>()
        try {
            val jsonObject = JSONObject(jsonString)
            val resultsArray = jsonObject.getJSONArray("results")

            for (i in 0 until resultsArray.length()) {
                val quoteObject = resultsArray.getJSONObject(i)
                val id = quoteObject.getString("_id")
                val content = quoteObject.getString("content")
                val author = quoteObject.getString("author")
                quotesList.add(QuoteItems(id, content, author))
            }
        } catch (e: Exception) {
            updateTextView("An error occurred while retrieving data from the server. This one: $e")
        }
        return quotesList
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

                    if (decisionFragment == "fragmentQ2A") {
                        val adviceList = processJson4Advice(text)
                        adviceFromQuoteList.addAll(adviceList)
                        val fragment = Quote2AdviceFragment.newInstance(adviceList, author, firstWord)

                        supportFragmentManager.beginTransaction()
                            .add(R.id.flQuotesFragment, fragment)
                            .commit()
                    }
                    if (decisionFragment == "fragmentQL") {
                        val quotesList = processQuoteJson(text)
                        quotesByAuthor.addAll(quotesList)
                        val fragment = QuoteListFragment.newInstance(quotesList, authorSlug)
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.flQuotesFragment, fragment)
                            .commit()
                    }
                } else {
                    updateTextView("the server returned an error. this one: $responseCode")
                }
            } catch (e: IOException) {
                updateTextView("the server returned an error. this one: $e")
            }
        }
        thread.start()
    }

    private fun updateTextView(text: String) {
        runOnUiThread {
           binding.textView4.text = text
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuotePhase2Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        author = intent.getStringExtra("author").toString()

        val decisionFragment = intent.getStringExtra("decisionFragment")
        if (decisionFragment != null) {
            this.decisionFragment = decisionFragment
        }
        val word = intent.getStringExtra("firstWord")

        if (word != null) {
            firstWord = word
        }

        if (this.decisionFragment == "fragmentQ2A") {
            getAdviceFromQuote()
        }
        if (this.decisionFragment == "fragmentQL") {
            val author = intent.getStringExtra("author")
            if (author != null) {
                authorSlug = author
                getQuotesByAuthor()
            }
        }
    }
}