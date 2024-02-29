package ac.uk.chester.j101418

import ac.uk.chester.j101418.databinding.ActivityQuoteBinding
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.Scanner


class QuoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuoteBinding

    private val quoteList = mutableListOf<QuoteData>()
    private lateinit var id : String
    private lateinit var quote : String
    private lateinit var author : String
    private lateinit var authorSlug : String
    private val quotes = mutableListOf<QuoteItems>()


    private val resultLauncher = registerForActivityResult (ActivityResultContracts.StartActivityForResult() ) {
            result ->
    }

    private fun loadMainActivity () {
        val intent = Intent(this, MainActivity::class.java )
        resultLauncher.launch(intent)
    }

    private fun loadShowQuotesActivity(quotes: List<QuoteItems>, author: String) {
        val intent = Intent(this, ShowQuotesActivity::class.java)
        intent.putExtra("quotesList", ArrayList(quotes))
        intent.putExtra("author", author)
        resultLauncher.launch(intent)
    }

    private fun getQuotesbyAuthor () {
        val authorSlug = intent.getStringExtra("authorSlug")

        fetchData("https://api.quotable.io/quotes?author=${authorSlug}&limit=150")
    }

    private fun processQuoteJson(jsonString: String) {
        return try {
            val jsonObject = JSONObject(jsonString)
            val resultsArray = jsonObject.getJSONArray("results")


            for (i in 0 until resultsArray.length()) {
                val quoteObject = resultsArray.getJSONObject(i)
                val id = quoteObject.getString("_id")
                val content = quoteObject.getString("content")
                val author = quoteObject.getString("author")
                val quote = QuoteItems(id, content, author)
                quotes.add(quote)
                this.author = author
            }


        }
        catch (e: Exception) {
            updateTextView("Exception: $e")
        }
    }

    private fun fetchData(urlString: String) {
        val  thread = Thread {
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

                    processQuoteJson(text)

                    loadShowQuotesActivity(quotes, author)

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
            binding.tvNUmberOfQuotes.text = quotes.size.toString()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuoteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.buttonHome.setOnClickListener {
            loadMainActivity()
        }

        val id = intent.getStringExtra("_id")
        val quote = intent.getStringExtra("quote")
        val author = intent.getStringExtra("author")
        val authorSlug = intent.getStringExtra("authorSlug")

        if (id != null) {
            this.id = id
        }
        if (quote != null) {
            this.quote = quote
        }
        if (author != null) {
            this.author = author
        }
        if (authorSlug != null) {
            this.authorSlug = authorSlug
        }

        val newQuote = QuoteData(this.id, this.quote, this.author, this.authorSlug)

        quoteList.add(newQuote)

        binding.tvQuote.text = newQuote.quote
        binding.tvAuthor.text = newQuote.author

        binding.tvFillText.text = "Please press the button if you want to read more quotes from ${newQuote.author}"

        binding.buttonGetQuoteList.setOnClickListener {
            getQuotesbyAuthor()
        }


    }
}
