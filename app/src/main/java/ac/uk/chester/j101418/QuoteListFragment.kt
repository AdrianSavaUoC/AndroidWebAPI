package ac.uk.chester.j101418
import ac.uk.chester.j101418.databinding.FragmentQuoteListBinding
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager

class QuoteListFragment : Fragment () {

    private lateinit var binding: FragmentQuoteListBinding
    private var quotes: List<QuoteItems>? = null
    private var author: String? = null

    private val resultLauncher = registerForActivityResult (ActivityResultContracts.StartActivityForResult() ) {
            result ->
    }
    private fun loadMainActivity() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        resultLauncher.launch(intent)
    }

    companion object {
        fun newInstance(data: List<QuoteItems>?, author: String?): QuoteListFragment {
            val fragment = QuoteListFragment()
            val args = Bundle().apply {
                putSerializable("quotesList", ArrayList(data))
                putString("author", author)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuoteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        quotes = arguments?.getSerializable("quotesList") as? List<QuoteItems>
        author = arguments?.getString("author")

        binding.tvFragmentQL.text = "We have found ${quotes!!.size} ${if (quotes!!.size == 1) "quote" else "quotes"} from $author"

        val adapter = quotes?.let { QuoteAdapter(it) }
            binding.rvQuotes.adapter = adapter
            binding.rvQuotes.layoutManager = LinearLayoutManager(requireContext())

        binding.btnHome.setOnClickListener {
            loadMainActivity()
        }
    }
}