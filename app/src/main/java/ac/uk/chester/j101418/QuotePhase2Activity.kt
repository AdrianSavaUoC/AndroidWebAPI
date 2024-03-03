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

    private lateinit var binding : ActivityQuotePhase2Binding
    private lateinit var firstWord : String
    private val adviceFromQuoteList = mutableListOf<AdviceData>()


    fun getAdviceFromQuote (){
        fetchData("https://api.adviceslip.com/advice/search/$firstWord")

    }

    private fun processJson4Advice (jsonString: String)  {
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

    }

    private fun fetchData(urlString: String) {
        val thread = Thread{
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
                    val decisionFragment = intent.getStringExtra("decisionFragment")

                    if (decisionFragment == "fragmentQ2A") {
                        processJson4Advice(text)
                        val word = intent.getStringExtra("firstWord")
                        if (word != null) {
                            firstWord = word
                        }
                        val fragment = Quote2AdviceFragment.newInstance(firstWord)
                        supportFragmentManager.beginTransaction()
                            .add(R.id.flQuotesFragment, fragment)
                            .commit()
                    }

                }
                else
                {
                    updateTextView("the server returned an error. this one: $responseCode")
                }

            }
            catch (e: IOException) {
                updateTextView("the server returned an error. this one: $e")
            }

        }
        thread.start()
    }
    private fun updateTextView (text: String) {
        runOnUiThread {
            binding.textView4.text
        }
    }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuotePhase2Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val fragmentQuoteList = QuoteListFragment()


        //this is how you switch between fragments.
        // you bind a button  onClick Listener fo the bellow, with the appropriate fragment.



        val decisionFragment = intent.getStringExtra("decisionFragment")

        if (decisionFragment == "fragmentQ2A") {
            val word = intent.getStringExtra("firstWord")
            if (word != null) {
                firstWord = word
            }
            val fragment = Quote2AdviceFragment.newInstance(firstWord)
            supportFragmentManager.beginTransaction()
                .add(R.id.flQuotesFragment, fragment)
                .commit()
        }
        else {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flQuotesFragment, fragmentQuoteList)
                commit()
            }
        }



    }
}