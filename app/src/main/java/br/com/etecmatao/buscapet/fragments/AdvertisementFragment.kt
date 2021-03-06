package br.com.etecmatao.buscapet.fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import br.com.etecmatao.buscapet.R
import br.com.etecmatao.buscapet.viewModel.NewPostViewModel
import kotlinx.android.synthetic.main.fragment_advertisement.*
import kotlinx.android.synthetic.main.pet_lost_confirm_picture_fragment.*

class AdvertisementFragment : Fragment() {

    private lateinit var vm: NewPostViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_advertisement, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProvider(requireActivity()).get(NewPostViewModel::class.java)

        vm.image.observe(viewLifecycleOwner, Observer {
            it?.let { encodedContent ->
                val decodedString = Base64.decode(encodedContent, Base64.DEFAULT)
                val image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

                adPicture.setImageBitmap(image)
            }
        })

        registerObservables()

        btnPost.setOnClickListener {
            val title = txtTitle.text.toString()
            val description = txtDescription.text.toString()

            vm.publish(title, description) {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.msg_post_complete_successful, it.id),
                    Toast.LENGTH_SHORT
                ).show()

                findNavController().popBackStack(R.id.nav_home, false)
            }
        }
    }

    private fun registerObservables(){
        vm.postType.observe(viewLifecycleOwner, Observer {
            it?.let { type ->
                imgPostType.setImageResource(type.resourceImage)
            }
        })
    }
}