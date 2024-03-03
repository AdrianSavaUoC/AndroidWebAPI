// QuotePhase2Activity.kt

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

    private fun getAdviceFromQuote() {
        fetchData("https://api.adviceslip.com/advice/search/$firstWord")
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
            e.printStackTrace()
        }
        return adviceFromQuoteList
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

                    val adviceList = processJson4Advice(text)
                    adviceFromQuoteList.addAll(adviceList)
                    val fragment = Quote2AdviceFragment.newInstance(adviceList)
                    supportFragmentManager.beginTransaction()
                        .add(R.id.flQuotesFragment, fragment)
                        .commit()
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
            // Update your text view here
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuotePhase2Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val decisionFragment = intent.getStringExtra("decisionFragment")

        if (decisionFragment == "fragmentQ2A") {
            val word = intent.getStringExtra("firstWord")
            if (word != null) {
                firstWord = word
            }
            getAdviceFromQuote()
        } else {
            val fragmentQuoteList = QuoteListFragment()
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flQuotesFragment, fragmentQuoteList)
                commit()
            }
        }
    }


}