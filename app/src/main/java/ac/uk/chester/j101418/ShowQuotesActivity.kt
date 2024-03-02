package ac.uk.chester.j101418

import ac.uk.chester.j101418.databinding.ActivityShowQuotesBinding
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager

class ShowQuotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowQuotesBinding

    private val resultLauncher = registerForActivityResult (ActivityResultContracts.StartActivityForResult() ) {
            result ->
    }

    private fun loadMainActivity () {
        val intent = Intent(this, MainActivity::class.java )
        resultLauncher.launch(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowQuotesBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val quotes = intent.getSerializableExtra("quotesList") as? List<QuoteItems> ?: emptyList()
        val author = intent.getStringExtra("author")

        binding.textView2.text = "We have found ${quotes.size} ${if (quotes.size == 1) "quote" else "quotes"} from $author"


        val adapter = QuoteAdapter(quotes)
        binding.rvQuotes.adapter = adapter
        binding.rvQuotes.layoutManager = LinearLayoutManager(this)

        binding.buttonHome.setOnClickListener {
            loadMainActivity()
        }

    }
}