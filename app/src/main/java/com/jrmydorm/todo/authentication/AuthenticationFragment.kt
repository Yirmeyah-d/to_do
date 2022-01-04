package com.jrmydorm.todo.authentication
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jrmydorm.todo.R
import com.jrmydorm.todo.databinding.FragmentAuthenticationBinding
import com.jrmydorm.todo.network.Api

class AuthenticationFragment : Fragment() {

    private lateinit var binding: FragmentAuthenticationBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentAuthenticationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val Token = Api.getToken()
        if(Token.isNullOrEmpty() == false){
            findNavController().navigate(R.id.action_authenticationFragment_to_taskListFragment)
        }

        binding.login.setOnClickListener {
            findNavController().navigate(R.id.action_authenticationFragment_to_loginFragment)

        }

        binding.signup.setOnClickListener {
            findNavController().navigate(R.id.action_authenticationFragment_to_signupFragment)
        }
    }

}