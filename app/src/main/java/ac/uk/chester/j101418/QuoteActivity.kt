package ac.uk.chester.j101418

import ac.uk.chester.j101418.databinding.ActivityQuoteBinding
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


class QuoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuoteBinding

    private val quoteList = mutableListOf<QuoteData>()
    private lateinit var id : String
    private lateinit var quote : String
    private lateinit var author : String
    private lateinit var authorSlug : String
    private val quotes = mutableListOf<QuoteItems>()

    private lateinit var choice : String
    private lateinit var variant1 : String
    private lateinit var variant2 : String
    private lateinit var firstWird : String



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

    private fun loadQuotePhase2Activity (quotes: List<QuoteItems>, author: String) {
        val intent = Intent(this, ShowQuotesActivity::class.java)
        intent.putExtra("quotesList", ArrayList(quotes))
        intent.putExtra("author", author)
        resultLauncher.launch(intent)
    }

    private fun loadQuotePhase2Activity (decisionFragment: String) {
        val intent = Intent(this, QuotePhase2Activity::class.java )
        intent.putExtra("firstWord", getFirstWord())
        intent.putExtra("decisionFragment", decisionFragment)
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
            updateTextView("An error occurred while retrieving data from the server: $e")
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



    private fun updateTextView(text: String?) {
        runOnUiThread {
            binding.tvNUmberOfQuotes.text = text
        }
    }

    private fun newQuote(): QuoteData {
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

        return QuoteData(this.id, this.quote, this.author, this.authorSlug)
    }

    private fun makeSpinner() {
        variant1 = "Look in the Advice Archive"
        variant2 = "Search all quotes by Author"
        val choiceRange = mutableListOf(variant1,variant2)
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, choiceRange)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spQuoteSectionChoice.adapter = adapter

        binding.spQuoteSectionChoice.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                choice = choiceRange[position]
                if (choice == variant1) {

                    binding.buttonGetQuoteList.text = "Go for advices"
                }
                if (choice == variant2) {

                    binding.buttonGetQuoteList.text = "Go for quotes"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun getFirstWord () : String? {
        val newQuote = newQuote()
        val activeQuote = newQuote.quote
        val words = activeQuote.split(Regex("[ ,?;.:!]+"))?.toMutableList()
        return words?.firstOrNull()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuoteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.buttonHome.setOnClickListener {
            loadMainActivity()
        }

        val newQuote = newQuote()

        quoteList.add(newQuote)

        binding.tvQuote.text = newQuote.quote
        binding.tvAuthor.text = newQuote.author

        binding.tvFillText.text = "Please press the button if you want to read more quotes from ${newQuote.author}"


        val firstWird = getFirstWord()
        if (firstWird != null) {
            this.firstWird = firstWird
        }
        updateTextView(this.firstWird)


        makeSpinner()
        binding.buttonGetQuoteList.setOnClickListener {
            if (choice == variant1) {
                loadQuotePhase2Activity("fragmentQ2A")
                //else load for Q2l


            }
            if (choice == variant2) {
                getQuotesbyAuthor()
            }

        }

    }
}
