package ac.uk.chester.j101418

import ac.uk.chester.j101418.databinding.ActivityAdviceBinding
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts

class AdviceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdviceBinding

    private val resultLauncher = registerForActivityResult (ActivityResultContracts.StartActivityForResult() ) {
            result ->
    }

    private fun loadMaintivity () {
        val intent = Intent (this, MainActivity::class.java)
        resultLauncher.launch(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdviceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val dummy = intent.getStringExtra("advice")

        binding.tvAdviceView.text = dummy

        binding.buttonHome.setOnClickListener {
            loadMaintivity()
        }


    }
}