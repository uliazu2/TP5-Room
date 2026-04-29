package com.example.userapp.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.userapp.R
import com.example.userapp.SessionManager
import com.example.userapp.data.entity.User
import com.example.userapp.databinding.FragmentRegisterBinding
import com.example.userapp.viewmodel.UserViewModel

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()
            val birthDate = binding.etBirthDate.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Preencha todos os campos obrigatórios!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.etEmail.error = "Email inválido"
                return@setOnClickListener
            }

            if (password.length < 6) {
                binding.etPassword.error = "Senha deve ter ao menos 6 caracteres"
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                binding.etConfirmPassword.error = "Senhas não coincidem"
                return@setOnClickListener
            }

            viewModel.checkEmailExists(email) { exists ->
                if (exists) {
                    binding.etEmail.error = "Este email já está cadastrado"
                } else {
                    val newUser = User(
                        name = name,
                        email = email,
                        phone = phone,
                        password = password,
                        birthDate = birthDate
                    )
                    viewModel.insert(newUser) { insertedId ->
                        if (insertedId > 0) {
                            val sessionManager = SessionManager(requireContext())
                            sessionManager.saveSession(insertedId.toInt())
                            Toast.makeText(requireContext(), "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                        } else {
                            Toast.makeText(requireContext(), "Erro ao cadastrar. Tente novamente.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        binding.btnGoToLogin.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
