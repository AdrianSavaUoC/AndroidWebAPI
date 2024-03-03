package ac.uk.chester.j101418

import ac.uk.chester.j101418.databinding.ActivityShowAdvicesListBinding
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager

class ShowAdvicesListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowAdvicesListBinding

    private val resultLauncher = registerForActivityResult (ActivityResultContracts.StartActivityForResult() ) {
            result ->
    }

    private fun loadMainActivity () {
        val intent = Intent(this, MainActivity::class.java )
        resultLauncher.launch(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowAdvicesListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val firstWord = intent.getStringExtra("firstWord")

        val advices = intent.getSerializableExtra("adviceList") as? List<AdviceData> ?: emptyList()

        val size = advices.size.toString()

        binding.tvHeader.text = "We have found $size advices containing the word $firstWord"

        val adapter = AdviceAdapter(advices)
        binding.rvAdvices.adapter = adapter
        binding.rvAdvices.layoutManager = LinearLayoutManager(this)


        binding.buttonHome.setOnClickListener {
            loadMainActivity()
        }




    }
}